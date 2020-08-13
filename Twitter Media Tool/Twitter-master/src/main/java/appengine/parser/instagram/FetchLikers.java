package appengine.parser.instagram;


import appengine.parser.utils.DataBaseConnector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.json.JSONArray;
import org.json.JSONObject;

import static appengine.parser.mysqlmodels.Tables.INSTAGRAMFOLLOWERS;
import static appengine.parser.mysqlmodels.Tables.INSTAGRAMLASTFETCH;

public class FetchLikers {

    public String fetch() {

        String pagename = "pugdadbod-likers";
        String usercurrentname = "anand4joy";

        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Record1<String> lastcursorRecord = dslContext.select(INSTAGRAMLASTFETCH.LASTCURSOR).from(INSTAGRAMLASTFETCH)
                .where(INSTAGRAMLASTFETCH.PAGENAME.eq(pagename)).fetchOne();

        String lastCursor;
        if (lastcursorRecord != null) {
            lastCursor = lastcursorRecord.value1();
        } else {
            lastCursor = "QVFBWmZFWjk2ZnI1LW1Zb3pGVzg4VWJ1c1dUcTZBWi1vWXZYVWhJcEhpU01YRFFCLXpKRkFnR3JtZjhtLWotTXFvNkVYUFdIR3VydUQ4Y29RUFNMMldXVg==";
        }

        makeRequest(pagename, usercurrentname, lastCursor);

        return "";
    }

    private void processResponse(String pageName, String currentUserName, String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject edgeFollowedByJSONObject = jsonObject.getJSONObject("data").getJSONObject("shortcode_media").getJSONObject("edge_liked_by");;

            String endCursor = edgeFollowedByJSONObject.getJSONObject("page_info").getString("end_cursor");

            JSONArray jsonArray = edgeFollowedByJSONObject.getJSONArray("edges");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject userObject = jsonArray.getJSONObject(i).getJSONObject("node");
                String user_id = userObject.getString("id");
                String user_name = userObject.getString("username");
                boolean is_verified = userObject.getBoolean("is_verified");
                boolean followed_by_viewer = userObject.getBoolean("followed_by_viewer");
                boolean requested_by_viewer = userObject.getBoolean("requested_by_viewer");

                insertUser(pageName, user_id, user_name, is_verified, followed_by_viewer, requested_by_viewer, currentUserName);
            }

            updateCursor(pageName, endCursor);

            makeRequest(pageName, currentUserName, endCursor);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private byte boolToByte(boolean vIn) {
        byte vOut = (byte) (vIn ? 1 : 0);
        return vOut;
    }

    private void insertUser(String pageName, String user_id, String user_name, boolean is_verified,
                            boolean followed_by_viewer, boolean requested_by_viewer, String currentUserName) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(INSTAGRAMFOLLOWERS, INSTAGRAMFOLLOWERS.PAGENAME, INSTAGRAMFOLLOWERS.USER_ID,
                INSTAGRAMFOLLOWERS.USER_NAME, INSTAGRAMFOLLOWERS.IS_VERIFIED, INSTAGRAMFOLLOWERS.FOLLOWED_BY_VIEWER,
                INSTAGRAMFOLLOWERS.REQUESTED_BY_VIEWER, INSTAGRAMFOLLOWERS.FROM_USER_NAME)
                .values(pageName, user_id, user_name, boolToByte(is_verified), boolToByte(followed_by_viewer), boolToByte(requested_by_viewer),
                        currentUserName)
                .execute();

    }

    private void updateCursor(String pageName, String lastCursor) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(INSTAGRAMLASTFETCH, INSTAGRAMLASTFETCH.PAGENAME, INSTAGRAMLASTFETCH.LASTCURSOR)
                .values(pageName, lastCursor).onDuplicateKeyUpdate()
                .set(INSTAGRAMLASTFETCH.LASTCURSOR, lastCursor)
                .execute();
    }

    private void makeRequest(String pageName, String currentUserName, String lastCursor) {

        try {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.instagram.com/graphql/query/?query_hash=e0f59e4a1c8d78d0161873bc2ee7ec44&variables=%7B%22shortcode%22%3A%22Bs3NPQlAJEu%22%2C%22include_reel%22%3Atrue%2C%22first%22%3A12%2C%22after%22%3A%22"+lastCursor+"%22%7D")
                    .get()
                    .addHeader("cookie", "mid=XEja7AAEAAFoRCR1XuqIlWgVxuHC; mcd=3; fbm_124024574287414=base_domain=.instagram.com; csrftoken=TU8D53WwzjRj8S6168pLVOPPlcfCj6BL; shbid=4980; ds_user_id=5704301242; sessionid=5704301242%3A1eucECgBNfQ5IS%3A9; rur=FTW; shbts=1548571738.8765042; urlgen=\"{\\\"66.253.196.25\\\": 23473\\054 \\\"66.253.196.27\\\": 23473}:1gnziV:OAkwBnLyoNRzC8VY-xeO2pQNdrc\"")
                    .addHeader("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
                    .addHeader("accept", "*/*")
                    .addHeader("referer", "https://www.instagram.com/p/Bs3NPQlAJEu/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-instagram-gis", "ba0f8997ccea4ac576e574bc8c9222c3")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "eadde66d-2101-4b90-bb5d-38327c97c240")
                    .build();

                Response response = client.newCall(request).execute();
               processResponse(pageName, currentUserName, response.body().string());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
