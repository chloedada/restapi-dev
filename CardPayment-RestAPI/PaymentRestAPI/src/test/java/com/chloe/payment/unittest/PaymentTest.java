package com.chloe.payment.unittest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.RestAssured;


@SpringBootTest
class PaymentTest {

	// 1. 결제 API
		// 1.1 정상 case(부가세 입력 X)
		@Test
	    public void paymentTest1() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"0542158642112459\",\n" + 
	        						"    \"validTHRU\": \"0425\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"0\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }

		// 1.2 정상 case(부가세 입력 O)
		@Test
		public void paymentTest2() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"0542158642112459\",\n" + 
	        						"    \"validTHRU\": \"0425\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"0\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.3 비정상 case(카드번호에 문자열 존재)
		@Test
	    public void paymentTest3() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"123456789abc\",\n" + 
	        						"    \"validTHRU\": \"0425\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"0\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
				
		// 1.4 비정상 case(카드번호 자릿수 10자리 미만)
		@Test
	    public void paymentTest4() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"123456789\",\n" + 
	        						"    \"validTHRU\": \"0425\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"0\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.5 비정상 case(카드번호 자릿수 16자리 이상)
		@Test
	    public void paymentTest5() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"12345678901234567\",\n" + 
	        						"    \"validTHRU\": \"0425\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"0\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.6 비정상 case(유효기간 자릿수 4자리 아님)
		@Test
	    public void paymentTest6() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"1234567890123456\",\n" + 
	        						"    \"validTHRU\": \"042\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"0\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.7 비정상 case(유효기간에 문자 존재)
		@Test
	    public void paymentTest7() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"1234567890123456\",\n" + 
	        						"    \"validTHRU\": \"042k\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"0\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.8 비정상 case(cvc 3자리가 아님)
		@Test
	    public void paymentTest8() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"1234567890123456\",\n" + 
	        						"    \"validTHRU\": \"0420\",\n" + 
	        						"    \"cvcNumber\": \"19\",\n" + 
	        						"    \"installment\": \"0\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.9 비정상 case(cvc에 문자 존재)
		@Test
	    public void paymentTest9() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"1234567890123456\",\n" + 
	        						"    \"validTHRU\": \"0420\",\n" + 
	        						"    \"cvcNumber\": \"19e\",\n" + 
	        						"    \"installment\": \"0\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.10 비정상 case(할부정보가 2자리 초과)
		@Test
	    public void paymentTest10() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"1234567890123456\",\n" + 
	        						"    \"validTHRU\": \"0420\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"012\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.11 비정상 case(할부정보에 문자 존재)
		@Test
	    public void paymentTest11() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"1234567890123456\",\n" + 
	        						"    \"validTHRU\": \"0420\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"1a\",\n" + 
	        						"    \"paymentAmount\": \"34500\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.12 비정상 case(결재금액에  문자 존재)
		@Test
	    public void paymentTest12() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"1234567890123456\",\n" + 
	        						"    \"validTHRU\": \"0420\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"1\",\n" + 
	        						"    \"paymentAmount\": \"aaa\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.13 비정상 case(결재금액  100원 미만)
		@Test
	    public void paymentTest13() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"1234567890123456\",\n" + 
	        						"    \"validTHRU\": \"0420\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"1\",\n" + 
	        						"    \"paymentAmount\": \"12\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
		
		// 1.14 비정상 case(결재금액  10억원 초과)
		@Test
	    public void paymentTest14() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"cardNumber\": \"1234567890123456\",\n" + 
	        						"    \"validTHRU\": \"0420\",\n" + 
	        						"    \"cvcNumber\": \"191\",\n" + 
	        						"    \"installment\": \"1\",\n" + 
	        						"    \"paymentAmount\": \"1000000001\",\n" + 
	        						"    \"paymentVat\": \"3450\"\n" + 
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/request")
	                    .then().log().all();
	    }
}
