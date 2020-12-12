package com.example.demo.utils;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import org.assertj.core.util.Lists;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class TestUtils {

    public static void injectObjects(Object target, String fieldName, Object toInject){

        boolean wasPrivate = false;
        try {
            Field f = target.getClass().getDeclaredField(fieldName);

            if(!f.isAccessible()){
                f.setAccessible(true);
                wasPrivate = true;
            }
            f.set(target, toInject);

            if(wasPrivate){
                f.setAccessible(false);
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static User getUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        return user;
    }

    public static User getUserWithEmptyCart(){
        User user = getUser();
        user.setCart(getCart(user));
        return user;
    }

    public static Cart getCart(User user){
        Cart cart = new Cart();
        cart.setUser(user);
        return cart;
    }

    public static User getUserWithItemInCart(){
        User user = getUser();
        user.setCart(getCartWithItem(user));
        return user;
    }

    public static Cart getCartWithItem(User user){
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(Lists.newArrayList(getItem()));
        return cart;
    }

    public static Item getItem() {
        Item item = new Item();
        item.setId(1L);
        item.setDescription("testItemDescription");
        item.setName("testItemName");
        item.setPrice(new BigDecimal(2.0));
        return item;
    }

    public static List<UserOrder> getUserOrder() {
        UserOrder userOrder = new UserOrder();
        userOrder.setId(1L);
        userOrder.setItems(Arrays.asList(getItem()));
        userOrder.setTotal(new BigDecimal(2.0));
        userOrder.setUser(getUser());
        return Arrays.asList(userOrder);
    }
}
