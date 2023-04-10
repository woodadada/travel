package com.proj.travel.service;

import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.Travel;
import com.proj.travel.repository.CityRepository;
import com.proj.travel.repository.TravelRepository;
import com.proj.travel.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName   : com.proj.travel.service
 * fileName      : TravelServiceTest
 * author        : kang_jungwoo
 * date          : 2023/04/09
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/09       kang_jungwoo         최초 생성
 */
@SpringBootTest
class TravelServiceTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Test
    void 여행등록() {
        //given
        LocalDateTime now = LocalDateTime.now();
        City city = cityRepository.saveAndFlush(City.builder().cityName("캘리포니아").build());
        Travel build = Travel.builder().title(city.getCityName() + " 여행")
                .city(city)
                .startTime(now.minusDays(2))
                .endTime(now.plusDays(7)).build();
        //when
        Travel travel = travelRepository.save(build);

        //then
        assertEquals(travel.getCity().getCityName(), city.getCityName());
        assertEquals(travel.getCity().getCityId(), city.getCityId());
        assertTrue(travel.getStartTime().isBefore(now));
    }

    @Test
    void 여행수정() {
        //given
        LocalDateTime now = LocalDateTime.now();
        City city = cityRepository.saveAndFlush(City.builder().cityName("캘리포니아").build());
        City city1 = cityRepository.saveAndFlush(City.builder().cityName("샌프란시스코").build());
        Travel build = Travel.builder().title(city.getCityName() + " 여행")
                .city(city)
                .startTime(now.minusDays(2))
                .endTime(now.plusDays(7)).build();
        Travel travel = travelRepository.saveAndFlush(build);
        //when
        Travel updateTravel = travelRepository.saveAndFlush(Travel.builder()
                .travelId(travel.getTravelId())
                .city(city1)
                .startTime(travel.getStartTime())
                .endTime(travel.getEndTime()).build());

        //then
        assertEquals(updateTravel.getCity().getCityName(), city1.getCityName());
        assertNotEquals(updateTravel.getCity().getCityName(), city.getCityName());
    }

    @Test
    void 여행삭제() {
        //given
        LocalDateTime now = LocalDateTime.now();
        City city = cityRepository.saveAndFlush(City.builder().cityName("마이애미").build());
        Travel build = Travel.builder().title(city.getCityName() + " 여행")
                .city(city)
                .startTime(now.minusDays(2))
                .endTime(now.plusDays(7)).build();
        Travel travel = travelRepository.saveAndFlush(build);
        //when
        travelRepository.delete(travel);
        Optional<Travel> byId = travelRepository.findById(travel.getTravelId());

        //then
        assertTrue(byId.isEmpty());
    }

    @Test
    void 단일여행조회() {
        //given
        LocalDateTime now = LocalDateTime.now();
        City city = cityRepository.saveAndFlush(City.builder().cityName("프라하").build());
        Travel build = Travel.builder().title(city.getCityName() + " 여행")
                .city(city)
                .startTime(now.minusDays(2))
                .endTime(now.plusDays(7)).build();
        Travel travel = travelRepository.saveAndFlush(build);
        //when
        Optional<Travel> byId = travelRepository.findById(travel.getTravelId());
        Travel findTravel = byId.get();

        //then
        assertEquals(findTravel.getTravelId(), travel.getTravelId());
        assertEquals(findTravel.getCity().getCityName(), travel.getCity().getCityName());
    }

}