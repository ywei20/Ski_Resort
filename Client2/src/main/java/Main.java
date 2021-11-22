import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

        CountDownLatch latchAllPhase = new CountDownLatch(Math.round((1+2/4f)*cmdParser.getNumThreads()));
        List<PostRecord> postRecords = Collections.synchronizedList(new ArrayList<PostRecord>());
        long startTime = System.currentTimeMillis();

        // phase 1
        System.out.println("Phase 1 start...");
        Phase phase1 = new Phase(1, cmdParser.getNumThreads(), cmdParser.getNumSkiers(), cmdParser.getNumRuns(),
                cmdParser.getNumLifts(),0.2, 1, 90, success, failure, latchAllPhase, cmdParser.getServerAddr(), postRecords);
        phase1.runPhase();
        phase1.getLatchBefNextPhase().await();

        // phase 2
        System.out.println("Phase 2 start...");
        Phase phase2 = new Phase(2, cmdParser.getNumThreads(), cmdParser.getNumSkiers(), cmdParser.getNumRuns(),
                cmdParser.getNumLifts(),0.6, 91, 360, success, failure, latchAllPhase, cmdParser.getServerAddr(), postRecords);
        phase2.runPhase();
        phase2.getLatchBefNextPhase().await();

        // phase 3
        System.out.println("Phase 3 start...");
        Phase phase3 = new Phase(3, cmdParser.getNumThreads(), cmdParser.getNumSkiers(), cmdParser.getNumRuns(),
                cmdParser.getNumLifts(), 0.1, 361, 420, success, failure, latchAllPhase, cmdParser.getServerAddr(), postRecords);
        phase3.runPhase();

        latchAllPhase.await();
        long endTime = System.currentTimeMillis();
        System.out.println("All three phases end...");

        DataAnalyzer analyzer = new DataAnalyzer(startTime, endTime, success, failure, cmdParser, postRecords);
        analyzer.printSummary();


    }
}