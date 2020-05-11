package com.chloe.payment.controller;

import java.net.ResponseCache;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
//import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.chloe.payment.common.GlobalConst;
import com.chloe.payment.common.JsonUtil;
import com.chloe.payment.common.ResponseCode;
import com.chloe.payment.common.ResponseCode.Code;
import com.chloe.payment.model.CardInfoRepository;
import com.chloe.payment.model.PaymentInfo;
import com.chloe.payment.model.ResponseInfo;
import com.chloe.payment.model.TransferDataInfo;
import com.chloe.payment.model.TransferDataRepository;

/**
 * 
 * @author chloelee
 * 
 *  Rest API 기반 결제시스템 
 *  결제, 결제취소요청을 받아서 string 데이터로 카드사와 통신하는 서비스 만들기
 * 
 *  1.결제 요청 API(POST) 
 *  - request  정보 : 카드번호,유효기간,cvc,할부개월 수,결제금액,부가가치세
 *  - response 정보 : 관리번호(유일키 ID), 결제결과코드(성공:S001, 실패:기타Code 로 정의)
 * 
 *  2.결제 취소 API(POST) 
 *  - request  정보 : 관리번호(유일키ID),취소 금액,부가가치세 
 *  - response 정보 : 취소 관리번호(유일키ID), 결과코드(성공:S002, 실패:기타Code 로 정의)
 * 
 *  3.조회 API(POST) 
 *  - request  정보 : 관리번호(유일키ID) 
 *  - response 정보 : 카드정보(카드번호,유효기간,cvc),결제/취소구분,금액정보(결제/취소금액,부가가치세)
 *
 */

@RestController
@RequestMapping(path = "/payment")
public class CardInfoController {

	private static final Logger logger = LoggerFactory.getLogger(CardInfoController.class);

	@Autowired
	private CardInfoRepository cardInfoRepository;

	@Autowired
	private TransferDataRepository tranDataRepository;

	// 확인용 Start //
	@RequestMapping("/cardInfo")
	public List<PaymentInfo> getCardInfos() {
		return (List<PaymentInfo>) cardInfoRepository.findAll();
	}

	@RequestMapping("/cardInfo/{id}")
	public PaymentInfo getCardInfoId(@PathVariable("id") int id) {

		return cardInfoRepository.findById(id).get();
	}

	@RequestMapping("/transferDataInfo")
	public List<TransferDataInfo> getTrDataInfos() {
		return (List<TransferDataInfo>) tranDataRepository.findAll();
	}
	// 확인용 End //

