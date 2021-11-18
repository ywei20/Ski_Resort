import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleThreadTest {

    private static final int NUM_REQUESTS = 10000;
    private static final int MAX_REQUEST_ATTEMPTS = 5;

    public static void main(String[] args) throws InterruptedException {
        CmdParser cmdParser = new CmdParser();
        cmdParser.parseArguments(args);

        int successReqCnt = 0;
        int failedReqCnt = 0;

        ApiClient apiClient = new ApiClient();
//    String path = cmdParser.getServerAddr() + "/Server_war_exploded/";  //for local tomcat server
        String path = cmdParser.getServerAddr() + "/Server_war/";  // for ec2 tomcat server
        apiClient.setBasePath(path);  //path e.g. "http://ip:port/war_name/"
        SkiersApi skiersApi = new SkiersApi(apiClient);

        //Before sending requests take a timestamp:
        long startTime = System.currentTimeMillis();
//        System.out.println("numSkiers=" + cmdParser.getNumSkiers());
        for (int i = 0; i < NUM_REQUESTS; i++) {
            int randomSkierId = ThreadLocalRandom.current().nextInt(1, cmdParser.getNumSkiers()+1);
            int randomLiftId = ThreadLocalRandom.current().nextInt(1, cmdParser.getNumLifts()+1);
            int randomTime = ThreadLocalRandom.current().nextInt(1, 420+1);
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
                    apiResponse = skiersApi.writeNewLiftRideWithHttpInfo(liftRide, resortId, seasonId, dayId, randomSkierId);
                    if (apiResponse.getStatusCode() == 200 || apiResponse.getStatusCode() == 201) {
                        break;
                    }
                    requestCnt += 1;
                }
                if (requestCnt < MAX_REQUEST_ATTEMPTS + 1) {
                    successReqCnt += 1;
                } else {
                    failedReqCnt += 1;
                }
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }

        // after sending requests take a timestamp
        long endTime = System.currentTimeMillis();
        System.out.println("******* Test Results Sending " + NUM_REQUESTS + " Requests From A Single Thread *******");
        System.out.println("# of successful requests: " + successReqCnt);
        System.out.println("# of failed requests: " + failedReqCnt);
        System.out.println("Total run time(ms)(wall time): " + (endTime - startTime));
        System.out.println("Total throughput(# of requests/sec): " + Math.round((double) NUM_REQUESTS / (endTime - startTime) * 1000));

    }
}
