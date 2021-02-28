package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    UserController userController;
    ItemController itemController;
    OrderController orderController;
    CartController cartController;
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
        cartController = new CartController();

        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }
    private ResponseEntity<User> createNewUser(){
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("TESTUSER");
        createUserRequest.setPassword("TESTPASSWORD");
        createUserRequest.setConfirmPassword("TESTPASSWORD");

        return userController.createUser(createUserRequest);
    }

    @Test
    public void add_to_cart_happy_path(){
        User user = new User();
        user.setUsername("TESTUSER");

        Item item = new Item();
        item.setDescription("TESTDESCRIPTION");
        item.setId(0L);
        item.setName("TESTITEM");
        item.setPrice(new BigDecimal(10));

        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("TESTUSER");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void add_to_cart_name_not_found(){
        User user = new User();
        user.setUsername("TESTUSER");

        Item item = new Item();
        item.setDescription("TESTDESCRIPTION");
        item.setId(0L);
        item.setName("TESTITEM");
        item.setPrice(new BigDecimal(10));

        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("TESTUSER");
        modifyCartRequest.setQuantity(1);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
    @Test
    public void add_to_cart_item_not_found(){
        User user = new User();
        user.setUsername("TESTUSER");

        Item item = new Item();
        item.setDescription("TESTDESCRIPTION");
        item.setId(0L);
        item.setName("TESTITEM");
        item.setPrice(new BigDecimal(10));

        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
    @Test
    public void remove_from_cart_happy_path(){
        User user = new User();
        user.setUsername("TESTUSER");

        Item items = new Item();
        items.setDescription("TESTDESCRIPTION");
        items.setId(0L);
        items.setName("TESTITEM");
        items.setPrice(new BigDecimal(10));

        Cart cart = new Cart();
        cart.addItem(items);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("TESTUSER");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(items));
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
    @Test
    public void remove_from_cart_name_not_found(){
        User user = new User();
        user.setUsername("TESTUSER");

        Item item = new Item();
        item.setDescription("TESTDESCRIPTION");
        item.setId(0L);
        item.setName("TESTITEM");
        item.setPrice(new BigDecimal(10));


        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("TESTUSER");
        modifyCartRequest.setQuantity(1);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    public void remove_form_cart_item_not_found(){
        User user = new User();
        user.setUsername("TESTUSER");


        Item item = new Item();
        item.setDescription("TESTDESCRIPTION");
        item.setId(0L);
        item.setName("TESTITEM");
        item.setPrice(new BigDecimal(10));


        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(0L);
        modifyCartRequest.setUsername("TESTUSER");
        modifyCartRequest.setQuantity(1);

        when(userRepository.findByUsername(anyString())).thenReturn(user);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(responseEntity);
        assertEquals(404, responseEntity.getStatusCodeValue());
    }
}