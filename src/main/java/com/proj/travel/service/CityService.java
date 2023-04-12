package com.proj.travel.service;

import com.proj.travel.constant.ErrorCode;
import com.proj.travel.exception.SiteException;
import com.proj.travel.model.dto.CityDto;
import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.CitySearchHistory;
import com.proj.travel.model.entity.Reservation;
import com.proj.travel.model.entity.Travel;
import com.proj.travel.repository.CityRepository;
import com.proj.travel.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * packageName   : com.proj.travel.service
 * fileName      : CityService
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;
    private final TravelRepository travelRepository;
    private final TravelService travelService;
    private final ReservationService reservationService;
    private final CitySearchHistoryService citySearchHistoryService;


    private final int MAX_SIZE = 10;

    public CityDto createCity(CityDto cityDto) {
        City city = new City(cityDto);
        City save = cityRepository.save(city);
        return new CityDto(save);
    }

    @Transactional
    public CityDto updateCity(CityDto cityDto) {
        City city = cityRepository.findById(cityDto.getCityId())
                .orElseThrow(() -> new SiteException(ErrorCode.NOT_FOUND_CITY));
        City build = City.builder().cityId(city.getCityId()).cityName(cityDto.getCityName()).build();
        City save = cityRepository.save(build);
        return new CityDto(save);
    }

    @Transactional
    public boolean deleteCity(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new SiteException(ErrorCode.NOT_FOUND_CITY));

        // 도시가 연결된 여행이 있다면 삭제 불가
        List<Travel> travels = travelRepository.findByCity(city);
        if(!travels.isEmpty()) {
            throw new SiteException(ErrorCode.DELETE_CITY_BAD_REQUEST);
        }

        List<CitySearchHistory> citySearchHistories = citySearchHistoryService.getCitySearchHistories(city);
        for (CitySearchHistory citySearchHistory : citySearchHistories) {
            citySearchHistory.setCity(null);
        }

        cityRepository.delete(city);
        return true;
    }

    public CityDto findCity(Long id, Long userId) {
        Optional<City> byId = cityRepository.findById(id);
        if (byId.isPresent()) {
            citySearchHistoryService.upsertCitySearchHistory(userId, id);
            return new CityDto(byId.get());
        }
        return null;
    }

    // 사용자 별 도시 목록 조회
    public List<CityDto> findCitiesByUserId(Long userId) {
        /**
         * 사용자별 도시 목록 조회 API
         * 기본적으로 중복 없이 상위 10개 도시만 노출 (Pagination 없음)
         * 단, 여행 중인 도시는 중복이 허용되며 노출 개수와 무관
         * 도시 노출 순서 (위에서 아래 순서대로 노출)
         * 여행 중인 도시 : 여행 시작일이 빠른 것부터
         * 여행이 예정된 도시 : 여행 시작일이 가까운 것부터
         * 하루 이내에 등록된 도시 : 가장 최근에 등록한 것부터
         * 최근 일주일 이내에 한 번 이상 조회된 도시 : 가장 최근에 조회한 것부터
         * 위의 조건에 해당하지 않는 모든 도시 : 무작위
         */
        LocalDateTime now = LocalDateTime.now();
        List<City> tempList = new ArrayList<>();

        List<Reservation> reservations = reservationService.getReservations(userId);
        //여행 중인 도시 : 여행 시작일이 빠른 것부터
        List<City> activeCities = getCitiesByTravels(travelService.getActiveTravels(reservations));

        //여행이 예정된 도시 : 여행 시작일이 가까운 것부터
        List<City> futureCities = getCitiesByTravels(travelService.getFutureTravels(reservations));
        if(!ObjectUtils.isEmpty(futureCities)){
            tempList.addAll(futureCities);
        }

        // 하루 이내 등록된 도시
        List<City> addedCities = cityRepository.findTop10ByCreatedAtIsAfterOrderByCreatedAtDesc(now.minusDays(1));
        if(!ObjectUtils.isEmpty(addedCities)){
            tempList.addAll(addedCities);
        }

        // 최근 일주일 이내에 한번 이상 조회된 도시
        List<City> searchedCities = citySearchHistoryService.getSearchedCities(userId);
        if(!ObjectUtils.isEmpty(searchedCities)){
            tempList.addAll(searchedCities);
        }

        // 제외할 도시 식별번호
        Set<Long> cityIds = tempList.stream().map(City::getCityId).collect(Collectors.toSet());
        List<City> randomCities = getRandomCities(cityIds);
        // 위의 조건에 해당하지 않는 모든 도시 : 무작위
        if(tempList.size() < MAX_SIZE && !ObjectUtils.isEmpty(randomCities)) {
            int addSize = MAX_SIZE - tempList.size();
            List<City> cities = randomCities.subList(0, addSize - 1);
            tempList.addAll(cities);
        }

        // 여행 중 도시는 무조건 추가, 중복 가능
        List<City> resultCities = new ArrayList<>();
        if(!ObjectUtils.isEmpty(activeCities)){
            resultCities.addAll(activeCities);
        }

        // 중복 제거
        List<City> filterList = tempList.stream().distinct().collect(Collectors.toList());

        if(filterList.size() < MAX_SIZE) {
            resultCities.addAll(tempList);
        }else if(filterList.size() >= MAX_SIZE){
            resultCities.addAll(filterList.subList(0, MAX_SIZE - 1));
        }

        List<CityDto> cityDtos = resultCities.stream().map(city -> new CityDto(city)).collect(Collectors.toList());

        return cityDtos;
    }

    public List<City> getCitiesByTravels(List<Travel> travels) {
        return travels.stream().map(Travel::getCity).collect(Collectors.toList());
    }

    public List<City> getRandomCities(Collection<Long> idList) {
        List<City> cities = cityRepository.findByCityIdNotIn(idList);
        if(cities.isEmpty()){
            return null;
        }
        Collections.shuffle(cities);

        return cities;
    }
}
