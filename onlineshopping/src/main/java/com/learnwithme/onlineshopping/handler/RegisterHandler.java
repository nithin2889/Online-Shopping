package com.learnwithme.onlineshopping.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.learnwithme.onlineshopping.model.RegisterModel;
import com.learnwithme.shoppingbackend.dao.UserDAO;
import com.learnwithme.shoppingbackend.dto.Address;
import com.learnwithme.shoppingbackend.dto.Cart;
import com.learnwithme.shoppingbackend.dto.User;

@Component
public class RegisterHandler {

	@Autowired
	private UserDAO userDAO;
	
	public RegisterModel init() {
		return new RegisterModel();
	}
	
	public void addUser(RegisterModel registerModel, User user) {
		registerModel.setUser(user);
	}
	
	public void addBilling(RegisterModel registerModel, Address billing) {
		registerModel.setBilling(billing);
	}
	
	public String saveall(RegisterModel model) {
		String transitionValue = "success";
		
		// Fetch the user
		User user = model.getUser();
		if(user.getRole() == "USER") {
			Cart cart = new Cart();
			cart.setUser(user);
			user.setCart(cart);
		}
		// Save the user
		userDAO.addUser(user);
		
		// Get the address
		Address billing = model.getBilling();
		billing.setUserId(user.getId());
		billing.setBilling(true);
		
		// Save the address
		userDAO.addAddress(billing);
		
		return transitionValue;
	}
	
}
