
# Rest API ��� �����ý���


����, ������� ��û�� �޾Ƽ� string �����ͷ� ī���� ����ϴ� ���񽺸� �����Ѵ�.   
�⺻���� ī�� ���� ��ɸ��� �ۼ��ϰ�, �ش� ��ɸ��� ���� �����ý��� API�� �����Ѵ�.




## ������Ʈ ����

### ���� ȯ��
##### - IDE : Eclipse (Version: 2019-09 R (4.13.0))
##### - FrameWork : SpringBoot 2.2.6
##### - Java Ver : 1.8
##### - OS : Windows10
##### - DB : Embedded Database(H2)




### ���� �� ���� ���
##### 1. Build Tool : Maven
##### 2. TEST Tool : PostMan
##### 3. TEST FrameWork : Junit 4




## ���̺� ����
#### 1. ���� ��û/��� ������ ���̺�
###### - ���� ��û ������ ���� ��� ������ �ϳ��� Table �� �����Ѵ�.
###### - ���� ���(��ü ���/ �κ� ���) �� �߻��Ǹ� ������ȣ�� �̿��Ͽ� �ش� ���� ������ ���� ��� ó�� ������ ������Ʈ ���ش�.
###### - ���� ��û ���� ������ȣ�� ���� ��� ���� ������ȣ�� ������ �����Ͽ�, ��ȸ API ���� �ùٸ� Response ���� return �ϵ��� �����Ͽ���.
����ID|��ȣȭ�� ī������|�Һΰ�����|�����ݾ�|���� �ΰ���ġ��|���� ������ȣ|��Ұ�����ȣ|�����ڵ�|��ȿ�Ⱓ|ī���ȣ|CVC|��ұݾ�|��Һΰ���ġ��|��������
------|-----------------|----------|--------|---------------|-------------|------------|--------|--------|--------|---|--------|--------------|-------


#### 2. ī��� ���� ������ ���̺�
###### - ī���� ���� �� �����Ͱ� ����� ���̺��� "String ������ ��"�� ���� �ùٸ��� �����Ͽ���.
PK|����/���Ÿ��|����/��� ������ȣ|����Data
--|-------------|------------------|--------




## �ҽ� ��Ű�� ����
###### 1. common  package : �������� ���Ǵ� static ���� Ŭ����, ���� ��� �Լ�(Util)�� ���� Ŭ����
###### 2. controller package : API ���� Controller
###### 3. model     package : ����/��� ���� ���̺�, ī���� ���� �� ������ ���� ���̺�, �� API �� Response ���� Ŭ���� ��.
###### 4. security   package : ������ ����(������ ��/��ȣȭ) ���� Ŭ���� 




## �� API Request/Reponse ����(JSON)  
#### 1. ���� ��û API  
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

#### 2. ���� ��ȸ API
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

#### 3. ���� ���(��ü���/�κ� ���) API
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


#### 4. ī���� �����ϴ� ������ ���̺� ��ȸ API
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



## �����ذ� ����
#### 1. �߸��� �Է� ���� ���� ����ڵ�, ������ �����ڵ� ����, ����ó�� ����

#### 2. �� API �� ���� ����
##### 1-1. ���� ��û API ����
           - method  : POST
           - ���� : ī������(ī���ȣ,��ȿ�Ⱓ,cvc) ��/��ȣȭ ó��, ����ŷ ó��, ���ڿ�Util�� ���� �Լ� ��üȭ�� �����Ͽ���.
                    ī�� ������ �ϸ� ���� ���ڸ޽����� ������ �ްԵǴ� ������ Response �� �߰��Ͽ���.

        
##### 1-2. ���� ��ȸ API ����
           - method  : POST
           - ���� : ���� ��û ���� ������ȣ�� ���� ��� ���� ������ȣ�� ������ �����Ͽ� �����Ѵ�.
                     ���� ��û�� ���� ������ȣ�� ��ȸ�ϸ� ���̺��� �ش� ������ȣ�� ã�� ���� �´� ������ Response �� �ְ�,
                     ���� ��ҿ� ���� ������ȣ�� ��ȸ�ϸ� ���� ���̺��� �ش� ������ȣ�� ã�� ���� �´� ������ Response �� �ش�.
                     (** ���� ��ҿ� ���� ������ȣ�� ���� ��� �� �����Ǿ� Response �� ���� �����̱� ������ Client �� ������ �˰��ִ�.)
##### 1-3. ���� ���(��ü���/�κ� ���) API ����
           - method  : POST
           - ���� : �κ� ��ҿ� ���� API �� ������ �߰����� �ʰ� ������� API �� �κ� ��� ����� �߰��Ͽ���.(API Interfase ����)

#### 3. API ��û ���� �� ������ Respose �� return ���ֵ��� ����

#### 4. ����ó��
##### - ���ڷθ� �Է��� ������ �� ���ڰ� ���� ��� ���Խ��� ����Ͽ� ����ó���� �Ѵ�.  
#####  EX) Client ���� ���ڷθ� �Է� ������ ī���ȣ�� ���ڰ� ������ ����ó���� �Ͽ� ���� �ڵ带 Return
       cardNumber : "ABC1234567" �� ���� ���ڸ� �����Ͽ� �Է� �� --> �����ڵ� Return
##### - �ּ� ���ڼ�~�ִ� ���ڼ� ���� ���� �Էµ��� ���� ���� ���� ����ó���� �Ѵ�.
#####  EX) Client ���� �ּ� 10�ڸ�~ �ִ� 16�ڸ��� ī���ȣ�� �ƴ� �̸�,�ʰ��� �ڸ����� ���ڸ� �Է� �� �����ڵ带 Return
       cardNumber : "123456" �Է� �� --> �����ڵ� Return




## �׽�Ʈ ����
##### 1. PostMan �� �̿��Ͽ� ���������� response ��� Ȯ��
##### 2. Junit �׽�Ʈ�� �̿��Ͽ� �� API �� ���� ����, �����ڵ� ��ȯ �� ����
###### �� API �� �׽�Ʈ�� Postman �� ���� �׽�Ʈ ���̽� ���� ��� ������ �Ǿ�����,
Junit �׽�Ʈ�� ����API ���� �׽�Ʈ �ۼ��� ��� �Ϸ�� �����̴�.
���,��ȸ�� ���� Junit �׽�Ʈ �ۼ��� ���� ���������� ������ȣ�� �������� �Է��Ͽ��߸� �����ϴ�.
