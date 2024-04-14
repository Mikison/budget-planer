package pl.sonmiike.mailservice;

public enum Template {
    WEEKLY_REPORT("weekly-report"),
    MONTHLY_REPORT("monthly-report");

    final String value;

    Template(String value) {
        this.value = value;
    }
}
