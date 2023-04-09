package com.proj.travel.service;

import com.proj.travel.constant.ErrorCode;
import com.proj.travel.exception.SiteException;
import com.proj.travel.model.dto.TravelDto;
import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.Reservation;
import com.proj.travel.model.entity.Travel;
import com.proj.travel.repository.TravelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final ReservationService reservationService;

    public TravelDto createTravel(TravelDto travelDto) {
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(travelDto.getEndTime())) {
            throw new SiteException(ErrorCode.TRAVEL_END_TIME_ERROR);
        }

        Travel travel = new Travel(travelDto);
        Travel save = travelRepository.save(travel);
        return new TravelDto(save);
    }

    public TravelDto updateTravel(TravelDto travelDto) {
        LocalDateTime now = LocalDateTime.now();
        if(now.isBefore(travelDto.getEndTime())) {
            throw new SiteException(ErrorCode.TRAVEL_END_TIME_ERROR);
        }
        Travel travel = travelRepository.findById(travelDto.getTravelId())
                .orElseThrow(() -> new SiteException(ErrorCode.NOT_FOUND_TRAVEL));

        Travel build = Travel.builder().travelId(travelDto.getTravelId())
                .city(City.builder().cityId(travelDto.getCityId()).build())
                .title(travelDto.getTitle())
                .startTime(travelDto.getStartTime())
                .endTime(travelDto.getEndTime())
                .build();

        Travel save = travelRepository.save(build);
        return new TravelDto(save);
    }

    public boolean deleteTravel(Long id) {
        Travel travel = travelRepository.findById(id)
                .orElseThrow(() -> new SiteException(ErrorCode.NOT_FOUND_TRAVEL));

        travelRepository.delete(travel);

        return true;
    }

    public TravelDto findTravel(Long id) {
        Optional<Travel> byId = travelRepository.findById(id);
        if(byId.isPresent()) {
            return new TravelDto(byId.get());
        }
        return null;
    }

    public List<Travel> getActiveTravels(Long userId) {
        List<Reservation> reservations = reservationService.getReservations(userId);

        if(reservations.isEmpty()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        // 여행 시작 날짜 <= 현재 >= 여행 종료 날짜 필터
        List<Travel> activeTravels = reservations.stream().map(Reservation::getTravel).filter(travel ->
                        travel.getStartTime().isBefore(now)
                        && travel.getEndTime().isAfter(now))
                        .collect(Collectors.toList());
        return activeTravels;
    }

    // 진행 중인 여행
    public List<Travel> getActiveTravels(List<Reservation> reservations) {
        if(reservations.isEmpty()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        // 여행 시작 날짜 <= 현재 >= 여행 종료 날짜 필터
        List<Travel> activeTravels = reservations.stream().map(Reservation::getTravel).filter(travel ->
                        travel.getStartTime().isBefore(now)
                        && travel.getEndTime().isAfter(now))
                .sorted(Comparator.comparing(Travel::getStartTime))
                .collect(Collectors.toList());
        return activeTravels;
    }

    // 예정된 여행
    public List<Travel> getFutureTravels(List<Reservation> reservations) {
        if(reservations.isEmpty()) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        // 현재 <= 여행 시작 날짜
        List<Travel> futureTravels = reservations.stream().map(Reservation::getTravel).filter(travel ->
                        travel.getStartTime().isAfter(now))
                .sorted(Comparator.comparing(Travel::getStartTime))
                .collect(Collectors.toList());
        return futureTravels;
    }



}
