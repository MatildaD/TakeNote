package components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SceneNote implements Serializable{

    private String note;
    private String startTime;
    private String endTime;
    private List<Tag> tagList;
    private SubtitleBit sub;


    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public SceneNote(String note, SubtitleBit sub) {
        this.note = note;
        this.startTime = sub.getStartTime();
        this.endTime = sub.getEndTime();
        this.tagList = new ArrayList<>();
        this.sub = sub;

    }

    public SceneNote(String note) {
        this.note = note;
        this.startTime = "00:00:00";
        this.endTime = "00:00:00";
        this.tagList = new ArrayList<>();
        this.sub = null;

    }

    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public String getNote() {
        return note;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public SubtitleBit getSub() {
        return sub;
    }

    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setNote(String note) {
        this.note = note;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public void setSub(SubtitleBit sub) {
        this.sub = sub;
    }

    /* -------------------------------------------------------------------
     * 	Update tagList
     *  ------------------------------------------------------------------*/

    public void addTag(Tag tag) {
        tagList.add(tag);
    }

    public void removeTag(Tag tag) {
        tagList.remove(tag);
    }

    public boolean hasTag(Tag tag) { return tagList.contains(tag);}

    public void clearTags() {
        this.tagList.clear();
    }




    public void removeSub() {
        sub = null;
    }
}
