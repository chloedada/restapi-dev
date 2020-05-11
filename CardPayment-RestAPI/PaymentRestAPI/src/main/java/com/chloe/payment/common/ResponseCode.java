package com.chloe.payment.common;

import lombok.Getter;
import lombok.Setter;

public class ResponseCode {
	
	public enum Code{
		/* 결제 요청 성공 */
		S001("S001", "Payment request success!!")
		/* 결제 요청에 대한 응답 실패 */
		,F001("F001", "Payment request failed..")
		
		/* 결제취소 요청 성공 */
		,S002("S002", "Cancel request success!!")
		/* 결제 취소에 대한 응답 실패 */
		,F002("F002", "Cancel request failed..")
			
		/* 결제/취소 조회 성공 */
		,S003("S003", "Lookup success!!")
		/* 결제/취소 조회에 대한 응답 실패 */
		,F003("F003", "Lookup failed..")
		
		/* 실패 */
		,R900("R900", "Make JSONFilter Fail")
		,R901("R901", "Invalid Parameter")
		,R999("R999", "Server Error")
		
		;

		@Getter @Setter
		private String code;
		@Getter @Setter
		private String message;
		
		private Code(String code, String message) {
			this.code = code;
			this.message = message;
		}
		
	    public String getMessage() {
	        return this.message;
	    }
	      
	    public void setMessage(String value) {
	        this.message = value;
	    }
	      
	    public String getCode() {
	        return code;
	    }
	}

}
