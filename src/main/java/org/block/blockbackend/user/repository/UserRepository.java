package org.block.blockbackend.user.repository;

import org.block.blockbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}

