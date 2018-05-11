package components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Season implements Serializable {

    private List<Episode> episodeList;
    private String name;
    private boolean expands;


    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public Season(String name) {
        this.episodeList = new ArrayList<>();
        this.name = name;
        this.expands = true;
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



    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    public void setName(String name) {
        this.name = name;
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


    public void flipExpands() {
        expands = !expands;
    }

    @Override
    public String toString() {
        return name;
    }
}
