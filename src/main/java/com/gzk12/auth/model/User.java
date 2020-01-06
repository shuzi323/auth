package com.gzk12.auth.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

/**
 * @Author Yang ShuNing
 * @Date 2019/12/26
 */
@Data
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    @Column(columnDefinition = "bit NOT NULL default 0")
    private Boolean deleted = false;

    @Transient
    private String passwordConfirm;

    @ManyToMany
    private Set<Role> roles;

    public User(){
    }

    public User(Long id, String  username){
        this.id = id;
        this.username = username;
    }
}
