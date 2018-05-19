package components;

import java.awt.*;
import java.io.Serializable;

public class SubtitleBit implements Serializable {

    private String lineOne;
    private String lineTwo;
    private String lineThree;

    private String startTime;
    private String endTime;
    private int number;
    private Point scrollPos;
    private Episode episode;
    private int timeSinceLast;

    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public SubtitleBit(String startTime, String endTime, String lineOne, String lineTwo, String lineThree, int number, Episode episode, int timeSinceLast) {
        this.lineOne = lineOne;
        this.lineTwo = lineTwo;
        this.lineThree = lineThree;

        this.startTime = startTime;
        this.endTime = endTime;
        this.number = number;
        this.scrollPos = new Point(0, 0);
        this.episode = episode;
        this.timeSinceLast = timeSinceLast;
    }

    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public String getSubtitle() {
        String sub = "";
        if (!lineOne.equals("")) {
            sub += lineOne;
        }

        if (!lineTwo.equals("")) {
            sub += "<br>" + lineTwo;
        }

        if (!lineThree.equals("")) {
            sub += "<br>" + lineThree;
        }
        return sub;
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

    public Point getScrollPos() {
        return scrollPos;
    }

    public Episode getEpisode() {
        return episode;
    }

    public int getTimeSinceLast() {
        return timeSinceLast;
    }

    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/


    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setScrollPos(Point scrollPos) {
        this.scrollPos = scrollPos;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public void setTimeSinceLast(int timeSinceLast) {
        this.timeSinceLast = timeSinceLast;
    }

    @Override
    public String toString() {
        return lineOne + " " + lineTwo + " " + lineThree;

    }
}
