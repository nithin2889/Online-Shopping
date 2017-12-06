package com.learnwithme.shoppingbackend.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.learnwithme.shoppingbackend.dao.CartLineDAO;
import com.learnwithme.shoppingbackend.dao.ProductDAO;
import com.learnwithme.shoppingbackend.dao.UserDAO;
import com.learnwithme.shoppingbackend.dto.Cart;
import com.learnwithme.shoppingbackend.dto.CartLine;
import com.learnwithme.shoppingbackend.dto.Product;
import com.learnwithme.shoppingbackend.dto.User;

public class CartLineTestCase {
	private static AnnotationConfigApplicationContext context;
	
	private static CartLineDAO cartLineDAO = null;
	private static ProductDAO productDAO = null;
	private static UserDAO userDAO = null;
	
	private Product product = null;
	private User user = null;
	private Cart cart = null;
	private CartLine cartLine = null;
	
	@BeforeClass
	public static void init() {
		context = new AnnotationConfigApplicationContext();
		context.scan("com.learnwithme.shoppingbackend");
		context.refresh();
		
		productDAO = (ProductDAO)context.getBean("productDAO");
		userDAO = (UserDAO)context.getBean("userDAO");
		cartLineDAO = (CartLineDAO)context.getBean("cartLineDAO");
	}
	
	@Test
	public void testAddNewCartLine() {
		
		// 1. get the user for whom we want to add the product in a cart line.
		user = userDAO.getByEmail("np@gmail.com");
		
		// 2. fetch the cart
		cart = user.getCart();
		
		// 3. fetch the product
		product = productDAO.get(1);
		
		// 4. create a new cart line
		cartLine = new CartLine();
		
		cartLine.setBuyingPrice(product.getUnitPrice());
		cartLine.setProductCount(cartLine.getProductCount() + 1);
		cartLine.setTotal(cartLine.getProductCount() * product.getUnitPrice());
		cartLine.setAvailable(true);
		cartLine.setCartId(cart.getId());
		
		// 5. Add the product to cart line. (One-To-One mapping)
		cartLine.setProduct(product);
		
		assertEquals("Failed to add the cartLine", true, cartLineDAO.add(cartLine));
		
		// Once the product is added it needs to be updated in the database with total and count increased by 1.
		// 6. update the cart
		cart.setGrandTotal(cart.getGrandTotal() + cartLine.getTotal());
		cart.setCartLines(cart.getCartLines() + 1);
		assertEquals("Failed to update the cart", true, cartLineDAO.updateCart(cart));
	}
	
}
