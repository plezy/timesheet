package lu.plezy.timesheet.entities.messages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor(access=AccessLevel.PUBLIC)
public class LoginForm {
    @NonNull
    @NotNull
    @NotBlank
    @Size(min=3, max = 32)
    private String username;
 
    @NonNull
    @NotNull
    @NotBlank
    @Size(min = 6, max = 32)
    private String password;
}