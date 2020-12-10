package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository repository;
    public UserController(UserRepository repository) {
        this.repository = repository;
    }
    @GetMapping("")
    public Iterable<User> all() {
        return this.repository.findAll();
    }

    @PostMapping("")
    public Iterable<Map<String, String>> add(@RequestBody User user) {
        this.repository.save(user);
        return this.repository.findByEmail(user.getEmail());
    }

    @GetMapping("{id}")
    public User findByID(@PathVariable long id) {
        Optional<User> u = this.repository.findById(id);
        return u.get();
    }

    @PatchMapping("{id}")
    public Iterable<Map<String, String>> findByID(@PathVariable long id, @RequestBody User new_user) {
        User u = this.repository.findById(id).get();
        if (new_user.getEmail() != null) {
            u.setEmail(new_user.getEmail());
        }
        if (new_user.getPassword() != null) {
            u.setEmail(new_user.getPassword());
        }
        this.repository.save(u);

        return this.repository.findByEmail(u.getEmail());
    }

    @DeleteMapping("{id}")
    public Map<String, Long> deleteID(@PathVariable long id) {
        Map<String, Long> count = new HashMap();
        this.repository.deleteById(id);
        count.put("count", this.repository.count());
        return count;
    }

    @PostMapping("/authenticate")
    public Map<String, Boolean> authenticate(@RequestBody User u) {
        Map<String, Boolean> authenticated = new HashMap();
        Iterable<User> us = this.repository.findByAuth(u.getEmail(), u.getPassword());
        int count = 0;
        for (User one : us) {
            count += 1;
        }
        if (count > 0) {
            authenticated.put("authenticated", new Boolean(true));
        } else {
            authenticated.put("authenticated", new Boolean(false));
        }
        return authenticated;
    }
}
