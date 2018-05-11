package components;

import java.io.Serializable;

public class SubtitleBit implements Serializable {

    private String lineOne;
    private String lineTwo;
    private String lineThree;
    private String startTime;
    private String endTime;
    private int number;

    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public SubtitleBit(String startTime, String endTime, String lineOne, String lineTwo, String lineThree, int number) {
        this.lineOne = lineOne;
        this.lineTwo = lineTwo;
        this.lineThree = lineThree;
        this.startTime = startTime;
        this.endTime = endTime;
        this.number = number;
    }

    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public String getLineOne() {
        return lineOne;
    }
    public String getLineTwo() {
        return lineTwo;
    }

    public String getLineThree() {
        return lineThree;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getNumber() {
        return number;
    }

    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setLineOne(String lineOne) {
        this.lineOne = lineOne;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = lineTwo;
    }

    public void setLineThree(String lineThree) {
        this.lineThree = lineThree;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    @Override
    public String toString() {
        return lineOne + "<br>" + lineTwo + "<br>" + lineThree;

    }
}
