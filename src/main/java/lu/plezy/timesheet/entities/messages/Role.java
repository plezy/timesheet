package lu.plezy.timesheet.entities.messages;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Role {
    @NonNull
    private String roleId;

    private String roleDescription;
}