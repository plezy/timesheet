package lu.plezy.timesheet.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="TASK_TIME_TRACK")
@SequenceGenerator(name = "TASK_TIME_TRACK_SEQ", initialValue = 100, allocationSize = 1)
public class TaskTimeTrack {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_TIME_TRACK_SEQ")
    @Column(name = "ID", updatable = false, nullable = false)
    private long id;

    @ManyToOne(targetEntity=User.class)
    @JoinColumn(name="USR_ID", foreignKey = @ForeignKey(name = "FK_TASK_TIME_TRACK_USER"))
    private User user;

    @ManyToOne(targetEntity=ContractualTask.class)
    @JoinColumn(name="TSK_ID", foreignKey = @ForeignKey(name = "FK_TASK_TIME_TRACK_TASK"))
    private ContractualTask task;

    @NonNull
    @Column(name="DURATION", nullable=false)
    private Integer duration;

    @Column(name="NOTE", length=128)
    private String note;
}
