package lu.plezy.timesheet.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lu.plezy.timesheet.entities.RoleEnum;
import lu.plezy.timesheet.entities.User;
import lu.plezy.timesheet.entities.messages.Role;
import lu.plezy.timesheet.entities.messages.StringMessage;
import lu.plezy.timesheet.i18n.StaticText;
import lu.plezy.timesheet.repository.UsersRepository;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/user")
public class ManageUserController {

    private static Logger log = LoggerFactory.getLogger(ApplicationInfosController.class);

    private int defaultPageSize = 10;

    @Autowired
    UsersRepository usersRepository;

    @GetMapping(value = "/me")
    @PreAuthorize("isAuthenticated()")
    public User getUser(Authentication authentication) {
        Optional<User> result = usersRepository.findByUsername(authentication.getName());
        return result.isPresent() ? result.get() : null;
    }

    @PutMapping(value = "/me")
    @PreAuthorize("isAuthenticated()")
    public User setUser(Authentication authentication, @Valid @RequestBody User userDetails) {
        Optional<User> result = usersRepository.findByUsername(authentication.getName());
        if (result.isPresent()) {
            if (result.get().getId() != userDetails.getId()) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                        "Authenticated User have not the same ID !");
            }
            if (!result.get().getUsername().equals(userDetails.getUsername())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Authenticated User have not the same Username !");
            }

            User updated = result.get();
            updated.setFirstName(userDetails.getFirstName());
            updated.setLastName(userDetails.getLastName());
            updated.setEmail(userDetails.getEmail());
            updated.setPhone(userDetails.getPhone());
            updated.setMobile(userDetails.getMobile());

            usersRepository.save(updated);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        return result.isPresent() ? result.get() : null;
    }

    @PutMapping(value = "/setPassword")
    public User setUserPassword(Authentication authentication, @Valid @RequestBody StringMessage message) {
        Optional<User> result = usersRepository.findByUsername(authentication.getName());
        if (result.isPresent()) {
            if (result.get().getId() != message.getId().longValue()) {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,
                        "Given ID is not the same as currently logged user ID !");
            }

            User updated = result.get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(message.getMessage());
            updated.setPassword(encodedPassword);

            usersRepository.save(updated);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        return result.isPresent() ? result.get() : null;
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public User getUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void deleteUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);

        if (result.isPresent()) {
            User user = result.get();
            user.setDeleted(true);

            // update user for logical deletion
            log.info("Perform user logical deletion");
            usersRepository.save(user);

            // attempt physical deletion is possible ...
            log.info("Attempt user physical deletion");
            try {
                usersRepository.deleteById(user.getId());
            } catch (Exception ex) {
                log.info("User can not be deleted physically ...");
            }
        }
    }

    @PutMapping(value = "/undelete/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void undeleteUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);

        if (result.isPresent()) {
            User user = result.get();
            user.setDeleted(false);

            // update user to reset logical deletion
            log.info("Undelete user");
            usersRepository.save(user);
        }
    }

    @PutMapping(value = "/lock/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void lockUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);

        if (result.isPresent()) {
            User user = result.get();

            setUserLock(user, true);
        }
    }

    @PutMapping(value = "/unlock/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void unlockUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);

        if (result.isPresent()) {
            User user = result.get();

            setUserLock(user, false);
        }
    }

    private void setUserLock(User user, boolean lockState) {
        user.setLocked(lockState);
        // update user lock state
        log.info("Set user lock State to " + lockState);
        usersRepository.save(user);
    }

    @GetMapping(value = "/list")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getUsers() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("username"));
        return usersRepository.findAllActive(p);
    }

    @GetMapping(value = "/list/{size}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getUsers(@PathVariable("size") int size) {
        Pageable p = PageRequest.of(0, size, Sort.by("username"));
        return usersRepository.findAllActive(p);
    }

    @GetMapping(value = "/list/{size}/{page}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getUsers(@PathVariable("size") int size, @PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, size, Sort.by("username"));
        return usersRepository.findAllActive(p);
    }

    @GetMapping(value = "/listAll")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getAllUsers() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("username"));
        return usersRepository.findAll(p);
    }

    @GetMapping(value = "/listAll/{size}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getAllUsers(@PathVariable("size") int size) {
        Pageable p = PageRequest.of(0, size, Sort.by("username"));
        return usersRepository.findAll(p);
    }

    @GetMapping(value = "/listAll/{size}/{page}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getAllUsers(@PathVariable("size") int size, @PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, size, Sort.by("username"));
        return usersRepository.findAll(p);
    }

    @GetMapping(value = "/roles")
    @PreAuthorize("isAuthenticated()")
    public List<Role> getRoles(Authentication authentication) {
        List<Role> response = null;

        Optional<User> result = usersRepository.findByUsername(authentication.getName());
        if (result.isPresent()) {
            response = new ArrayList<>();
            for (RoleEnum role : result.get().getRoles()) {
                Role roleEntry = new Role(role.name());
                roleEntry.setRoleDescription(StaticText.getInstance().getText(role.toString()));
                response.add(roleEntry);
            }
        }
        return response;
    }

    @GetMapping(value = "/roles/all")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public List<Role> getAllRoles() {
        List<Role> response = new ArrayList<>();

        for (RoleEnum role : EnumSet.allOf(RoleEnum.class)) {
            Role roleEntry = new Role(role.name());
            roleEntry.setRoleDescription(StaticText.getInstance().getText(role.toString()));
            response.add(roleEntry);
        }
        return response;
    }

}