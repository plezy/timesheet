package lu.plezy.timesheet.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lu.plezy.timesheet.entities.RoleEnum;
import lu.plezy.timesheet.entities.User;
import lu.plezy.timesheet.entities.messages.Role;
import lu.plezy.timesheet.entities.messages.RoleSet;
import lu.plezy.timesheet.entities.messages.UserDto;
import lu.plezy.timesheet.entities.messages.MessageDto;
import lu.plezy.timesheet.i18n.StaticText;
import lu.plezy.timesheet.repository.UsersRepository;
import lu.plezy.tools.RandomString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(path = "/user")
public class ManageUserController {

    private static Logger log = LoggerFactory.getLogger(ApplicationInfosController.class);

    private int defaultPageSize = 10;

    @Autowired
    UsersRepository usersRepository;

    /**
     * Add a new user.
     * 
     * @param newUser User entity to add
     * @return the newly adde user.
     */
    @PostMapping(value = "/add")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public User addNewUser(@Valid @RequestBody User newUser) {
        Optional<User> result = usersRepository.findByUsername(newUser.getUsername());
        if (result.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User exists with the same username !");
        }
        newUser.setPassword(RandomString.getAlphaNumericString(16));
        newUser.setLocked(true);
        User addedUser = usersRepository.save(newUser);

        return addedUser;
    }

    /**
     * Gets currently logged user details.
     * 
     * @param authentication currently logged user's credentials
     * 
     * @return User's details
     */
    @GetMapping(value = "/me")
    @PreAuthorize("isAuthenticated()")
    public User getUser(Authentication authentication) {
        Optional<User> result = usersRepository.findByUsername(authentication.getName());
        return result.isPresent() ? result.get() : null;
    }

    /**
     * Update the profile of currently logged user.
     * 
     * @param authentication currently logged user's credentials
     * 
     * @return Updated user's details
     */
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

    /**
     * Gets the roles of the currently logged user.
     * 
     * @param authentication currently logged user's credentials
     * 
     * @return List of roles
     */
    @GetMapping(value = "/me/roles")
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

    /**
     * Sets the password of a user, identified by ID.
     * 
     * @param authentication currently logged user's credentials
     * @param id             user's ID
     * @param message        message object containing new user's password
     * 
     * @return User's details
     */
    @PutMapping(value = "/setPassword/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public User setUserPassword(Authentication authentication, @PathVariable("id") long id,
            @Valid @RequestBody MessageDto message) {
        Optional<User> result = usersRepository.findById(id);
        if (result.isPresent()) {

            User user = result.get();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String encodedPassword = encoder.encode(message.getMessage());
            user.setPassword(encodedPassword);
            user.setLocked(false);

            usersRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        return result.isPresent() ? result.get() : null;
    }

    /**
     * Sets the password of the currently logged user.
     * 
     * @param authentication currently logged user's credentials
     * @param message        message object containing new user's password
     * 
     * @return User's details
     */
    @PutMapping(value = "/setPassword")
    @PreAuthorize("isAuthenticated()")
    public User setUserPassword(Authentication authentication, @Valid @RequestBody MessageDto message) {
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

    /**
     * Gets details of user identified by ID.
     * 
     * @param id user's ID
     * @return User's details
     */
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public User getUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    /**
     * Updates profile of a user, identified by ID
     * 
     * @param id          user's ID
     * @param userDetails Updated profile details of user
     * 
     * @return User's details
     */
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public User updateUser(@PathVariable("id") long id, @Valid @RequestBody User userDetails) {
        Optional<User> result = usersRepository.findById(id);
        if (result.isPresent()) {
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

    /**
     * Delete a user. The user is first logically deleted and then an attempt to
     * delete the user physically is performed.
     * 
     * @param id User's ID
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void deleteUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);

        if (result.isPresent()) {
            User user = result.get();

            deleteUserLogically(user);

            deleteUserPhysically(user.getId());
        }
    }

    /**
     * Deletes a user logically.
     * 
     * @param user User entity
     */
    @Transactional
    void deleteUserLogically(User user) {
        user.setDeleted(true);
        // update user for logical deletion
        log.info("Perform user logical deletion");
        usersRepository.save(user);
    }

    /**
     * Attempt to delete physically a user with ID. If an exception occurs it is
     * trapped in order to avoir returning an error.
     * 
     * @param id user's ID
     */
    @Transactional
    void deleteUserPhysically(Long id) {
        // attempt physical deletion is possible ...
        log.info("Attempt user physical deletion");
        try {
            usersRepository.deleteById(id);
        } catch (Exception ex) {
            log.info("User can not be deleted physically ...");
        }
    }

    /**
     * Deletes user's in list.
     * 
     * @param userIds user's IDs list
     */
    @DeleteMapping(path = "/list/{ids}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void deleteUsers(@PathVariable("ids") String userIds) {
        log.info("Deleting user's Ids : " + userIds);
        List<Long> idsList = Arrays.asList(userIds.split(",")).stream().map(Long::parseLong)
                .collect(Collectors.toList());
        log.info("list of ids : " + idsList);

        for (Long id : idsList) {
            Optional<User> result = usersRepository.findById(id);
            if (result.isPresent()) {
                User user = result.get();

                deleteUserLogically(user);

                deleteUserPhysically(user.getId());
            }
        }

    }

    /**
     * Undelete (logically deleted) user.
     * 
     * @param id
     */
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

    /**
     * lock user.
     * 
     * @param id user's ID
     */
    @PutMapping(value = "/lock/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void lockUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);

        if (result.isPresent()) {
            User user = result.get();

            setUserLock(user, true);
        }
    }

    /**
     * unlock user.
     * 
     * @param id user's ID
     */
    @PutMapping(value = "/unlock/{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public void unlockUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);

        if (result.isPresent()) {
            User user = result.get();

            setUserLock(user, false);
        }
    }

    /**
     * Set users lock state.
     * 
     * @param user      User entity
     * @param lockState status of lock state (true = locked, false = unlocked)
     */
    private void setUserLock(User user, boolean lockState) {
        user.setLocked(lockState);
        // update user lock state
        log.info("Set user lock State to " + lockState);
        usersRepository.save(user);
    }

    /**
     * returns first page of a paged list of all active users. Page sise is equals
     * to the deafault page size.
     */
    @GetMapping(value = "/list")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getUsers() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("username"));
        return usersRepository.findAllActive(p);
    }

    /**
     * returns first page of a paged list of all active users.
     * 
     * @param size list size
     */
    @GetMapping(value = "/list/{size}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getUsers(@PathVariable("size") int size) {
        Pageable p = PageRequest.of(0, size, Sort.by("username"));
        return usersRepository.findAllActive(p);
    }

    /**
     * returns a page of a paged list of all active users.
     * 
     * @param size list size
     * @param page page number (0 based)
     */
    @GetMapping(value = "/list/{size}/{page}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getUsers(@PathVariable("size") int size, @PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, size, Sort.by("username"));
        return usersRepository.findAllActive(p);
    }

    /**
     * returns first page of the paged list of all users (also logically deleted).
     * Page sise is equals to the deafault page size.
     */
    @GetMapping(value = "/listAll")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getAllUsers() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("username"));
        return usersRepository.findAll(p);
    }

