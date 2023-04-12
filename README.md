# 여행관리 API 

## 개발 환경

### 기본 환경

  > IDE : IntelliJ IDEA Ultimate\
  > OS : Max OS

### Server
  > 언어 : Java\
  > 프레임워크 : SpringBoot\
  > Build : Gradle\
  > Test : JUnit5\
  > DB : H2\
  > ORM : JPA\
  > 접속 Base URI : `http://localhost:8080`

## 설계

- 레이어드 아키텍처
  - Controller, Service, Repository 계층 구조로 설계하였습니다.
  - Controller는 Client에게 받은 Parameter를 검사 후 Service 레이어로 요청합니다.
  - 비즈니스 로직은 Service 레이어에서 실행되며 Repository를 통해 CRUD를 진행합니다.
  - Client에 전달할 데이터는 DTO 클래스로 변환하여 전달됩니다.
  
## DB 설계
<img width="810" alt="image" src="https://user-images.githubusercontent.com/60130985/231522207-85d52247-2920-4215-b7ee-38e1c0c3f21b.png">

## API 기능 명세

- 프로젝트 파일 최상단`travel.postman_collection.json`파일을 포스트맨에 import하시면 편리하게 테스트가 가능합니다.
- 원활한 테스트를 위해 Application 실행 시 10명의 유저, 10개의 도시, 10개의 여행, 9개의 예약 데이터를 생성합니다.
- 도시 삭제 시 여행과 연결되어 있다면 삭제가 불가능합니다.
- 여행 삭제 시 예약과 연결되어 있다면 삭제가 불가능합니다.

### 1. 도시 등록 API
- POST `/api/v1/city`
  - body
```
{
    "cityName" : "테스트1"
}
```
- Request
```
http://localhost:8080/api/v1/city
```
- Response
```
{
    "code": 200,
    "errorMessage": null,
    "result": {
        "cityId": 11,
        "cityName": "테스트1",
        "createdAt": "2023-04-13T01:47:55.941613",
        "updatedAt": "2023-04-13T01:47:55.941613"
    }
}
```


### 2. 도시 수정 API
- PUT `/api/v1/city`
  - body
```
{
    "cityId" : 11,
    "cityName" : "테스트수정12222"
}
```
- Request
```
http://localhost:8080/api/v1/city
```
- Response
```
{
    "code": 200,
    "errorMessage": null,
    "result": {
        "cityId": 11,
        "cityName": "테스트수정12222",
        "createdAt": null,
        "updatedAt": null
    }
}
```


### 3. 도시 삭제 API
- DELETE `/api/v1/city/11`
  - pathVariable
    - cityId
- Request
```
http://localhost:8080/api/v1/city/11
```
- Response
```
{
    "code": 200,
    "errorMessage": null,
    "result": true
}
```
- 도시 삭제 시 여행에 등록된 도시라면 `DELETE_CITY_BAD_REQUEST` 오류를 반환합니다.

### 4. 단일 도시 조회 API
- GET `/api/v1/city/5?userid=5`
  - pathVariable
    - cityId
  - parameter
    - userid 
      - 도시검색 히스토리 데이터를 적재하기 위해서 필요합니다.(사용자별도시조회 API 일주일 이내 조회한 도시)
      - 1번부터 10번까지 10명의 유저가 등록되어 있습니다.
- Request
```
http://localhost:8080/api/v1/city/5?userid=5
```
- Response
```
{
    "code": 200,
    "errorMessage": null,
    "result": {
        "cityId": 5,
        "cityName": "라스베가스",
        "createdAt": "2023-04-13T02:06:02.826785",
        "updatedAt": "2023-04-13T02:06:02.826785"
    }
}
```

### 5. 사용자별 도시 목록 조회 API
- GET `http://localhost:8080/api/v1/city/user/9`
  - pathVariable
    - userId
- Request
```
http://localhost:8080/api/v1/city/user/9
```
- Response
```
{
    "code": 200,
    "errorMessage": null,
    "result": [
        {
            "cityId": 9,
            "cityName": "베이징",
            "createdAt": "2023-04-13T02:11:03.852737",
            "updatedAt": "2023-04-13T02:11:03.852737"
        },
        {
            "cityId": 10,
            "cityName": "카이로",
            "createdAt": "2023-04-13T02:11:03.853428",
            "updatedAt": "2023-04-13T02:11:03.853428"
        },
        {
            "cityId": 9,
            "cityName": "베이징",
            "createdAt": "2023-04-13T02:11:03.852737",
            "updatedAt": "2023-04-13T02:11:03.852737"
        },
        ...
    ]
}
```

### 6. 여행 등록 API
- POST `/api/v1/travel`
  - body
```
{
    "title" : "여행 저장 테스트",
    "cityId" : 10,
    "startTime" : "2023-04-15T12:00:00",
    "endTime" : "2023-04-29T12:00:00"
}
```
- Request
```
http://localhost:8080/api/v1/travel
```
- Response
```
{
    "code": 200,
    "errorMessage": null,
    "result": {
        "travelId": 11,
        "title": "여행 저장 테스트",
        "cityId": 10,
        "startTime": "2023-04-15T12:00:00",
        "endTime": "2023-04-29T12:00:00",
        "createdAt": "2023-04-13T02:14:11.723115",
        "updatedAt": "2023-04-13T02:14:11.723115"
    }
}
```
- 여행 종료일이 현재 시간 이전이라면 `TRAVEL_END_TIME_ERROR` 에러를 반환합니다. 


### 7. 여행 수정 API
- PUT `/api/v1/travel`
  - body
```
{
    "travelId" : 11,
    "title" : "여행 저장 테스트 수정~~~!!",
    "cityId" : 10,
    "startTime" : "2023-04-04T12:00:00",
    "endTime" : "2023-04-29T12:00:00"
}
```
- Request
```
http://localhost:8080/api/v1/travel
```
- Response
```
{
    "code": 200,
    "errorMessage": null,
    "result": {
        "travelId": 11,
        "title": "여행 저장 테스트 수정~~~!!",
        "cityId": 10,
        "startTime": "2023-04-04T12:00:00",
        "endTime": "2023-04-29T12:00:00",
        "createdAt": null,
        "updatedAt": null
    }
}
```

### 8. 여행 삭제 API
- DELETE `/api/v1/travel/11`
  - pathVariable
    - travelId
- Request
```
http://localhost:8080/api/v1/travel/11
```
- Response
```
{
    "code": 200,
    "errorMessage": null,
    "result": true
}
```
- 예약 데이터에 연결된 데이터라면 `DELETE_RESERVATION_BAD_REQUEST` 에러를 반환합니다.

### 9. 단일 여행 조회 API
- GET `/api/v1/travel/10`
  - pathVariable
    - travelId
- Request
```
http://localhost:8080/api/v1/travel/10
```
- Response
```
{
    "code": 200,
    "errorMessage": null,
    "result": {
        "travelId": 10,
        "title": "카이로 여행",
        "cityId": 10,
        "startTime": "2023-04-03T02:11:03.775184",
        "endTime": "2023-05-03T02:11:03.775184",
        "createdAt": "2023-04-13T02:11:03.86854",
        "updatedAt": "2023-04-13T02:11:03.86854"
    }
}
```
