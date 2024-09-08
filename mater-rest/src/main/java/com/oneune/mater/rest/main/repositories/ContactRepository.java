package com.oneune.mater.rest.main.repositories;

import com.oneune.mater.rest.main.store.entities.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
    List<ContactEntity> findAllBySellerId(Long id);
}
