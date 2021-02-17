package com.online.board.games.dao;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.online.board.games.entity.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	private EntityManager entityManager;

	@Override
	public User findByUserName(String userName) {
		// Current Hibernate session
		Session currentSession = entityManager.unwrap(Session.class);

		// Retrieve user from database
		Query<User> query = currentSession.createQuery("from User where userName=:userName", User.class);
		query.setParameter("userName", userName);
		User user = null;
		try {
			user = query.getSingleResult();
		} catch (Exception e) {
			user = null;
		}

		return user;
	}

	@Override
	public void save(User user) {

		Session currentSession = entityManager.unwrap(Session.class);

		currentSession.saveOrUpdate(user);
	}

}
