package components;

import java.io.Serializable;


public class Tag implements Serializable {

    private String tag;

    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/
    public Tag(String tag) {
        this.tag = tag;
    }


    @Override public String toString() {
        return tag;
    }
}

