package com.learnwithme.shoppingbackend.dao;

import java.util.List;

import com.learnwithme.shoppingbackend.dto.Category;

public interface CategoryDAO {
	List<Category> list();
	Category get(int id);
}
