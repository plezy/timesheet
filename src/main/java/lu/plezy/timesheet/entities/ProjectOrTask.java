package lu.plezy.timesheet.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name="PROJECTS_OR_TASKS")
@SequenceGenerator(name = "PROJECTS_OR_TASKS_SEQ", initialValue = 100, allocationSize = 1)
public class ProjectOrTask {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTS_OR_TASKS_SEQ")
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

    @ManyToOne(targetEntity=Customer.class)
    @JoinColumn(name="CUST_ID")
    private Customer customer;

    @NonNull
    @Column(name="NAME", length=64, nullable=false)
    private String name;

    @Column(name="DESCRIPTION", length=1024, nullable=true)
    private String description;

    @ManyToOne
    @JoinColumn(name="PARENT_ID")
    public ProjectOrTask parent;

    @OneToMany(mappedBy="parent")
    public Set<ProjectOrTask> children;

    @Column(name = "COMPLETED", length=1)
    @Convert(converter=BooleanToStringConverter.class)
    private boolean completed = false;

    @Column(name = "ARCHIVED", length=1)
    @Convert(converter=BooleanToStringConverter.class)
    private boolean archived = false;
}