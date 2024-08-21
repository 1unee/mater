package com.oneune.mater.rest.main.repositories;

import com.oneune.mater.rest.main.store.entities.RoleEntity;
import com.oneune.mater.rest.main.store.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(RoleEnum name);
    List<RoleEntity> findByNameIn(List<RoleEnum> names);
}
