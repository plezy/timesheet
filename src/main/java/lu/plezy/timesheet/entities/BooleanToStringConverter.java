package lu.plezy.timesheet.entities;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import lu.plezy.timesheet.i18n.StaticText;

@Converter
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        if (attribute == null)
            return null;
        else
            return attribute ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;
        else if (dbData.equals("Y"))
            return true;
        else if (dbData.equals("N"))
            return false;
        else
            throw new IllegalStateException(
                    String.format("%s => %s)", StaticText.getInstance().getText("error.invalid.boolean.char"), dbData));
    }

}