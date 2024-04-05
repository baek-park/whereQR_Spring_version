# whereQR_Spring_version
> [기존 WhereQr](https://github.com/baeksoojin/whereQR) 에서 backend 부분을 변경하는 레포입니다.
- Backend:  🎈python/Django 에서 java/Spring 로 전면 수정
- Frontend : React유지

**whereQR이 무엇인가요??**

## [whereQR DEV 구경가기](https://where-qr.com/)

### 가장 손쉬운 분실물과 습득자의 연락 플랫폼, whereQR

## Architecture

<img width="697" alt="image" src="https://github.com/baek-park/whereQR_Spring_version/assets/74058047/3737da91-e372-4ed9-af12-a5abefb1a25d">


## 🌱 api 

### Api Document

- 협업을 위해, localhost가 아닌 EC2의 탄력적 ip를 사용해 api를 제공
**[postman](https://documenter.getpostman.com/view/31138114/2sA2xiVBD3)** 에서 체크 가능

### dockerfile build후 docker hub에 배포
1. docker hub에 배포된 image 를 NCP Server에서 배포
2. qrcode image 저장된 경로 ( docker container 안에서 확인 -> /app/src/main/resources/static/qrcode )

## Feature

- 소개 페이지
<img width="346" alt="image (1)" src="https://github.com/baek-park/whereQR_Spring_version/assets/74058047/da17cf0f-e0ee-4431-9cc2-317b417541b1">

- 습득자와 분실자의 프로세스
<img width="1156" alt="image" src="https://github.com/baek-park/whereQR_Spring_version/assets/74058047/81832511-43b6-48e9-a095-05b050ec0ae6">
<img width="1228" alt="image" src="https://github.com/baek-park/whereQR_Spring_version/assets/74058047/9c16345a-cecf-4231-a64c-bf4203ab7872">


