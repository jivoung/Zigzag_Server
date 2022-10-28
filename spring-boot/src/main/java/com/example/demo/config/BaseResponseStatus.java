package com.example.demo.config;

import lombok.Getter;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,2003,"권한이 없는 유저의 접근입니다."),

    // users
    USERS_EMPTY_USER_ID(false, 2010, "유저 아이디를 입력해주세요."),

    //review-evaluation
    USERS_EMPTY_EVAL_VALUE(false, 2033, "평가 값을 입력해주세요."),
    INVALID_REVIEW_ID(false, 2034, "존재하지 않는 리뷰 아이디입니다."),
    USERS_EMPTY_REVIEW_ID(false, 2035, "리뷰 아이디를 입력해주세요."),

    //coupon
    EMPTY_COUPON_ID(false, 2031, "쿠폰 아이디를 입력해주세요."),
    INVALID_COUPON_ID(false, 2032, "존재하지 않는 쿠폰 아이디입니다."),

    // [POST] /users
    // [POST] /users/logIn
    POST_USERS_INVALID_PHONENUM(false, 2015, "올바른 휴대폰 번호를 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, 2016, "올바른 이메일을 입력해주세요."),
    POST_USERS_EXISTS_EMAIL(false,2017,"이미 가입된 이메일입니다."),
    POST_USERS_INVALID_PASSWORD(false, 2018, "8자 이상 입력해주세요."),

    // product
    USERS_EMPTY_PRODUCT_ID(false, 2020, "상품 아이디를 입력해주세요."),
    USERS_EMPTY_STORE_ID(false, 2021, "스토어 아이디를 입력해주세요."),
    USERS_EMPTY_PRODUCT_DETAIL_ID(false, 2022, "상품 디테일 아이디를 입력해주세요."),
    USERS_EMPTY_COUNT_ID(false, 2023, "갯수를 입력해주세요."),
    USERS_EMPTY_PRICE_ID(false, 2024, "가격을 입력해주세요."),
    INVALID_STORE_ID(false, 2026, "존재하지 않는 스토어 아이디입니다."),
    INVALID_CATEGORY(false, 2029, "존재하지 않는 카테고리 아이디입니다."),
    INVALID_ORDER_PRODUCT_ID(false, 2030, "존재하지 않는 주문배송 아이디입니다."),

    USERS_EMPTY_FOLDER_ID(false, 2025, " 폴더 아이디를 입력해주세요."),
    USERS_EMPTY_FOLDER_NAME(false, 2026, " 폴더명을 입력해주세요."),


    //search
    SEARCH_EMPTY_KEYWORD(false, 2025, "키워드를 입력해주세요."),

    //tag
    INVALID_TAG_KEYWORD(false, 2027, "존재하지 않는 키워드입니다."),
    TAGS_EMPTY_KEYWORD(false, 2028, "키워드를 입력해주세요."),

    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),

    // [POST] /users/logIn
    FAILED_TO_LOGIN(false,3014,"이메일 또는 비밀번호가 틀립니다."),

    //search
    FAILED_TO_SEARCH(false,3015,"검색 결과가 없습니다."),
    FAILED_TO_SEARCH_REGISTER(false,3016,"키워드 등록이 불가합니다."),
    FAILED_TO_SEARCH_USER(false,3017,"유저가 검색한 키워드가 없습니다."),
    DELETE_FAIL_KEYWORD(false,3018,"키워드 삭제가 불가합니다."),

    //product
    DELETE_FAIL_ORDER_PRODUCT(false,3023,"해당 상품 삭제가 불가합니다."),

    //coupon
    FAILED_TO_COUPON_REGISTER(false,3024,"이미 등록한 쿠폰입니다."),
    FAILED_TO_STORE_FAVORITE_COUPON_REGISTER(false,3025,"즐겨찾기 등록을 하시겠습니까?"),

    //review-evaluation
    FAILED_TO_REVIEW_EVAL_REGISTER(false,3026,"리뷰 평가가 불가합니다"),
    FAILED_TO_REVIEW_EVAL_UPDATE(false,3027,"리뷰 평가 변경이 불가합니다."),
    FAILED_TO_REVIEW_EVAL_DELETE(false,3028,"리뷰 평가 취소가 불가합니다."),

    //store-favorite
    FAILED_TO_FAVORITE_REGISTER(false,3019,"즐겨찾기 등록이 불가합니다."),
    FAILED_TO_FAVORITE_DELETE(false,3020,"즐겨찾기 취소가 불가합니다."),

    //tag
    FAILED_TO_TAG_REGISTER(false,3021,"태그 등록이 불가합니다."),
    DELETE_FAIL_TAG(false,3022,"태그 삭제가 불가합니다."),


    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),

    //[PATCH] /users/{userIdx}
    MODIFY_FAIL_USERNAME(false,4014,"유저네임 수정 실패"),

    PASSWORD_ENCRYPTION_ERROR(false, 4011, "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, 4012, "비밀번호 복호화에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
