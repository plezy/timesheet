package lu.plezy.timesheet.entities.messages;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lu.plezy.timesheet.entities.ContractProfile;
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
    
    public static ContractProfileDto convertToDto(ContractProfile contractProfile) {
        List<Long> assignees = new ArrayList<>();
        for (User user : contractProfile.getAssignees()) {
            assignees.add(user.getId());
        }
        return new ContractProfileDto(
            contractProfile.getId(),
            contractProfile.getContract().getId(),
            contractProfile.getName(),
            contractProfile.getDescription(),
            contractProfile.isCompleted(),
            contractProfile.getHourlyRate(),
            contractProfile.getMinimumDailyInvoiced(),
            contractProfile.getMaximumDailyInvoiced(),
            contractProfile.getMultipleUnitInvoiced(),
            assignees
        );
    }

}