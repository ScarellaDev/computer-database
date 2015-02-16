package com.excilys.computerdatabase.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excilys.computerdatabase.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

}

