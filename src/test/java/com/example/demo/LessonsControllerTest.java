package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LessonsControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    LessonsRepository repository;

    @BeforeEach
    public void init() {
        Lesson l = new Lesson();
        l.setTitle("Yolo");
        for (int i = 0 ; i < 10; i++) {
            repository.save(l);
        }
    }

    @Test
    @Transactional
    @Rollback
    public void testPatch() throws Exception {
        String req = getJSON("/new_lesson.json");
        MockHttpServletRequestBuilder request = patch("/lessons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(req);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"title\":\"Spring Security\"," +
                        "\"deliveredOn\":\"2017-04-12\"}"));
    }

    @Test
    @Transactional
    @Rollback
    public void testList() throws Exception {
        MockHttpServletRequestBuilder request = get("/lessons/all_lessons")
                .contentType(MediaType.APPLICATION_JSON);;

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":3,\"title\":\"Yolo\"}]"));
    }

    private String getJSON(String path) throws Exception {
        URL url = this.getClass().getResource(path);
        return new String(Files.readAllBytes(Paths.get(url.getFile())));
    }
}