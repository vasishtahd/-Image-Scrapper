package appengine.parser.slack;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class SlackUtil {

    static String auth_token = "xoxp-3865425964-152649577281-453699740900-82872a4d4ea9361aadf1390fdab21825";


    public static void getChannelMembers() {
        String channelid = "C03RFCHUN";//general

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://slack.com/api/channels.info?channel=" + channelid + "&token=" +
                            auth_token)
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "166c8068-32c4-901b-4a3c-594fc2aabdd7")
                    .build();

            Response response = client.newCall(request).execute();

            String responseBody = response.body().string();

            JSONObject jsonObject = new JSONObject(responseBody);
            JSONObject channelObject = jsonObject.getJSONObject("channel");
            JSONArray membersArray = channelObject.getJSONArray("members");

            for (int i = 0; i < membersArray.length(); i++) {
                String memberid = membersArray.getString(i);

                String newChannel = "CCURRQ64W";//weareswiggy

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "token=" + auth_token + "&channel=" + newChannel +
                        "&user=" + memberid);
                Request inviterequest = new Request.Builder()
                        .url("https://slack.com/api/channels.invite")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .addHeader("postman-token", "500ef826-9468-cfca-5c28-00e3dec87ac9")
                        .build();

                Response inviteresponse = client.newCall(inviterequest).execute();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
