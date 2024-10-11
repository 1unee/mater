package com.oneune.mater.rest.main.repositories;

import com.oneune.mater.rest.main.store.entities.UserFavoriteCarLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoriteCarLinkRepository extends JpaRepository<UserFavoriteCarLinkEntity, Long> {
}
