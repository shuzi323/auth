package com.gzk12.auth.repository;

import com.gzk12.auth.model.Role;
import com.gzk12.auth.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @Author Yang ShuNing
 * @Date 2019/12/26
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "select new User(u.id, u.username) from User as u where u.deleted=false")
    List<User> findAllByDeletedFalse();

    Page<UserInfo> findAllByDeletedFalse(Pageable pageable);

    User findUserByUsernameAndDeleted(String username, Boolean deleted);


    interface UserInfo{
        Long getId();
        String getUsername();
        Set<RoleInfo> getRoles();

        interface RoleInfo{
            String getName();
        }
    }
}
