package takenote;

import com.sun.org.apache.xpath.internal.SourceTree;
import components.*;
import enums.Selected;
import javafx.scene.Scene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Note implements Serializable{

    private List<Season> seasons;
    private String name;
    private Episode selectedEpisode;
    private Season selectedSeason;
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

    public Season getSelectedSeason() {
        return selectedSeason;
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
        this.selectedSeason = null;
    }

    public void setSelectedSeason(Season selectedSeason) {
        this.selectedSeason = selectedSeason;
        this.selectedEpisode = null;
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

        } else if (selectedSeason != null) {
            selectedSeason.removeNote(s);
        }
        for (Tag t: s.getTagList()) {
            if (!multipleOccurrencesOfTag(t) ) {
                tagList.remove(t);
            }
        }
    }

    public List<SceneNote> searchNotes(String searchString, List<SceneNote> foundSoFar) {
        List<SceneNote> foundNotes = new ArrayList<>();
        searchString = searchString.toLowerCase();

        System.out.println("found so far = " +foundNotes.toString());

        if (foundSoFar != null) { //If search has already begun
            System.out.println("Search has already begun (foundSoFar != null)");
            for (SceneNote note : foundSoFar) {
                System.out.println("Note= " + note.getNote());
                if (note.getNote().toLowerCase().contains(searchString)) {
                    System.out.println("Adding note");
                    foundNotes.add(note);
                }
            }
        } else {

            System.out.println("found so far = null");
            for (Season s : seasons) {
                for (SceneNote note : s.getNotes()) {
                    System.out.println("for season note " + note.getNote());
                    if (note.getNote().toLowerCase().contains(searchString)) {
                        System.out.println("adding note " + note.getNote());
                        foundNotes.add(note);
                    }
                }

                for (Episode e : s.getEpisodeList()) {
                    for (SceneNote note : e.getNotes()) {
                        System.out.println("for note " + note.getNote());
                        if (note.getNote().toLowerCase().contains(searchString)) {
                            System.out.println("adding note " + note.getNote());
                            foundNotes.add(note);
                        }

                    }
                }
            }


        }
        return foundNotes;
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

    public List<Tag> getSelectedTags() {
        List<Tag> selectedTags = new ArrayList<>();

        for (Tag t : tagList) {
            if (t.getSelectedStatus() == Selected.CONTAINS) {
                selectedTags.add(t);
            }
        }
        return selectedTags;
    }

    public List<Tag> getNotSelectedTags() {
        List<Tag> notSelectedTags = new ArrayList<>();

        for (Tag t : tagList) {
            if (t.getSelectedStatus() == Selected.NOT_CONTAINS) {
                notSelectedTags.add(t);
            }
        }
        return notSelectedTags;



    }



    public List<SceneNote> notesFilteredByTags(boolean searchOnlySelected) {
        List<SceneNote> foundNotes = new ArrayList<>();
        List<SceneNote> searchList = new ArrayList<>();

        List<Tag> selectedTags = getSelectedTags();
        List<Tag> notSelectedTags = getNotSelectedTags();

        if (selectedTags.isEmpty()) {
            selectedTags = tagList;
        }

        System.out.println(selectedTags.toString());
        System.out.println(notSelectedTags.toString());

        System.out.println("OnlySelected = " + searchOnlySelected);
        if (searchOnlySelected) {
            if (episodeOrSeasonIsSelected()) {
                searchList = (selectedEpisode != null) ? selectedEpisode.getNotes() : selectedSeason.getNotes() ;
            }
            if (selectedSeason != null) {
                for (Episode e : selectedSeason.getEpisodeList()) {
                    for (SceneNote note : e.getNotes()) {
                        searchList.add(note);
                    }
                }
            }
        }



        System.out.println("Search list size = "  + searchList.size() );

        if (!searchList.isEmpty()) {
            for (SceneNote note : searchList) {

                for (Tag t : selectedTags) {
                    if (note.hasTag(t)) {
                        boolean hasNotSelectedTag = false;
                        for (Tag notT : notSelectedTags) {
                            if (note.hasTag(notT)) {
                                hasNotSelectedTag = true;
                                break;
                            }
                        }
                        if (!hasNotSelectedTag && !foundNotes.contains(note)) {
                            foundNotes.add(note);
                        }

                    }
                }

            }
        } else {
            for (Season s : seasons) {
                for (SceneNote note : s.getNotes()) {

                    for (Tag t : selectedTags) {
                        if (note.hasTag(t)) {
                            boolean hasNotSelectedTag = false;
                            for (Tag notT : notSelectedTags) {
                                if (note.hasTag(notT)) {
                                    hasNotSelectedTag = true;
                                    break;
                                }
                            }
                            if (!hasNotSelectedTag && !foundNotes.contains(note)) {
                                foundNotes.add(note);
                            }

                        }
                    }
                }

                for (Episode e : s.getEpisodeList()) {
                    for (SceneNote note : e.getNotes()) {



                        for (Tag t : selectedTags) {
                            if (note.hasTag(t)) {
                                boolean hasNotSelectedTag = false;
                                for (Tag notT : notSelectedTags) {
                                    if (note.hasTag(notT)) {
                                        hasNotSelectedTag = true;
                                        break;
                                    }
                                }
                                if (!hasNotSelectedTag && !foundNotes.contains(note)) {
                                    foundNotes.add(note);
                                }
                            }
                        }

                    }
                }
            }
        }

        return foundNotes;
    }


    public boolean episodeOrSeasonIsSelected() {
        return (selectedSeason != null) || (selectedEpisode != null);
    }

    public boolean atLeastOneTagIsSelected() {
        for (Tag t : tagList) {
            if (t.getSelectedStatus() == Selected.CONTAINS || t.getSelectedStatus() == Selected.NOT_CONTAINS) {
                return true;
            }
        }

        return false;
    }

    public void deselectAllTags() {
        for (Tag t : tagList) {
            t.setSelectedStatus(Selected.DESELECTED);
        }
    }
}
