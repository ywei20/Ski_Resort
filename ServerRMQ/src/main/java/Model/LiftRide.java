package Model;

public class LiftRide {
    private int time;
    private int liftID;
    private int skierID;
    private int resortID;
    private int seasonID;
    private int dayID;


    public LiftRide(int time, int liftID, int skierID, int resortID, int seasonID, int dayID) {
        this.time = time;
        this.liftID = liftID;
        this.skierID = skierID;
        this.resortID = resortID;
        this.seasonID = seasonID;
        this.dayID = dayID;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setLiftID(int liftID) {
        this.liftID = liftID;
    }

    public void setSkierID(int skierID) {
        this.skierID = skierID;
    }

    public void setResortID(int resortID) {
        this.resortID = resortID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }
}
