package lu.plezy.timesheet.entities.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private Long id;
    private Integer number;
    @NonNull
    private String message;
}