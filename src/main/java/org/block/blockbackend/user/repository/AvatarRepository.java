package org.block.blockbackend.user.repository;

import org.block.blockbackend.user.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Avatar findByUserId(Long userId);
}
