package com.oneune.mater.rest.main.repositories.settings;

import com.oneune.mater.rest.main.store.entities.settings.UserSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<UserSettingsEntity, Long> {
}
