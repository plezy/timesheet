package lu.plezy.timesheet.entities.messages;

import lombok.Data;
import lu.plezy.timesheet.entities.ProfileTask;

@Data
public class TaskDto {
    private long taskID;
    private String taskType;
    private String taskDescription;
    private long contractID;
    private String contractName;

    public static TaskDto convertToDto(ProfileTask profile) {
        TaskDto result = new TaskDto();
        result.setTaskID(profile.getId());
        result.setTaskType("P");
        result.setTaskDescription(profile.getDescription());
        result.setContractID(profile.getContract().getId());
        result.setContractName(profile.getContract().getName());
        return result;
    }
}
