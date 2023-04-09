package com.proj.travel.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DictionaryKey {
    QUERY("query"),
    SORT("sort"),
    PAGE("page"),
    ;


    private final String key;
}
