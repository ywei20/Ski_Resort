import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DataAnalyzer {
    private long startTime;
    private long endTime;
    private AtomicInteger success;
    private AtomicInteger failure;
    private CmdParser parser;
    private List<PostRecord> postRecords;

    public DataAnalyzer(long startTime, long endTime, AtomicInteger success,
            AtomicInteger failure, CmdParser parser, List<PostRecord> postRecords) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.success = success;
        this.failure = failure;
        this.parser = parser;
        this.postRecords = postRecords;
    }

    public void printSummary() {
        System.out.println("============Print Out Summary===========");
        System.out.println("# of successful requests: " + success.get());
        System.out.println("# of failed requests: " + failure.get());
        System.out.println("Total run time(wall time)(millisec): " + (endTime - startTime));
        System.out.println("Total throughput(# of requests/sec): " + Math.round((success.get() + failure.get())/(double)(endTime - startTime)*1000));

        Collections.sort(this.postRecords);
        System.out.println("Mean Response Time(millisec): " + getMeanResponseTime());
        System.out.println("Median Response Time(millisec): " + getMedianResponseTime());
        System.out.println("99 Percentile Response Time(millisec): " + getP99ResponseTime());
        System.out.println("Max Response Time(millisec): " + getMaxResponseTime());
    }

    public long getMeanResponseTime() {
        long responseSum = 0;
        for (PostRecord pr : postRecords) {
            responseSum += pr.getLatency();
        }
        return responseSum / postRecords.size();
    }

    public long getMedianResponseTime() {
        if (postRecords.size() % 2 != 0) {
            return postRecords.get(postRecords.size()/2).getLatency();
        } else {
            return (postRecords.get(postRecords.size()/2).getLatency() +
                    postRecords.get((postRecords.size()+1)/2).getLatency()) / 2;
        }
    }

    public long getP99ResponseTime() {
        return postRecords.get(postRecords.size() * 99 / 100).getLatency();
    }

    public long getMaxResponseTime() {
        return postRecords.get(postRecords.size()-1).getLatency();
    }
}
