package com.oneune.mater.rest.main.repositories.settings;

import com.oneune.mater.rest.main.store.entities.settings.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<SettingEntity, Long> {
}
