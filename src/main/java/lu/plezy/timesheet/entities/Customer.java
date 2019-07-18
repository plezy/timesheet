package lu.plezy.timesheet.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity(name = "CUSTOMERS")
@SequenceGenerator(name = "CUSTOMERS_SEQ", initialValue = 100, allocationSize = 1)
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CUSTOMERS_SEQ")
    @Column(name = "ID", updatable = false, nullable = false)
    private long id;

    @NonNull
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "CREATED_BY")
    @JsonIgnore
    private User createdBy;

    @NonNull
    @Column(name = "CREATED_ON", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date createdOn;

    @NonNull
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "UPDATED_BY")
    @JsonIgnore
    private User updatedBy;

    @NonNull
    @Column(name = "UPDATED_ON", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonIgnore
    private Date updatedOn;

    @NonNull
    @Column(name = "NAME", length = 64, nullable = false)
    private String name;

    @Embedded
    private Address address;

    @Column(name = "USE_BILLING_ADDR", length = 1)
    @Convert(converter = BooleanToStringConverter.class)
    private boolean useBillingAddress = false;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "addressLine1", column = @Column(name = "BILL_ADDRLINE1")),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "BILL_ADDRLINE2")),
            @AttributeOverride(name = "city", column = @Column(name = "BILL_CITY")),
            @AttributeOverride(name = "area", column = @Column(name = "BILL_AREA")),
            @AttributeOverride(name = "country", column = @Column(name = "BILL_COUNTRY1")),
            @AttributeOverride(name = "postCode", column = @Column(name = "BILL_POSTCODE")) })
    private Address billingAddress;

    @Column(name = "ARCHIVED", length = 1)
    @Convert(converter = BooleanToStringConverter.class)
    private boolean archived = false;

    @Column(name = "DELETED", length = 1)
    @Convert(converter = BooleanToStringConverter.class)
    private boolean deleted = false;

}