package appengine.parser.wordpress;

import appengine.parser.utils.ImageUtils;
import com.restfb.types.Post;
import net.bican.wordpress.MediaItem;
import net.bican.wordpress.MediaItemUploadResult;
import net.bican.wordpress.Wordpress;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by anand.kurapati on 27/06/17.
 */
public class WordPress {

    private static Wordpress mWordPress;

    private static Wordpress getInstance() {
        try {
            if (mWordPress == null) {
                mWordPress = new Wordpress("anandapi", "G5IBr%KN(&2IagIts46J@i(C",
                        "http://happyd.press/xmlrpc.php");
            }

            return mWordPress;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String publish(Post fbPost) {

        try {
            Wordpress wp = getInstance();

            String filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
            System.out.println("image url: " + fbPost.getPicture() + "  file name:" + filename);

            final net.bican.wordpress.Post recentPost = new net.bican.wordpress.Post();
            recentPost.setPost_title(fbPost.getCaption());
            recentPost.setPost_content(fbPost.getDescription() + "\n");
            recentPost.setPost_status("publish");

            InputStream is = new URL(fbPost.getPicture()).openStream();
            BufferedImage image = ImageIO.read(is);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(ImageUtils.createResizedMediumCopy(image), ImageUtils.getImageFormatFromUrl(fbPost.getPicture()), baos);
            InputStream is_small = new ByteArrayInputStream(baos.toByteArray());

            MediaItemUploadResult mediaItemUploadResult = wp.uploadFile(is_small, filename, true);
            MediaItem mediaItem = new MediaItem();
            mediaItem.setAttachment_id(mediaItemUploadResult.getId());
            mediaItem.setLink(mediaItemUploadResult.getUrl());

            recentPost.setPost_thumbnail(mediaItem);

            final Integer result = wp.newPost(recentPost);

            net.bican.wordpress.Post post = wp.getPost(result);
            System.out.println("Post Link " + post.getLink());
            return post.getLink();

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }

    public static String publish(String title, String url, String image_url) {

        try {
            Wordpress wp = getInstance();

            String filename = String.valueOf(System.currentTimeMillis()) + ".jpg";
            System.out.println("image url: " + image_url + "  file name:" + filename);

            final net.bican.wordpress.Post recentPost = new net.bican.wordpress.Post();
            recentPost.setPost_title(title);
            recentPost.setPost_content(url + "\n");
            recentPost.setPost_status("publish");


            InputStream is = new URL(image_url).openStream();
            BufferedImage image = ImageIO.read(is);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(ImageUtils.createResizedMediumCopy(image), ImageUtils.getImageFormatFromUrl(image_url), baos);
            InputStream is_small = new ByteArrayInputStream(baos.toByteArray());

            MediaItemUploadResult mediaItemUploadResult = wp.uploadFile(is_small, filename, true);
            MediaItem mediaItem = new MediaItem();
            mediaItem.setAttachment_id(mediaItemUploadResult.getId());
            mediaItem.setLink(image_url);

            recentPost.setPost_thumbnail(mediaItem);

            final Integer result = wp.newPost(recentPost);

            net.bican.wordpress.Post post = wp.getPost(result);
            System.out.println("Post Link " + post.getLink());



            return post.getLink();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
