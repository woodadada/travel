package com.proj.travel.exception;

import com.proj.travel.constant.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SiteException extends RuntimeException {
    private final ErrorCode errorCode;
}