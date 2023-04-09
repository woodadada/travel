package com.proj.travel;

import com.proj.travel.model.entity.City;
import com.proj.travel.model.entity.Reservation;
import com.proj.travel.model.entity.Travel;
import com.proj.travel.model.entity.User;
import com.proj.travel.repository.CityRepository;
import com.proj.travel.repository.ReservationRepository;
import com.proj.travel.repository.TravelRepository;
import com.proj.travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
@RequiredArgsConstructor
@EnableJpaAuditing
public class TravelApplication implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final TravelRepository travelRepository;
    private final ReservationRepository reservationRepository;

    public static void main(String[] args) {
        SpringApplication.run(TravelApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        List<User> userList = new ArrayList<>();
        List<City> cityList = new ArrayList<>();
        List<Travel> travelList = new ArrayList<>();
        List<Reservation> reservationList = new ArrayList<>();
        List<String> strings = Arrays.asList("서울", "도쿄", "싱가포르", "홍콩", "라스베가스", "뉴욕", "파리", "베를린", "베이징", "카이로");

        for (int i = 1; i <= 10; i++) {
            User user = User.builder().name("강정우" + i).build();
            userList.add(user);

            City city = City.builder().cityName(strings.get(i - 1)).build();
            cityList.add(city);


            Travel travel = Travel.builder().city(city)
                    .title(city.getCityName() + " 여행")
                    .startTime(now.minusDays(i))
                    .endTime(now.plusDays(i + 10))
                    .build();
            travelList.add(travel);

        }

        userRepository.saveAllAndFlush(userList);
        cityRepository.saveAll(cityList);
        travelRepository.saveAll(travelList);
        List<User> findUserList = userRepository.findAll();
        for (int i = 0; i < 10; i++) {
            Reservation reservation = Reservation.builder().travel(travelList.get(i)).userId(findUserList.get(i).getUserId()).build();
            reservationList.add(reservation);
        }

        reservationRepository.saveAll(reservationList);
        reservationRepository.save(Reservation.builder().travel(travelList.get(5)).userId(findUserList.get(0).getUserId()).build());
        reservationRepository.save(Reservation.builder().travel(travelList.get(8)).userId(findUserList.get(0).getUserId()).build());
    }
}
