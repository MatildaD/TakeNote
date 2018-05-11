package components;

import javafx.scene.Scene;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Episode implements Serializable{

    private String episodeName;
    private List<SubtitleBit> subtitles;
    private List<SceneNote> notes;
    private Season season;
    private Point subtitleScrollPos;
    private Point sceneNoteScrollPos;




    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public Episode(String episodeName, Season season) {
        this.episodeName = episodeName;
        this.subtitles = new ArrayList<>();
        this.notes = new ArrayList<>();
        this.season = season;
        this.subtitleScrollPos = new Point(0, 0);
        this.sceneNoteScrollPos = new Point(0, 0);

    }

    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public String getName() {
        return episodeName;
    }

    public List<SubtitleBit> getSubtitles() {
        return subtitles;
    }

    public List<SceneNote> getNotes() {
        return notes;
    }

    public Season getSeason() {
        return season;
    }

    public Point getSubtitleScrollPos() {
        return subtitleScrollPos;
    }

    public Point getSceneNoteScrollPos() {
        return sceneNoteScrollPos;
    }

    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setEpisodeName(String episodeName) {
        this.episodeName = episodeName;
    }

    public void setSubtitles(List<SubtitleBit> subtitles) {
        this.subtitles = subtitles;
    }

    public void setNotes(List<SceneNote> notes) {
        this.notes = notes;
    }


    public void setSeason(Season season) {
        this.season = season;
    }

    public void setSubtitleScrollPos(Point subtitleScrollPos) {
        this.subtitleScrollPos = subtitleScrollPos;
    }

    public void setSceneNoteScrollPos(Point sceneNoteScrollPos) {
        this.sceneNoteScrollPos = sceneNoteScrollPos;
    }

    /* -------------------------------------------------------------------
     * 	Manipulate notes
     *  ------------------------------------------------------------------*/

    public void addNote(SceneNote note) {
        notes.add(note);
    }

    public void removeNode(SceneNote note) {
        notes.remove(note);
    }

    public void clearNotes() {
        notes.clear();
    }

    /* -------------------------------------------------------------------
     * 	Reading in subtitles
     *  ------------------------------------------------------------------*/

    private void addSubtitleBit(SubtitleBit sub) {
        subtitles.add(sub);
    }



    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }


    /**
     * Open and read a file, and return the lines in the file as a list
     * of Strings.
     * (Demonstrates Java FileReader, BufferedReader, and Java5.)
     */
    public void importSubtitles(String filename)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (isInteger(line)) {
                    int number = Integer.parseInt(line);
                    String time = reader.readLine();
                    String[] timeSplit = time.split(" --> ");
                    String startTime = timeSplit[0];
                    String endTime = timeSplit[1];
                    String lineOne = reader.readLine();
                    String lineTwo = reader.readLine();
                    String lineThree = "";
                    if (!lineTwo.equals("")) {
                        lineThree = reader.readLine();
                    }

                    System.out.println("LineOne= " + lineOne + "\n"
                            + "LineTwo= " + lineTwo + "\n"
                            + "LineThree= " + lineThree + "\n"
                            + "StartTime= " + startTime + "\n"
                            + "EndTime= " + endTime + "\n"
                            + "Number= " + number + "\n"
                    );

                    SubtitleBit sub = new SubtitleBit(startTime, endTime, lineOne, lineTwo, lineThree, number);
                    addSubtitleBit(sub);

                }

            }
            reader.close();

        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
        }
    }



    /* -------------------------------------------------------------------
     * 	toString
     *  ------------------------------------------------------------------*/

    @Override
    public String toString() {
        return episodeName;
    }
}
