package com.learnwithme.shoppingbackend.dao;

import java.util.List;

import com.learnwithme.shoppingbackend.dto.Address;
import com.learnwithme.shoppingbackend.dto.Cart;
import com.learnwithme.shoppingbackend.dto.User;

public interface UserDAO {
	// add an user
	boolean addUser(User user);
	
	User getByEmail(String email);
	
	// add an address
	boolean addAddress(Address address);
	
	// Multiple select queries affects the performance
	Address getBillingAddress(User user);
	List<Address> listShippingAddresses(User user);
	
	// Alternate approach of getting the billing and shipping addresses and avoid multiple select queries
	// Address getBillingAddress(int userId);
	// List<Address> listShippingAddresses(int userId);
	
	// add a cart
	boolean updateCart(Cart cart);
}
