package com.proj.travel.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.proj.travel.model.dto.CityDto;
import com.proj.travel.model.entity.City;
import com.proj.travel.repository.CityRepository;
import com.proj.travel.service.CityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * packageName   : com.proj.travel.controller
 * fileName      : CityControllerTest
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
class CityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CityRepository cityRepository;


    @Test
    void 도시등록() throws Exception {
        //given
        CityDto cityDto = CityDto.builder().cityName("후쿠오카").build();
        //when
        //then
        String content = objectMapper.writeValueAsString(cityDto);
        mockMvc.perform(post("/api/v1/city")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void 도시수정() throws Exception {
        //given
        List<City> all = cityRepository.findAll();
        City city = all.get(0);
        CityDto cityDto = CityDto.builder().cityId(city.getCityId()).cityName("체인지")
                .createdAt(city.getCreatedAt())
                .updatedAt(city.getUpdatedAt()).build();
        //when
        //then
        String content = objectMapper.writeValueAsString(cityDto);
        mockMvc.perform(put("/api/v1/city")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void 도시삭제() throws Exception {
        //given
        City city = cityRepository.saveAndFlush(City.builder().cityName("독도").build());
        //when
        //then
        mockMvc.perform(delete("/api/v1/city/{id}", city.getCityId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void 단일도시조회() throws Exception {
        //given
        List<City> all = cityRepository.findAll();
        City city = all.get(0);
        //when
        //then
        mockMvc.perform(get("/api/v1/city/{id}", city.getCityId())
                        .param("userid", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void 사용자별도시조회() throws Exception {
        //given

        //when

        //then
        mockMvc.perform(get("/api/v1/city/user/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}