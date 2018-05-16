package takenote;

import components.*;
import javafx.scene.Scene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Note implements Serializable{

    private List<Season> seasons;
    private String name;
    private Episode selectedEpisode;
    private List<Tag> tagList;
    private String lastSavedPath;


    final String FILEENDING = ".note";




    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public Note(String name) {
        this.name = name;
        this.seasons = new ArrayList<>();
        this.selectedEpisode = null;
        this.tagList = new ArrayList<>();
        this.lastSavedPath = "";
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

    public List<Tag> getTagList() {
        return tagList;
    }

    public String getFILEENDING() {
        return FILEENDING;
    }

    public String getLastSavedPath() {
        return lastSavedPath;
    }

    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setSeasons(List<Season> seasons) {
        this.seasons = seasons;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelectedEpisode(Episode selectedEpisode) {
        this.selectedEpisode = selectedEpisode;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public void setLastSavedPath(String lastSavedPath) {
        this.lastSavedPath = lastSavedPath;
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
    /* -------------------------------------------------------------------
     * 	Notes
     *  ------------------------------------------------------------------*/

    public void deleteSceneNote(SceneNote s) {
        if (selectedEpisode != null) {
            selectedEpisode.removeNote(s);
            selectedEpisode.setActiveSceneNote(null);
            for (Tag t: s.getTagList()) {
                if (!multipleOccurrencesOfTag(t) ) {
                    tagList.remove(t);
                }
            }
        }
    }
    /* -------------------------------------------------------------------
     * 	Subtitles
     *  ------------------------------------------------------------------*/

    public void removeSubtitles() {
        if (selectedEpisode != null) {

            List<SubtitleBit> subtitleBitList = selectedEpisode.getSubtitles();
            for (SceneNote s : selectedEpisode.getNotes()) {
                if (subtitleBitList.contains(s.getSub())) {
                    s.removeSub();
                }
            }

            selectedEpisode.clearSubtitles();
        }
    }


    /* -------------------------------------------------------------------
     * 	Tags
     *  ------------------------------------------------------------------*/

    public boolean tagExists(String name) {
        for (Tag t: tagList) {
            if (t.getTag().toLowerCase().equals(name.toLowerCase()) || t.hasAlias(name)) {
                return true;
            }

        }

        return false;
    }

    public Tag getTag(String name) {
        for (Tag t: tagList) {
            if (t.getTag().toLowerCase().equals(name.toLowerCase()) || t.hasAlias(name)) {
                return t;
            }

        }
        return null;
    }

    public void addTag(Tag tag) {
        tagList.add(tag);
    }


    public List<String> getAllTagNames() {
        List<String> allTagNames = new ArrayList<>();
        for (Tag t: tagList) {
            allTagNames.add(t.getTag());
            for (String a: t.getAliases()) {
                allTagNames.add(a + " (" + t.getTag() + ")");
            }
        }

        return allTagNames;
    }


    public void removeTag(Tag tag) {
        if (tagList.contains(tag)) {
            tagList.remove(tag);
            for (Season season: seasons) {
                for (Episode episode: season.getEpisodeList()) {
                    for (SceneNote s: episode.getNotes()) {
                        if (s.hasTag(tag)) {
                            s.removeTag(tag);
                        }
                    }
                }
            }
        }
    }

    public boolean multipleOccurrencesOfTag(Tag tag) {
        if (tagList.contains(tag)) {
            for (Season season : seasons) {
                for (Episode episode : season.getEpisodeList()) {
                    for (SceneNote s : episode.getNotes()) {
                        if (s.hasTag(tag)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }



    public List<Tag> searchTags(String searchString, List<Tag> foundSoFar) {
        searchString = searchString.toLowerCase();
        List<Tag> foundTags = new ArrayList<>();
        List<Tag> searchFrom = foundSoFar;
        if (foundSoFar == null) {
            searchFrom = tagList;
        }

        for (Tag t: searchFrom) {
            if (t.getTag().toLowerCase().contains(searchString)) {
                foundTags.add(t);

            } else {
                for (String alias: t.getAliases()) {
                    if (alias.toLowerCase().contains(searchString)) {
                        foundTags.add(t);
                        break;
                    }
                }
            }
        }
        return foundTags;

    }


}
