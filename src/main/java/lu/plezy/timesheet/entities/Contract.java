package lu.plezy.timesheet.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name="CONTRACTS")
@SequenceGenerator(name = "CONTRACTS_SEQ", initialValue = 100, allocationSize = 1)
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTRACTS_SEQ")
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

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "UPDATED_BY")
    @JsonIgnore
    private User updatedBy;

    @Column(name = "UPDATED_ON", nullable=true)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updatedOn;
    
    @ManyToOne(targetEntity=Customer.class)
    @JoinColumn(name="CUST_ID")
    private Customer customer;

    @NonNull
    @Column(name="NAME", length=64, nullable=false)
    private String name;

    @Column(name="DESCRIPTION", length=1024, nullable=true)
    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name="CONTRACT_TYPE", length=16, nullable=false)
    private ContractTypeEnum contractType;

    @Column(name="ORDER_NUMBER", length=32, nullable=true)
    private String orderNumber;

    @Column(name="ORDER_DATE", nullable=true)
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @Column(name="PLANNED_START_DATE", nullable=true)
    @Temporal(TemporalType.DATE)
    private Date plannedStart;

    @Column(name="PLANNED_END_DATE", nullable=true)
    @Temporal(TemporalType.DATE)
    private Date plannedEnd;

    @Column(name = "DELETED", length=1)
    @Convert(converter=BooleanToStringConverter.class)
    private boolean deleted = false;

    @Column(name = "ARCHIVED", length=1)
    @Convert(converter=BooleanToStringConverter.class)
    private boolean archived = false;

}