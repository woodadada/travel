package com.proj.travel.service;

import com.proj.travel.constant.ErrorCode;
import com.proj.travel.exception.SiteException;
import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.Travel;
import com.proj.travel.repository.CityRepository;
import com.proj.travel.repository.TravelRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName   : com.proj.travel.service
 * fileName      : CityServiceTest
 * author        : kang_jungwoo
 * date          : 2023/04/09
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/09       kang_jungwoo         최초 생성
 */
@SpringBootTest
class CityServiceTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Test
    public void 도시생성() {
        //given
        City city = City.builder().cityName("바르셀로나").build();

        //when
        City saveCity = cityRepository.save(city);

        //then
        assertEquals(saveCity.getCityName(), city.getCityName());
    }

    @Test
    public void 도시수정() {
        //given
        City city = City.builder().cityName("마드리드").build();
        City saveCity = cityRepository.saveAndFlush(city);

        //when
        City updateCity = cityRepository.save(City.builder().cityId(saveCity
                .getCityId()).cityName("마드리드에서변경").build());

        //then
        assertEquals(updateCity.getCityName(), "마드리드에서변경");
    }

    @Test
    public void 도시삭제() {
        //given
        City city = City.builder().cityName("파리").build();
        City saveCity = cityRepository.saveAndFlush(city);

        //when
        cityRepository.delete(saveCity);
        Optional<City> byId = cityRepository.findById(saveCity.getCityId());

        //then
        assertFalse(byId.isPresent());
    }

    @Test
    public void 도시삭제시여행존재() {
        //given
        City city = City.builder().cityName("파리").build();
        City saveCity = cityRepository.saveAndFlush(city);

        LocalDateTime now = LocalDateTime.now();
        Travel travel = Travel.builder().title("파리 여행").city(city).startTime(now).endTime(now.plusDays(10)).build();
        travelRepository.saveAndFlush(travel);
        List<Travel> travels = travelRepository.findByCity(saveCity);

        //when
        //then
        Assertions.assertThrows(SiteException.class, () -> {
            if(!travels.isEmpty()) {
                throw new SiteException(ErrorCode.DELETE_CITY_BAD_REQUEST);
            }
        });
    }

    @Test
    public void 도시단일조회() {
        //given
        City city = City.builder().cityName("프랑크푸르트").build();
        City saveCity = cityRepository.save(city);

        //when
        Optional<City> byId = cityRepository.findById(city.getCityId());
        City findCity = byId.get();

        //then
        assertEquals(findCity.getCityName(), city.getCityName());
    }

    @Test
    public void 사용자별도시조회() {
        //given
        City city = City.builder().cityName("프랑크푸르트").build();
        City saveCity = cityRepository.save(city);

        //when
        Optional<City> byId = cityRepository.findById(city.getCityId());
        City findCity = byId.get();

        //then
        assertEquals(findCity.getCityName(), city.getCityName());
    }
}