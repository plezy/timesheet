package lu.plezy.timesheet.controller;

import lu.plezy.timesheet.entities.ContractualTask;
import lu.plezy.timesheet.entities.ProfileTask;
import lu.plezy.timesheet.entities.TaskTimeTrack;
import lu.plezy.timesheet.entities.User;
import lu.plezy.timesheet.entities.messages.TaskDto;
import lu.plezy.timesheet.entities.messages.TaskTimeTrackDto;
import lu.plezy.timesheet.repository.ContractualTaskRepository;
import lu.plezy.timesheet.repository.ProfileTaskRepository;
import lu.plezy.timesheet.repository.TaskTimeTrackRepository;
import lu.plezy.timesheet.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/timesheet")
public class TimeTrackController {

    private static Logger log = LoggerFactory.getLogger(TimeTrackController.class);

    @Autowired
    private ContractualTaskRepository contractualTaskRepository;

    @Autowired
    private ProfileTaskRepository profileTaskRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TaskTimeTrackRepository taskTimeTrackRepository;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyyy");

    @GetMapping("/mytasks/{date}")
    @PreAuthorize("hasAuthority('ENTER_TIME_TRACK')")
    public List<TaskDto> getTasksForMe(Authentication authentication, @PathVariable("date") String dateStr) {
        log.debug("getTasksForMe called");
        Date requestedDate = null;
        try {
             requestedDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error("Exception occurs while parsing date");
            log.error(e.toString());
            return null;
        }
        log.debug("Requested Date {}", requestedDate);
        Optional<User> userOpt = usersRepository.findByUsername(authentication.getName());
        if (userOpt.isPresent()) {
            List<ProfileTask> profileTaskList = profileTaskRepository.findActiveByUserId(userOpt.get().getId(), requestedDate);
            return profileTaskList.stream()
                    .map(contractProfile -> TaskDto.convertToDto(contractProfile))
                    .collect(Collectors.toList());
        } else {
            return null;
        }

    }

    @GetMapping("/tasks/{id}/{date}")
    @PreAuthorize("hasAuthority('ENTER_OTHERS_TIME_TRACK')")
    public List<TaskDto> getTasksForOther(@PathVariable("id") long userId, @PathVariable("date") String dateStr) {
        log.debug("getTasksForOther called");
        Date requestedDate = null;
        try {
            requestedDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error("Exception occurs while parsing date");
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "date format incorrect  use dd-MM-yyyy format");
        }
        log.debug("Requested Date {}", requestedDate);
        Optional<User> userOpt = usersRepository.findById(userId);
        if (userOpt.isPresent()) {
            List<ProfileTask> profileTaskList = profileTaskRepository.findActiveByUserId(userOpt.get().getId(), requestedDate);
            return profileTaskList.stream()
                    .map(contractProfile -> TaskDto.convertToDto(contractProfile))
                    .collect(Collectors.toList());
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "current user can not be retrieved");
        }

    }

    @PreAuthorize("hasAuthority('ENTER_TIME_TRACK')")
    @GetMapping("/mytimetrack/{date}")
    public List<TaskTimeTrack> getTaskTimeTrackForMe(Authentication authentication, @PathVariable("date") String dateStr) {
        log.debug("getTaskTimeTrackForMe called");
        Date requestedDate = null;
        try {
            requestedDate = sdf.parse(dateStr);
        } catch (ParseException e) {
            log.error("Exception occurs while parsing date");
            log.error(e.toString());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "date format incorrect  use dd-MM-yyyy format");
        }
        log.debug("Received requested date : {}", requestedDate);
        Optional<User> userOpt = usersRepository.findByUsername(authentication.getName());
        if (!userOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "current user can not be retrieved");
        }
        log.debug("User ID found : {}", userOpt.get().getId());
        List<TaskTimeTrack> listTaskTimeTracks = taskTimeTrackRepository.findByUserIdAndDate(userOpt.get().getId(), requestedDate);

        return listTaskTimeTracks;
    }

    @PreAuthorize("hasAuthority('ENTER_TIME_TRACK')")
    @PostMapping("/mytimetrack")
    public TaskTimeTrack addTaskTimeTrackForMe(Authentication authentication, @Valid @RequestBody TaskTimeTrackDto newTaskTimeTrackDto) {
        log.debug("addTaskTimeTrackForMe called");
        Optional<User> userOpt = usersRepository.findByUsername(authentication.getName());
        if (!userOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "current user can not be retrieved");
        }
        Optional<ContractualTask> contractTask = contractualTaskRepository.findById(newTaskTimeTrackDto.getTaskID());
        if (!contractTask.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "task not found");
        }

        TaskTimeTrack ttt = new TaskTimeTrack();
        ttt.setUser(userOpt.get());
        ttt.setTask(contractTask.get());
        ttt.setDate(newTaskTimeTrackDto.getDate());
        ttt.setDuration(newTaskTimeTrackDto.getDuration());
        ttt.setNote(newTaskTimeTrackDto.getNote());

        TaskTimeTrack result = taskTimeTrackRepository.save(ttt);

        return result;
    }

    @PreAuthorize("hasAuthority('ENTER_TIME_TRACK')")
    @PutMapping("/mytimetrack")
    public TaskTimeTrack updateTaskTimeTrackForMe(Authentication authentication, @Valid @RequestBody TaskTimeTrackDto newTaskTimeTrackDto) {
        log.debug("updateTaskTimeTrackForMe called");
        Optional<User> userOpt = usersRepository.findByUsername(authentication.getName());
        if (!userOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "current user can not be retrieved");
        }

        Optional<TaskTimeTrack> tttOpt = taskTimeTrackRepository.findById(newTaskTimeTrackDto.getTastTimetrackID());
        if (!tttOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "task time track not found");
        }

        TaskTimeTrack ttt = tttOpt.get();
        if (ttt.getUser().getId() != userOpt.get().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "task time track does not belong to you");
        }

        ttt.setDuration(newTaskTimeTrackDto.getDuration());
        ttt.setNote(newTaskTimeTrackDto.getNote());

        TaskTimeTrack result = taskTimeTrackRepository.save(ttt);

        return result;
    }

    @PreAuthorize("hasAuthority('ENTER_TIME_TRACK')")
    @DeleteMapping("/mytimetrack/{id}")
    public void deleteTaskTimeTrackForMe(Authentication authentication, @PathVariable("id") long tttId) {
        log.debug("deleteTaskTimeTrackForMe called");
        Optional<User> userOpt = usersRepository.findByUsername(authentication.getName());
        if (!userOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "current user can not be retrieved");
        }

        Optional<TaskTimeTrack> tttOpt = taskTimeTrackRepository.findById(tttId);
        if (!tttOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "task time track not found");
        }

        TaskTimeTrack ttt = tttOpt.get();
        if (ttt.getUser().getId() != userOpt.get().getId()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "task time track does not belong to you");
        }

        taskTimeTrackRepository.delete(ttt);
    }

}
