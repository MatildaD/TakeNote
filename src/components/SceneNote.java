package components;

import java.io.Serializable;
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

    public SceneNote(String note, String startTime, String endTime, List<Tag> tagList, SubtitleBit sub) {
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tagList = tagList;
        this.sub = sub;
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
        this.tagList.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tagList.remove(tag);
    }

    public void clearTags() {
        this.tagList.clear();
    }



}
