package com.learnwithme.onlineshopping.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
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
			// If the role of the user is "USER" then create a Cart and assign it to the User.
			Cart cart = new Cart();
			cart.setUser(user);
			user.setCart(cart);
		}
		
		// encode the password
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
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
	
	public String validateUser(User user, MessageContext errorMessages) {
		String transitionValue = "success";
		
		// When password doesn't match the confirm password we display an error message.
		if(!(user.getPassword().equals(user.getConfirmPassword()))) {
			errorMessages.addMessage(new MessageBuilder()
					.error()
					.source("confirmPassword")
					.defaultText("Password doesn't match the confirm password")
					.build());
			
			transitionValue="failure";
		}
		
		// Check the uniqueness of the email id
		if(userDAO.getByEmail(user.getEmail()) != null) {
			errorMessages.addMessage(new MessageBuilder()
					.error()
					.source("email")
					.defaultText("Email address is already in use")
					.build());
			
			transitionValue="failure";
		}
		return transitionValue;
	}
	
}
