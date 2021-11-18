import java.util.concurrent.atomic.AtomicInteger;

public class DataAnalyzer {
  private long startTime;
  private long endTime;
  private AtomicInteger success;
  private AtomicInteger failure;
  private CmdParser parser;

  public DataAnalyzer(long startTime, long endTime, AtomicInteger success,
      AtomicInteger failure, CmdParser parser) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.success = success;
    this.failure = failure;
    this.parser = parser;
  }

  public void printSummary() {
    System.out.println("============Summary===========");
    System.out.println(parser);
    System.out.println("# of successful requests: " + success.get());
    System.out.println("# of failed requests: " + failure.get());
    System.out.println("Total run time(ms)(wall time): " + (endTime - startTime));
    System.out.println("Total throughput(# of requests/sec): " + Math.round((success.get() + failure.get())/(double)(endTime - startTime)*1000));
  }
}
