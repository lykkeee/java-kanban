public class Status {
    static String NEW;
    static String IN_PROGRESS;
    static String DONE;

    protected Status(String NEW, String IN_PROGRESS, String DONE) {
        Status.NEW = NEW;
        Status.IN_PROGRESS = IN_PROGRESS;
        Status.DONE = DONE;
    }
}