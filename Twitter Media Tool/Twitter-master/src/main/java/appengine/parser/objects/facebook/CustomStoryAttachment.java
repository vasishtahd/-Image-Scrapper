package appengine.parser.objects.facebook;

import com.restfb.Facebook;
import com.restfb.types.StoryAttachment;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by anand.kurapati on 10/12/17.
 */
public class CustomStoryAttachment extends StoryAttachment {

    public static final String TYPE_PHOTO = "photo";
    public static final String TYPE_SHARE = "share";
    public static final String TYPE_VIDEO = "video_autoplay";

    @Getter
    @Setter
    @Facebook
    private String type;
}
