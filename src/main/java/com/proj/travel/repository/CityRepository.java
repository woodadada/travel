package com.proj.travel.repository;

import com.proj.travel.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * packageName   : com.proj.travel.repository
 * fileName      : CityRepository
 * author        : kang_jungwoo
 * date          : 2023/04/08
 * description   :
 * ===========================================================
 * DATE              AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/04/08       kang_jungwoo         최초 생성
 */
@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    List<City> findTop10ByCreatedAtIsAfterOrderByCreatedAtDesc(LocalDateTime now);

    List<City> findByCityIdNotIn(Collection<Long> idList);

    boolean existsById(Long id);
}
