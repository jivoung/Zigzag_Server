# Zigzag_Server

지그재그 서버 클론코딩
  <br /><br />
  프로젝트 수행기간
  2021.10.30 ~ 2021.11.11
  
  <br /> <br />
  프로젝트 기획서 링크
    - https://docs.google.com/document/d/1NVAXYba0Fus0O7YMNdzyo7cReB0HugSb/edit
  
  API 명세서 링크 
    - https://docs.google.com/spreadsheets/d/1_waFfNO5WE9wbpgFeNqQYrXh3gA_OzHE/edit#gid=446451871
  <br /> <br />

 ---------

<br /><br />
> 2021.10.30.토

  - 깃허브에 템플릿 업로드
  - application.yml 파일에 구축해둔 RDS 서버 계정 등록
  - 도메인 등록
  - SSL 인증
  - 서브 도메인 등록 (dev/prod)
  - 리다이렉트
    - HTTP -> HTTPS
    - IP -> Domain
  - ERD 설계

<br /><br /> 
> 2021.10.31.일
  - ERD 관련 질문 사항 정리
  - 피드백 후 ERD 수정
  - 프로젝트 기획서 수정
  - API 명세서 작성
  
<br /><br /> 
> 2021.11.01.월
  - API 명세서 수정 및 질문사항 정리
  - 피드백 후 명세서 수정
  - ERD 수정
  - 테스트 데이터 추가

<br /><br />
> 2021.11.02.화
  - ERD 수정 및 테스트 데이터 추가
  - 회원가입 API 
  - 로그인 API 
  - 특정 유저 조회 (마이페이지 상단) API 
  - 상품 검색 결과 조회 API 
  - 스토어 검색 결과 조회 API 
  - 지금 인기있는 검색 키워드 조회 API 
  - 검색 키워드 등록 API 
  - 내가 찾아 봤던 검색 키워드 조회 API 
  - 내가 찾아 봤던 검색 키워드 전체 삭제 API 
  - ERD 설명 영상 제작
 
 <br /><br />
 > 2021.11.03.수
  - 1차 피드백 전 질문 사항 정리
  - EC2 서버 배포 테스트 <br />
    🔻 자바 파일 실행 도중에 발생한 오류 - 1 <br />
       Logging system failed to initialize using configuration from 'null' java.lang.IllegalStateException: Logback configuration error detected:
       ERROR in ch.qos.logback.core.rolling.RollingFileAppender[APP_FILE] - openFile(logs/app.log,true) call failed. java.io.FileNotFoundException: 
       logs/app.log (Permission denied) <br />
       ✔ sudo chmod 777 logs/app.log 입력하여 해결 <br />
    🔻 자바 파일 실행 도중에 발생한 오류 - 2 <br />
       Logging system failed to initialize using configuration from 'null' java.lang.IllegalStateException: Logback configuration error detected:
       ERROR in ch.qos.logback.core.rolling.RollingFileAppender[APP_FILE] - openFile(logs/error.log,true) call failed. java.io.FileNotFoundException: 
       logs/error.log (Permission denied) <br />
       ✔ sudo chmod 777 logs/error.log 입력하여 해결
  - ERD 최종 수정
  - API 명세서 수정
  - 스토어 - 쇼핑몰 랭킹 순으로 조회 API
  - 스토어 - 브랜드 랭킹 순으로 조회 API
  - 스토어 즐겨찾기 조회 API
  - 스토어 즐겨찾기 등록 API
  - 스토어 즐겨찾기 취소 API

<br /><br />
 > 2021.11.04.목
  - 1차 피드백 후 변경된 내용 <br />
    ◾ API uri 명사형으로 변경 <br />
    ◾ 비회원, 회원 모두 사용 가능한 API는 회원용 API로 간주하여 개발 <br />
    ◾ Category 테이블 계층화 <br />
    ◾ 구매하기, 소셜로그인 등 API는 낮은 우선순위로 변경 <br />
  - 즐겨찾기한 스토어 스토리 목록 조회 API
  - 즐겨찾기한 스토어 스토리 상세 조회 API
  - 전체 태그 조회 API
  - 태그별 스토어 조회 API
  - 태그 등록 및 삭제 API
  - 최근 태그 조회 API
  - 추천 태그 조회 API
  - 상품 모아보기 API
  - 특정 스토어 기본 정보 조회 API
  - 스토어 이번주 판매 베스트 상품 목록 조회 API
  - 스토어 전체 상품 조회 API
  - 유저 멤버십 등급 조회 API
  - 전체 멤버십 등급 조회 API

<br /><br />
 > 2021.11.05.금
  - 전체 주문/배송 상품 조회 API
  - 특정 상품 주문 정보 조회 API
  - 특정 상품 주문 정보 삭제 API
  - 리뷰에 대한 평가 등록 API
  - 리뷰에 대한 평가 취소 API
  - 쿠폰 등록 API
  - 스토어 쿠폰 내려받기 API
  - 스토어별 쿠폰 조회 API
  - 보유한 쿠폰 조회 API
  - 완료/만료된 쿠폰 조회 API
  - 보유한 포인트 정보 조회 API

<br /><br />
 > 2021.11.07.일
  - 유저 생성 (회원가입) API validation 처리
  - 로그인 API validation 처리 
  - 특정 유저 조회 (마이페이지 상단) API validation 처리
  - 상품 검색 결과 조회 API validation 처리
  - 스토어 검색 결과 조회 API validation 처리
  - 지금 인기있는 검색 키워드 조회 API validation 처리
  - 검색 키워드 등록 API validation 처리
  - 내가 찾아 봤던 키워드 조회 API validation 처리
  - 내가 찾아 봤던 키워드 전체 삭제 API validation 처리

<br /><br />
 > 2021.11.08.월
  - 나머지 API들에 대한 validation 처리
  - 2차 피드백 질문 사항 정리<br />
  - EC2 서버 접속 <br />
  🔻 서버 접속 도중에 발생한 오류 <br />
       백그라운드 서버 실행 중 강제로 WinSCP를 종료한 후 발생한 문제<br />
       이후 재접속에서 "연결시간초과" 오류 발생<br />
       ✔ EC2 인스턴스 재부팅 이후 10분 정도 기다린 다음 해결 <br />
  
<br /><br />
 > 2021.11.09.화
  - 2차 피드백 내용 <br />
    ◾ API 생산성보다 코드 리팩토링에 집중 <br />
    ◾ API validation 꼼꼼하게 처리 <br />
  - SSL 인증 오류 해결
  - 프록시패스 설정 완료 <br />
    ✔ 프록시 패스 설정 후 SSL 인증된 도메인으로 리다이렉션 되도록 하여 해결
  - Query String 관련 API 구현 마무리

    
<br /><br />
 > 2021.11.11.목
  - 실제 데이터 삽입
  - 전체 API 점검 
  - 포스트맨 실행 영상 촬영

