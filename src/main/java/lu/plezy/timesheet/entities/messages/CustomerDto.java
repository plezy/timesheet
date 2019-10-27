package lu.plezy.timesheet.entities.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lu.plezy.timesheet.entities.Customer;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private long id;
    private String name;

    public static CustomerDto convertToDto(Customer customer) {
        return new CustomerDto(
            customer.getId(),
            customer.getName()
        );
    }
}