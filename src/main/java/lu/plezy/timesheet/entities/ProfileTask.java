package lu.plezy.timesheet.entities;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="PROFILE_TASKS")
@PrimaryKeyJoinColumn(foreignKey=@ForeignKey(name = "FK_PROFILE_CONTRACTUAL_TASK"))
@SequenceGenerator(name = "PROFILE_TASKS_SEQ", initialValue = 100, allocationSize = 1)
public class ProfileTask extends ContractualTask {

    /* Invoice rate */
    @Column(name = "HOURLY_RATE")
    private Double hourlyRate;

    /* Minimum units (hours) invoiced per day */
    @Column(name = "MIN_DAILY_INVOICED")
    private Integer minimumDailyInvoiced;
    
    /* Maximum units (hours) invoiced per day */
    @Column(name = "MAX_DAILY_INVOICED")
    private Integer maximumDailyInvoiced;
    
    /**
     * Daily units modules (hours) invoiced.
     * This number is used to check granularity of the amount of hours to be invoiced
     * If you need two invoice only multiple of 4 hours, indicate 4.
     * If you can invoice by half of hours, indicate here 0.5
     * 0 -> check is not performed
     * Could be a limited range of values -> 0: disabled, quarter hour, half hour, 1, 2 or 4 hours
     */
    @Column(name = "DAILY_MULT_INVOICED")
    private Double multipleUnitInvoiced;

}