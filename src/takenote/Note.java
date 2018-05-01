package takenote;

import components.Episode;
import components.Season;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Note implements Serializable{

    private List seasons;
    private String name;


    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public Note(String name) {
        this.name = name;
        this.seasons = new ArrayList<>();
    }

    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public List getSeasons() {
        return seasons;
    }

    public String getName() {
        return name;
    }

    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setSeasons(List seasons) {
        this.seasons = seasons;
    }

    public void setName(String name) {
        this.name = name;
    }
    /* -------------------------------------------------------------------
     * 	Seasons
     *  ------------------------------------------------------------------*/

    public void addSeason(String name) {
        Season season = new Season(name);
        this.seasons.add(season);
    }

    public void removeSeason(Season season) {
        season.clearEpisodes();
        this.seasons.remove(season);
    }

    public void clearSeasons() {
        this.seasons.clear();
    }

    /* -------------------------------------------------------------------
     * 	Episodes
     *  ------------------------------------------------------------------*/


    public void addEpisode(String name, Season season) {
        Episode ep = new Episode(name, season);
        season.addEpisode(ep);
    }

    public  void removeEpisode(Episode episode) {
        Season s = episode.getSeason();
        s.removeEpisode(episode);

    }




}
