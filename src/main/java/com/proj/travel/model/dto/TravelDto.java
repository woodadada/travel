package com.proj.travel.model.dto;

import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.Travel;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * packageName   : com.proj.travel.model.dto
 * fileName      : TravelDto
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
@Getter
public class TravelDto {

    private Long travelId;

    @NotBlank(message = "여행 제목은 필수 입력 값입니다.")
    private String title;

    @NotNull(message = "도시는 필수 입력 값입니다.")
    private Long cityId;

    @NotNull(message = "여행 시작 날짜는 필수 입력 값입니다.")
    private LocalDateTime startTime;

    @NotNull(message = "여행 종료 날짜는 필수 입력 값입니다.")
    private LocalDateTime endTime;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public TravelDto(Travel travel) {
        this.travelId = travel.getTravelId();
        this.title = travel.getTitle();
        this.cityId = travel.getCity().getCityId();
        this.startTime = travel.getStartTime();
        this.endTime = travel.getEndTime();
        this.createdAt = travel.getCreatedAt();
        this.updatedAt = travel.getUpdatedAt();
    }
}
