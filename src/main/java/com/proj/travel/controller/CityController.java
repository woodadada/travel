package com.proj.travel.controller;

import com.proj.travel.model.dto.CityDto;
import com.proj.travel.model.request.CityCreateRequest;
import com.proj.travel.model.response.APIResponse;
import com.proj.travel.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * packageName   : com.proj.travel.controller
 * fileName      : TravelController
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/city")
public class CityController {

    private final CityService cityService;

    // 도시 등록
    @PostMapping("/")
    public APIResponse<CityDto> createCity(@Valid @RequestBody CityDto cityDto){
        return APIResponse.success(cityService.createCity(cityDto));
    }

    // 도시 수정
    @PutMapping("/")
    public APIResponse<CityDto> updateCity(@Valid @RequestBody CityDto cityDto){
        return APIResponse.success(cityService.updateCity(cityDto));
    }

    // 도시 삭제
    @DeleteMapping("/{id}")
    public APIResponse<Boolean> deleteCity(@PathVariable("id") long id){
        return APIResponse.success(cityService.deleteCity(id));
    }

    // 단일 도시 조회
    @GetMapping("/{id}")
    public APIResponse<CityDto> getCity(@PathVariable("id") long id, @RequestParam(name = "userid") long userId){
        return APIResponse.success(cityService.findCity(id, userId));
    }

    // 사용자별 도시 목록 조회
    @GetMapping("/user/{id}")
    public APIResponse<List<CityDto>> findCityByUserId(@PathVariable("id") long userId){
        return APIResponse.success(cityService.findCitiesByUserId(userId));
    }

}
