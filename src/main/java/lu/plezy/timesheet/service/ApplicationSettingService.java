package lu.plezy.timesheet.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lu.plezy.timesheet.entities.ApplicationSetting;
import lu.plezy.timesheet.entities.messages.ApplicationSettingDto;
import lu.plezy.timesheet.repository.ApplicationSettingRepository;

@Service
public class ApplicationSettingService {
    
    private static Logger log = LoggerFactory.getLogger(ApplicationSettingService.class);

    @Autowired
    ApplicationSettingRepository applicationSettingRepository;

    /*
     * Retrieve All Application Settings for today's date
     */
	public List<ApplicationSettingDto> getSettings() {
        log.info("getSettings today's setting");
        return getSettings(null);
	}
 

    /*
     * Retrieve All Application Settings for given date. If date is null,
     * use today's date
     */
    public List<ApplicationSettingDto> getSettings(Date settingsDate) {
        log.info("getSettings for date : {}", settingsDate);
        List<ApplicationSetting> settings = applicationSettingRepository.findAll();
        List<ApplicationSettingDto> settingsDto = settings.stream()
                .map(setting->ApplicationSettingDto.convertToDto(setting, settingsDate))
                .collect(Collectors.toList());
        return settingsDto;
    }
    
    /*
     * Retrieve Application Settings for today's date
     * 
     * @param settingId Setting string identifier
     */
	public ApplicationSettingDto getSetting(String settingId) {
        log.info("getSetting [{}] applicable now", settingId);
        return getSetting(settingId, null);
	}
 

    /*
     * Retrieve Application Settings for given date. If date is null,
     * use today's date
     * 
     * @param settingId Setting string identifier
     */
    public ApplicationSettingDto getSetting(String settingId, Date settingsDate) {
        log.info("getSetting [{}] for date : {}", settingId, settingsDate);

        Optional<ApplicationSetting> setting = applicationSettingRepository.findBySettingId(settingId);
        if (setting.isPresent())
            return ApplicationSettingDto.convertToDto(setting.get(), settingsDate);
        else
            return null;
    }
    

}