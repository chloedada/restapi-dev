package com.chloe.payment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.chloe.payment.common.CurrentTime;
import com.chloe.payment.common.GlobalConst;
import com.chloe.payment.common.StringUtil;
import com.chloe.payment.security.AESUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 결제요청 시 카드정보 테이블
 * @author chloelee
 * @since 2020.04.15
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Entity
@Table(name = "payment")
public class PaymentInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;          // 
	
	@Column(name= "card_info")
	private String cardInfo;  // 카드 정보(카드번호, 유효기간, cvc 번호의 암호화결과)
	
	@Column(name= "installment", length = 2)
	private int installment;  // 결제 할부 기간
	
	@Column(name= "payment_amount", length = 10)
	private int paymentAmount; // 결제요청 금액
	
	@Column(name= "payment_vat", length = 10)
	private int paymentVat;         // 결제부가가치세
	
	//@Column(name= "payment_manage_num", unique = true, length = 20)
	@Column(name= "payment_manage_num", length = 20)
	private String paymentManageNum;           // 결제 관리번호(PK)
	
	@Column(name= "cancel_manage_num", length = 20)
	private String cancelManageNum;      // 결제취소 관리번호(PK)
	
	@Column(name= "report_code", length = 3)
	private String reportCode = "A00";   // 요청 결과코드
	
	@Column(name= "validTHRU", length = 4)
	private int validTHRU;  // 결제 할부 기간
	
	private long cardNumber; // 카드 번호
	//private int validTHRU; // 카드 유효 기간
	private int cvcNumber;   // cvc 번호
	private int cancelAmount; // 취소요청 금액
	private int cancelVat; // 취소요청 금액
	
	private String causeMessage;
	
	public PaymentInfo() {
		this.cardInfo = "";
		this.installment = 0;
		this.paymentAmount = 0;
		this.paymentVat = 0;
		this.paymentManageNum = "";
		this.reportCode = "";
		this.cancelManageNum = "";
		this.cardNumber = 0l;
		this.validTHRU = 0;
		this.cvcNumber = 0;
		this.cancelAmount = 0;
		this.cancelVat = 0;
		this.causeMessage = "";
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCardInfo() {
		return cardInfo;
	}

	public void setCardInfo(String cardInfo) {
		this.cardInfo = cardInfo;
	}
	
	public int getInstallment() {
		return installment;
	}

	public void setInstallment(int installment) {
		this.installment = installment;
	}

	public int getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(int paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public int getPaymentVat() {
		return paymentVat;
	}

	public void setPaymentVat(int paymentVat) {
		this.paymentVat = paymentVat;
	}
	
	public String getPaymentManageNum() {
		return paymentManageNum;
	}

	public void setPaymentManageNum() {
		CurrentTime curTime = new CurrentTime();
		String paymentCurTime = curTime.getCurrentTime("yyyyMMddHHmmss");
		
		this.paymentManageNum = paymentCurTime + String.format("%06d", getId());
	}

	public String getCancelManageNum() {
		return cancelManageNum;
	}
	
	public String setCancelManageNum() {
		CurrentTime curTime = new CurrentTime();
		String cancelCurTime = curTime.getCurrentTime("yyyyMMddHHmmss");
		
		return cancelManageNum = cancelCurTime + String.format("%06d", getId());
	}
	
	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	
	public long getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getValidTHRU() {
		return this.validTHRU;
	}

	public void setValidTHRU(int validTHRU) {
		this.validTHRU = validTHRU;
	}

	public int getCvcNumber() {
		return this.cvcNumber;
	}

	public void setCvcNumber(int cvcNumber) {
		this.cvcNumber = cvcNumber;
	}
	
	public int getCancelAmount() {
		return cancelAmount;
	}
	
	public void setCancelAmount(int cancelAmount) {
		this.cancelAmount = cancelAmount;
	}
	
	public int getCancelVat() {
		return cancelVat;
	}

	public void setCancelVat(int cancelVat) {
		this.cancelVat = cancelVat;
	}
	
	public String getCauseMessage() {
		return causeMessage;
	}

	public void setCauseMessage(String causeMessage) {
		this.causeMessage = causeMessage;
	}
	
	
	// Function
	public boolean isValidPaymentInfo(String payInfo, String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(payInfo);
		if (!matcher.find()) {
			return false;
		}
		return true;
	}
	
	public boolean checkValidInfo(JSONObject jsonObj) {
		try {
			// 카드번호 10~16자리 숫자가 아닐 경우, 공백일 경우 실패
			if(!isValidPaymentInfo(jsonObj.get("cardNumber").toString(), ("(^[0-9]{10,16}$)")) || jsonObj.get("cardNumber").equals("")) {
				setCauseMessage("Please check card number. (Digit between 10 to 16)");
				return false;
			}
				
			// 유효기간 1~4자리 숫자가 아닐 경우 실패, 공백일 경우 실패
			if(!isValidPaymentInfo(jsonObj.get("validTHRU").toString(), ("(^[0-9]{4}$)")) || jsonObj.get("validTHRU").equals("")){
				setCauseMessage("Please check ValidTHRU number. (Three digits)");
				return false;
			}
			
			// cvc번호 1~3자리 숫자가 아닐 경우 실패, 공백일 경우 실패
			if(!isValidPaymentInfo(jsonObj.get("cvcNumber").toString(), ("(^[0-9]{3}$)")) || jsonObj.get("cvcNumber").equals("")){
				setCauseMessage("Please check cvc number. (Four digits)");
				return false;
			}
			
			// 할부개월수 1~2자리 숫자가 아닐 경우 실패, 공백일 경우 실패
			if(!isValidPaymentInfo(jsonObj.get("installment").toString(), ("(^[0-9]{1,2}$)")) || jsonObj.get("installment").equals("")){
				setCauseMessage("Please check installment number. (Digit between 1 to 2)");
				return false;
			}
			
			// 결제금액 1~10자리 숫자가 아닐 경우, 결제금액이 100원 미만일 경우 실패, 공백일 경우 실패
			if(!isValidPaymentInfo(jsonObj.get("paymentAmount").toString(), ("(^[0-9]{3,10}$)")) || jsonObj.get("paymentAmount").equals("")
				|| Integer.parseInt(jsonObj.get("paymentAmount").toString()) > 1000000000){
				setCauseMessage("Please check payment amount. (Numbers from 100 won to 1 billion won)");
				return false;
			}
			
			
			// 부가가치세가 공백이 아니지만 1~10자리 숫자가 아닐 경우 실패
			if(!isValidPaymentInfo(jsonObj.get("paymentVat").toString(), ("(^[0-9]{1,10}$)")) && !jsonObj.get("paymentVat").equals("")){
				setCauseMessage("Please check payment vat. (optional)");
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public boolean processingPayment(JSONObject jsonObj) {
		String cardInfoEnc = "";
		String cardInfo = "";
		String[] cardInfoArr = new String[3];

		try {
			// 예외처리
			if(!checkValidInfo(jsonObj))
				return false;

			// 1. 결제 정보 DB에 저장
			// 카드정보 생성(카드번호+유효기간+cvc 를 구분자 "|" 로 하나의 String 으로 붙이기)
	        cardInfoArr[0] = jsonObj.get("cardNumber").toString();
	        cardInfoArr[1] = jsonObj.get("validTHRU").toString();
	        cardInfoArr[2] = jsonObj.get("cvcNumber").toString();
			cardInfo = StringUtil.resultCodeJoin(cardInfoArr, GlobalConst.DELIM_PIPE, true);
			
			// 카드정보 암호화(카드번호+유효기간+cvc)
			cardInfoEnc = AESUtils.encrypt(cardInfo);
			
			// 카드정보, 유효기간, cvc, 암호화된 카드정보, 할부, 결제금액 저장
			setCardNumber(Long.parseLong(cardInfoArr[0]));
			setValidTHRU(Integer.parseInt(cardInfoArr[1]));
			setCvcNumber(Integer.parseInt(cardInfoArr[2]));
			setCardInfo(cardInfoEnc);
			setInstallment(Integer.parseInt(jsonObj.get("installment").toString()));
			setPaymentAmount(Integer.parseInt(jsonObj.get("paymentAmount").toString()));
			
			// vat가 비어있을 경우 결제금액 정보로 자동계산하여 저장
			if(jsonObj.get("paymentVat").toString().equals("")) {
				setPaymentVat(Math.round((float)getPaymentAmount()/11));
			}
			else {
				setPaymentVat(Integer.parseInt(jsonObj.get("paymentVat").toString()));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        
		
		return true;
	}
	
	public boolean processingCancellation(String cancelAmount, String cancelVat) {
		// 1. 취소 관리번호 생성
		setCancelManageNum();
		
		// 취소 금액 처리
		int currPayAmount = getPaymentAmount();
		int reqCancelAmount = Integer.parseInt(cancelAmount);
		int currVat = getPaymentVat();
		int reqCancelVat;
		String cause = "";
		if(cancelVat.equals("")) {
			reqCancelVat = (Math.round((float)reqCancelAmount/11));
			// 취소 부가세가를 입력하지 않았을 때
			// 결제상태 부가세보다 더 클 경우 
			// 결제상태금액과 취소금액이 같을 경우(결제상태금액이 0이 되므로 남은 결제상태 부가세도 0으로 만들어줌)
			// 자동으로 남아있는 결제상태 부가세만큼 취소
			if(currVat < reqCancelVat || currPayAmount == reqCancelAmount) {
				reqCancelVat = currVat;
			}
		}
		else {
			reqCancelVat = Integer.parseInt(cancelVat);
			// 결제상태 부가세보다 취소 부가세가 더 클 경우 취소 실패처리
			if(currVat < reqCancelVat) {
				cause = String.format("Please check cancel vat. (%d(cur vat) < %d(cancel vat) )", currVat, reqCancelVat);
				setCauseMessage(cause);
				return false;
			}
		}
		
		
		// 결제상태금액보다 취소금액이 더 크면 실패처리
		if(currPayAmount < reqCancelAmount) {
			cause = String.format("Please check cancel amount. (%d(cur pay amount) < %d(cancel amount) )", currPayAmount, reqCancelAmount);
			setCauseMessage(cause);
			return false;
		}
		
		currPayAmount -= reqCancelAmount;
		currVat -= reqCancelVat;
		
		// 부가세 정보가 없을경우 내부적으로 결제금액을 11로 나눈 금액을 소숫점 첫째 자리에서 반올림했으므로
		//if(currVat<=1)
		//	currVat = 0;
		
		// 결제취소처리후 부가세가 남은 결제상태금액보다 클 경우 실패처리
		if(currPayAmount < currVat) {
			cause = String.format("Please check vat amount. (After processing cancel amount %d(cur pay amount) < %d(cur vat )", currPayAmount, currVat);
			setCauseMessage(cause);
			return false;
		}

		// 결제취소금액, 결제취소부가세, 결제상태금액, 결제상태부가세 정보 변경
		setCancelAmount(reqCancelAmount);
		setCancelVat(reqCancelVat);
		setPaymentAmount(currPayAmount);
		setPaymentVat(currVat);

		return true;
	}
	
	@Override
	public String toString() {
		return String.format("id=%d, installment=%d, paymentAmount=%d, vat=%d", id, installment, paymentAmount, paymentVat);
	}

}
