package com.gzk12.auth.repository;

import com.gzk12.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author Yang ShuNing
 * @Date 2019/12/26
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "select new User(u.id, u.username) from User as u where u.deleted=false")
    List<User> findAllByDeletedFalse();
}
