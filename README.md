# whereQR_Spring_version

> [기존 WhereQr](https://github.com/baeksoojin/whereQR) 에서 backend 부분을 변경하는 레포입니다.

- 큰 틀의 계획은 [ 기존 whereQr issue](https://github.com/baeksoojin/whereQR/issues/1)에서 확인가능합니다.
- 세부 일정관리 및 계획관리는 [notion kanban board](https://www.notion.so/WhereQr-b178e0e834044f5e9bd1bac471afcae2?p=f4454d1e1de843f2b982f659eab889d2&pm=s)를 통해서 확인이 가능합니다.
- 세부 **code** 작성관리 및 issue는 [해당프로젝트](https://github.com/baeksoojin/whereQR_Spring_version/projects?query=is%3Aopen)에서 확인 가능합니다.

## 🛠 setting

- 개발완료 후 docker multicontainer 하나의 앱으로 관리할 예정입니다.

```
docker compose up -d
```

- 개발진행중일 때는 backend는 직접 실행시켜줍니다.

frontend, database
```
docker compose up -d
```
컨테이너로 빌드합니다.
database는 본인의 password를 gitignore의 위치에서 ```db_password.txt```를 만들어 입력합니다.

backend
```
src/main/java/backend/BackendApplication 
```
Run을 시켜줍니다.

## 🎈python/Django 에서 java/Spring 로 전면 수정

- 애플리케이션 확장을 위해 더 객체지향적인 java를 사용해 개발
- python 코드중 쓸데없이 작성됐고 구조화 되어있지 않은 부분을 전부 없애거나 리팩토링 진행


