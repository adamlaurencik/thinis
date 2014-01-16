package sk.gymdb.thinis.gcm;

/**
 * Created by matejkobza on 15.1.2014.
 */
public class GcmServiceException extends Exception {

    public GcmServiceException() {
    }

    public GcmServiceException(String detailMessage) {
        super(detailMessage);
    }

    public GcmServiceException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public GcmServiceException(Throwable throwable) {
        super(throwable);
    }
}
