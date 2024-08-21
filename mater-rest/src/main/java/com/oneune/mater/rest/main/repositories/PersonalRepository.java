package com.oneune.mater.rest.main.repositories;

import com.oneune.mater.rest.main.store.entities.PersonalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalRepository extends JpaRepository<PersonalEntity, Long> {
}
