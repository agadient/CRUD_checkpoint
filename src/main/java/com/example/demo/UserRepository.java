package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(value =  "SELECT id, email from users u WHERE u.email = ?1", nativeQuery = true)
    Iterable<Map<String, String>> findByEmail(String email);

    @Query(value =  "SELECT * from users u WHERE u.email = ?1 and u.password = ?2", nativeQuery = true)
    Iterable<User> findByAuth(String email, String password);
}
