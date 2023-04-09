package com.proj.travel.repository;

import com.proj.travel.model.entity.Reservation;
import com.proj.travel.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * packageName   : com.proj.travel.repository
 * fileName      : UserRepository
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
