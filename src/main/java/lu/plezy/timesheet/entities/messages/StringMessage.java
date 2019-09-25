package lu.plezy.timesheet.entities.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class StringMessage {
    private Long id;
    @NonNull
    private String message;
}