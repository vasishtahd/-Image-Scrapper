
package appengine.parser.objects.jsonobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Url {

    @SerializedName("urls")
    @Expose
    private List<Url_> urls = new ArrayList<Url_>();

    public List<Url_> getUrls() {
        return urls;
    }

    public void setUrls(List<Url_> urls) {
        this.urls = urls;
    }

}
