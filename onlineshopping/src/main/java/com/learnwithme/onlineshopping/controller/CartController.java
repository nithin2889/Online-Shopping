package com.learnwithme.onlineshopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.learnwithme.onlineshopping.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	private CartService cartService;

	@RequestMapping("/show")
	public ModelAndView showCart(@RequestParam(name="result", required=false) String result) {
		ModelAndView mv = new ModelAndView();
		
		if(result != null) {
			switch(result) {
				case "updated":
					mv.addObject("message", "Cart line updated successfully");
					break;
				case "error":
					mv.addObject("message", "Something went wrong while updating the cart line");
					break;
				case "deleted":
					mv.addObject("message", "Cart Line deleted successfully!");
					break;
				case "added":
					mv.addObject("message", "Cart Line added successfully!");
					break;
			}
		}
		
		mv.addObject("title", "User Cart");
		mv.addObject("userClickShowCart", true);
		mv.addObject("cartLines", cartService.getCartLines());
		
		return mv;
	}
	
	@RequestMapping("/{cartLineId}/update")
	public String updateCart(@PathVariable int cartLineId, @RequestParam int count) {
		String response = cartService.updateCartLine(cartLineId, count);
		
		return "redirect:/cart/show?"+response;
	}
	
	@RequestMapping("/{cartLineId}/delete")
	public String deleteCart(@PathVariable int cartLineId) {
		String response = cartService.deleteCartLine(cartLineId);
		
		return "redirect:/cart/show?"+response;
	}
	
	@RequestMapping("/{cartLineId}/delete")
	public String addCart(@PathVariable int productId) {
		String response = cartService.addCartLine(productId);
		
		return "redirect:/cart/show?"+response;
	}
	
}
