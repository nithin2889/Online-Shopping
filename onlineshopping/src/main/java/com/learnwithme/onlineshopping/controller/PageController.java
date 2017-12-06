package com.learnwithme.onlineshopping.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.ModelAndView;

import com.learnwithme.onlineshopping.exception.ProductNotFoundException;
import com.learnwithme.shoppingbackend.dao.CategoryDAO;
import com.learnwithme.shoppingbackend.dao.ProductDAO;
import com.learnwithme.shoppingbackend.dto.Category;
import com.learnwithme.shoppingbackend.dto.Product;

@Controller
public class PageController {
	
	
	private static final Logger logger = LoggerFactory.getLogger(PageController.class);
	
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private ProductDAO productDAO;
	
	ContextLoaderListener c;
	
	@RequestMapping(value={"/","/home","/index"})
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Home");
		
		logger.info("Inside PageController index method-INFO");
		logger.debug("Inside PageController index method-DEBUG");
		
		mv.addObject("categories", categoryDAO.list());
		mv.addObject("userClickHome", true);
		return mv;
	}
	
	@RequestMapping(value={"/about"})
	public ModelAndView about() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "About");
		mv.addObject("userClickAbout", true);
		return mv;
	}
	
	@RequestMapping(value={"/contact"})
	public ModelAndView contact() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "Contact");
		mv.addObject("userClickContact", true);
		return mv;
	}
	
	/*
	 * Methods to load all the products and based on category
	 */
	@RequestMapping(value={"/show/all/products"})
	public ModelAndView showAllProducts() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "All products");
		mv.addObject("categories", categoryDAO.list());
		mv.addObject("userClickAllProducts", true);
		return mv;
	}
	
	@RequestMapping(value={"/show/category/{id}/products"})
	public ModelAndView showCategoryProducts(@PathVariable("id") int id) {
		ModelAndView mv = new ModelAndView("page");
		
		// category DAO to fetch a single repository
		Category category = null;
		category = categoryDAO.get(id);
		
		mv.addObject("title", category.getName());
		mv.addObject("categories", categoryDAO.list());
		// passing the single category object
		mv.addObject("category", category);
		mv.addObject("userClickCategoryProducts", true);
		return mv;
	}
	
	/**
	 * Viewing a single product
	 */
	@RequestMapping(value="/show/{id}/product")
	public ModelAndView showSingleProduct(@PathVariable int id) throws ProductNotFoundException {
		ModelAndView mv = new ModelAndView("page");
		Product product = productDAO.get(id);
		
		if(product == null) {
			throw new ProductNotFoundException();
		}
		
		// Update the view count
		product.setViews(product.getViews() + 1);
		productDAO.update(product);
		// -----------------------
		
		mv.addObject("title", product.getName());
		mv.addObject("product", product);
		mv.addObject("userClickShowProduct", true);
		
		return mv;
	}
	
	/* Having similar mapping to our flow id */
	@RequestMapping(value={"/register"})
	public ModelAndView register() {
		ModelAndView mv = new ModelAndView("page");
		mv.addObject("title", "About Us");
		return mv;
	}
	
	/* Login */
	@RequestMapping(value={"/login"})
	public ModelAndView login(@RequestParam(name="error", required=false) String error,
			@RequestParam(name="logout", required=false) String logout) {
		ModelAndView mv = new ModelAndView("login");
		if(error != null) {
			mv.addObject("message", "Invalid username and password!");
		}
		
		if(logout != null) {
			mv.addObject("logout", "Successfully logged out the user!");
		}
		
		mv.addObject("title", "Login");
		return mv;
	}
	
	@RequestMapping(value={"/access-denied"})
	public ModelAndView accessDenied() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("title", "403 - Access Denied");
		mv.addObject("errorTitle", "Aha! Caught You.");
		mv.addObject("errorDescription", "You are not authorized to view this page!");
		return mv;
	}
	
	/* Perform logout */
	@RequestMapping(value= {"/perform-logout"})
	public String logout(HttpServletRequest req, HttpServletResponse resp) {
		// first fetch the authentication object
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null) {
			// internally logout method helps us to clear out the session, unbind 
			// all the objects attached to it, and also removes the authentication 
			// from the security context.
			new SecurityContextLogoutHandler().logout(req, resp, auth);
		}
		
		return "redirect:/login?logout";
	}
	
}
