package com.example.demo.controllers;

import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private OrderRepository orderRepo = mock(OrderRepository.class);

    private UserRepository userRepo = mock(UserRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
    }

    @Test
    public void submit_order_happy_path(){
        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUserWithItemInCart());
        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());
        UserOrder u = response.getBody();
        assertNotNull(u);
        assertEquals("test", response.getBody().getUser().getUsername());
        assertEquals(1L, u.getItems().get(0).getId());
    }

    @Test
    public void submit_order_user_not_found(){
        when(userRepo.findByUsername("test")).thenReturn(null);

        ResponseEntity<UserOrder> response = orderController.submit("test");

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getStatusCodeValue());
    }

    @Test
    public void get_history_happy_path(){
        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUserWithEmptyCart());
        when(orderRepo.findByUser(any())).thenReturn(TestUtils.getUserOrder());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());
        List<UserOrder> userOrderList = response.getBody();
        assertNotNull(userOrderList);
        assertEquals("test",userOrderList.get(0).getUser().getUsername());
        assertEquals("testItemName",userOrderList.get(0).getItems().get(0).getName());
    }

    @Test
    public void get_history_empty_history(){
        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUserWithEmptyCart());
        when(orderRepo.findByUser(any())).thenReturn(Arrays.asList());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());
        assertEquals(0, response.getBody().size());
    }

    @Test
    public void get_history_user_not_found(){
        when(userRepo.findByUsername("test")).thenReturn(null);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getStatusCodeValue());
    }
}
