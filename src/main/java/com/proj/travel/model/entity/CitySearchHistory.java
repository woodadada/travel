package com.proj.travel.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * packageName   : com.proj.travel.model.entity
 * fileName      : CitySearchHistory
 * author        : kang_jungwoo
 * date          : 2023/04/09
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/09       kang_jungwoo         최초 생성
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CITY_SEARCH_HISTORIES")
public class CitySearchHistory {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long citySearchHistoryId;

    Long userId;

    @OneToOne
    @JoinColumn(name = "city_id")
    private City city;

    LocalDateTime searchedAt;

    public void updateSearchedAt(LocalDateTime now) {
        this.searchedAt = now;
    }
}
