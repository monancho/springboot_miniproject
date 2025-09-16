package com.monancho.monanchoboard.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//import com.monancho.monanchoboard.answer.Answer;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	public Optional<SiteUser> findByUsername(String username);
}
