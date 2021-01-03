package lu.plezy.timesheet.entities;

import javax.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="PROJECTS_TASKS")
@PrimaryKeyJoinColumn(foreignKey=@ForeignKey(name = "FK_PROJECT_CONTRACTUAL_TASK"))
@SequenceGenerator(name = "PROJECTS_TASKS_SEQ", initialValue = 100, allocationSize = 1)
public class ProjectTask extends ContractualTask {

    /* Invoiced amount on completioon */
    @Column(name = "INVOICED_AMOUNT")
    private Double invoicedAmount;

}