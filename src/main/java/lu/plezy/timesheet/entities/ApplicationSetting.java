package lu.plezy.timesheet.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLInsert;

@Data
@NoArgsConstructor
@Entity
@Table(name="APP_SETTINGS",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "SETTING_ID" }, name = "APS_SETTING_ID_UK")
    }
)
@SequenceGenerator(name = "APP_SETTINGS_SEQ", initialValue = 100, allocationSize = 1)
// NO inserts neither deletes authorised at this level !
@SQLInsert(sql = "")
@SQLDelete(sql = "")
public class ApplicationSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_SETTINGS_SEQ")
    @Column(name = "ID", updatable = false, nullable = false)
    private long id;

    @Column(name = "SETTING_ID", updatable = false, nullable = false, length=64)
    private String settingId;

    @Column(name = "SORTING", updatable = false, nullable = false)
    private Integer sorting;

    @Column(name = "JAVA_TYPE", updatable = false, nullable = false, length=32)
    private String javaType;

    @Column(name = "DATE_LINKED", length=1)
    @Convert(converter=BooleanToStringConverter.class)
    private boolean dateLinked = false;

    /**
     *  The current value for the setting. If this setting may change with time, the applicable
     *  value until date is given in the datedValues List. The date given in this table is the value
     *  used until the date, inclusive !
     *  If all dates in this list are in the past then the value used is the one given in the current value column.
     */
    @Column(name = "SETTING_VALUE", updatable = false, nullable = false)
    private String value;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "APP_SETTING_ID", foreignKey = @ForeignKey(name = "FK_APP_SETTINGS_DATED_VALUE"))
    private List<ApplicationSettingDatedValue> datedValues = new ArrayList<>();
}