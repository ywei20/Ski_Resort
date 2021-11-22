public class PostRecord implements Comparable<PostRecord> {
    private long startTime;
    private long latency;
    private int statusCode;
    private String threadName;

    public PostRecord(long startTime, long latency, int statusCode, String threadName) {
        this.startTime = startTime;
        this.latency = latency;
        this.statusCode = statusCode;
        this.threadName = threadName;
    }

    @Override
    public String toString() {
        return startTime + "," + "POST," + latency + "," + statusCode + "," + threadName;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getLatency() {
        return latency;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public int compareTo(PostRecord otherPost) {
        if (this.latency < otherPost.latency) {
            return -1;
        } else if (this.latency == otherPost.latency) {
            return 0;
        } else {
            return 1;
        }
    }


}
