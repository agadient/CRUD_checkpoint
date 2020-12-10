package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lessons")
public class LessonsController {

    private final LessonsRepository repository;

    public LessonsController(LessonsRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/all_lessons")
    public Iterable<Lesson> all() {
        return this.repository.findAll();
    }

    @GetMapping("/5")
    public Optional<Lesson> get5() {
        return this.repository.findById((long) 5);
    }

    @DeleteMapping("/5")
    public void delete5() {
        this.repository.deleteById((long) 5);
    }

    @PostMapping("")
    public Lesson create(@RequestBody Lesson lesson) {
        return this.repository.save(lesson);
    }

    @PatchMapping("/{id}")
    public Lesson update(@PathVariable Long id, @RequestBody Lesson lesson) {
        Optional<Lesson> old = this.repository.findById(id);
        if (old.isPresent()) {
            Lesson oldL = old.get();
            oldL.setTitle(lesson.getTitle());
            oldL.setDeliveredOn(lesson.getDeliveredOn());
            this.repository.deleteById(oldL.getId());
            this.repository.save(lesson);
            return oldL;
        }
        return lesson;
    }

    @GetMapping("/find/{title}")
    public List<Lesson> update(@PathVariable String title) {
        return this.repository.findByTitle(title);
    }

    @GetMapping("/find/between")
    public List<Lesson> update(@RequestParam Date date1, @RequestParam Date date2) {
        return this.repository.findByDates(date1, date2);
    }


}