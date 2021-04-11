package lu.plezy.timesheet.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="APP_SETTING_VALUES")
@SequenceGenerator(name = "APP_SETTING_VALUE_SEQ", initialValue = 100, allocationSize = 1)

public class ApplicationSettingDatedValue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_SETTING_VALUE_SEQ")
    @Column(name = "ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "SETTING_VALUE", updatable = false, nullable = false)
    private String value;

    // setting validity date INCLUSIVE ! When date is over, the setting is changed
    @Column(name = "DATE_END_VALIDITY", updatable = false, nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dateEndValid;
}