package com.proj.travel.service;

import com.proj.travel.constant.ErrorCode;
import com.proj.travel.exception.SiteException;
import com.proj.travel.model.dto.CityDto;
import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.Travel;
import com.proj.travel.model.request.CityCreateRequest;
import com.proj.travel.repository.CityRepository;
import com.proj.travel.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public CityDto createCity(CityDto cityDto) {
        City city = new City(cityDto);
        City save = cityRepository.save(city);
        return new CityDto(save);
    }

    public CityDto updateCity(CityDto cityDto) {
        City city = cityRepository.findById(cityDto.getCityId())
                .orElseThrow(() -> new SiteException(ErrorCode.NOT_FOUND_CITY));
        City build = City.builder().cityId(city.getCityId()).cityName(city.getCityName()).build();
        City save = cityRepository.save(build);
        return new CityDto(save);
    }

    public boolean deleteCity(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new SiteException(ErrorCode.NOT_FOUND_CITY));

        // 도시가 연결된 여행이 있다면 삭제 불가
        List<Travel> travels = travelRepository.findByCity(city);
        if(!travels.isEmpty()) {
            throw new SiteException(ErrorCode.DELETE_CITY_BAD_REQUEST);
        }

        cityRepository.delete(city);
        return true;
    }

    public CityDto findCity(Long id) {
        Optional<City> byId = cityRepository.findById(id);
        if (byId.isPresent()) {
            return new CityDto(byId.get());
        }
        return null;
    }

    public List<City> findCities() {
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

        return null;
    }
}
