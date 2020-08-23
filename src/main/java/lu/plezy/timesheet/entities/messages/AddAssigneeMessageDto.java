package lu.plezy.timesheet.entities.messages;

import lombok.Data;

@Data
public class AddAssigneeMessageDto {
    Long contractId;
    Long profileId;
    Long[] assigneeIds;
}
