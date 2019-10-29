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
@Table(name="PROJECTS_TASKS")
@SequenceGenerator(name = "PROJECTS_TASKS_SEQ", initialValue = 100, allocationSize = 1)
public class ProjectTask {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECTS_TASKS_SEQ")
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

    /* Project's contract */
    @ManyToOne(targetEntity=Contract.class)
    @JoinColumn(name="CONTRACT_ID")
    private Contract contract;

    @NonNull
    @Column(name="NAME", length=64, nullable=false)
    private String name;

    @Column(name="DESCRIPTION", length=1024, nullable=true)
    private String description;

    @ManyToOne
    @JoinColumn(name="PARENT_ID")
    public ProjectTask parent;

    @OneToMany(mappedBy="parent")
    public Set<ProjectTask> children;

    @Column(name = "COMPLETED", length=1)
    @Convert(converter=BooleanToStringConverter.class)
    private boolean completed = false;

    /* Invoiced amount on completioon */
    @Column(name = "INVOICED_AMOUNT")
    private Double invoicedAmount;
}