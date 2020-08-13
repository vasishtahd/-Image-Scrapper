
package appengine.parser.objects.jsonobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Url_ {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("expanded_url")
    @Expose
    private String expandedUrl;
    @SerializedName("display_url")
    @Expose
    private String displayUrl;
    @SerializedName("indices")
    @Expose
    private List<Long> indices = new ArrayList<Long>();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public String getPreviewUrl() {

        return expandedUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public List<Long> getIndices() {
        return indices;
    }

    public void setIndices(List<Long> indices) {
        this.indices = indices;
    }

}
