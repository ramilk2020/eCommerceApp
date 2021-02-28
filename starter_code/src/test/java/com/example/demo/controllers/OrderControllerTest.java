package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    UserController userController;

    OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository =mock(CartRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setup(){
        orderController = new OrderController();
        userController = new UserController();

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }
    private ResponseEntity<User> createNewUser(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("TEST");
        createUserRequest.setPassword("TESTPASSWORD");
        createUserRequest.setConfirmPassword("TESTPASSWORD");

        return userController.createUser(createUserRequest);
    }

    @Test
    public void submit_order(){
        when(encoder.encode("TESTPASSWORD")).thenReturn("HASHED");
        ResponseEntity<User> response = createNewUser();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = new User();
        user.setUsername("TESTUSER");
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        Cart cart = new Cart();
        Item item = new Item();
        item.setDescription("TESTDESCRIPTION");
        item.setId(0L);
        item.setName("TESTITEM");
        item.setPrice(new BigDecimal(10));

        cart.addItem(item);
        user.setCart(cart);

        ResponseEntity<UserOrder> responseEntity = orderController.submit(user.getUsername());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void submit_order_cannot_find_user(){
        ResponseEntity<UserOrder> response = orderController.submit("NONUSER");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void get_order_for_user_happy_path(){
        when(encoder.encode("TESTPASSWORD")).thenReturn("HASHED");
        ResponseEntity<User> response = createNewUser();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = new User();
        user.setUsername("TESTUSER");
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        UserOrder userOrder = new UserOrder();
        userOrder.setUser(user);
        Item item = new Item();
        item.setDescription("TESTDESCRIPTION");
        item.setId(0L);
        item.setName("TESTITEM");
        item.setPrice(new BigDecimal(10));
        List<Item> itemList = new ArrayList<>();
        itemList.add(item);

        userOrder.setItems(itemList);
        userOrder.setTotal(new BigDecimal(10));
        userOrder.setId(0L);

        List<UserOrder> userOrderList = new ArrayList<>();
        userOrderList.add(userOrder);

        when(orderRepository.findByUser(user)).thenReturn(userOrderList);
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(user.getUsername());
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void get_order_of_user_not_found(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("NONUSER");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }
}