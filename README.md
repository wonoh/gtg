# GTG

## What is GTG?

GTG는 학생에게 맞는 시간표를 제공하는 웹 서비스입니다. GTG는 Spring Boot 기반으로 동작하며 Naver 로그인을 지원합니다.

다음은 GTG에 사용되는 구성요소입니다.

- Spring Boot
  - Web
  - JPA
  - MySQL
  - Security
  - thymeleaf
  - lombok
- Jsoup (crawling)
- Adminlte 3
- etc.

## Getting Started

GTG를 시작하기 위해서는 다음의 설정이 필요합니다.

- datasource (데이터베이스 정보)
- Naver Login Open API (client ID, client Secret)

`datasource`는 GTG가 사용할 데이터베이스에 관한 설정입니다. 각 실행 환경에 맞게 해당 값을 변경 및 설정하면 됩니다.

`client ID`, `client Secret`은 네이버 아이디로 로그인하기 위해 필요한 값들 입니다. [네이버 개발자센터](https://developers.naver.com/docs/common/openapiguide/appregister.md#%ED%81%B4%EB%9D%BC%EC%9D%B4%EC%96%B8%ED%8A%B8-%EC%95%84%EC%9D%B4%EB%94%94%EC%99%80-%ED%81%B4%EB%9D%BC%EC%9D%B4%EC%96%B8%ED%8A%B8-%EC%8B%9C%ED%81%AC%EB%A6%BF-%ED%99%95%EC%9D%B8)에서 애플리케이션을 등록 후 해당 값을 받을 수 있습니다.

이후 **application.properties** 파일에 해당 값들을 입력합니다.

```java
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://URL_OR_ADDRESS/DATABASE_NAME?useSSL=false
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_USER_PASSWORD

naver.client.clientId=YOUR_CLIENT_ID
naver.client.clientSecret=YOUR_CLIENT_SECRET
``` 



