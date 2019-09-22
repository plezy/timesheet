package lu.plezy.timesheet.entities;

public enum ContractTypeEnum {
    PROJECT("task.type.project"),
    TIMES_MEANS("task.type.times_means")
    ;
    
    private final String text;

    ContractTypeEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}