package com.proj.travel.model.response;

import com.proj.travel.model.dto.CityDto;
import lombok.Getter;

/**
 * packageName   : com.proj.travel.model.response
 * fileName      : CityCreateResponse
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
@Getter
public class CityCreateResponse {

    private CityDto result;

    public CityCreateResponse(CityDto result) {
        this.result = result;
    }
}
