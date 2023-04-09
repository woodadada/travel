package com.proj.travel.service;

import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.CitySearchHistory;
import com.proj.travel.repository.CitySearchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * packageName   : com.proj.travel.service
 * fileName      : CitySearchHistoryService
 * author        : kang_jungwoo
 * date          : 2023/04/09
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/09       kang_jungwoo         최초 생성
 */
@Service
@RequiredArgsConstructor
public class CitySearchHistoryService {

    private final CitySearchHistoryRepository citySearchHistoryRepository;

    public void createCitySearchHistory(Long userId, Long cityId) {
        CitySearchHistory build = CitySearchHistory.builder()
                .userId(userId)
                .city(City.builder().cityId(cityId).build())
                .searchedAt(LocalDateTime.now())
                .build();

        citySearchHistoryRepository.save(build);
    }

    @Transactional
    public void updateCitySearchHistory(Long userId, Long cityId) {
        CitySearchHistory byUserIdAndCity = citySearchHistoryRepository.findByUserIdAndCity(userId, City.builder().cityId(cityId).build());
        byUserIdAndCity.updateSearchedAt(LocalDateTime.now());
    }

    @Transactional
    public void upsertCitySearchHistory(Long userId, Long cityId) {
        CitySearchHistory byUserIdAndCity = citySearchHistoryRepository.findByUserIdAndCity(userId, City.builder().cityId(cityId).build());
        if(byUserIdAndCity != null) {
            updateCitySearchHistory(userId, cityId);
        }else{
            createCitySearchHistory(userId, cityId);
        }
    }
}
