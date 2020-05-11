
# Rest API 기반 결제시스템


결제, 결제취소 요청을 받아서 string 데이터로 카드사와 통신하는 서비스를 구현한다.   
기본적인 카드 결제 기능명세를 작성하고, 해당 기능명세에 따라 결제시스템 API를 개발한다.




## 프로젝트 정보

### 개발 환경
##### - IDE : Eclipse (Version: 2019-09 R (4.13.0))
##### - FrameWork : SpringBoot 2.2.6
##### - Java Ver : 1.8
##### - OS : Windows10
##### - DB : Embedded Database(H2)




### 빌드 및 실행 방법
##### 1. Build Tool : Maven
##### 2. TEST Tool : PostMan
##### 3. TEST FrameWork : Junit 4




## 테이블 설계
#### 1. 결제 요청/취소 데이터 테이블
###### - 결제 요청 정보와 결제 취소 정보를 하나의 Table 로 관리한다.
###### - 결제 취소(전체 취소/ 부분 취소) 가 발생되면 관리번호를 이용하여 해당 결제 정보에 대해 취소 처리 정보를 업데이트 해준다.
###### - 결제 요청 시의 관리번호와 결제 취소 시의 관리번호를 별도로 생성하여, 조회 API 에서 올바른 Response 값을 return 하도록 구현하였다.
고유ID|암호화된 카드정보|할부개월수|결제금액|결제 부가가치세|결제 관리번호|취소관리번호|응답코드|유효기간|카드번호|CVC|취소금액|취소부가가치세|에러원인
------|-----------------|----------|--------|---------------|-------------|------------|--------|--------|--------|---|--------|--------------|-------


#### 2. 카드사 전송 데이터 테이블
###### - 카드사로 전송 할 데이터가 저장된 테이블에는 "String 데이터 명세"에 따라 올바르게 저장하였다.
PK|결제/취소타입|결제/취소 관리번호|전송Data
--|-------------|------------------|--------




## 소스 패키지 설계
###### 1. common  package : 공통으로 사용되는 static 정의 클래스, 공통 사용 함수(Util)를 정의 클래스
###### 2. controller package : API 수행 Controller
###### 3. model     package : 결제/취소 정보 테이블, 카드사로 전송 할 데이터 저장 테이블, 각 API 별 Response 구현 클래스 등.
###### 4. security   package : 데이터 보안(데이터 암/복호화) 관련 클래스 




## 각 API Request/Reponse 예시(JSON)  
#### 1. 결제 요청 API  
##### - Request : 
POST URI : http://localhost:8080/payment/request  
Headers  : "Accept=application/json", "Content-type=application/json"  
Body :  
{  
    "cardNumber": "1234567890123456",  
    "validTHRU": "1125",  
    "cvcNumber": "444",  
    "installment": "0",  
    "paymentAmount": "70000",  
    "vat": "321"  
}  

##### - Response :  
{  
    "manageNumber": "20200421044307000001",  
    "paymentAmount": 30000,  
    "installment": 2,  
    "date": "04/21 04:43",  
    "store": "ChloePay",  
    "reportCode": "S001"  
}  

#### 2. 결제 조회 API
##### - Request :
POST URI : http://localhost:8080/payment/search  
Headers  : "Accept=application/json", "Content-type=application/json"  
Body :  
{  
    "manageNum": "20200421044522000001"  
}  
##### - Response :  
{  
    "cardInfo": [  
        {  
            "cardNumber": "054215*******459",  
            "validTHRU": "0425",  
            "cvcNumber": "119"  
        }  
    ],  
    "cancelManageNum": "20200421044522000001",  
    "type": "CANCEL",  
    "recentCancelAmount": 10000,  
    "recentCancelVat": 909,  
    "reportCode": "S003"  
}  

#### 3. 결제 취소(전체취소/부분 취소) API
##### - Request : 
POST URI : http://localhost:8080/payment/cancel  
Headers  : "Accept=application/json", "Content-type=application/json" 
Body :  
{  
    "payManageNum": "20200421044443000001",  
    "cancelAmount": "10000",  
    "cancelVat": ""  
}  

##### - Response :
{  
    "manageNum": "20200421044522000001",  
    "remainAmount": 20000,  
    "remainVat": 2541,  
    "reportCode": "S002"  
}  


