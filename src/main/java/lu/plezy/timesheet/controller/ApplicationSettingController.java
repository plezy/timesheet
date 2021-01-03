package lu.plezy.timesheet.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lu.plezy.timesheet.entities.ApplicationSetting;
import lu.plezy.timesheet.entities.messages.ApplicationSettingDto;
import lu.plezy.timesheet.service.ApplicationSettingService;

@RestController
@RequestMapping("/settings")
public class ApplicationSettingController {

    private static Logger log = LoggerFactory.getLogger(ApplicationSettingController.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyyy");

    @Autowired
    ApplicationSettingService applicationSettingService;

    @GetMapping({"", "/", "/list"})
    @PreAuthorize("isAuthenticated()")
    public List<ApplicationSettingDto> getSetttings(@RequestParam(name = "date", required = false) Optional<String> dateApplicableParam) {
        log.info("getSetttings called");
        if (dateApplicableParam.isPresent()) {
            Date dateApplicable = null;
            try {
                dateApplicable = sdf.parse(dateApplicableParam.get());
            } catch (ParseException pex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date format error");
            }
            return applicationSettingService.getSettings(dateApplicable);
        } else {
            return applicationSettingService.getSettings();
        }
    }

    @GetMapping("/value/{settingId}")
    @PreAuthorize("isAuthenticated()")
    public ApplicationSettingDto getSettting(@PathVariable("settingId") String settingId, @RequestParam(name = "date", required = false) Optional<String> dateApplicableParam) {
        log.info("getSettting called for {}", settingId);
        ApplicationSettingDto dto = null;
        if (dateApplicableParam.isPresent()) {
            Date dateApplicable = null;
            try {
                dateApplicable = sdf.parse(dateApplicableParam.get());
            } catch (ParseException pex) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date format error");
            }
            dto = applicationSettingService.getSetting(settingId, dateApplicable); 
        } else {
            dto = applicationSettingService.getSetting(settingId);
        }

        if (dto == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Setting [" + settingId + "] not found");
        
        return dto;
    }
    
    @PreAuthorize("hasAuthority('MANAGE_SETTINGS')")
    @GetMapping({"/cache/clear"})
    public void clearCache() {
        log.info("clearCache called");
        applicationSettingService.clearCache();
    }

    @PreAuthorize("hasAuthority('MANAGE_SETTINGS')")
    @GetMapping({"/raw"})
    public List<ApplicationSetting> getRawSettings() {
        log.info("getRawSettings called");
        return applicationSettingService.getRawSettings();
    }

}