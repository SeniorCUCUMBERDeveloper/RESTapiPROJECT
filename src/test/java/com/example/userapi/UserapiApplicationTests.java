package com.example.userapi;

import com.example.userapi.model.Country;
import com.example.userapi.model.User;
import com.example.userapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jakarta.validation.ConstraintViolationException; // Jakarta EE
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserapiApplicationTests {

    @Autowired
    private UserService svc;


    @Test
    void listNotEmpty() {
        assertFalse(svc.findAll().isEmpty(),
                "Список пользователей пуст — data.sql не загрузился или сервис вернул 0 записей");
    }


    @Test
    void addUserWorks() {
        int before = svc.findAll().size();
        User u = new User(null, "Test", 99, Country.USA); // id = null, сгенерируется JPA
        svc.save(u);
        int after = svc.findAll().size();
        assertEquals(before + 1, after, "Пользователь не добавился в базу");
    }


    @Test
    void filterByCountries() {
        List<User> filtered = svc.findByCountries(List.of(Country.RUSSIA, Country.GERMANY));
        assertFalse(filtered.isEmpty(), "После фильтра список пустой, записи должны быть");
        assertTrue(filtered.stream().allMatch(u ->
                        u.getCountry() == Country.RUSSIA || u.getCountry() == Country.GERMANY),
                "В выборку попали пользователи из других стран");
    }


    @Test
    void ageMustBePositive() {
        User bad = new User(null, "Bob", -5, Country.USA);
        assertThrows(ConstraintViolationException.class, () -> svc.save(bad));
    }
}
