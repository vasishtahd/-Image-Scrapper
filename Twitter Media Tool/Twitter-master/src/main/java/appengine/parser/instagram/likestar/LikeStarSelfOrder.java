package appengine.parser.instagram.likestar;

import okhttp3.*;
import org.json.JSONObject;

import java.util.List;

public class LikeStarSelfOrder {

    OkHttpClient client;

    private long lastRegDate = -1;

    private LikeStarApiUtil likeStarApiUtil;

    private String userName;
    private String token;

    //7865920163 for 3 and 60 for indians
    String[] userNames = {"h4ppyboys"
    };
    String[] tokens = {
            "HHdvezvUBORCzcBMOPao74vccdOQc6M/gMt//Pri0QJFszi4HG27cpyH0xhB2NxwNSmOjqBLeWyYNlcX/TOgXu7UjiW3vy0UX2Pn2W0/AfZ2ow7jWlHym5C7ppVQDS41ZKT0Lx1qVbs6NuxP6SEcOB6SXjNTf8nOLkbgmE0okVhlgvksecSQ+HltyPdFgSzw0w65eftLvEYzkadALYQWszRNuZtx7dOmI+33arLvp2PJn/uUdVg8aI3H0AIWQh2fL7qoWyeLGBYR7QJJjWGo5N/0i0c0cu01fMvT6AKjpura6CJxCmFHEIQtQQwVMQLZg4IDvlBQjl0k/St+4SaYgw=="
    };

    public void earnPointsMultipleUsers() {
        client = new OkHttpClient();

        likeStarApiUtil = new LikeStarApiUtil(client);

        for (int i = 0; i < tokens.length; i++) {
            userName = userNames[i];
            token = tokens[i];
            earnPoints();
        }

    }

    private void earnPoints() {
        lastRegDate = -1;
        try {
            for (int i = 0; i < 10; i++) {
                List<JSONObject> itemJSONObjectList = fetch(lastRegDate);

                if (itemJSONObjectList.size() == 0) {
                    updateOrderHappyBoys();
                    return;
                }

                for (JSONObject itemJSONObject : itemJSONObjectList) {
                    earnPoint(itemJSONObject);
                    Thread.sleep(2000);
                    break;
                }

                Thread.sleep(2000);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void earnPoint(JSONObject itemJSONObject) {

        int points = likeStarApiUtil.earnPoint(itemJSONObject, userName, token);
        if (points > 100) {
            updateOrderHappyBoys();
        }

    }

    public void updateOrderHappyBoys() {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "jentity=%7B%22category%22%3A0%2C%22code%22%3A%22%22%2C%22count%22%3A0%2C%22hightimageurl%22%3A%22https%3A%2F%2Fscontent.cdninstagram.com%2Fvp%2F11b65d8bf7ef0a9cfd656ef1ed7929d4%2F5BBD061B%2Ft51.2885-19%2Fs150x150%2F32224814_198152794139533_7258373710946500608_n.jpg%22%2C%22id%22%3A11834%2C%22imageurl%22%3A%22https%3A%2F%2Fscontent.cdninstagram.com%2Fvp%2F11b65d8bf7ef0a9cfd656ef1ed7929d4%2F5BBD061B%2Ft51.2885-19%2Fs150x150%2F32224814_198152794139533_7258373710946500608_n.jpg%22%2C%22impropercount%22%3A0%2C%22isowner%22%3A0%2C%22ispremium%22%3A0%2C%22itemid%22%3A%225704301242%22%2C%22maxcount%22%3A0%2C%22regdate%22%3A1529705882956%2C%22status%22%3A1%2C%22type%22%3A2%2C%22user%22%3A%7B%22id%22%3A6829%2C%22impropermaxcount%22%3A10%2C%22isroute%22%3Atrue%2C%22point%22%3A253%2C%22profile_picture%22%3A%22https%3A%2F%2Fscontent.cdninstagram.com%2Fvp%2F11b65d8bf7ef0a9cfd656ef1ed7929d4%2F5BBD061B%2Ft51.2885-19%2Fs150x150%2F32224814_198152794139533_7258373710946500608_n.jpg%22%2C%22usegather%22%3Afalse%2C%22usenotification%22%3Atrue%2C%22userid%22%3A%225704301243%22%2C%22username%22%3A%22h4ppyboys%22%2C%22usertype%22%3A0%7D%7D&mode=update&count=10");
            Request request = new Request.Builder()
                    .url("http://www.likestarglobal.com:8080/LikeStar/Item")
                    .post(body)
                    .addHeader("user-agent", "gzip")
                    .addHeader("usertoken", userName)
                    .addHeader("authtoken", token)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("host", "www.likestarglobal.com:8080")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "7ecb598b-325e-ddd8-c8b3-cc37ed1c49bd")
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            JSONObject responseJSON = new JSONObject(responseString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<JSONObject> fetch(long regdate) {

        return likeStarApiUtil.fetch(regdate, userName, token);
    }
}
