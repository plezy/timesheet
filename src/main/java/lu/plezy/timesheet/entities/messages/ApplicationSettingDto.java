package lu.plezy.timesheet.entities.messages;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lu.plezy.timesheet.entities.ApplicationSetting;
import lu.plezy.timesheet.entities.ApplicationSettingDatedValue;

@Getter
public class ApplicationSettingDto {

    private static Logger log = LoggerFactory.getLogger(ApplicationSettingDto.class);

    private long id;
    private String settingId;
    private boolean dateLinked;
    // dates value was requested or date value is applicable.
    // Note: in DB the recorded date is end of applicability INCLUSIVE ! SO, end of applicability = applicableDate - 1 day 
    private Date applicableDate = null;
    private Date endOfApplicabilityDate = null;
    private String value;

    public static ApplicationSettingDto convertToDto(ApplicationSetting applicationSetting) {
        log.debug("convertToDto called for today's date");
        return convertToDto(applicationSetting, null);
    }

    public static ApplicationSettingDto convertToDto(ApplicationSetting applicationSetting, Date applicableDate) {
        log.debug("convertToDto called for date {}", applicableDate);
        log.debug("convertToDto : setting ID = {}", applicationSetting.getSettingId());
        ApplicationSettingDto result = new ApplicationSettingDto();
        result.id = applicationSetting.getId();
        result.settingId = applicationSetting.getSettingId();
        result.dateLinked = applicationSetting.isDateLinked();
        result.value = applicationSetting.getValue();
        if (result.dateLinked) {
            if (applicableDate == null) {
                result.applicableDate = new Date();
            } else {
                result.applicableDate = applicableDate;
            }
            log.debug("Applicable date used : {}", result.applicableDate);
            if (applicationSetting.getDatedValues().size() > 0) {
                log.debug("convertToDto : dated values exist, sorting them");
                Collections.sort(applicationSetting.getDatedValues(), new Comparator<ApplicationSettingDatedValue>(){

                    @Override
                    public int compare(ApplicationSettingDatedValue o1, ApplicationSettingDatedValue o2) {
                        Long t1 = o1.getDateEndValid().getTime();
                        Long t2 = o2.getDateEndValid().getTime();
                        return t1.compareTo(t2);
                    }
                    
                });
                log.debug("convertToDto : searching applicable value for date");
                //for (int i = applicationSetting.getDatedValues().size() -1; i>=0; i--) {
                for (int i = 0; i<applicationSetting.getDatedValues().size(); i++) {
                    log.debug("Testing value for {}, on date {}", applicationSetting.getDatedValues().get(i).getValue(),
                                    applicationSetting.getDatedValues().get(i).getDateEndValid());
                    if ( ! result.applicableDate.after(applicationSetting.getDatedValues().get(i).getDateEndValid())) {
                        log.debug("using this value ...");
                        result.value = applicationSetting.getDatedValues().get(i).getValue();
                        result.endOfApplicabilityDate = applicationSetting.getDatedValues().get(i).getDateEndValid();
                        break;
                    }
                }
            }
        }
        return result;
    }
}