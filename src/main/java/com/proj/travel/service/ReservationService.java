package com.proj.travel.service;

import com.proj.travel.model.entity.Reservation;
import com.proj.travel.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName   : com.proj.travel.service
 * fileName      : ReservationService
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
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> getReservations(Long userId) {
        List<Reservation> reservations = reservationRepository.findAllByUserId(userId);
        return reservations;
    }

    // 유저가 여행 중인 예약들 조회
    public List<Reservation> getActiveReservations(Long userId) {
        List<Reservation> reservations = getReservations(userId);
        if(reservations.isEmpty()) {
            return null;
        }

        LocalDateTime now = LocalDateTime.now();
        // 여행 시작 날짜 <= 현재 >= 여행 종료 날짜 필터
        List<Reservation> activeReservations = reservations.stream().filter(res ->
                res.getTravel().getStartTime().isBefore(now)
                && res.getTravel().getEndTime().isAfter(now))
                .collect(Collectors.toList());

        return activeReservations;
    }

}
