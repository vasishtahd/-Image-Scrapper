package appengine.parser.facebook;

import appengine.parser.objects.AccessToken;
import appengine.parser.repository.BaseRepository;
import appengine.parser.utils.DataBaseConnector;
import appengine.parser.utils.Log;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.FacebookType;
import com.restfb.types.Post;
import okhttp3.*;
import org.jooq.DSLContext;
import org.jooq.Record1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static appengine.parser.mysqlmodels.Tables.FROMFBPAGE;
import static appengine.parser.mysqlmodels.Tables.PROMOTEFBPAGE;
import static java.lang.System.currentTimeMillis;

/**
 * Created by anand.kurapati on 27/06/17.
 */
public class Facebook {

    private static final Logger logger = Logger.getLogger(Facebook.class.getName());


    BaseRepository baseRepository;

    public Facebook(BaseRepository baseRepository) {
        this.baseRepository = baseRepository;
    }

    public ArrayList<DetailedPost> getPhotoPostsOfPage(String pageId) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<Timestamp> timestampResult = dslContext.select(FROMFBPAGE.UPDATED_TIME).from(FROMFBPAGE).
                where(FROMFBPAGE.FROM_PAGE_ID.eq(pageId)).fetchOne();
        long updatedTime = currentTimeMillis() - 1000L * 60L * 60L * 1L * 1L;
        if (timestampResult != null) {
            updatedTime = timestampResult.value1().getTime();
        }


        String access_token = baseRepository.getDefaultAccessToken();
        FacebookClient facebookClient = new DefaultFacebookClient(access_token, Version.LATEST);
        Date dateAgo = new Date(updatedTime); // 1day- 1000L * 60L * 60L * 24L * 1L
        com.restfb.Connection<Post> myFeed = facebookClient.fetchConnection(pageId + "/feed", com.restfb.types.Post.class, Parameter.with("limit", 100),
                Parameter.with("since", dateAgo));


        ArrayList<DetailedPost> fbPosts = new ArrayList<DetailedPost>();
        for (List<Post> myFeedConnectionPage : myFeed)
            for (Post post : myFeedConnectionPage) {
                DetailedPost detailedPost = new DetailedPost(post, facebookClient);
                if (detailedPost.isPhotoType()) {
                    fbPosts.add(detailedPost);
                }
            }

        Log.logIfWarning(logger, "number of posts from page" + pageId, fbPosts.size(), 0);

        if(fbPosts.size()>0) {
            dslContext.insertInto(FROMFBPAGE, FROMFBPAGE.FROM_PAGE_ID, FROMFBPAGE.UPDATED_TIME)
                    .values(pageId, new Timestamp(currentTimeMillis())).onDuplicateKeyUpdate()
                    .set(FROMFBPAGE.UPDATED_TIME, new Timestamp(currentTimeMillis())).execute();
        }
        else{
            Log.logIfWarning(logger,"the last successful fetch was "+updatedTime ,0 ,0);
        }

