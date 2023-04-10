package com.proj.travel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proj.travel.model.dto.CityDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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

    @Test
    void 여행등록() throws Exception {
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
    void 여행수정() {
        //given

        //when

        //then
    }

    @Test
    void 여행삭제() {
        //given

        //when

        //then
    }

    @Test
    void 여행단일조회() {
        //given

        //when

        //then
    }
}