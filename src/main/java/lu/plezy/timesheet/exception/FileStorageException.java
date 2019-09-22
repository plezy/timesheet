package lu.plezy.timesheet.exception;

public class FileStorageException extends Throwable {
    private static final long serialVersionUID = 1L;

    public FileStorageException(String msg) {
        super(msg);
    }

    public FileStorageException(Throwable cause) {
        super(cause);
    }

}