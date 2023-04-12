package com.proj.travel.repository;

import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.CitySearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName   : com.proj.travel.repository
 * fileName      : CitySearchHistoryRepository
 * author        : kang_jungwoo
 * date          : 2023/04/09
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/09       kang_jungwoo         최초 생성
 */
@Repository
public interface CitySearchHistoryRepository extends JpaRepository<CitySearchHistory, Long> {
    List<CitySearchHistory> findTop10ByUserIdAndSearchedAtIsAfterOrderBySearchedAtDesc(Long userId, LocalDateTime now);

    CitySearchHistory findByUserIdAndCity(Long userId, City city);

    List<CitySearchHistory> findByCity(City city);
}
