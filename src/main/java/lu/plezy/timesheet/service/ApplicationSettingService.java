package lu.plezy.timesheet.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lu.plezy.timesheet.entities.ApplicationSetting;
import lu.plezy.timesheet.entities.ApplicationSettingDatedValue;
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
    @Cacheable(cacheNames = "cache4settings")
	public List<ApplicationSettingDto> getSettings() {
        log.info("getSettings today's setting");
        return getSettings(null);
	}

    /*
     * Retrieve All Application Settings for given date. If date is null,
     * use today's date
     */
    @Cacheable(cacheNames = "cache4settings")
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
    @Cacheable(cacheNames = "cache4settings")
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
    @Cacheable(cacheNames = "cache4settings")
    public ApplicationSettingDto getSetting(String settingId, Date settingsDate) {
        log.info("getSetting [{}] for date : {}", settingId, settingsDate);

        Optional<ApplicationSetting> setting = applicationSettingRepository.findBySettingIdIgnoreCase(settingId);
        if (setting.isPresent())
            return ApplicationSettingDto.convertToDto(setting.get(), settingsDate);
        else
            return null;
    }
    
    @CacheEvict(cacheNames = { "cache4settings" }, allEntries = true)
    public void clearCache() { }

    public List<ApplicationSetting> getRawSettings() {
        List<ApplicationSetting> settings = applicationSettingRepository.findAll(Sort.by(Sort.Direction.ASC, "sorting"));
        for (ApplicationSetting setting : settings) {
            if (setting.isDateLinked()) {
                if (setting.getDatedValues().size() > 0) {
                    Collections.sort(setting.getDatedValues(), new Comparator<ApplicationSettingDatedValue>(){

                        @Override
                        public int compare(ApplicationSettingDatedValue o1, ApplicationSettingDatedValue o2) {
                            Long t1 = o1.getDateEndValid().getTime();
                            Long t2 = o2.getDateEndValid().getTime();
                            return t2.compareTo(t1);
                        }
                        
                    });
                }
                
            }
        }
        return settings;
    }
}