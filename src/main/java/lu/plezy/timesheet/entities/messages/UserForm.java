package lu.plezy.timesheet.entities.messages;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lu.plezy.timesheet.entities.RoleEnum;

@Data
@NoArgsConstructor(access=AccessLevel.PUBLIC)
public class UserForm {
    private long id;
    @NonNull
    @NotNull
    @NotBlank
    @Size(min=3, max = 32)
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String mobile;
    private String email;

    private boolean locked = false;
    private Set<RoleEnum> roles;    
}