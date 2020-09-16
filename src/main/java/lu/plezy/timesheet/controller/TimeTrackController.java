package lu.plezy.timesheet.controller;

import lu.plezy.timesheet.entities.ProfileTask;
import lu.plezy.timesheet.entities.User;
import lu.plezy.timesheet.entities.messages.TaskDto;
import lu.plezy.timesheet.repository.ProfileTaskRepository;
import lu.plezy.timesheet.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
    private ProfileTaskRepository profileTaskRepository;

    @Autowired
    private UsersRepository usersRepository;

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
            return null;
        }
        log.debug("Requested Date {}", requestedDate);
        Optional<User> userOpt = usersRepository.findById(userId);
        if (userOpt.isPresent()) {
            List<ProfileTask> profileTaskList = profileTaskRepository.findActiveByUserId(userOpt.get().getId(), requestedDate);
            return profileTaskList.stream()
                    .map(contractProfile -> TaskDto.convertToDto(contractProfile))
                    .collect(Collectors.toList());
        } else {
            return null;
        }

    }

}
