package whereQR.project.utils.response;

public enum Status {
    SUCCESS("SUCCESS"),
    FAILED("FAILED");

    private final String type;

    Status(String type) {
        this.type = type;
    }

}
