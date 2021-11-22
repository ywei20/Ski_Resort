import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class Phase {
    private int phaseName;
    private int totNumThreads;
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
    private List<PostRecord> postRecordsAllPhase;

    public Phase(int phaseName, int totNumThreads, int numSkiers, int numRuns, int numLifts,
            double numRunsRatio, int startTime, int endTime,
            AtomicInteger successReqCnt, AtomicInteger failedReqCnt,
            CountDownLatch latchAllPhase, String serverAddress, List<PostRecord> postRecordsAllPhase) {
        this.phaseName = phaseName;
        this.totNumThreads = totNumThreads;
        this.numRunningThreads = calNumRunningThreads(totNumThreads);
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
        this.latchBefNextPhase = (phaseName != 3)? new CountDownLatch((int)Math.round(numRunningThreads * 0.1)) : null;
        this.postRecordsAllPhase = postRecordsAllPhase;
    }

    public CountDownLatch getLatchBefNextPhase() {
        return latchBefNextPhase;
    }

    public int calNumRunningThreads(int totNumThreads) {
        if (phaseName == 2) {
            return totNumThreads;
        } else {
            return Math.round(totNumThreads / 4f);
        }
    }

    public int calNumReqPerThread() {
        if (phaseName == 3) {
            return (int) Math.round(numRuns * numRunsRatio);
        } else {
            return (int) Math.round(numRuns * numRunsRatio * numSkiers / (double) numRunningThreads);
        }
    }

    public void runPhase() {
        int skiersIdInterval = (int) numSkiers / numRunningThreads;

        for (int i = 0; i < numRunningThreads; i++) {
            int skierStartId = i * skiersIdInterval + 1;
            int skierEndId = (i == numRunningThreads-1)? numSkiers : (i+1)*skiersIdInterval;

            PostThread pthread = new PostThread(skierStartId, skierEndId, startTime, endTime, numLifts,
                    calNumReqPerThread(), successReqCnt, failedReqCnt, latchBefNextPhase, latchAllPhase,
                    serverAddress, postRecordsAllPhase, totNumThreads);
            Thread thread = new Thread(pthread);
            thread.start();
        }
    }
}
