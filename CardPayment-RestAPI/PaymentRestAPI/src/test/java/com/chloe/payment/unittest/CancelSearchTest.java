package com.chloe.payment.unittest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import io.restassured.RestAssured;


@SpringBootTest
class CancelSearchTest {
	// 1. 취소 API	
		// 1.1 정상 Case(vat 입력 x)
		@Test
	    public void cancelTest1() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"payManageNum\": \"1234567890123456\",\n" + 
	        						"    \"cancelAmount\": \"10000\",\n" + 
	        						"    \"cancelVat\": \"1000\",\n" +
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/cancel")
	                    .then().log().all();
	    }

		// 1.2 정상 Case(vat 입력 O)
		public void cancelTest2() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"payManageNum\": \"1234567890123456\",\n" + 
	        						"    \"cancelAmount\": \"10000\",\n" + 
	        						"    \"cancelVat\": \"500\",\n" +
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/cancel")
	                    .then().log().all();
	    }

		// 1.3 비정상 Case(결제상태금액 < 취소요청금액)
		public void cancelTest3() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"payManageNum\": \"1234567890123456\",\n" + 
	        						"    \"cancelAmount\": \"1000000\",\n" + 
	        						"    \"cancelVat\": \"500\",\n" +
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/cancel")
	                    .then().log().all();
	    }

		// 1.4 비정상 Case(결제상태 부가가치세 < 취소요청부가가치세)
		public void cancelTest4() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"payManageNum\": \"1234567890123456\",\n" + 
	        						"    \"cancelAmount\": \"1000000\",\n" + 
	        						"    \"cancelVat\": \"500\",\n" +
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/cancel")
	                    .then().log().all();
	    }

		// 1.5 비정상 Case(결제상태금액 < 결제상태부가가치세  : 결제취소 반영 후 )
		public void cancelTest5() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"payManageNum\": \"1234567890123456\",\n" + 
	        						"    \"cancelAmount\": \"1000000\",\n" + 
	        						"    \"cancelVat\": \"500\",\n" +
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/cancel")
	                    .then().log().all();
	    }
		
		// 1.6 정상 Case(전체 취소 )
		public void cancelTest6() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"payManageNum\": \"1234567890123456\",\n" + 
	        						"    \"cancelAmount\": \"1000000\",\n" + 
	        						"    \"cancelVat\": \"500\",\n" +
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/cancel")
	                    .then().log().all();
	    }
			


	// 2. 조회 API
		// 2.1 정상 Case(결제관리번호 존재O)
		public void searchTest1() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"manageNum\": \"1234567890123456\",\n" +
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/cancel")
	                    .then().log().all(); 
	    }

		// 2.2 정상 Case(취소관리번호 존재O)
		public void searchTest2() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"manageNum\": \"1234567890123456\",\n" +
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/cancel")
	                    .then().log().all();
	    }
		
		// 2.3 비정상 Case(관리번호 존재X)
		public void searchTest3() {
	        RestAssured.given()
	        				.accept("application/json")
	        				.contentType("application/json")
	        				.body("{\n" + 
	        						"    \"manageNum\": \"aaa\",\n" +
	        						"}")
					        .log().all()
	                    .when().post("http://localhost:8080/payment/cancel")
	                    .then().log().all();
	    }
}
