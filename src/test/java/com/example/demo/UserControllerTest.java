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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository repository;
    @BeforeEach
    public void init() {
        User u = new User();
        for (int i = 0 ; i < 10; i++) {
            u.setId((long) i);
            u.setEmail(String.format("user%d@gmail.com", i));
            u.setPassword(String.format("password%d", i));
            repository.save(u);
        }
    }

    @Test
    @Transactional
    @Rollback
    public void testList() throws Exception {
        MockHttpServletRequestBuilder request = get("/users");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":1,\"email\":\"user1@gmail.com\",\"password\":\"" +
                        "password1\"},{\"id\":2,\"email\":\"user2@gmail.com\",\"password\":\"password2\"},{\"id\":3,\"" +
                        "email\":\"user3@gmail.com\",\"password\":\"password3\"},{\"id\":4,\"email\":\"user4@gmail.com\"" +
                        ",\"password\":\"password4\"},{\"id\":5,\"email\":\"user5@gmail.com\",\"password\":\"password5\"},{\"id\":6,\"email\":\"user6@gmail.com\",\"password\":\"password6\"},{\"id\":7,\"email\":\"user7@gmail.com\",\"password\":\"password7\"},{\"id\":8,\"email\":\"user8@gmail.com\",\"password\":\"password8\"},{\"id\":9,\"email\":\"user9@gmail.com\",\"password\":\"password9\"}]"));
    }

    @Test
    @Transactional
    @Rollback
    public void testPost() throws Exception {
        String req = getJSON("/new_user.json");
        MockHttpServletRequestBuilder request = post("/users")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":20,\"email\":\"john@example.com\"}]"));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByID() throws Exception {
        MockHttpServletRequestBuilder request = get("/users/40");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":40,\"email\":\"user9@gmail.com\"" +
                        ",\"password\":\"password9\"}"));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateUser() throws Exception {
        String req = getJSON("/new_user.json");
        MockHttpServletRequestBuilder request = patch("/users/30")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":30,\"email\":\"something-secret\"}]"));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteId() throws Exception {
        String req = getJSON("/new_user.json");
        MockHttpServletRequestBuilder request = delete("/users/70")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("{\"count\":9}"));
    }

    @Test
    @Transactional
    @Rollback
    public void testAuthenticatedFalse() throws Exception {
        String req = getJSON("/new_user.json");
        MockHttpServletRequestBuilder request = post("/users/authenticate")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("{\"authenticated\":false}"));
    }

    @Test
    @Transactional
    @Rollback
    public void testAuthenticatedTrue() throws Exception {
        String req = getJSON("/new_user.json");

        User u = new User();
        u.setEmail("john@example.com");
        u.setPassword("something-secret");
        repository.save(u);

        MockHttpServletRequestBuilder request = post("/users/authenticate")
                .content(req)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("{\"authenticated\":true}"));
    }

    private String getJSON(String path) throws Exception {
        URL url = this.getClass().getResource(path);
        return new String(Files.readAllBytes(Paths.get(url.getFile())));
    }
}
