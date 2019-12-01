package lu.plezy.timesheet.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name="CONTRACT_PROFILES")
@SequenceGenerator(name = "CONTRACT_PROFILES_SEQ", initialValue = 100, allocationSize = 1)
public class ContractProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTRACT_PROFILES_SEQ")
    @Column(name = "ID", updatable = false, nullable = false)
    private long id;
    
    @NonNull
    @ManyToOne(targetEntity=User.class)
    @JoinColumn(name="CREATED_BY")
    private User createdBy;

    @NonNull
    @Column(name="CREATED_ON", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    /* Contract */
    @ManyToOne(targetEntity=Contract.class, fetch = FetchType.LAZY)
    @JoinColumn(name="CONTRACT_ID")
    @JsonIgnore // to prevent errors on serialization due to lazy initialization
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // to prevent errors on serialization due to lazy initialization, in this case data is fetched
    private Contract contract;

    @NonNull
    @Column(name="NAME", length=64, nullable=false)
    private String name;

    @Column(name="DESCRIPTION", length=1024, nullable=true)
    private String description;

    @Column(name = "COMPLETED", length=1)
    @Convert(converter=BooleanToStringConverter.class)
    private boolean completed = false;

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

    @ManyToMany
	@JoinTable(name = "USERS_CONTRACT_PROFILES",
		joinColumns = { @JoinColumn(name = "CPF_ID") },
		inverseJoinColumns = { @JoinColumn(name = "USR_ID") })
	private List<User> assignees = new ArrayList<User>();
}