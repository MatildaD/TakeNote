package components;

import javafx.scene.Scene;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Title implements Serializable{

    private String titleName;
    private List<SubtitleBit> subtitles;
    private List<SceneNote> notes;
    private Title parent;
    private List<Title> children;




    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public Title(String titleName, List<SubtitleBit> subtitles, List<SceneNote> notes, Title parent, List<Title> children) {
        this.titleName = titleName;
        this.subtitles = subtitles;
        this.notes = notes;
        this.parent = parent;
        for (Title child:children) {
            if (! child.equals(parent)) {
                this.children.add(child);
            }
        }
    }

    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public String getTitleName() {
        return titleName;
    }

    public List<SubtitleBit> getSubtitles() {
        return subtitles;
    }

    public List<SceneNote> getNotes() {
        return notes;
    }

    public Title getParent() {
        return parent;
    }

    public List<Title> getChildren() {
        return children;
    }

    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public void setSubtitles(List<SubtitleBit> subtitles) {
        this.subtitles = subtitles;
    }

    public void setNotes(List<SceneNote> notes) {
        this.notes = notes;
    }

    private Boolean isChild(Title title) {
        for (Title child:this.children) {
            if (title.equals(child)) {
                return true;
            }
        }
        return false;
    }

    public void setParent(Title parent) {
        if (! isChild(parent)) {
            this.parent = parent;
        }
    }

    public void setChildren(List<Title> children) {
        this.children.clear();
        for (Title child:children) {
            if (! child.equals(this.parent)) {
                this.children.add(child);
            }
        }
    }

    /* -------------------------------------------------------------------
     * 	Manipulate children
     *  ------------------------------------------------------------------*/

    public void addChild(Title child) {
        if (! this.parent.equals(child)) {
            this.children.add(child);
        }

    }

    /* -------------------------------------------------------------------
     * 	Manipulate notes
     *  ------------------------------------------------------------------*/

    public void addNote(SceneNote note) {
        this.notes.add(note);
    }

    public void removeNode(SceneNote note) {
        this.notes.remove(note);
    }

    public void clearNotes() {
        this.notes.clear();
    }

    /* -------------------------------------------------------------------
     * 	Reading in subtitles
     *  ------------------------------------------------------------------*/

    private void addSubtitleBit(SubtitleBit sub) {
        this.subtitles.add(sub);
    }



    public static boolean isInteger(String s) {
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
    private void importSubtitles(String filename)
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
                    if (lineTwo != "") {
                        lineThree = reader.readLine();
                    }

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



}
