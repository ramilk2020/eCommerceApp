package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;


public class UserControllerTest {
    private  UserController userController;

    private UserRepository userRepo = Mockito.mock(UserRepository.class);

    private CartRepository cartRepo = Mockito.mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        User user = new User();
        Cart cart = new Cart();
        user.setId(0L);
        user.setUsername("TESTUSER");
        user.setCart(cart);
        userRepo.save(user);
        System.out.println(userRepo.findByUsername("TESTUSER"));
        Mockito.when(userRepo.findByUsername("TESTUSER")).thenReturn(user);
        Mockito.when(userRepo.findById(0L)).thenReturn(Optional.of(user));

    }

    @Test
    public void create_user_happy_path() throws Exception {
        Mockito.when(encoder.encode("testPassword")).thenReturn("thisIsHashed");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        final ResponseEntity<User> response = userController.createUser(r);
        Assert.assertNotEquals(200, response.getStatusCode());

        User u = response.getBody();
        Assert.assertNotNull(u);
        Assert.assertEquals(0, u.getId());
        Assert.assertEquals("test", u.getUsername());
        Assert.assertEquals("thisIsHashed", u.getPassword());
    }

    @Test
    public void find_user_by_id_happy_path() {
        ResponseEntity<User> response = userController.findById(0L);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals(0L, user.getId());
    }

    @Test
    public void find_user_by_name_happy_path() {
        ResponseEntity<User> response = userController.findByUserName("TESTUSER");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        User user = response.getBody();
        Assert.assertNotNull(user);
        Assert.assertEquals("TESTUSER", user.getUsername());
    }

    @Test
    public void find_by_id_not_found() throws Exception {
        final ResponseEntity<User> response = userController.findById(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void find_user_by_name_not_found() {
        final ResponseEntity<User> response = userController.findByUserName("NONUSER");
        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());
    }
}
