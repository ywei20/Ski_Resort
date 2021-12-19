import com.google.gson.Gson;

public class LiftRide {
    private int skierID;
    private int seasonID;
    private int dayID;
    private int liftID;
    private int vertical;

    public LiftRide(int skierID, int seasonID, int dayID, int liftID, int vertical) {
        this.skierID = skierID;
        this.seasonID = seasonID;
        this.dayID = dayID;
        this.liftID = liftID;
        this.vertical = vertical;
    }

    public int getSkierID() {
        return skierID;
    }

    public int getSeasonID() {
        return seasonID;
    }

    public int getDayID() {
        return dayID;
    }

    public int getLiftID() {
        return liftID;
    }

    public int getVertical() {
        return vertical;
    }

    public void setSkierID(int skierID) {
        this.skierID = skierID;
    }

    public void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public void setLiftID(int liftID) {
        this.liftID = liftID;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}

