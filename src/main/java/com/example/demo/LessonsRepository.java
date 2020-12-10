package com.example.demo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;

public interface LessonsRepository extends CrudRepository<Lesson, Long> {

    @Query(value =  "SELECT * from lessons l WHERE l.title = ?1", nativeQuery = true)
    List<Lesson> findByTitle(String title);

    @Query(value =  "SELECT * from lessons l WHERE l.delivered_on between ?1 and ?2", nativeQuery = true)
    List<Lesson> findByDates(Date date1, Date date2);
}