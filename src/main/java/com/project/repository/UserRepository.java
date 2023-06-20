package com.project.repository;

import com.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPassword(String username, String password);
    Optional<User> getUserByUsername(String username);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.active = false WHERE u.userId = :userId")
    int deactivateUser(@Param("userId") int userId);
    Optional<User> findById(int userId);
    List<User> findAll();


}

