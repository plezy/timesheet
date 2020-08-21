package lu.plezy.timesheet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lu.plezy.timesheet.entities.ContractProfile;
import lu.plezy.timesheet.repository.ContractProfileRepository;
import lu.plezy.timesheet.repository.UsersRepository;

@RestController
@RequestMapping(value = "/contract/profiles")
public class ContractProfileController {
    
    private static Logger log = LoggerFactory.getLogger(ApplicationInfosController.class);

    //private int defaultPageSize = 10;

    @Autowired
    ContractProfileRepository profilesRepository;

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
    public List<ContractProfile> getProfilesForContract(@PathVariable("id") Long contractId) {
        log.info("Get profiles for contract id {}", contractId);
        return profilesRepository.findByContractId(contractId);
    }

    @DeleteMapping("{id}/{profileId}/{assignmentId}")
    @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
    public List<ContractProfile> deleteAssignmentForProfile(@PathVariable("id") Long contractId, @PathVariable("profileId") Long profileId, @PathVariable("assignmentId") Long assignmentId) {
        return null;
    }
}