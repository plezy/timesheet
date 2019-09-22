package lu.plezy.timesheet.entities;

public enum RoleEnum {
    ENTER_TIME_TRACK("role.enter_time_track"),
    ENTER_OTHERS_TIME_TRACK("role.enter_others_time_track"),
    GENERATE_REPORT("role.generate_report"),
    PREPARE_INVOICE("role.prepare_invoice"),
    MANAGE_CUSTOMERS("role.manage_customers"),
    MANAGE_CONTRACTS("role.manage_contracts"),
    // MANAGE_PROJECTS("role.manage_projects"),
    MANAGE_USERS("role.manage_users"),
    MANAGE_BILLING_TYPES("role.manage_billing_types"),
    MANAGE_WORK_SCHEDULE("role.manage_work_schedule"),
    MANAGE_SETTINGS("role.manage_setting")
;
    private final String text;

    RoleEnum(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}