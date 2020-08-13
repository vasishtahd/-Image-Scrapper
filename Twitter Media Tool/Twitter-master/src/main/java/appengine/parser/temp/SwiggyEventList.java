package appengine.parser.temp;

import com.google.gson.Gson;

import java.util.List;

public class SwiggyEventList {

    public List<SwiggyEvent> events;

    public String toJSON(){
       return new Gson().toJson(this);
    }
}
