package com.proj.travel.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.proj.travel.model.dto.CityDto;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * packageName   : com.proj.travel.model.entity
 * fileName      : City
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
@Entity
@Table(name = "CITIES")
@EqualsAndHashCode
@ToString
public class City extends BaseTimeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;

    @Column
    private String cityName;

    public City(CityDto cityDto) {
        this.cityId = cityDto.getCityId();
        this.cityName = cityDto.getCityName();
//        this.createdAt = cityDto.getCreatedAt();
//        this.updatedAt = cityDto.getUpdatedAt();
    }
}
