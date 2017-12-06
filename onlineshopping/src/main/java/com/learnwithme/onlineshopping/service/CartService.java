package com.learnwithme.onlineshopping.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnwithme.onlineshopping.model.UserModel;
import com.learnwithme.shoppingbackend.dao.CartLineDAO;
import com.learnwithme.shoppingbackend.dao.ProductDAO;
import com.learnwithme.shoppingbackend.dto.Cart;
import com.learnwithme.shoppingbackend.dto.CartLine;
import com.learnwithme.shoppingbackend.dto.Product;

@Service("cartService")
public class CartService {

	@Autowired
	private CartLineDAO cartLineDAO;
	
	@Autowired
	ProductDAO productDAO;
	
	@Autowired
	private HttpSession session;
	
	// returns the cart for the user who has logged in
	private Cart getCart() {
		return ((UserModel) session.getAttribute("userModel")).getCart();
	}
	
	// returns the entire cart line for a particular user
	public List<CartLine> getCartLines() {
		return cartLineDAO.list(getCart().getId());
	}

	public String updateCartLine(int cartLineId, int count) {
		// fetch the cart line
		CartLine cartLine = cartLineDAO.get(cartLineId);
		
		if(cartLine == null) {
			return "result=error";
		} else {
			// update the cart line
			Product product = cartLine.getProduct();
			double oldTotal = cartLine.getTotal();
			
			// but there might be a scenario where we might not have the user desirable quantity for a product.
			// in that case set the count to the number of quantity available in store
			if(product.getQuantity() <= count) {
				count = product.getQuantity();
			}
			cartLine.setProductCount(count);
			cartLine.setBuyingPrice(product.getUnitPrice());
			
			cartLine.setTotal(product.getUnitPrice() * count);
			
			// update the cart line
			cartLineDAO.update(cartLine);
			
			// update the cart
			Cart cart = this.getCart();
			cart.setGrandTotal(cart.getGrandTotal() - oldTotal + cartLine.getTotal());
			
			cartLineDAO.updateCart(cart);
			
			return "result=updated";
		}
	}

	public String deleteCartLine(int cartLineId) {
		// fetch the cart line
		CartLine cartLine = cartLineDAO.get(cartLineId);
		
		if(cartLine == null) {
			return "result=error";
		} else {
			// updating the cart
			Cart cart = this.getCart();
			cart.setGrandTotal(cart.getGrandTotal() - cartLine.getTotal());
			cart.setCartLines(cart.getCartLines() - 1);
			cartLineDAO.updateCart(cart);
			
			// removing the cart after updating the cart
			cartLineDAO.delete(cartLine);
			
			return "result=deleted";
		}
	}

	public String addCartLine(int productId) {
		String response = null;
		
		Cart cart = this.getCart();
		CartLine cartLine = cartLineDAO.getByCartAndProduct(cart.getId(), productId);
		
		if(cartLine == null) {
			// add a new cart line
			cartLine = new CartLine();
			
			// fetch the product
			Product product = productDAO.get(productId);
			cartLine.setCartId(cart.getId());
			cartLine.setProduct(product);
			cartLine.setBuyingPrice(product.getUnitPrice());
			cartLine.setProductCount(1);
			
			cartLine.setTotal(product.getUnitPrice());
			cartLine.setAvailable(true);
			
			cartLineDAO.add(cartLine);
			
			cart.setCartLines(cart.getCartLines() + 1);
			cart.setGrandTotal(cart.getGrandTotal() + cartLine.getTotal());
			
			cartLineDAO.updateCart(cart);
			
			return "result=added";
		}
		
		return response;
	}
	
}
