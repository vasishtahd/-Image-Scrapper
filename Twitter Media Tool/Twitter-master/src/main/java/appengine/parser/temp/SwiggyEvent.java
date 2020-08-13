package appengine.parser.temp;

import com.google.gson.Gson;

public class SwiggyEvent {

    public String e;
    public String sn;
    public String on;
    public String sc;
    public String eno;
    public String real;
    public String ud;
    public String op;
    public String cx;
    public String ts;
    public String p;
    public String us;
    public String ov;
    public String ui;
    public String rf;
    public String av;
    public String itd;
    public String is_test_event;
    public String sqn;
    public String id;
    public String lt;
    public String lg;
    public String device_id;

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
