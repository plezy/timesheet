package lu.plezy.timesheet.entities.messages;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class StringMessage {
    @NonNull
    private Long id;
    @NonNull
    private String message;
}