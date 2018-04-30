package components;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.Serializable;

public class Subtitle implements Serializable{

    private List<SubtitleBit> subtitleBits;



    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/

    public Subtitle(List<SubtitleBit> subtitleBits) {
        this.subtitleBits = subtitleBits;
    }


    /* -------------------------------------------------------------------
     * 	Getters
     *  ------------------------------------------------------------------*/

    public List<SubtitleBit> getSubtitleBits() {
        return subtitleBits;
    }
    /* -------------------------------------------------------------------
     * 	Setters
     *  ------------------------------------------------------------------*/

    public void setSubtitleBits(List<SubtitleBit> subtitleBits) {
        this.subtitleBits = subtitleBits;
    }
}