        return fbPosts;
    }

    public ArrayList<Post> getPermaLinksOfPage(String pageId){
        return getPermaLinksOfPage(pageId,1000L*60L*60L*1L*1L);
    }

    public ArrayList<Post> getPermaLinksOfPage(String pageId , long subtractLong) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<Timestamp> timestampResult = dslContext.select(PROMOTEFBPAGE.UPDATED_TIME).from(PROMOTEFBPAGE).
                where(PROMOTEFBPAGE.FB_PAGE_ID.eq(pageId)).fetchOne();
        long updatedTime = currentTimeMillis() - subtractLong ;
        if (timestampResult != null) {
            updatedTime = timestampResult.value1().getTime();
        }

        String access_token = baseRepository.getDefaultAccessToken();

        FacebookClient facebookClient = new DefaultFacebookClient(access_token, Version.LATEST);
        Date date = new Date(updatedTime);
        com.restfb.Connection<Post> myFeed = facebookClient.fetchConnection(pageId + "/feed", com.restfb.types.Post.class,
                Parameter.with("fields", "permalink_url"),
                Parameter.with("limit", 100),
                Parameter.with("since", date));

        ArrayList<Post> fbPosts = new ArrayList<Post>();
        for (List<Post> myFeedConnectionPage : myFeed)
            for (Post post : myFeedConnectionPage) {
                fbPosts.add(post);
            }

        Log.logIfWarning(logger, "number of posts from own page" + pageId, fbPosts.size(), 0);

        if (fbPosts.size() > 0) {
            dslContext.insertInto(PROMOTEFBPAGE, PROMOTEFBPAGE.FB_PAGE_ID, PROMOTEFBPAGE.UPDATED_TIME)
                    .values(pageId, new Timestamp(currentTimeMillis())).onDuplicateKeyUpdate()
                    .set(PROMOTEFBPAGE.UPDATED_TIME, new Timestamp(currentTimeMillis())).execute();
        }
        else{
            Log.logIfWarning(logger,"the last successful fetch was "+date ,0 ,0);
        }

        return fbPosts;
    }


    public void publish(DetailedPost post) {
        for (AccessToken accessToken : baseRepository.getAccessTokens()) {
            publish(post, accessToken);
        }
    }

    public void publish(DetailedPost post, AccessToken accessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken.access_token, Version.LATEST);
        FacebookType publishMessageResponse =
                facebookClient.publish("me/feed", FacebookType.class,
                        Parameter.with("message", post.getDescription()));
        post.getPermalinkUrl();
        System.out.println("Published message ID: " + publishMessageResponse.getId());
    }

    public void publish(String post, String previewUrl, String imageUrl) {
        for (AccessToken accessToken : baseRepository.getAccessTokens()) {
            publish(post, previewUrl, imageUrl, accessToken);
        }
    }

    public void publishImageForPages(DetailedPost post) {
        for (AccessToken accessToken : baseRepository.getAccessTokens()) {
            if (accessToken.id_type == AccessToken.ID_TYPE.PAGE) {
                publishImage(post, accessToken);
            }
        }
    }

    public void publishImage(DetailedPost post) {
        for (AccessToken accessToken : baseRepository.getAccessTokens()) {
            publishImage(post, accessToken);
        }
    }

    public void publishImage(DetailedPost post, AccessToken accessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken.access_token, Version.LATEST);
        FacebookType publishMessageResponse =
                facebookClient.publish("me/photos", FacebookType.class,
                        Parameter.with("url", post.getFirstPictureLink()),
                        Parameter.with("caption", post.getDescription()),
                        Parameter.with("no_story", false)
                );
        Log.logIfSevere(logger, "publishImage " + accessToken.id, publishMessageResponse);
    }

    public void publishVideo(DetailedPost post, AccessToken accessToken) {
        FacebookClient facebookClient = new DefaultFacebookClient(accessToken.access_token, Version.LATEST);
        FacebookType publishMessageResponse =
                facebookClient.publish("me/videos", FacebookType.class,
                        Parameter.with("url", post.getFirstPictureLink()),
                        Parameter.with("caption", post.getDescription()),
                        Parameter.with("no_story", false)
                );
        System.out.println("Published message ID: " + publishMessageResponse.getId());
    }

    public void shareFromPage(Post post) {

        for (AccessToken accessToken : baseRepository.getAccessTokens()) {
            if (accessToken.id_type == AccessToken.ID_TYPE.USER) {
                shareFromPage(post, accessToken);
            }
        }

    }

    public void shareFromPage(Post post, AccessToken accessToken) {

        FacebookClient facebookClient = new DefaultFacebookClient(accessToken.access_token, Version.LATEST);
        FacebookType publishMessageResponse =
                facebookClient.publish("me/feed", FacebookType.class,
                        Parameter.with("link", post.getPermalinkUrl())
                );
        Log.logIfSevere(logger, "shareFromPage " + accessToken.id, publishMessageResponse);
    }

    public void publish(String post, String previewUrl, String imageUrl, AccessToken accessToken) {
        try {
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).
                    build();

            MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
            RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                    "name=\"message\"\r\n\r\n" + post + "+\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                    "name=\"access_token\"\r\n\r\n" + accessToken.access_token + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: " +
                    "form-data; name=\"link\"\r\n\r\n" + previewUrl + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; " +
                    "name=\"picture\"\r\n\r\n" + imageUrl + "\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
            Request request = new Request.Builder()
                    .url("https://graph.facebook.com/v2.9/me/feed/")
                    .post(body)
                    .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "2b4f9053-dc75-f1ac-bac9-4f3d90183e4c")
                    .build();

            Response response = client.newCall(request).execute();
            Log.print("FB response " + response.body().string());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
