package lu.plezy.timesheet.entities.messages;

import lombok.Data;

@Data
public class StringResponse {
    private String value;

    public StringResponse(String s) {
        this.value = s;
    }
}