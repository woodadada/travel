package com.proj.travel.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "리프레시 토큰이 유효하지 않습니다"),
    DELETE_CITY_BAD_REQUEST(BAD_REQUEST, "여행 데이터에 연결된 도시입니다."),
    NOT_FOUND_CITY(BAD_REQUEST, "도시를 찾을 수 없습니다."),
    SEARCH_API_SEVER_ERROR(INTERNAL_SERVER_ERROR, "API 서버 통신이 정상적이지 않습니다."),

    /* 409 CONFLICT : Resource 의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(CONFLICT, "데이터가 이미 존재합니다"),

    /* 500 INTERNAL_SERVER_ERROR : 서버에서 오류 발생 */
    ;

    private final HttpStatus httpStatus;
    private final String detail;
}