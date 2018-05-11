package takenote;

import components.Episode;
import components.Season;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Note implements Serializable{

    private List<Season> seasons;
    private String name;
    private Episode selectedEpisode;


    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public Note(String name) {
        this.name = name;
        this.seasons = new ArrayList<>();
        this.selectedEpisode = null;
    }

    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public List<Season> getSeasons() {
        return seasons;
    }

    public String getName() {
        return name;
    }

    public Episode getSelectedEpisode() {
        return selectedEpisode;
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

    public void setSelectedEpisode(Episode selectedEpisode) {
        this.selectedEpisode = selectedEpisode;
    }

    /* -------------------------------------------------------------------
     * 	Seasons
     *  ------------------------------------------------------------------*/

    public void addSeason(Season s) {
        seasons.add(s);
    }

    public void removeSeason(Season season) {
        season.clearEpisodes();
        seasons.remove(season);
    }

    public void clearSeasons() {
        this.seasons.clear();
    }

    /* -------------------------------------------------------------------
     * 	Episodes
     *  ------------------------------------------------------------------*/


    public void addEpisode(Episode episode, Season season) {
        season.addEpisode(episode);
    }

    public  void removeEpisode(Episode episode) {
        Season s = episode.getSeason();
        s.removeEpisode(episode);
        if (selectedEpisode.equals(episode)) {
            selectedEpisode = null;
            for (Season se:seasons) {
                if (!se.getEpisodeList().isEmpty()) {
                    selectedEpisode = se.getEpisodeList().get(0);
                    break;
                }
            }



        }

    }







}
