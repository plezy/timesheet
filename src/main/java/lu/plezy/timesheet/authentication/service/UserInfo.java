package lu.plezy.timesheet.authentication.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lu.plezy.timesheet.entities.User;

/*
* UserPrinciple will implement UserDetails.
* UserPrinciple is not used directly by Spring Security for security purposes.
* It simply stores user information which is later encapsulated into Authentication objects. This allows non-security related
* user information (such as email addresses, telephone numbers etc) to be stored.
*/
public class UserInfo implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String username;
    private String email;
    private boolean locked;

    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public UserInfo(Long id, String name, String username, String email, String password, boolean locked,
            Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.locked = locked;
        this.authorities = authorities;
    }

    public static UserInfo build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toList());
        StringBuilder givenName = new StringBuilder(user.getFirstName());
        givenName.append(' ').append(user.getLastName());
        return new UserInfo(user.getId(), givenName.toString(), user.getUsername(), user.getEmail(), user.getPassword(),
                user.isLocked(), authorities);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UserInfo user = (UserInfo) o;
        return Objects.equals(id, user.id);
    }
}