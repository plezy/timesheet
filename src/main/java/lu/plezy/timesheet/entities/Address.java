package lu.plezy.timesheet.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor(access=AccessLevel.PUBLIC)
@RequiredArgsConstructor
@Embeddable
public class Address {
    @NonNull
    @Size(max = 128)
    @Column(name="ADDR_LINE1", length=128)
    private String addressLine1;

    @Size(max = 128)
    @Column(name="ADDR_LINE2", length=128)
    private String addressLine2;

    @NonNull
    @Size(max = 80)
    @Column(name="ADDR_CITY", length=80)
    private String city;

    @Size(max = 80)
    @Column(name="ADDR_AREA", length=80)
    private String area;

    @Size(max = 80)
    @Column(name="ADDR_COUNTRY", length=80)
    private String country;

    @Size(max = 8)
    @Column(name="ADDR_POSTCODE", length=8)
    private String postCode;    

}