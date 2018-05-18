package components;

import javafx.scene.Scene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Season implements Serializable {

    private List<Episode> episodeList;
    private String name;
    private boolean expands;
    private List<SceneNote> notes;


    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public Season(String name) {
        this.episodeList = new ArrayList<>();
        this.name = name;
        this.expands = true;
        this.notes = new ArrayList<>();
    }

    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public String getName() {
        return name;
    }

    public boolean expands() {
        return expands;
    }

    public List<SceneNote> getNotes() {
        return notes;
    }

    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(List<SceneNote> notes) {
        this.notes = notes;
    }

    /* -------------------------------------------------------------------
     * 	Manipulate Episode List
     *  ------------------------------------------------------------------*/

    public void removeEpisode(Episode episode) {
        if( episodeList.contains(episode)) {
            episodeList.remove(episode);
        }
    }


    public void addEpisode(Episode episode) {
        episodeList.add(episode);
    }

    public void clearEpisodes() {
        episodeList.clear();
    }



    /* -------------------------------------------------------------------
     * 	Manipulate Notes List
     *  ------------------------------------------------------------------*/


    public void removeNote(SceneNote note) {
        if (notes.contains(note)) {
            notes.remove(note);
        }
    }

    public void addNote(SceneNote note) {
        notes.add(note);
    }



    /* -------------------------------------------------------------------
     * 	Misc
     *  ------------------------------------------------------------------*/


    public void flipExpands() {
        expands = !expands;
    }

    @Override
    public String toString() {
        return name;
    }
}
