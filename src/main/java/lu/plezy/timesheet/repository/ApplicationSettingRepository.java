package lu.plezy.timesheet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lu.plezy.timesheet.entities.ApplicationSetting;

@Repository
public interface ApplicationSettingRepository extends JpaRepository<ApplicationSetting, Long> {
    Optional<ApplicationSetting> findBySettingId(String settingId);
}
