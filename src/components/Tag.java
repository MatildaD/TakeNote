package components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Tag implements Serializable {

    private String tag;
    private List<String> aliases;

    /* -------------------------------------------------------------------
     * 	Constructor
     *  ------------------------------------------------------------------*/
    public Tag(String tag) {
        this.tag = tag;
        this.aliases = new ArrayList<>();
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void addAlias(String alias) {
        aliases.add(alias);
    }

    public void removeAlias(String alias) {
        if (aliases.contains(alias)) {
            aliases.remove(alias);
        }
    }



    public boolean hasAlias(String name) {
        for (String s: aliases) {
            if (s.toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }

        return false;

    }

    @Override public String toString() {
        return tag;
    }
}

