package lu.plezy.timesheet.entities.messages;

import lombok.Data;
import lu.plezy.timesheet.entities.TaskTimeTrack;

import java.util.Date;

@Data
public class TaskTimeTrackDto {
    private long tastTimetrackID;
    private long taskID;
    private long userID;
    private Date date;
    private Double duration;
    private String note;

    public static TaskTimeTrackDto convertToDto(TaskTimeTrack taskTimeTrack) {
        TaskTimeTrackDto taskTimeTrackDto = new TaskTimeTrackDto();
        taskTimeTrackDto.setTastTimetrackID(taskTimeTrack.getId());
        taskTimeTrackDto.setTaskID(taskTimeTrack.getTask().getId());
        taskTimeTrackDto.setUserID(taskTimeTrack.getUser().getId());
        taskTimeTrackDto.setDate(taskTimeTrack.getDate());
        taskTimeTrackDto.setDuration(taskTimeTrack.getDuration());
        taskTimeTrackDto.setNote(taskTimeTrack.getNote());
        return taskTimeTrackDto;
    }
}
