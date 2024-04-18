package model;

public enum Template {
    WEEKLY_REPORT("weekly-report"),
    MONTHLY_REPORT("monthly-report"),
    GREETING("greeting");

    final String value;

    Template(String value) {
        this.value = value;
    }
}
