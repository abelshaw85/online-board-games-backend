package com.online.board.games.dao;

import com.online.board.games.entity.User;

public interface UserDao {
    public User findByUserName(String userName);
    public void save(User user);
}
