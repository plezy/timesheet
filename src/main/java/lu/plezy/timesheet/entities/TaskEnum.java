package lu.plezy.timesheet.entities;

public enum TaskEnum {
    P("task.type.project"),
    T("task.type.task")
    ;
    
    private final String text;

    TaskEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}