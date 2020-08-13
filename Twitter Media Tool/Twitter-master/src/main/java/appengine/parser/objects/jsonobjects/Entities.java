
package appengine.parser.objects.jsonobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Entities {

    @SerializedName("hashtags")
    @Expose
    private List<Object> hashtags = new ArrayList<Object>();
    @SerializedName("symbols")
    @Expose
    private List<Object> symbols = new ArrayList<Object>();
    @SerializedName("user_mentions")
    @Expose
    private List<UserMention> userMentions = new ArrayList<UserMention>();
    @SerializedName("urls")
    @Expose
    private List<Url_> urls = new ArrayList<Url_>();
    @SerializedName("media")
    @Expose
    private List<Medium> media = new ArrayList<Medium>();

    public List<Object> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Object> hashtags) {
        this.hashtags = hashtags;
    }

    public List<Object> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Object> symbols) {
        this.symbols = symbols;
    }

    public List<UserMention> getUserMentions() {
        return userMentions;
    }

    public void setUserMentions(List<UserMention> userMentions) {
        this.userMentions = userMentions;
    }

    public List<Url_> getUrls() {
        return urls;
    }

    public void setUrls(List<Url_> urls) {
        this.urls = urls;
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }


}
