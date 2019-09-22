package lu.plezy.timesheet.entities.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ContractType {
    @NonNull
    private String id;

    @JsonProperty(value="description")
    private String contractTypeDescription;
}