package com.oneune.mater.rest.main.repositories;

import com.oneune.mater.rest.main.store.entities.ExceptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExceptionRepository extends JpaRepository<ExceptionEntity, Long> {
}
