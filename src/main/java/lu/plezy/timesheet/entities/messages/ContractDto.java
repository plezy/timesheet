package lu.plezy.timesheet.entities.messages;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lu.plezy.timesheet.entities.Contract;
import lu.plezy.timesheet.i18n.StaticText;

@Getter
@AllArgsConstructor
public class ContractDto {
    private long id;
    private String name;
    private String contractType;
    private String contractTypeName;
    private long customerId;
    private String customerName;
    private Date plannedStart;
    private Date plannedEnd;
    private boolean deleted = false;
    private boolean archived = false;

    public static ContractDto convertToDto(Contract contract) {
        return new ContractDto(
            contract.getId(),
            contract.getName(),
            contract.getContractType().name(),
            StaticText.getInstance().getText(contract.getContractType().toString()),
            contract.getCustomer().getId(),
            contract.getCustomer().getName(),
            contract.getPlannedStart(),
            contract.getPlannedEnd(),
            contract.isDeleted(),
            contract.isArchived()
        );
    }
}