package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);
    }

    @Test
    public void find_item_by_id_happy_path(){
        when(itemRepo.findById(1L)).thenReturn(Optional.of(TestUtils.getItem()));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());
        assertEquals(1L,response.getBody().getId());
    }

    @Test
    public void find_item_by_name_not_found(){
        when(itemRepo.findByName("testItemName")).thenReturn(null);

        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItemName");

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_NOT_FOUND, response.getStatusCodeValue());

    }

    @Test
    public void find_item_by_name_happy_path(){
        when(itemRepo.findByName("testItemName")).thenReturn(Arrays.asList(TestUtils.getItem()));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("testItemName");

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());
        assertEquals("testItemName",response.getBody().get(0).getName());
    }

    @Test
    public void find_all_items_happy_path(){
        when(itemRepo.findAll()).thenReturn(Arrays.asList(TestUtils.getItem()));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());
        assertEquals("testItemName",response.getBody().get(0).getName());
    }

    @Test
    public void find_all_items_empty(){
        when(itemRepo.findAll()).thenReturn(Arrays.asList());

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusCodeValue());
        assertEquals(0,response.getBody().size());
    }

}
