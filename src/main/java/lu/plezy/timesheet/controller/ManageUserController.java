package lu.plezy.timesheet.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lu.plezy.timesheet.entities.RoleEnum;
import lu.plezy.timesheet.entities.User;
import lu.plezy.timesheet.entities.messages.Role;
import lu.plezy.timesheet.i18n.StaticText;
import lu.plezy.timesheet.repository.UsersRepository;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(path = "/user")
public class ManageUserController {

    private int defaultPageSize = 10;

    @Autowired
    UsersRepository usersRepository;

    @GetMapping(value = "")
    @PreAuthorize("isAuthenticated()")
    public User getUser(Authentication authentication) {
        Optional<User> result = usersRepository.findByUsername(authentication.getName());
        return result.isPresent() ? result.get() : null;
    }

    @GetMapping(value = "{id}")
    @PreAuthorize("hasAuthority('MANAGE_USERS')")
    public User getUser(@PathVariable("id") long id) {
        Optional<User> result = usersRepository.findById(id);
        return result.isPresent() ? result.get() : null;
    }

    @GetMapping(value = "/list")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getUsers() {
        Pageable p = PageRequest.of(0, defaultPageSize, Sort.by("lastName", "firstName"));
        return usersRepository.findAll(p);
    }

    @GetMapping(value = "/list/{page}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getUsers(@PathVariable("page") int page) {
        Pageable p = PageRequest.of(page, defaultPageSize, Sort.by("lastName", "firstName"));
        return usersRepository.findAll(p);
    }

    @GetMapping(value = "/list/{page}/{size}")
    @PreAuthorize("isAuthenticated()")
    public Page<User> getUsers(@PathVariable("page") int page, @PathVariable("size") int size) {
        Pageable p = PageRequest.of(page, size, Sort.by("lastName", "firstName"));
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