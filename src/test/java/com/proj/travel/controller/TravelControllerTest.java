package com.proj.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.travel.model.dto.CityDto;
import com.proj.travel.model.dto.TravelDto;
import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.Travel;
import com.proj.travel.repository.CityRepository;
import com.proj.travel.repository.TravelRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * packageName   : com.proj.travel.controller
 * fileName      : TravelControllerTest
 * author        : kang_jungwoo
 * date          : 2023/04/11
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/11       kang_jungwoo         최초 생성
 */
@SpringBootTest
@AutoConfigureMockMvc
class TravelControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private CityRepository cityRepository;

    @Test
    void 여행등록() throws Exception {
        //given
        List<City> cities = cityRepository.findAll();
        City city = cities.get(0);
        LocalDateTime now = LocalDateTime.now();

        TravelDto travelDto = TravelDto.builder()
                .cityId(city.getCityId())
                .title(city.getCityName() + "로 떠나는 즐거운 여행")
                .startTime(now.plusDays(1))
                .endTime(now.plusDays(10))
                .build();
        //when
        //then
        String content = objectMapper.writeValueAsString(travelDto);
        mockMvc.perform(post("/api/v1/travel")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void 여행수정() throws Exception {
        //given
        List<Travel> travels = travelRepository.findAll();
        List<City> cities = cityRepository.findAll();

        Travel travel = travels.get(0);
        TravelDto travelDto = TravelDto.builder()
                .travelId(travel.getTravelId()).cityId(cities.get(0).getCityId())
                .title(travel.getTitle())
                .startTime(travel.getStartTime())
                .endTime(travel.getEndTime())
                .build();
        //when
        //then
        String content = objectMapper.writeValueAsString(travelDto);
        mockMvc.perform(put("/api/v1/travel")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void 여행삭제() throws Exception {
        //given
        List<Travel> travels = travelRepository.findAll();
        System.out.println("travels = " + travels);
        List<Travel> filterList = travels.stream().filter(travel -> travel.getCity().getCityName().equals("남극")).collect(Collectors.toList());
        Travel travel = filterList.get(0);
        //when
        //then
        mockMvc.perform(delete("/api/v1/travel/{id}", travel.getTravelId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void 여행단일조회() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/v1/travel/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}