	/* 
	 * 결제 요청 API(POST)
	 * user가 결제 요청을 하면 결제한 카드의 정보들이 h2 DB 에 저장되고, 해당 user 의 유일키(관리번호) 와 결과코드(성공) 을 response 로 내려준다.
	 * request  정보 : 카드번호,유효기간,cvc,할부개월 수,결제금액,부가가치세(optional)
     * response 정보 : 관리번호,결제결과코드(optional, 성공:S001/실패:기타 Code에 정의)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/request", method = RequestMethod.POST, headers = "Accept=application/json")
	public String postPaymentRequest(@RequestBody Map<String, Object> requestPayInfoJson) throws JSONException {
		
		JSONObject jsonObj = JsonUtil.getJsonStringFromMap(requestPayInfoJson);

		PaymentInfo paymentInfo = new PaymentInfo();
		TransferDataInfo tranDataInfo = new TransferDataInfo();
		JSONObject response = new JSONObject();
		ResponseInfo responseInfo = new ResponseInfo();
		
		try {
			
			// DB payment table 데이터 입력
			// 암호화된 카드정보, 할부, 금액, 부가세 정보 저장
			if (!paymentInfo.processingPayment(jsonObj)) {
				response.put("cause", paymentInfo.getCauseMessage());
				throw new Exception();
			}

			// DB payment table에 암호화된 카드정보, 할부, 금액, 부가가치세 정보 저장
			cardInfoRepository.save(paymentInfo);

			// 관리번호 생성 (yyyyMMddHHmmss포맷의 현재시간 14자리 + payment table 고유 id 우측정렬 6자리(빈자리는 0으로 채움))
			paymentInfo.setPaymentManageNum();

			// DB payment table에 관리번호 업데이트
			cardInfoRepository.save(paymentInfo);

			// 카드사로 전송하는 string 데이터 생성 및  DB transfer_data table 데이터 입력
			if (!tranDataInfo.processingTransferData(GlobalConst.TRAN_DATA_TYPE_PAYMENT, paymentInfo)) {
				; // 카드사로 전송하는 데이터 생성 에러 처리 생략
			}

			// DB transfer_data table에 카드사로 전송하는 string 데이터 저장
			tranDataRepository.save(tranDataInfo);

			// 3. response 생성
			response = responseInfo.processingPaymentResponse(paymentInfo);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("errorCode", Code.F001.getCode());
			response.put("errorMessage", Code.F001.getMessage());
		}

		return response.toString();
	}

	/*
	 * 결제 취소 API(POST) 
	 * request 정보 : 관리번호(유일키ID), 취소할 금액 
	 * response 정보 : 해당 관리번호(유일키ID), 취소처리 결과코드(S001)
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/cancel", method = RequestMethod.POST, headers = "Accept=application/json")
	public String postPaymentCancel(@RequestBody Map<String, Object> requestJson) throws JSONException {
		
		
		JSONObject response = new JSONObject();
		JSONObject jsonObj = JsonUtil.getJsonStringFromMap(requestJson);

		PaymentInfo paymentInfo = new PaymentInfo();
		TransferDataInfo tranDataInfo = new TransferDataInfo();
		ResponseInfo responseInfo = new ResponseInfo();
		boolean bFound = false;
		try {
			String payManageNum = jsonObj.get("payManageNum").toString();
			String cancelAmount = jsonObj.get("cancelAmount").toString();
			String vat = jsonObj.get("cancelVat").toString();

			for (int i = 1; i < cardInfoRepository.count() + 1; i++) {
				// payment 테이블에서 결제관리번호를 확인하여 request로 들어온 결제관리번호와 비교함
				paymentInfo = cardInfoRepository.findById(i).get();
				if (paymentInfo.getPaymentManageNum().equals(payManageNum)) {
					// request로 들어온 관리번호와 동일한 정보가 결제번호에 존재할 경우

					// 1. 취소관리번호 생성 및 결제상태금액,결제상태부가세 처리
					if (!paymentInfo.processingCancellation(cancelAmount, vat)) {
						response.put("cause", paymentInfo.getCauseMessage());
						throw new Exception();
					}
					// table 업데이트
					cardInfoRepository.save(paymentInfo);

					// 2. 카드사로 전송하는 string 데이터 생성 및 DB transfer_data table 데이터 입력
					if (!tranDataInfo.processingTransferData(GlobalConst.TRAN_DATA_TYPE_CANCEL, paymentInfo)) {
						; // 카드사로 전송하는 데이터 생성 에러 처리 생략
					}

					// DB transfer_data table에 카드사로 전송하는 string 데이터 저장
					tranDataRepository.save(tranDataInfo);

					// 3. response 생성
					response = responseInfo.processingCancelResponse(paymentInfo);
					
					bFound = true;
				}
			}

			if (!bFound) {
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("errorCode", Code.F002.getCode());
			response.put("errorMessage", Code.F002.getMessage());
		}

		return response.toString();
	}

	/*
	 * 3.조회 API(POST)
	 * request  정보 : 관리번호(유일키ID)
	 * response 정보 : 결제 금액, 취소금액, 카드번호, 결제요청 일자, 결제취소 일자
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/search", method = RequestMethod.POST, headers = "Accept=application/json")
	public String getPaymentAsk(HttpServletRequest req, HttpServletResponse res,
			@RequestBody Map<String, Object> requestJson) throws JSONException {

		JSONObject response = new JSONObject();
		PaymentInfo paymentInfo = new PaymentInfo();
		ResponseInfo responseInfo = new ResponseInfo();
		boolean bFound = false;

		JSONObject jsonObj = JsonUtil.getJsonStringFromMap(requestJson);
		try {
			String manageNum = (String) jsonObj.get("manageNum");
			for (int i = 1; i < cardInfoRepository.count() + 1; i++) {
				// payment 테이블에서 결제관리번호를 확인하여 request로 들어온 관리번호와 비교함
				paymentInfo = cardInfoRepository.findById(i).get();
				if (paymentInfo.getPaymentManageNum().equals(manageNum)) {
					// request로 들어온 관리번호와 동일한 정보가 결제번호에 존재할 경우 해당 행에서 원하는 정보를 가져옴

					response = responseInfo.processingSearchResponse(paymentInfo, GlobalConst.RESP_TYPE_PAYMENT);
					bFound = true;
					break;
				}

				if (paymentInfo.getCancelManageNum().equals("")) {
					continue;
				}

				if (paymentInfo.getCancelManageNum().equals(manageNum)) {
					response = responseInfo.processingSearchResponse(paymentInfo, GlobalConst.RESP_TYPE_CANCEL);
					bFound = true;
					break;
				}
			}

			if (!bFound) {
				response.put("cause", "There is no management number.");
				throw new Exception();
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("errorCode", Code.F003.getCode());
			response.put("errorMessage", Code.F003.getMessage());
		}

		return response.toString();

	}
}
