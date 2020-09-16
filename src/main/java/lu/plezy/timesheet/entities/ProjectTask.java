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
public class ProjectTask extends ContractualTask {

    /* Invoiced amount on completioon */
    @Column(name = "INVOICED_AMOUNT")
    private Double invoicedAmount;

}