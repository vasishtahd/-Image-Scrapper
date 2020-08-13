package appengine.parser.instagram.likestar;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class LikeStarApiUtil {

    private long lastRegDate = -1;

    OkHttpClient client;

    public LikeStarApiUtil(OkHttpClient okHttpClient) {
        this.client = okHttpClient;
    }

    public List<JSONObject> fetch(long regdate, String userName, String token) {

        List<JSONObject> itemObjectList = new ArrayList<>();

        try {


            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "mode=paging&regdate=" + regdate + "&type=0&category=-1");
            Request request = new Request.Builder()
                    .url("http://www.likestarglobal.com:8080/LikeStar/ItemList")
                    .post(body)
                    .addHeader("usertoken", userName)
                    .addHeader("authtoken", token)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("host", "www.likestarglobal.com:8080")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "373eb092-4dc6-fce1-886a-60539de4e426")
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            JSONObject jsonObject = new JSONObject(responseString);
            JSONArray jsonArray = jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemJSONObject = jsonArray.getJSONObject(i);
                lastRegDate = itemJSONObject.getLong("regdate");
                itemObjectList.add(itemJSONObject);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemObjectList;
    }

    public int earnPoint(JSONObject itemJSONObject, String userName, String token) {

        int points=0;
        try {

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "mode=gather&jentity=" + URLEncoder.encode(itemJSONObject.toString(), "UTF-8"));
            Request request = new Request.Builder()
                    .url("http://www.likestarglobal.com:8080/LikeStar/Item")
                    .post(body)
                    .addHeader("user-agent", "gzip")
                    .addHeader("usertoken", userName)
                    .addHeader("authtoken", token)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("host", "www.likestarglobal.com:8080")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "438ea8eb-2827-6ad6-2317-e5c4714c18af")
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            JSONObject jsonObject = new JSONObject(responseString);
            points = jsonObject.getJSONObject("result").getInt("point");


        } catch (Exception e) {
            e.printStackTrace();
        }

        return points;

    }
}
