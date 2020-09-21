package lu.plezy.timesheet.entities.messages;

import lombok.Data;
import lu.plezy.timesheet.entities.TaskTimeTrack;
import lu.plezy.timesheet.i18n.StaticText;

import java.util.Date;

@Data
public class TaskTimeTrackDto {
    private long tastTimetrackID;
    private long taskID;
    private long userID;
    private String contractName;
    private String contractType;
    private String taskName;
    private Date date;
    private Double duration;
    private String note;
    private boolean locked;

    public static TaskTimeTrackDto convertToDto(TaskTimeTrack taskTimeTrack) {
        TaskTimeTrackDto taskTimeTrackDto = new TaskTimeTrackDto();
        taskTimeTrackDto.setTastTimetrackID(taskTimeTrack.getId());
        taskTimeTrackDto.setTaskID(taskTimeTrack.getTask().getId());
        taskTimeTrackDto.setUserID(taskTimeTrack.getUser().getId());
        taskTimeTrackDto.setContractName(taskTimeTrack.getTask().getContract().getName());
        taskTimeTrackDto.setContractType(StaticText.getInstance().getText(taskTimeTrack.getTask().getContract().getContractType().toString()));
        taskTimeTrackDto.setTaskName(taskTimeTrack.getTask().getName());
        taskTimeTrackDto.setDate(taskTimeTrack.getDate());
        taskTimeTrackDto.setDuration(taskTimeTrack.getDuration());
        taskTimeTrackDto.setNote(taskTimeTrack.getNote());
        taskTimeTrackDto.setLocked(taskTimeTrack.isLocked());
        return taskTimeTrackDto;
    }
}
