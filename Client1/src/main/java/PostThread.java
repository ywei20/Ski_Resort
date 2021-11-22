import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class PostThread implements Runnable {

    private int MAX_REQUEST_ATTEMPTS = 5;
    private int skierStartId;
    private int skierEndId;
    private int startTime;
    private int endTime;
    private int numLifts;
    private int numRequestsPerThread;
    private AtomicInteger successReqCnt;
    private AtomicInteger failedReqCnt;
    private CountDownLatch latchBefNextPhase;
    private CountDownLatch latchAllPhase;
    private String serverAddress; // format: "http://ip:port/web_app/"

    public PostThread(int skierStartId, int skierEndId, int startTime, int endTime, int numLifts,
            int numRequestsPerThread, AtomicInteger successReqCnt,
            AtomicInteger failedReqCnt, CountDownLatch latchBefNextPhase,
            CountDownLatch latchAllPhase, String serverAddress) {
        this.skierStartId = skierStartId;
        this.skierEndId = skierEndId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numLifts = numLifts;
        this.numRequestsPerThread = numRequestsPerThread;
        this.successReqCnt = successReqCnt;
        this.failedReqCnt = failedReqCnt;
        this.latchBefNextPhase = latchBefNextPhase;
        this.latchAllPhase = latchAllPhase;
//        this.serverAddress = serverAddress + "/Server_war_exploded/";  //for local tomcat server
        this.serverAddress = serverAddress + "/Server_war/";  // for ec2 tomcat server
    }

    @Override
    public void run() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(this.serverAddress);
        SkiersApi skiersApi = new SkiersApi(apiClient);

        for (int i = 0; i < numRequestsPerThread; i++) {
            int randomSkierId = ThreadLocalRandom.current()
                    .nextInt(this.skierStartId, this.skierEndId + 1);
            int randomLiftId = ThreadLocalRandom.current().nextInt(1, this.numLifts + 1);
            int randomTime = ThreadLocalRandom.current().nextInt(startTime, endTime + 1);
            int resortId = 1;
            String seasonId = "2020";
            String dayId = "12";

            LiftRide liftRide = new LiftRide();
            liftRide.setLiftID(randomLiftId);
            liftRide.setTime(randomTime);
            ApiResponse<Void> apiResponse;

            try {
                int requestCnt = 0;
                while (requestCnt <= MAX_REQUEST_ATTEMPTS) {
                    apiResponse = skiersApi
                            .writeNewLiftRideWithHttpInfo(liftRide, resortId, seasonId, dayId,
                                    randomSkierId);
                    if (apiResponse.getStatusCode() == 200 || apiResponse.getStatusCode() == 201) {
                        break;
                    }
                    requestCnt += 1;
                }
                if (requestCnt < MAX_REQUEST_ATTEMPTS + 1) {
                    this.successReqCnt.getAndIncrement();
                } else {
                    this.failedReqCnt.getAndIncrement();
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

        if (latchBefNextPhase != null) {
            latchBefNextPhase.countDown();
        }
        latchAllPhase.countDown();
    }
}

