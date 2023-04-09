package com.proj.travel.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * packageName   : com.proj.travel.model.response
 * fileName      : APIResponse
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
@Getter
@Setter
@Builder
public class APIResponse<T> {
    private int code;
    private String errorMessage;
    private T result;

    public static <T> APIResponse<T> success(T data) {
        return APIResponse.<T>builder()
                .code(HttpStatus.OK.value())
                .result(data)
                .build();
    }
}
