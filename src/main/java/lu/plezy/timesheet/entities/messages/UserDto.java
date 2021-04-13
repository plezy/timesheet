package lu.plezy.timesheet.entities.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lu.plezy.timesheet.entities.User;

@Data
@AllArgsConstructor
public class UserDto {
    private long id;
    private String firstName;
    private String lastName;
    private String applicationLanguage;
    
    public static UserDto convertToDto(User user) {
        return new UserDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getApplicationLanguage()
        );
    }
    
}