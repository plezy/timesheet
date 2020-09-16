package lu.plezy.timesheet.entities.messages;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lu.plezy.timesheet.entities.ProfileTask;
import lu.plezy.timesheet.entities.User;

@Data
@AllArgsConstructor
public class ContractProfileDto {
    private long id;
    private long contractId;
    private String name;
    private String description;
    private boolean completed = false;
    private Double hourlyRate;
    private Integer minimumDailyInvoiced;
    private Integer maximumDailyInvoiced;
    private Double multipleUnitInvoiced;
    private List<Long> assigneesId = new ArrayList<Long>();
    
    public static ContractProfileDto convertToDto(ProfileTask profileTask) {
        List<Long> assignees = new ArrayList<>();
        for (User user : profileTask.getAssignees()) {
            assignees.add(user.getId());
        }
        return new ContractProfileDto(
            profileTask.getId(),
            profileTask.getContract().getId(),
            profileTask.getName(),
            profileTask.getDescription(),
            profileTask.isCompleted(),
            profileTask.getHourlyRate(),
            profileTask.getMinimumDailyInvoiced(),
            profileTask.getMaximumDailyInvoiced(),
            profileTask.getMultipleUnitInvoiced(),
            assignees
        );
    }

}