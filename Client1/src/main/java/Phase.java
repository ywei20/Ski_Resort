import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Phase {

    private int phaseName;
    private int numRunningThreads;
    private int numSkiers;
    private int numRuns;
    private int numLifts;
    private double numRunsRatio;
    private int startTime;
    private int endTime;
    private AtomicInteger successReqCnt;
    private AtomicInteger failedReqCnt;
    private CountDownLatch latchAllPhase;
    private CountDownLatch latchBefNextPhase;
    private String serverAddress;

    public Phase(int phaseName, int numRunningThreads, int numSkiers, int numRuns, int numLifts,
            double numRunsRatio, int startTime, int endTime,
            AtomicInteger successReqCnt, AtomicInteger failedReqCnt,
            CountDownLatch latchAllPhase, String serverAddress) {
        this.phaseName = phaseName;
        this.numRunningThreads = numRunningThreads;
        this.numSkiers = numSkiers;
        this.numRuns = numRuns;
        this.numLifts = numLifts;
        this.numRunsRatio = numRunsRatio;
        this.startTime = startTime;
        this.endTime = endTime;
        this.successReqCnt = successReqCnt;
        this.failedReqCnt = failedReqCnt;
        this.latchAllPhase = latchAllPhase;
        this.serverAddress = serverAddress;
        this.latchBefNextPhase =
                (phaseName != 3) ? new CountDownLatch((int) Math.round(numRunningThreads * 0.1))
                        : null;
    }

    public CountDownLatch getLatchBefNextPhase() {
        return latchBefNextPhase;
    }

    public int calNumReqPerThread() {
        if (phaseName == 3) {
            return (int) Math.round(numRuns * numRunsRatio);
        } else {
            return (int) Math
                    .round(numRuns * numRunsRatio * numSkiers / (double) numRunningThreads);
        }
    }

    public void runPhase() {
        int skiersIdInterval = (int) numSkiers / numRunningThreads;
//    System.out.println("skierIDInterval: " + skiersIdInterval + "numSkiers: " + numSkiers + "numRunningThread: " + numRunningThreads);

        for (int i = 0; i < numRunningThreads; i++) {
            int skierStartId = i * skiersIdInterval + 1;
            int skierEndId = (i == numRunningThreads - 1) ? numSkiers : (i + 1) * skiersIdInterval;
//      System.out.println("skierStartId: " + skierStartId + " skierEndId: " + skierEndId);

            PostThread pthread = new PostThread(skierStartId, skierEndId, startTime, endTime,
                    numLifts,
                    calNumReqPerThread(), successReqCnt, failedReqCnt, latchBefNextPhase,
                    latchAllPhase, serverAddress);
            Thread thread = new Thread(pthread);
            thread.start();
        }
    }
}
