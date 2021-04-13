package lu.plezy.timesheet.entities;

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@Entity(name = "USERS")
@SequenceGenerator(name = "USERS_SEQ", initialValue = 100, allocationSize = 1)
public class User {

    @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERS_SEQ")
    @Column(name = "ID", updatable = false, nullable = false)
    private long id;

    @NonNull
    @Column(name = "USERNAME", nullable = false, length = 32, unique = true)
    private String username;
    @JsonIgnore
    @NonNull
    @Column(name = "PASSWORD", nullable = false, length = 128)
    private String password;
    @NonNull
    @Column(name = "FIRSTNAME", nullable = false, length = 64)
    private String firstName;
    @NonNull
    @Column(name = "LASTNAME", nullable = false, length = 64)
    private String lastName;

    @Column(name = "PHONE", length = 64)
    private String phone;
    @Column(name = "MOBILE", length = 64)
    private String mobile;
    @NonNull
    @Column(name = "EMAIL", length = 92)
    private String email;

    @Column(name = "LOCKED", length = 1)
    @Convert(converter = BooleanToStringConverter.class)
    private boolean locked = false;

    @Column(name = "DELETED", length = 1)
    @Convert(converter = BooleanToStringConverter.class)
    private boolean deleted = false;

    @ElementCollection(targetClass = RoleEnum.class)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USR_ID"))
    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", length=32)
    private Set<RoleEnum> roles;

    @NonNull
    @Column(name = "APP_LANG", nullable = false,columnDefinition = "CHAR(2) DEFAULT 'en'")
    private String applicationLanguage = "en";

}