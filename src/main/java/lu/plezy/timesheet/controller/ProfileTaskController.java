package lu.plezy.timesheet.controller;

import java.util.List;
import java.util.Optional;

import lu.plezy.timesheet.entities.RoleEnum;
import lu.plezy.timesheet.entities.User;
import lu.plezy.timesheet.entities.messages.AddAssigneeMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lu.plezy.timesheet.entities.ProfileTask;
import lu.plezy.timesheet.repository.ProfileTaskRepository;
import lu.plezy.timesheet.repository.UsersRepository;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/contract/profiles")
public class ProfileTaskController {
    
    private static Logger log = LoggerFactory.getLogger(ApplicationInfosController.class);

    //private int defaultPageSize = 10;

    @Autowired
    ProfileTaskRepository profilesRepository;

    @Autowired
    UsersRepository usersRepository;

    /**
     * Get profiles for contract contractId
     *
     * @param contractId
     * @return
     */
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public List<ProfileTask> getProfilesForContract(@PathVariable("id") Long contractId) {
        log.info("Get profiles for contract id {}", contractId);
        return profilesRepository.findByContractId(contractId);
    }

    // TODO : A-t-on besoin du contract ID ? On pourrait simplifier en supprimant la référence qu contrat ...
    @DeleteMapping("delassignees/{id}/{profileId}/{assigneeId}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public ProfileTask deleteAssignmentForProfile(@PathVariable("id") Long contractId, @PathVariable("profileId") Long profileId, @PathVariable("assigneeId") Long assigneeId) {
        Optional<ProfileTask> contractProfile = profilesRepository.findById(profileId);
        if (contractProfile.isPresent()) {
            if (contractProfile.get().getContract().getId() == contractId) {
                List<User> assignees = contractProfile.get().getAssignees();
                boolean found = false;
                for (User assignee : assignees) {
                    if (assignee.getId() == assigneeId) {
                        assignees.remove(assignee);
                        profilesRepository.save(contractProfile.get());
                        found = true;
                        break;
                    }
                }
                if (found) {
                    return contractProfile.get();
                } else {
                    // sends not found assigneeID
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "assignee ID not found");
                }
            } else {
                // Generate error "Contract Id Wrong"
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "contract ID not found");
            }
        } else {
            // Generate error "profile not found"
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "contract profile ID not found");
        }
    }

    // TODO : A-t-on besoin du contract ID ? On pourrait simplifier en supprimant la référence du contrat ...
    @PutMapping("addassignees")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    //public ContractProfile addAssignmentForProfile(@PathVariable("id") Long contractId, @PathVariable("profileId") Long profileId, @PathVariable("assigneeIds") Long[] assigneeIds) {
    public ProfileTask addAssignmentForProfile(@Valid @RequestBody AddAssigneeMessageDto addAssigneeMessageDto) {
        Optional<ProfileTask> contractProfile = profilesRepository.findById(addAssigneeMessageDto.getProfileId());
        if (contractProfile.isPresent()) {
            for (Long assigneeId : addAssigneeMessageDto.getAssigneeIds()) {
                Optional<User> user = usersRepository.findById(assigneeId);
                if (user.isPresent()) {
                    if (user.get().getRoles().contains(RoleEnum.ENTER_TIME_TRACK)) {
                        if (contractProfile.get().getAssignees().contains(user.get())) {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "user already assigned");
                        } else {
                            List<User> assignees = contractProfile.get().getAssignees();
                            assignees.add(user.get());
                        }
                    } else {
                        // User do not have proper role to enter times. So he can't be referenced !
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, String.format("assignee %d can not be assigned (check roles)", assigneeId ));
                    }
                } else {
                    // User was not found
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "assignee ID not found");
                }
            }
            profilesRepository.save(contractProfile.get());
            return contractProfile.get();
        } else {
            // Generate error "profile not found"
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "contract profile ID not found");
        }
    }

    @GetMapping("assignable/{profileId}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public List<User> addAssignableForProfile(@PathVariable("profileId") Long profileId) {
        List<User> usersInResult = usersRepository.findBillable();
        Optional<ProfileTask> contractProfile = profilesRepository.findById(profileId);
        if (contractProfile.isPresent()) {
            ProfileTask profile = contractProfile.get();
            if (profile.getAssignees().size() > 0) {
                for (User assignee : profile.getAssignees()) {
                    usersInResult.remove(assignee);
                }
            }
        }
        return usersInResult;
    }

}