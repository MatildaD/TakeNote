package components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Season implements Serializable {

    private List<Episode> episodeList;
    private List<SceneNote> notes;
    private String name;


    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public Season(String name) {
        this.episodeList = new ArrayList<>();
        this.notes = new ArrayList<>();
        this.name = name;
    }

    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public List<SceneNote> getNotes() {
        return notes;
    }

    public String getName() {
        return name;
    }

    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    public void setNotes(List<SceneNote> notes) {
        this.notes = notes;
    }

    public void setName(String name) {
        this.name = name;
    }


    /* -------------------------------------------------------------------
     * 	Manipulate Episode List
     *  ------------------------------------------------------------------*/

    public void removeEpisode(Episode episode) {
        if( this.episodeList.contains(episode)) {
            this.episodeList.remove(episode);
        }
    }


    public void addEpisode(Episode episode) {
        this.episodeList.add(episode);
        Season prevSeason = episode.getSeason();
        prevSeason.removeEpisode(episode);
        episode.setSeason(this);
    }

    public void clearEpisodes() {
        this.episodeList.clear();
    }




}
