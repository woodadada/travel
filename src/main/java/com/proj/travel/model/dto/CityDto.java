package com.proj.travel.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.proj.travel.model.entity.City;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * packageName   : com.proj.travel.model.dto
 * fileName      : CityDto
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {

    private Long cityId;

    @NotBlank(message = "도시명은 필수 입력 값입니다.")
    private String cityName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public CityDto(City city) {
        this.cityId = city.getCityId();
        this.cityName = city.getCityName();
        this.createdAt = city.getCreatedAt();
        this.updatedAt = city.getUpdatedAt();
    }
}
