package com.proj.travel.repository;

import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.Reservation;
import com.proj.travel.model.entity.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName   : com.proj.travel.repository
 * fileName      : ReservationRepository
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByUserId(Long userId);

    List<Reservation> findReservationsByTravel(Travel travel);
}
