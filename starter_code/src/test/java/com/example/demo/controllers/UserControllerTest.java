package com.example.demo.controllers;

import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.utils.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.HttpURLConnection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private CartRepository cartRepo = mock(CartRepository.class);

    private UserRepository userRepo = mock(UserRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path(){
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("pwd12345");
        r.setConfirmPassword("pwd12345");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());

    }

    @Test
    public void create_user_pwd_length_fail(){
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("pwd");
        r.setConfirmPassword("pwd");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, response.getStatusCodeValue());
    }

    @Test
    public void create_user_pwd_confirmation_fail(){
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("pwd12345");
        r.setConfirmPassword("pwd1234");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_id_happy_path(){
        when(userRepo.findById(1L)).thenReturn(Optional.of(TestUtils.getUser()));

        ResponseEntity<User> response = userController.findById(1L);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());
        assertEquals("test",response.getBody().getUsername());
    }

    @Test
    public void find_user_by_name_not_found(){
        when(userRepo.findByUsername("test")).thenReturn(null);

        ResponseEntity<User> response = userController.findByUserName("test");

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_name_happy_path(){
        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUser());

        ResponseEntity<User> response = userController.findByUserName("test");

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());
        assertEquals("test",response.getBody().getUsername());
    }
}
