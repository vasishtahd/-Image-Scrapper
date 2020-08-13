package appengine.parser.facebook;

import appengine.parser.objects.facebook.CustomStoryAttachment;
import appengine.parser.utils.StringUtils;
import com.restfb.FacebookClient;
import com.restfb.types.Post;

import java.util.List;

import static org.apache.commons.beanutils.BeanUtils.copyProperties;

/**
 * Created by anand.kurapati on 28/06/17.
 */
public class DetailedPost extends Post {

    private final String PROMOTIONAL_DESCRIPTION = "Like the page for more fun :p :p";

    CustomStoryAttachment customStoryAttachment = null;


    public DetailedPost(Post post, FacebookClient facebookClient) {

        try {
            copyProperties(this, post);
        } catch (Exception e) {

        }
        com.restfb.Connection<CustomStoryAttachment> postAttachments = facebookClient.fetchConnection(post.getId() + "/attachments", CustomStoryAttachment.class);
        Post.Attachments attachments = new Post.Attachments();
        for (List<CustomStoryAttachment> myStoryConnectionPage : postAttachments) {
            for (CustomStoryAttachment storyAttachment : myStoryConnectionPage) {
                attachments.addData(storyAttachment);
                if (customStoryAttachment == null) {
                    customStoryAttachment = storyAttachment;
                }
            }
        }
        this.setAttachments(attachments);

    }

    public boolean hasAttachment() {

        if (customStoryAttachment != null)
            return true;

        return false;
    }

    private boolean hasDescription() {

        if (hasAttachment()) {

            if (StringUtils.isNonEmpty(customStoryAttachment.getDescription())) {

                return true;
            }
        }

        return false;
    }

    public String getFirstPictureLink() {

        if (hasAttachment()) {

            return customStoryAttachment.getMedia().getImage().getSrc();
        }
        return "";
    }

    public String getDescription() {
        if (hasAttachment()) {
            if (hasDescription()) {
                if (customStoryAttachment.getTitle() != null) {
                    return customStoryAttachment.getTitle();
                }
            }
        }
        return PROMOTIONAL_DESCRIPTION;
    }

    public boolean isPhotoType() {
        if (hasAttachment()) {
            if (customStoryAttachment.getType().equals(CustomStoryAttachment.TYPE_PHOTO)) {
                return true;
            }
        }
        return false;
    }


}