    /**
     * returns first page of the paged list of all users (also logically deleted)
     * 
     * @param size list size
     */
    @GetMapping(value = "/listAll/{size}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getAllUsers(@PathVariable("size") int size) {
        Pageable p = PageRequest.of(0, size, Sort.by("username"));
        return usersRepository.findAll(p);
    }

    /**
     * returns a page of a paged list of all users (also logically deleted)
     * 
     * @param size list size
     * @param page page number (0 based)
     */
    @GetMapping(value = "/listAll/{size}/{page}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getAllUsers(@PathVariable("size") int size, @PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, size, Sort.by("username"));
        return usersRepository.findAll(p);
    }

    /**
     * Returns complete list of available roles in the application.
     * 
     * @return List of all available roles in the application
     */
    @GetMapping(value = "/roles")
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

    /**
     * Returns roles for a given user.
     * 
     * @param id User's ID
     * @return List of current user's roles in the application
     */
    @GetMapping(value = "/{id}/roles")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public List<Role> getUserRoles(@PathVariable("id") long id) {
        List<Role> response = null;

        Optional<User> result = usersRepository.findById(id);
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

    /**
     * Updates roles for a given user.
     * 
     * @param id    User's ID
     * @param roles RoleSet body element
     * @return Updated user's details
     */
    @PutMapping(value = "/{id}/roles")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public User getUserRoles(@PathVariable("id") long id, @Valid @RequestBody RoleSet roles) {
        User user = null;
        Optional<User> result = usersRepository.findById(id);
        if (result.isPresent()) {
            user = result.get();

            Set<RoleEnum> roleSet = new HashSet<>();

            for (String role : roles.getRoles()) {
                roleSet.add(RoleEnum.valueOf(role));
            }

            user.setRoles(roleSet);
            usersRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        }
        return user;
    }

    /**
     * Gets filtered Billeable users. These are users which have the rights to record times in timesheet
     * 
     */

     @PostMapping(value = "/billable")
     @PreAuthorize("hasAuthority('MANAGE_CONTRACTS')")
     List<UserDto> getInvoiceableUsers(@Valid @RequestBody MessageDto message) {
        Integer size = message.getNumber() == null ? 10 : message.getNumber();
        List<User> users = usersRepository.findBillableWithFilter(message.getMessage());
        return users.stream().map(user->UserDto.convertToDto(user)).collect(Collectors.toList());
     }
}