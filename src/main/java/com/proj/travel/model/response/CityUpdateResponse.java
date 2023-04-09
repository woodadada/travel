package com.proj.travel.model.response;

import com.proj.travel.model.dto.CityDto;

/**
 * packageName   : com.proj.travel.model.response
 * fileName      : CityUpdateResponse
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
public class CityUpdateResponse {

    private CityDto result;

    public CityUpdateResponse(CityDto result) {
        this.result = result;
    }
}
