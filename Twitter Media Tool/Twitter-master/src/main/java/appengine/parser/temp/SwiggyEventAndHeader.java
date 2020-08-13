package appengine.parser.temp;

import com.google.gson.Gson;


public class SwiggyEventAndHeader {

   public SwiggyEvent event;
   public SwiggyHeader header;

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
