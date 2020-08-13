package appengine.parser.instagram.likestar;

import okhttp3.*;
import org.json.JSONObject;

import java.util.List;

public class LikeStarEarnPoints {

    OkHttpClient client;

    private long lastRegDate = -1;

    private LikeStarApiUtil likeStarApiUtil;

    private String userName;
    private String token;

    //7865920163 for 3 and 60 for indians
    String[] userNames = {"beautifully_indian", "beautifully_indians", "beautifully_indian1", "beautifully_indian3", "beautifully_indian4",
    };
    String[] tokens = {
            "Tecjb0XR5ZzZ5hQkkB6n64HxfyjCrLhvdO8lpSjdlKEPHRXSkGmm68O4FRaBUbog6dvt8aDuILA8w11JAwiL/OgfN++o48I/GEROWvJqpSzj0fylkMi06gqbhgiZDgpj9HzAGQ2hYAA0WYlA4BpTif51kUeqWf0cGcl0at0QJdhDq7VtdQYBkIwkF8J/pIZA5snGc2gVOD8/LacoM35KK6pVUV5luogdfrSeRhBWGxyr5iNNrS404s5/Jz1M/K7yMgzsmMM/FCAJ9J8jL5aAaf7zl0eQ0cHddxgyMt0DVsw4M4OSsn4UHzirqXrhjatixgwII2fVl+eZJDIfYeH14A==",
            "ETkDoNoFJjtV9AndgvBztjM9TgMuN9X5ov3DZC25qWFS9BoqJgEJChDd6/kUhIU5vK1eCTotmG3oozRq6p5WkwOyKU1HQm/0sMnvwoiA0GfLt+yJvbWLPlhgysCc2TDeU1yKLNPEzoekTgs5Ie9ltODB/dJrsROiq/DJBj1Hq2aX/KG0ThUc7bEaiEAoeS1rV97wOKkstTK7AHLFJwXDMx7AN55K7dIqvYT+iwpAywVEffQNby8JGtzVmBx5C8LJctHY5EC3vOwPEisP/oBSQoK5GpJKLvc2CBhHE8/d//vwPU4TQsgJY1wdERRyarG6SpvXrXF/DUZGsceZg5OTSw==",
            "Y9fl+L4eigTNqZoENuUGKJhQAuMZgzWDQNNASEg+BcgT8zOZGeFwFrSWZ7o/cOajaymjAqwMK6PUTEfUCuSkvvlftKMgTMJImTWI2hMEoebjvf9NXKQH7uyHiRLhH0+XYMSuOgTe/n7drvBOmwiLATxX5iWWvyMtdh0BHUH1uhjOUjTOzYIWiJ6rAOzGDXX0p/ooYJ6sk2x0jEAuHDhYVplEHsXZQVnqarWJmzvHN7Ui8Rb6dEm1pLbX4ER7dqHguw4C0Sxn7ZLE3KBD6MzISem39C+/K+0p2VTm/EMaXv5+AD2Zw7/YxGFbD9VLlY1rHbBdPy7K4sANdvlkx8fvJw==",
            "DzyHnUW+x8xhTxhEky5dvS7j+4T0JJOY2GowgA+6ArqQGLBdQuvgWecjGCQfRHDJCAFtIVcMpn8CNL0ylRJOEM3m4euWfq7IJBwtsf0mYElACHReK++rPzJ4+KXj8dNiKximfiptSrUacgQG2k0U6Hnl5mCip3ueQqdRP3ahDvRiZTZfNCTQpP43+UEuvfYPNUZDLlYdsfKaIn4pFq7lmv1uE2afw+s2SVATHN8B5RfRzkIocchKK+p7a00mCtZxFWd8/Uo7SCXijRRGE8TcK5RSbFVuUpP7q/bnXTVxBJMZOFNvLdEwNSQN8QD12Lg07d9Oq01+F0I7zLTD78pKYw==",
            "fxiRg0hLY+3n55VX5bIb1V5PtnZeFIodAdiR9LnAs10X/tdKSihM0xvs2Cb1RBQltK5DMw+UFq595I3yxNl3JRGRMISKoaEnJBMqpTVpbf8NqwWxl8t/c8QSQ3/603yCEp7tuST+crtBwJvOFRq19GvS9v3P4v5vgwiZ2QSd2d8CrOWEGP7CmSvIa9Ji36iIpSlqY+tmBvy9heb9OVmm3dPV7v/F0BhTHRuTahXJHRtm7K0wBiXMY0lzzhGd8tCq913J0hFFI4X8o/hcdQvRNujcGByDrt/Y1/KXFZimdmXMIS4v8opW6b+BrS73ktLSDEBGaTUZ2s47KmB7Ucv1Cg==",

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
            for (int i = 0; i < 1; i++) {
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

    }

    public void updateOrderHappyBoys() {
        try {

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8");
            RequestBody body = RequestBody.create(mediaType, "mode=update&jentity=%7B%22category%22%3A3%2C%22code%22%3A%22h4ppyboys%22%2C%22count%22%3A0%2C%22hightimageurl%22%3A%22https%3A%2F%2Fscontent-bom1-1.cdninstagram.com%2Fvp%2F549672c0aad8e69a3b8cb36d6cbaa1cc%2F5BEB0CEB%2Ft51.2885-19%2Fs320x320%2F32224814_198152794139533_7258373710946500608_n.jpg%22%2C%22id%22%3A7584%2C%22imageurl%22%3A%22https%3A%2F%2Fscontent-bom1-1.cdninstagram.com%2Fvp%2F549672c0aad8e69a3b8cb36d6cbaa1cc%2F5BEB0CEB%2Ft51.2885-19%2Fs320x320%2F32224814_198152794139533_7258373710946500608_n.jpg%22%2C%22impropercount%22%3A0%2C%22isowner%22%3A1%2C%22ispremium%22%3A0%2C%22itemid%22%3A%225704301242%22%2C%22maxcount%22%3A0%2C%22regdate%22%3A1529430489370%2C%22status%22%3A1%2C%22type%22%3A2%2C%22user%22%3A%7B%22id%22%3A4641%2C%22impropermaxcount%22%3A10%2C%22isroute%22%3Atrue%2C%22phone%22%3A%227019542378%22%2C%22point%22%3A139%2C%22profile_picture%22%3A%22https%3A%2F%2Fscontent.cdninstagram.com%2Fvp%2Fabc2f9fa3c06a5d4f6aedd3036432b91%2F5BC60113%2Ft51.2885-19%2Fs150x150%2F29404031_361021757748204_4967120374924836864_n.jpg%22%2C%22usegather%22%3Atrue%2C%22usenotification%22%3Atrue%2C%22userid%22%3A%227339557412%22%2C%22username%22%3A%22beautifully_indian%22%2C%22usertype%22%3A0%7D%7D&count=10");
            Request request = new Request.Builder()
                    .url("http://www.likestarglobal.com:8080/LikeStar/Item")
                    .post(body)
                    .addHeader("usertoken", userName)
                    .addHeader("authtoken", token)
                    .addHeader("content-type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .addHeader("host", "www.likestarglobal.com:8080")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "78eaacf6-571c-b32f-b47c-fade1e2638fe")
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
