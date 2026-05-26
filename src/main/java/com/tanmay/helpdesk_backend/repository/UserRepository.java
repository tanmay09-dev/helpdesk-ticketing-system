package com.tanmay.helpdesk_backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanmay.helpdesk_backend.entity.User;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    User findByEmail(String email);
}