package com.proj.travel.controller;

import com.proj.travel.model.dto.CityDto;
import com.proj.travel.model.dto.TravelDto;
import com.proj.travel.model.response.APIResponse;
import com.proj.travel.service.TravelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
@RequestMapping("/api/v1/travel")
public class TravelController {

    private final TravelService travelService;

    // 여행 등록
    @PostMapping
    public APIResponse<TravelDto> createCity(@Valid @RequestBody TravelDto travelDto){
        return APIResponse.success(travelService.createTravel(travelDto));
    }

    // 여행 수정
    @PutMapping
    public APIResponse<TravelDto> updateCity(@Valid @RequestBody TravelDto travelDto){
        return APIResponse.success(travelService.updateTravel(travelDto));
    }

    // 여행 삭제
    @DeleteMapping("/{id}")
    public APIResponse<Boolean> deleteTravel(@PathVariable("id") long id){
        return APIResponse.success(travelService.deleteTravel(id));
    }

    // 단일 여행 조회
    @GetMapping("/{id}")
    public APIResponse<TravelDto> getTravel(@PathVariable("id") long id){
        return APIResponse.success(travelService.findTravel(id));
    }
}
