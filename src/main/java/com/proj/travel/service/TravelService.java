package com.proj.travel.service;

import com.proj.travel.model.dto.TravelDto;
import com.proj.travel.model.entity.Travel;
import com.proj.travel.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * packageName   : com.proj.travel.service
 * fileName      : TravelService
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
public class TravelService {

    private final TravelRepository travelRepository;

    public TravelDto createTravel(TravelDto travelDto) {
        Travel travel = new Travel(travelDto);
        Travel save = travelRepository.save(travel);
        return new TravelDto(save);
    }

    public TravelDto updateTravel() {

        return null;
    }

    public void deleteTravel() {

    }

    public Travel findTravel() {
        return null;
    }
}