#### 4. 카드사로 전송하는 데이터 테이블 조회 API
##### - Request : 
GET URI : http://localhost:8080/payment/transferDataInfo  
Headers  : "Accept=application/json", "Content-type=application/json" 
Body :  none 

##### - Response :  
[  
    {  
        "id": 1,  
        "type": "PAYMENT   ",  
        "manageNumber": "20200421060600000001",  
        "tranData": " 446PAYMENT   20200421060600000001542158642112459     02425 119     300000000003450                                                                                                                                                                                                                                                                                      nsLFLeNeC4qTw3AKx0+XSxspN8AnPRBvk3CkomPialE=                                               "
    },  
    {  
        "id": 2,  
        "type": "PAYMENT   ",  
        "manageNumber": "20200421060614000002",  
        "tranData": " 446PAYMENT   20200421060614000002542158642112459     02425 119     300000000003450                                                                                                                                                                                                                                                                                      nsLFLeNeC4qTw3AKx0+XSxspN8AnPRBvk3CkomPialE=                                               "
    },  
    {  
        "id": 3,  
        "type": "CANCEL    ",  
        "manageNumber": "20200421060747000002",  
        "tranData": " 446CANCEL    20200421060747000002542158642112459     02425 119     10000000000090920200421060614000002                                                                                                                                                                                                                                                                  nsLFLeNeC4qTw3AKx0+XSxspN8AnPRBvk3CkomPialE=                                               "
    }  
]  



## 문제해결 전략
#### 1. 잘못된 입력 값에 대한 방어코드, 적절한 에러코드 리턴, 예외처리 구현

#### 2. 각 API 별 설계 전략
##### 1-1. 결제 요청 API 설계
           - method  : POST
           - 설명 : 카드정보(카드번호,유효기간,cvc) 암/복호화 처리, 마스킹 처리, 문자열Util을 위한 함수 객체화를 구현하였다.
                    카드 결제를 하면 고객이 문자메시지로 실제로 받게되는 정보를 Response 에 추가하였다.

        
##### 1-2. 결제 조회 API 설계
           - method  : POST
           - 설명 : 결제 요청 시의 관리번호와 결제 취소 시의 관리번호를 별도로 생성하여 관리한다.
                     결제 요청에 대한 관리번호를 조회하면 테이블에서 해당 관리번호를 찾아 명세에 맞는 정보를 Response 로 주고,
                     결제 취소에 대한 관리번호를 조회하면 결제 테이블에서 해당 관리번호를 찾아 명세에 맞는 정보를 Response 로 준다.
                     (** 결제 취소에 대한 관리번호는 결제 취소 시 생성되어 Response 로 받은 정보이기 때문에 Client 는 정보를 알고있다.)
##### 1-3. 결제 취소(전체취소/부분 취소) API 설계
           - method  : POST
           - 설명 : 부분 취소에 대한 API 는 별도로 추가하지 않고 결제취소 API 에 부분 취소 기능을 추가하였다.(API Interfase 동일)

#### 3. API 요청 실패 시 적절한 Respose 로 return 해주도록 설계

#### 4. 예외처리
##### - 숫자로만 입력이 가능한 값 문자가 들어올 경우 정규식을 사용하여 예외처리를 한다.  
#####  EX) Client 에서 숫자로만 입력 가능한 카드번호에 문자가 들어오면 예외처리를 하여 에러 코드를 Return
       cardNumber : "ABC1234567" 와 같이 문자를 포함하여 입력 시 --> 에러코드 Return
##### - 최소 글자수~최대 글자수 범위 내에 입력되지 않은 값에 대해 예외처리를 한다.
#####  EX) Client 에서 최소 10자리~ 최대 16자리의 카드번호가 아닌 미만,초과된 자릿수의 숫자를 입력 시 에러코드를 Return
       cardNumber : "123456" 입력 시 --> 에러코드 Return




## 테스트 검증
##### 1. PostMan 을 이용하여 직관적으로 response 결과 확인
##### 2. Junit 테스트를 이용하여 각 API 별 정상 동작, 에러코드 반환 등 검증
###### 각 API 별 테스트는 Postman 을 통해 테스트 케이스 별로 모두 검증이 되었으나,
Junit 테스트는 결제API 검증 테스트 작성만 모두 완료된 상태이다.
취소,조회에 대한 Junit 테스트 작성은 현재 버전에서는 관리번호를 수동으로 입력하여야만 가능하다.
