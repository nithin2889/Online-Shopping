package com.learnwithme.shoppingbackend.daoimpl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.learnwithme.shoppingbackend.dao.ProductDAO;
import com.learnwithme.shoppingbackend.dto.Product;

@Repository("productDAO")
@Transactional
public class ProductDAOImpl implements ProductDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	/**
	 * Fetches a single product
	 */
	@Override
	public Product get(int productId) {
		try {
			return sessionFactory.getCurrentSession().get(Product.class, Integer.valueOf(productId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Fetches a list of products
	 */
	@Override
	public List<Product> list() {
		return sessionFactory.getCurrentSession().createQuery("FROM Product", Product.class).getResultList();
	}

	/**
	 * Inserts a product
	 */
	@Override
	public boolean add(Product product) {
		try {
			sessionFactory.getCurrentSession().persist(product);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Updates a product
	 */
	@Override
	public boolean update(Product product) {
		try {
			sessionFactory.getCurrentSession().update(product);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean delete(Product product) {
		try {
			// Soft delete a product by setting the active value to false
			product.setActive(false);
			// Then update the value in the table by calling the update method
			return this.update(product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public List<Product> listActiveProducts() {
		String activeProducts = "FROM Product WHERE active = :active";
		return sessionFactory.getCurrentSession()
				.createQuery(activeProducts)
					.setParameter("active", true)
						.getResultList();
	}

	@Override
	public List<Product> listActiveProductsByCategory(int categoryId) {
		String activeProductsByCategory = "FROM Product WHERE active = :active AND categoryId = :categoryId";
		return sessionFactory.getCurrentSession()
					.createQuery(activeProductsByCategory)
						.setParameter("active", true)
							.setParameter("categoryId", categoryId)
								.getResultList();
	}

	@Override
	public List<Product> getLatestActiveProducts(int count) {
		String activeProductsByCount = "FROM Product WHERE active = :active ORDER BY id";
		return sessionFactory.getCurrentSession()
				.createQuery(activeProductsByCount, Product.class)
					.setParameter("active", true)
						.setFirstResult(0)
							.setMaxResults(count)
								.getResultList();
	}
	
}
