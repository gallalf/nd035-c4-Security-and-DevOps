package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.net.HttpURLConnection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_item_to_cart_happy_path(){
        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUserWithEmptyCart());
        when(itemRepo.findById(1L)).thenReturn(Optional.of(TestUtils.getItem()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());

        Cart c = response.getBody();
        assertNotNull(c);
        assertNotNull(c.getItems());
        assertNotNull(c.getUser());
        assertEquals(1L, c.getItems().get(0).getId());
        assertEquals("test", c.getUser().getUsername());
    }

    @Test
    public void add_item_to_cart_user_not_found(){
        when(userRepo.findByUsername("test")).thenReturn(null);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getStatusCodeValue());
    }

    @Test
    public void add_item_to_cart_item_not_found(){
        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUserWithEmptyCart());
        when(itemRepo.findById(1L)).thenReturn(Optional.empty());
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getStatusCodeValue());
    }

    @Test
    public void remove_item_from_cart_happy_path(){
        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUserWithItemInCart());
        when(itemRepo.findById(1L)).thenReturn(Optional.of(TestUtils.getItem()));
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());

        Cart c = response.getBody();
        assertNotNull(c);
        assertNotNull(c.getItems());
        assertNotNull(c.getUser());
        assertEquals(0, c.getItems().size());
        assertEquals("test", c.getUser().getUsername());
    }

    @Test
    public void remove_item_from_cart_user_not_found(){
        when(userRepo.findByUsername("test")).thenReturn(null);
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getStatusCodeValue());
    }

    @Test
    public void remove_item_from_cart_item_not_found(){
        when(userRepo.findByUsername("test")).thenReturn(TestUtils.getUserWithEmptyCart());
        when(itemRepo.findById(1L)).thenReturn(Optional.empty());
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getStatusCodeValue());
    }


}
