package lu.plezy.timesheet.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name="CONTRACTUAL_TASKS")
@SequenceGenerator(name = "CONTRACTUAL_TASKS_SEQ", initialValue = 100, allocationSize = 1)
@Inheritance(strategy = InheritanceType.JOINED)
public class ContractualTask {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTRACTUAL_TASKS_SEQ")
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

    @Column(name = "DATE_COMPLETED", nullable=true)
    private Date dateCompleted;

    @ManyToMany
    @JoinTable(name = "USERS_CONTRACTUAL_TASK",
            joinColumns = { @JoinColumn(name = "CTK_ID") },
            inverseJoinColumns = { @JoinColumn(name = "USR_ID") })
    private List<User> assignees = new ArrayList<User>();
}
