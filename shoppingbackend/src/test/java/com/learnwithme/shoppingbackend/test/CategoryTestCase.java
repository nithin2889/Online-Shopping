package com.learnwithme.shoppingbackend.test;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.learnwithme.shoppingbackend.dao.CategoryDAO;
import com.learnwithme.shoppingbackend.dto.Category;

public class CategoryTestCase {
	private static AnnotationConfigApplicationContext context;
	private static CategoryDAO categoryDAO;
	
	private Category category;
	
	@BeforeClass
	public static void init() {
		context = new AnnotationConfigApplicationContext();
		context.scan("com.learnwithme.shoppingbackend");
		context.refresh();
		
		categoryDAO = (CategoryDAO)context.getBean("categoryDAO");
	}
	
	/*@Test
	public void testAddCategory() {
		category = new Category();
		category.setName("Television");
		category.setDescription("This is some description for Television");
		category.setImageUrl("CAT_1.png");
		
		assertEquals("Error adding a category", true, categoryDAO.add(category));
	}
	
	@Test
	public void testGetCategory() {
		category = categoryDAO.get(1);
		assertEquals("Error fetching a single category", "Laptop", category.getName());
	}
	
	@Test
	public void testUpdateCategory() {
		category = categoryDAO.get(1);
		category.setName("Lappy");
		assertEquals("Error updating a single category", true, categoryDAO.update(category));
	}
	
	@Test
	public void testDeletedCategory() {
		// Soft delete
		category = categoryDAO.get(4);		
		assertEquals("Error deleting a single category", true, categoryDAO.delete(category));
	}
	
	@Test
	public void testListCategory() {
		assertEquals("Error fetching the list of active categories", 3, categoryDAO.list().size());
	}*/
	
	// Single test method for CRUD operation.
	@Test
	public void testCRUDCategory() {
		// ADD operation
		category = new Category();
		category.setName("Television");
		category.setDescription("This is some description for Television");
		category.setImageUrl("CAT_1.png");
		
		assertEquals("Failed to add a category", true, categoryDAO.add(category));
		
		category = new Category();
		category.setName("Mobile");
		category.setDescription("This is some description for Mobile");
		category.setImageUrl("CAT_2.png");
		
		assertEquals("Failed to add a category", true, categoryDAO.add(category));
		
		// FETCHING AND UPDATING operation
		category = categoryDAO.get(2);
		category.setName("Updated Mobile");
		assertEquals("Failed to update a single category", true, categoryDAO.update(category));
		
		// DELETE operation		
		assertEquals("Failed to delete a single category", true, categoryDAO.delete(category));
		
		// READ operation
		assertEquals("Failed to fetch the list of categories", 6, categoryDAO.list().size());
	}
}
