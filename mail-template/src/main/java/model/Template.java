package model;

public enum Template {
    WEEKLY_REPORT("weekly-report"),
    MONTHLY_REPORT("monthly-report"),
    CUSTOM_DATE_REPORT("custom-date-report"),
    GREETING("greeting");

    final String value;

    Template(String value) {
        this.value = value;
    }
}
