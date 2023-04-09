package com.proj.travel.service;

import com.proj.travel.constant.ErrorCode;
import com.proj.travel.exception.SiteException;
import com.proj.travel.model.entity.*;
import com.proj.travel.repository.CityRepository;
import com.proj.travel.repository.CitySearchHistoryRepository;
import com.proj.travel.repository.TravelRepository;
import com.proj.travel.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private TravelService travelService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CitySearchHistoryService citySearchHistoryService;

    @Autowired
    private CitySearchHistoryRepository citySearchHistoryRepository;

    @Test
    void 도시생성() {
        //given
        City city = City.builder().cityName("바르셀로나").build();

        //when
        City saveCity = cityRepository.save(city);

        //then
        assertEquals(saveCity.getCityName(), city.getCityName());
    }

    @Test
    void 도시수정() {
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
    void 도시삭제() {
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
    void 도시삭제시여행존재() {
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
    void 도시단일조회() {
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
    void 사용자별도시조회() {
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
    void 여행중인도시() {
        //given
        User user = User.builder().name("고양이").build();
        User saveUser = userRepository.save(user);
        City city = City.builder().cityName("로마").build();
        City saveCity1 = cityRepository.save(city);
        City city2 = City.builder().cityName("시칠리아").build();
        City saveCity2 = cityRepository.save(city2);
        LocalDateTime now = LocalDateTime.now();

        // 여행 생성
        Travel travel = Travel.builder().city(saveCity1)
                .title(saveCity1.getCityName() + " 여행")
                .startTime(now.minusDays(1))
                .endTime(now.plusDays(10))
                .build();
        travelRepository.save(travel);
        // 예정 여행 추가
        Travel travel2 = Travel.builder().city(saveCity2)
                .title(saveCity2.getCityName() + " 여행")
                .startTime(now.plusDays(1))
                .endTime(now.plusDays(10))
                .build();
        travelRepository.save(travel2);

        // 예약 생성, 조회
        reservationService.createReservation(Reservation.builder().userId(saveUser.getUserId()).travel(travel).build());
        reservationService.createReservation(Reservation.builder().userId(saveUser.getUserId()).travel(travel2).build());

        List<Reservation> reservations = reservationService.getReservations(saveUser.getUserId());
        List<Travel> activeTravels = travelService.getActiveTravels(reservations);
        //when
        List<City> cities = activeTravels.stream().map(Travel::getCity).collect(Collectors.toList());

        //then
        assertTrue(!cities.isEmpty());
        assertEquals(cities.size(), 1);
        assertEquals(cities.get(0).getCityName(), "로마");
    }

    @Test
    void 여행예정도시() {
        //given
        User user = User.builder().name("강정우").build();
        User saveUser = userRepository.save(user);
        City city = City.builder().cityName("베를린").build();
        City saveCity1 = cityRepository.save(city);
        LocalDateTime now = LocalDateTime.now();

        // 여행 생성
        Travel travel = Travel.builder().city(saveCity1)
                .title(saveCity1.getCityName() + " 여행")
                .startTime(now.plusDays(7))
                .endTime(now.plusDays(17))
                .build();
        travelRepository.save(travel);

        // 예약 생성, 조회
        reservationService.createReservation(Reservation.builder().userId(saveUser.getUserId()).travel(travel).build());
        List<Reservation> reservations = reservationService.getReservations(saveUser.getUserId());
        List<Travel> futureTravels = travelService.getFutureTravels(reservations);
        //when
        List<City> cities = futureTravels.stream().map(Travel::getCity).collect(Collectors.toList());

        //then
        assertTrue(!cities.isEmpty());
        assertEquals(cities.get(0).getCityName(), "베를린");
    }

    @Test
    void 하루이내등록된도시() {
        //given
        City city = City.builder().cityName("맨시티").build();
        cityRepository.save(city);
        LocalDateTime now = LocalDateTime.now();
        //when
        List<City> cities = cityRepository.findTop10ByCreatedAtIsAfterOrderByCreatedAtDesc(now.minusDays(1));

        //then
        assertTrue(!cities.isEmpty());
        assertEquals(cities.size(), 10);
    }

    @Test
    void 일주일이내한번이상조회된도시() {
        //given
        LocalDateTime now = LocalDateTime.now();
        User user = User.builder().name("강아지").build();
        User saveUser = userRepository.save(user);
        City city = City.builder().cityName("멕시코시티").build();
        City saveCity = cityRepository.save(city);
        citySearchHistoryService.upsertCitySearchHistory(saveUser.getUserId(), saveCity.getCityId());

        //when
        List<CitySearchHistory> citySearchHistories = citySearchHistoryRepository.findTop10ByUserIdAndSearchedAtIsAfterOrderBySearchedAtDesc(saveUser.getUserId(), now.minusDays(7));

        //then
        assertTrue(!citySearchHistories.isEmpty());
        assertEquals(citySearchHistories.size(), 1);
        assertEquals(citySearchHistories.get(0).getCity().getCityName(), "멕시코시티");
    }

    @Test
    void 무작위도시() {
        City saveCity = cityRepository.save(City.builder().cityName("멕시코시티").build());
        City saveCity1 = cityRepository.save(City.builder().cityName("맨시티").build());

        Set<Long> cityIds = new HashSet<>();
        cityIds.add(saveCity.getCityId());
        cityIds.add(saveCity1.getCityId());
        List<City> cities = cityRepository.findByCityIdNotIn(cityIds);
        Collections.shuffle(cities);
        List<City> all = cityRepository.findAll();

        assertNotEquals(cities.size(), all.size());
    }
}