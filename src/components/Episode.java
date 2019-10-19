package components;

import javafx.scene.Scene;

import javax.swing.*;
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
    private SceneNote activeSceneNote;




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
        this.activeSceneNote = null;

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

    public SceneNote getActiveSceneNote() {
        return activeSceneNote;
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

    public void setActiveSceneNote(SceneNote activeSceneNote) {
        this.activeSceneNote = activeSceneNote;
    }

    /* -------------------------------------------------------------------
     * 	Manipulate notes
     *  ------------------------------------------------------------------*/

    public void addNote(SceneNote note) {
        notes.add(note);
    }

    public void removeNote(SceneNote note) {
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

    public void clearSubtitles() {
        subtitles.clear();
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
    public void importSubtitles(String filename) {
        String fileEnding = filename.substring(filename.length() - 4);

        int lastEndTime = 0;

        if (fileEnding.equals(".txt") || fileEnding.equals(".srt")) {

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                String line;


                Boolean first = true;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    if (first == true) {
                        first = false;
                        line = "1";
                    }
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

                        int startT = Integer.parseInt(startTime.substring(0,2) + startTime.substring(3,5) + startTime.substring(6,8) + startTime.substring(9));
                        int endT = Integer.parseInt(endTime.substring(0,2) + endTime.substring(3,5) + endTime.substring(6,8) + endTime.substring(9));

                        int timeSinceLast = startT - lastEndTime;

                        lastEndTime = endT;

                        SubtitleBit sub = new SubtitleBit(startTime, endTime, lineOne, lineTwo, lineThree, number, this, timeSinceLast);
                        addSubtitleBit(sub);

                    }

                }
                reader.close();

            } catch (Exception e) {
                System.err.format("Exception occurred trying to read '%s'.", filename);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "TakeNote can currently only open SubRip Subtitles, with file endings .txt or .srt");
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
