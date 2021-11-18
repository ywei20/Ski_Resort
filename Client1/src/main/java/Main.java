import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    // parse command-line arguments
    CmdParser cmdParser = new CmdParser();
    cmdParser.parseArguments(args);
    System.out.println(cmdParser);

    // create atomic counter
    AtomicInteger success = new AtomicInteger(0);
    AtomicInteger failure = new AtomicInteger(0);

    // actual running threads
    int numRunningThreadsPhase1n3 = (int) cmdParser.getNumThreads() / 4;
    int numRunningThreadsPhase2 = cmdParser.getNumThreads();

    CountDownLatch latchAllPhase = new CountDownLatch(2 * numRunningThreadsPhase1n3 + numRunningThreadsPhase2);
    long startTime = System.currentTimeMillis();

    // phase 1
    Phase phase1 = new Phase(1, numRunningThreadsPhase1n3, cmdParser.getNumSkiers(), cmdParser.getNumRuns(),
        cmdParser.getNumLifts(),0.2, 1, 90, success, failure, latchAllPhase, cmdParser.getServerAddr());
    phase1.runPhase();
    phase1.getLatchBefNextPhase().await();

    // phase 2
    Phase phase2 = new Phase(2, numRunningThreadsPhase2, cmdParser.getNumSkiers(), cmdParser.getNumRuns(),
        cmdParser.getNumLifts(),0.6, 91, 360, success, failure, latchAllPhase, cmdParser.getServerAddr());
    phase2.runPhase();
    phase2.getLatchBefNextPhase().await();

    // phase 3
    Phase phase3 = new Phase(3, numRunningThreadsPhase1n3, cmdParser.getNumSkiers(), cmdParser.getNumRuns(),
        cmdParser.getNumLifts(), 0.1, 361, 420, success, failure, latchAllPhase, cmdParser.getServerAddr());
    phase3.runPhase();

    latchAllPhase.await();
    long endTime = System.currentTimeMillis();

    DataAnalyzer analyzer = new DataAnalyzer(startTime, endTime, success, failure, cmdParser);
    analyzer.printSummary();

  }
}