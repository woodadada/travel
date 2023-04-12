package com.proj.travel.model.entity;

import com.proj.travel.model.dto.TravelDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * packageName   : com.proj.travel.model.entity
 * fileName      : Travel
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
@Table(name = "TRAVELS")
public class Travel extends BaseTimeEntity{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelId;

    @Column
    private String title;

    @OneToOne
    @JoinColumn(name = "city_id")
    private City city;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    public void setCity(City city) {
        this.city = city;
    }

    public Travel(TravelDto travelDto) {
        this.travelId = travelDto.getTravelId();
        this.title = travelDto.getTitle();
        this.city = City.builder().cityId(travelDto.getCityId()).build();
        this.startTime = travelDto.getStartTime();
        this.endTime = travelDto.getEndTime();
    }
}
