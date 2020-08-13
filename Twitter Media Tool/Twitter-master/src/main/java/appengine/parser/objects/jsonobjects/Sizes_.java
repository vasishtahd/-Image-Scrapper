
package appengine.parser.objects.jsonobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sizes_ {

    @SerializedName("thumb")
    @Expose
    private Thumb_ thumb;
    @SerializedName("large")
    @Expose
    private Large_ large;
    @SerializedName("medium")
    @Expose
    private Medium___ medium;
    @SerializedName("small")
    @Expose
    private Small_ small;

    public Thumb_ getThumb() {
        return thumb;
    }

    public void setThumb(Thumb_ thumb) {
        this.thumb = thumb;
    }

    public Large_ getLarge() {
        return large;
    }

    public void setLarge(Large_ large) {
        this.large = large;
    }

    public Medium___ getMedium() {
        return medium;
    }

    public void setMedium(Medium___ medium) {
        this.medium = medium;
    }

    public Small_ getSmall() {
        return small;
    }

    public void setSmall(Small_ small) {
        this.small = small;
    }

}
