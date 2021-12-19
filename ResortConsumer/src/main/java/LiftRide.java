public class LiftRide {
    private int skierID;
    private int resortID;
    private int dayID;
    private int liftID;
    private int time;

    public LiftRide(int skierID, int resortID, int dayID, int liftID, int time) {
        this.skierID = skierID;
        this.resortID = resortID;
        this.dayID = dayID;
        this.liftID = liftID;
        this.time = time;
    }

    public int getSkierID() {
        return skierID;
    }

    public int getResortID() {
        return resortID;
    }

    public int getDayID() {
        return dayID;
    }

    public int getLiftID() {
        return liftID;
    }

    public int getTime() {
        return time;
    }

    public void setSkierID(int skierID) {
        this.skierID = skierID;
    }

    public void setResortID(int resortID) {
        this.resortID = resortID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public void setLiftID(int liftID) {
        this.liftID = liftID;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
