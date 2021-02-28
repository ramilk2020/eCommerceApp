package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    UserController userController;
    ItemController itemController;
    OrderController orderController;

    private UserRepository userRepository=mock(UserRepository.class);
    private CartRepository cartRepository=mock(CartRepository.class);
    private OrderRepository orderRepository= mock(OrderRepository.class);
    private ItemRepository itemRepository= mock(ItemRepository.class);
    private BCryptPasswordEncoder encoder=mock(BCryptPasswordEncoder.class);


    @Before
    public void setup(){
        orderController = new OrderController();
        userController = new UserController();
        itemController = new ItemController();

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }
    private ResponseEntity<User> createNewUser(){

        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("test");
        createUserRequest.setPassword("testPassword");
        createUserRequest.setConfirmPassword("testPassword");

        return userController.createUser(createUserRequest);
    }

    @Test
    public void get_all_items(){
        Item items = new Item();
        items.setDescription("TESTDESCRIPTION");
        items.setId(0L);
        items.setName("TESTITEM");
        items.setPrice(new BigDecimal(10));
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void get_all_items_empty_list(){
        Item items = new Item();
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        when(itemRepository.findAll()).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void get_item_by_id(){
        Item items = new Item();
        items.setDescription("TESTDESCRIPTION");
        items.setId(0L);
        items.setName("TESTITEM");
        items.setPrice(new BigDecimal(10));
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        when(itemRepository.findById(anyLong())).thenReturn(java.util.Optional.of(items));
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void get_item_by_id_not_found(){
        ResponseEntity<Item> response = itemController.getItemById(0L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
    @Test
    public void get_item_by_name(){
        Item items = new Item();
        items.setDescription("TESTDESCRIPTION");
        items.setId(0L);
        items.setName("TESTITEM");
        items.setPrice(new BigDecimal(10));
        List<Item> itemList = new ArrayList<>();
        itemList.add(items);
        when(itemRepository.findByName(anyString())).thenReturn(itemList);
        ResponseEntity<List<Item>> response = itemController.getItemsByName(items.getName());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void get_item_by_name_not_found_(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("TESTITEM");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}