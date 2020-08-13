
package appengine.parser.objects.jsonobjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Medium___ {

    @SerializedName("w")
    @Expose
    private Long w;
    @SerializedName("h")
    @Expose
    private Long h;
    @SerializedName("resize")
    @Expose
    private String resize;

    public Long getW() {
        return w;
    }

    public void setW(Long w) {
        this.w = w;
    }

    public Long getH() {
        return h;
    }

    public void setH(Long h) {
        this.h = h;
    }

    public String getResize() {
        return resize;
    }

    public void setResize(String resize) {
        this.resize = resize;
    }

}
