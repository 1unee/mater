package com.oneune.mater.rest.main.repositories;

import com.oneune.mater.rest.main.store.entities.SaleLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleLinkRepository extends JpaRepository<SaleLinkEntity, Long> {
}
