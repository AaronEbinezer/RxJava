package retromodels;

public interface DownloadProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
