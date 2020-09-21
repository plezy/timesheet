package lu.plezy.timesheet.entities.messages;

import lombok.Data;
import lu.plezy.timesheet.entities.ContractualTask;
import lu.plezy.timesheet.entities.ProfileTask;
import lu.plezy.timesheet.entities.ProjectTask;

@Data
public class TaskDto {
    private long taskID;
    private String taskType;
    private String taskDescription;
    private long contractID;
    private String contractName;

    public static TaskDto convertToDto(ContractualTask task) {
        TaskDto result = new TaskDto();
        result.setTaskID(task.getId());
        result.setTaskType(task.getContract().getContractType().toString());
        result.setTaskDescription(task.getDescription());
        result.setContractID(task.getContract().getId());
        result.setContractName(task.getContract().getName());
        return result;
    }
}
