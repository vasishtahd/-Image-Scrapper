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

public class FetchMyFollowers {

    public String fetch() {

        String pagename = "h4ppyboys";
        String usercurrentname = "anand4joy";

        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Record1<String> lastcursorRecord = dslContext.select(INSTAGRAMLASTFETCH.LASTCURSOR).from(INSTAGRAMLASTFETCH)
                .where(INSTAGRAMLASTFETCH.PAGENAME.eq(pagename)).fetchOne();

        String lastCursor;
        if (lastcursorRecord != null) {
            lastCursor = lastcursorRecord.value1();
        } else {
            lastCursor = "QVFBOE1CdzFzdUtaMGZxVGY5YmEzR3pNaFpLaUo2Nk1DRVJHdnBtVVRxRHRFMzgzSHVYVFZMYk40WG5BM1d2U2hXRC0tTUxxa3BrNU5GbXJnenJZTEV5dg==";
        }

        makeRequest(pagename, usercurrentname, lastCursor);

        return "";
    }

    private void processResponse(String pageName, String currentUserName, String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject edgeFollowedByJSONObject = jsonObject.getJSONObject("data").getJSONObject("user").
                    getJSONObject("edge_followed_by");

            String endCursor = edgeFollowedByJSONObject.getJSONObject("page_info").getString("end_cursor");

            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONObject("user").
                    getJSONObject("edge_followed_by").getJSONArray("edges");

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
                    .url("https://www.instagram.com/graphql/query/?query_hash=56066f031e6239f35a904ac20c9f37d9&variables=%7B%22id%22%3A%225704301242%22%2C%22include_reel%22%3Atrue%2C%22fetch_mutual%22%3Afalse%2C%22first%22%3A12%2C%22after%22%3A%22"+lastCursor+"%22%7D")
                    .get()
                    .addHeader("cookie", "mid=XEja7AAEAAFoRCR1XuqIlWgVxuHC; mcd=3; fbm_124024574287414=base_domain=.instagram.com; ds_user_id=5704301242; sessionid=5704301242%3A1eucECgBNfQ5IS%3A9; shbid=4980; rur=FTW; csrftoken=FzI3Sw2Cm7t1oE5Eo3m77nKdOS66o4AA; shbts=1553140891.570628; urlgen=\"{\\\"66.253.196.25\\\": 23473}:1h6r7a:y3zK1wNoVlra8uXdrUAQESpb72o\"")
                    .addHeader("x-ig-app-id", "1217981644879628")
                    .addHeader("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Mobile Safari/537.36")
                    .addHeader("accept", "*/*")
                    .addHeader("referer", "https://www.instagram.com/h4ppyboys/followers/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-instagram-gis", "ff368ff3503541e898ae1b9770324f10")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "4237f2c4-3191-4990-bb7e-c73a7ff19de7")
                    .build();



            Response response = client.newCall(request).execute();
            processResponse(pageName, currentUserName, response.body().string());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
