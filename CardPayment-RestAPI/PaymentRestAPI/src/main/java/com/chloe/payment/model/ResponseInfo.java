package com.chloe.payment.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.chloe.payment.security.AESUtils;

import com.chloe.payment.common.CurrentTime;
import com.chloe.payment.common.GlobalConst;
import com.chloe.payment.common.StringUtil;
import com.chloe.payment.common.ResponseCode.Code;


public class ResponseInfo {

	public ResponseInfo() {
		
	}
	
	/*
	 * 결제요청(request API) 에 대한 Response
	 * Response 정보 : 
	 * - Mandatory : 관리번호
	 * - Optional  : 결제금액, 할부개월, 결제요청시간, 상호명(KakaoPay), 응답코드
	 */
	public JSONObject processingPaymentResponse(PaymentInfo paymentInfo) throws JSONException {
		JSONObject response = new JSONObject();
		
		CurrentTime curTime = new CurrentTime();
		String responseCurTime = curTime.getCurrentTime("MM/dd HH:mm");
		try {
			response.put("manageNumber", paymentInfo.getPaymentManageNum());
			response.put("paymentAmount", paymentInfo.getPaymentAmount());
			response.put("installment", paymentInfo.getInstallment());
			response.put("date", responseCurTime);
			response.put("store", "KakaoPay");
			response.put("reportCode", Code.S001);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return response;
	}

	/*
	 * 결제취소(request API) 에 대한 Response
	 * Response 정보 : 
	 * - Mandatory : 관리번호
	 * - Optional  : 결제상태 금액, 결제상태 부가가치세, 응답코드
	 */
	public JSONObject processingCancelResponse(PaymentInfo paymentInfo) throws JSONException {
		JSONObject response = new JSONObject();
		
		try {
			response.put("manageNum", paymentInfo.getCancelManageNum());
			response.put("remainAmount", paymentInfo.getPaymentAmount());
			response.put("remainVat", paymentInfo.getPaymentVat());
			response.put("reportCode", Code.S002);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	/*
	 * 결제조회(request API) 에 대한 Response
	 * Response 정보 : 
	 * - Mandatory : 관리번호, 카드정보(카드번호,유효기간,cvc), 결제/취소 구분, 금액정보(결제/취소금액, 부가가치세)
	 * - Optional  : 응답코드
	 */
	public JSONObject processingSearchResponse(PaymentInfo paymentInfo, String type) throws JSONException {
		JSONObject response = new JSONObject();
		
		// 카드 정보 복호화
	    String cardInfoDec = "";
	    String[] cardInfoList = null;
	    JSONObject cardInfo = new JSONObject();
	       
        // 복호화
        try {
			cardInfoDec = AESUtils.decrypt(paymentInfo.getCardInfo());
			
			// 카드번호|유효기간|cvc 추출하여 String 배열로 return. (idx 0: 카드번호, idx 1: 유효기간, idx 2: cvc)
	        cardInfoList = StringUtil.resultCodeSplit(cardInfoDec, GlobalConst.DELIM_PIPE, true);

	        // 카드번호 마스킹처리
    		StringUtil masking = new StringUtil();
	        cardInfo.put("cardNumber", masking.maskingCardNum(cardInfoList[0]));
	        cardInfo.put("validTHRU", cardInfoList[1]);
	        cardInfo.put("cvcNumber", cardInfoList[2]);
	      
	        JSONArray req_array = new JSONArray();
	        req_array.put(cardInfo);
	        
	        response.put("cardInfo", req_array);
	        if(type.equals(GlobalConst.RESP_TYPE_PAYMENT)) {
		        response.put("paymentManageNum", paymentInfo.getPaymentManageNum());
		        response.put("type", "PAYMENT");
		        response.put("paymentAmount", paymentInfo.getPaymentAmount());
		        response.put("paymentVat", paymentInfo.getPaymentVat());
		        response.put("reportCode", Code.S003);
	        } else if(type.equals(GlobalConst.RESP_TYPE_CANCEL)) {
	        	response.put("cancelManageNum", paymentInfo.getCancelManageNum());
		        response.put("type", "CANCEL");
		        response.put("recentCancelAmount", paymentInfo.getCancelAmount());
		        response.put("recentCancelVat", paymentInfo.getCancelVat());
		        response.put("reportCode", Code.S003);
	        } else {
	        	throw new Exception();
	        }

	        
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return response;
	}
}
