public class CmdParser {

  private static final int MAX_NUMTHREADS = 256;
  private static final int MAX_NUMSKIERS = 100000;
  private static final int DEFAULT_NUMLIFTS = 40;
  private static final int MIN_NUMLIFTS = 5;
  private static final int MAX_NUMLIFTS = 60;
  private static final int DEFAULT_NUMRUNS = 10;
  private static final int MAX_NUMRUNS = 20;

  private int numThreads;
  private int numSkiers;
  private int numLifts;
  private int numRuns;
  private String serverAddr;

  public CmdParser() {
    this.numThreads = 0;
    this.numSkiers = 0;
    this.numLifts = DEFAULT_NUMLIFTS;
    this.numRuns = DEFAULT_NUMRUNS;
    this.serverAddr= null;
  }

  public void parseArguments(String[] args) {
    for (int i = 0; i < args.length; i+=2) {
      String item = args[i];
      String value = args[i+1];

      if (item.equals("numThreads")) {
        int val = Integer.valueOf(value);
        if (val > MAX_NUMTHREADS) {
          val = MAX_NUMTHREADS;
          System.out.println("Warning: parameter numThreads out of bound. Set numThreads to maximum numThreads(" + MAX_NUMTHREADS + ")");
        }
        this.numThreads = val;
      }

      if (item.equals("numSkiers")) {
        int val = Integer.valueOf(value);
        if (val > MAX_NUMSKIERS) {
          val = MAX_NUMSKIERS;
          System.out.println("Warning: parameter numSkiers out of bound. Set numSkiers to maximum numSkiers(" + MAX_NUMSKIERS + ")");
        }
        this.numSkiers = val;
      }

      if (item.equals("numLifts")) {
        int val = Integer.valueOf(value);
        if (val > MAX_NUMLIFTS || val < MIN_NUMLIFTS) {
          val = DEFAULT_NUMLIFTS;
          System.out.println("Warning: parameter numLifts out of bound. Set numLifts to default numLifts(" + DEFAULT_NUMLIFTS + ")");
        }
        this.numLifts = val;
      }

      if (item.equals("numRuns")) {
        int val = Integer.valueOf(value);
        if (val > MAX_NUMRUNS) {
          val = DEFAULT_NUMRUNS;
          System.out.println("Warning: parameter numRuns out of bound. Set numRuns to default numLifts(" + DEFAULT_NUMRUNS + ")");
        }
        this.numRuns = val;
      }

      if (item.equals("serverAddress")) {
        this.serverAddr = value;
      }

    }
  }

  public int getNumThreads() {
    return numThreads;
  }

  public int getNumSkiers() {
    return numSkiers;
  }

  public int getNumLifts() {
    return numLifts;
  }

  public int getNumRuns() {
    return numRuns;
  }

  public String getServerAddr() {
    return serverAddr;
  }

  @Override
  public String toString() {
    return "CmdParser{" +
        "numThreads=" + numThreads +
        ", numSkiers=" + numSkiers +
        ", numLifts=" + numLifts +
        ", numRuns=" + numRuns +
        ", serverAddr='" + serverAddr + '\'' +
        '}';
  }


}
