package plugins.tracker;

/**
 * Created with IntelliJ IDEA.
 * User: peter.georgiev
 * Date: 8/25/14
 * Time: 9:51 AM
 * To change this template use File | Settings | File Templates.
 */

import org.jdom2.*;
import org.jdom2.input.*;

import java.util.List;

public class Highlights {

    private List<String> highlights;
    public Highlights(List<String> hs) {
        this.highlights = hs;
    }

    public String getMask() {
        String result = "";
        for (String i : this.highlights) {
            result += i + "|";
        }
        return ".*(" + result.substring(0, result.length()-1) + ").*";
    }
}
