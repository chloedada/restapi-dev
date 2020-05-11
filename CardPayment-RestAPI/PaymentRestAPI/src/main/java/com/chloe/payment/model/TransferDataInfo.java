package com.chloe.payment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.chloe.payment.common.GlobalConst;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
@Entity
@Table(name = "transfer_data")
public class TransferDataInfo {
	
	@Id // primaryKey
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id; // primaryKey
	
	@Column(name= "type", length = 10)
	private String type;   // 결제or취소 종류
	
	@Column(name= "manage_number", length = 20)
	private String manageNumber;   // 결제or취소 관리번호
	
	@Column(name= "transfer_data", length = 450)
	private String transferData;   // 카드사 전송 데이터
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getManageNumber() {
		return manageNumber;
	}

	public void setManageNumber(String manageNumber) {
		this.manageNumber = manageNumber;
	}

	public String getTranData() {
		return transferData;
	}

	public void setTranData(String transferData) {
		this.transferData = transferData;
	}
	
	public TransferDataInfo() {
	}
	
	public boolean processingTransferData(String type, PaymentInfo paymentInfo) {
		String resultData = "";
		
		setType(type);
		
		int dataLength = 446;
		// # 공통 헤더 부문 #
		// 0. 데이터 길이
		// 우측으로 정렬해서 4자리 채움 (빈자리는 공백으로)
		resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_EMPTY, 4, dataLength);

		// 1. 데이터 구분
		// 좌측으로 정렬해서 10자리 채움 (빈자리는 공백으로)
		resultData += GetDataFormat(GlobalConst.ALIGN_LEFT_EMPTY, 10, type);
		
		// 2. 관리번호
		// 좌측으로 정렬해서 20자리 채움 (빈자리는 공백으로)
		if(type.equals(GlobalConst.TRAN_DATA_TYPE_PAYMENT))
			resultData += GetDataFormat(GlobalConst.ALIGN_LEFT_EMPTY, 20, paymentInfo.getPaymentManageNum());
		else if(type.equals(GlobalConst.TRAN_DATA_TYPE_CANCEL))
			resultData += GetDataFormat(GlobalConst.ALIGN_LEFT_EMPTY, 20, paymentInfo.getCancelManageNum());
		
		// # 데이터  부문 #
		// 0. 카드번호
		// 좌측으로 정렬해서 20자리 채움 (빈자리는 공백으로)
		resultData += GetDataFormat(GlobalConst.ALIGN_LEFT_EMPTY, 20 , paymentInfo.getCardNumber());
	
		// 1. 할부 개월수
		// 우측으로 정렬해서 2자리 채움 (빈자리는 0으로)
		resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_ZERO, 2 , paymentInfo.getInstallment());
		
		// 2. 카드 유효기간
		// 좌측으로 정렬해서 4자리 채움 (빈자리는 공백으로)
		resultData += GetDataFormat(GlobalConst.ALIGN_LEFT_EMPTY, 4 , paymentInfo.getValidTHRU());
		
		// 3. CVC
		// 좌측으로 정렬해서 3자리 채움 (빈자리는 공백으로)
		resultData += GetDataFormat(GlobalConst.ALIGN_LEFT_EMPTY, 3 , paymentInfo.getCvcNumber()); 
		
		// 4. 거래금액(결제or취소)
		// 우측으로 정렬해서 10자리 채움 (빈자리는 공백으로)
		if(type.equals(GlobalConst.TRAN_DATA_TYPE_PAYMENT)) {
			resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_EMPTY, 10 , paymentInfo.getPaymentAmount()); 
		}
		else if(type.equals(GlobalConst.TRAN_DATA_TYPE_CANCEL)) {
			resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_EMPTY, 10 , paymentInfo.getCancelAmount()); 
		}
		
		// 5. 부가가치세
		// 우측으로 정렬해서 10자리 채움 (빈자리는 0으로)
		if(type.equals(GlobalConst.TRAN_DATA_TYPE_PAYMENT)) {
			resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_ZERO, 10 , paymentInfo.getPaymentVat());
		}
		else if(type.equals(GlobalConst.TRAN_DATA_TYPE_CANCEL)) {
			resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_ZERO, 10 , paymentInfo.getCancelVat());
		}

		// 6. 원거래 관리번호
		// 우측으로 정렬해서 20자리 채움 (빈자리는 공백으로)
		if(type.equals(GlobalConst.TRAN_DATA_TYPE_PAYMENT)) {
			resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_EMPTY, 20 , "");
			setManageNumber(paymentInfo.getPaymentManageNum());
		}
		else if(type.equals(GlobalConst.TRAN_DATA_TYPE_CANCEL)) {
			resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_EMPTY, 20 , paymentInfo.getPaymentManageNum());
			setManageNumber(paymentInfo.getCancelManageNum());
		}
			
		// 7. 암호화된 카드정보
		// 우측으로 정렬해서 300자리 채움 (빈자리는 공백으로)
		resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_EMPTY, 300 , paymentInfo.getCardInfo());
		
		// 8. 예비 필드
		// 우측으로 정렬해서 47자리 채움 (빈자리는 공백으로)
		resultData += GetDataFormat(GlobalConst.ALIGN_RIGHT_EMPTY, 47 , "");
		
		if(resultData.length() > 450)
			return false;
		
		setTranData(resultData);
		return true;
	}
	
	public String GetDataFormat(int formatType, int length, String data) {
		String resultData = "";
		String formatLen  = "";
		
		switch(formatType) {
		case GlobalConst.ALIGN_RIGHT_ZERO:
			// 우측으로 정렬해서 length 길이만큼 문자를 채움 (빈자리는 0으로)
			formatLen = "%0" +  Integer.toString(length) + "d";
			resultData = String.format(formatLen, Integer.parseInt(data));
			break;
		case GlobalConst.ALIGN_RIGHT_EMPTY:
			// 우측으로 정렬해서 length 길이만큼 문자를 채움 (빈자리는 공백으로)
			formatLen = "%" +  Integer.toString(length) + "s";
			resultData = String.format(formatLen, data);
			break;
		case GlobalConst.ALIGN_LEFT_EMPTY:
			// 좌측으로 정렬해서 length 길이만큼 문자를 채움 (빈자리는 공백으로)
			formatLen = "%-" +  Integer.toString(length) + "s";
			String strCardNum = String.format(formatLen, data);
			resultData = strCardNum;
			break;
		}
		
		return resultData;
	}
	
	public String GetDataFormat(int formatType, int length, long data) {
		String resultData = "";
		String formatLen  = "";
		
		switch(formatType) {
		case GlobalConst.ALIGN_RIGHT_ZERO:
			// 우측으로 정렬해서 length 길이만큼 문자를 채움 (빈자리는 0으로)
			formatLen = "%0" +  Integer.toString(length) + "d";
			resultData = String.format(formatLen, data);
			break;
		case GlobalConst.ALIGN_RIGHT_EMPTY:
			// 우측으로 정렬해서 length 길이만큼 문자를 채움 (빈자리는 공백으로)
			formatLen = "%" +  Integer.toString(length) + "s";
			resultData = String.format(formatLen, String.valueOf(data));
			break;
		case GlobalConst.ALIGN_LEFT_EMPTY:
			// 좌측으로 정렬해서 length 길이만큼 문자를 채움 (빈자리는 공백으로)
			formatLen = "%-" +  Integer.toString(length) + "s";
			String strCardNum = String.format(formatLen, String.valueOf(data));
			resultData = strCardNum;
			break;
		}
		
		return resultData;
	}
}
