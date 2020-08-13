package appengine.parser.instagram;

import appengine.parser.instagram.objects.UserNameAndID;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class UnFollowCurrentUsers {


    public String fetchUsersIamFollowing(){

        try {


            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.instagram.com/graphql/query/?query_hash=c56ee0ae1f89cdbd1c89e2bc6b8f3d18&variables=%7B%22id%22%3A%225704301242%22%2C%22include_reel%22%3Atrue%2C%22fetch_mutual%22%3Afalse%2C%22first%22%3A1000%7D")
                    .get()
                    .addHeader("cookie", "mid=XEja7AAEAAFoRCR1XuqIlWgVxuHC; mcd=3; fbm_124024574287414=base_domain=.instagram.com; csrftoken=TU8D53WwzjRj8S6168pLVOPPlcfCj6BL; shbid=4980; shbts=1548278515.0078; ds_user_id=5704301242; sessionid=5704301242%3A1eucECgBNfQ5IS%3A9; rur=FTW; urlgen=\"{\\\"129.219.21.183\\\": 2900\\054 \\\"129.219.21.1\\\": 2900\\054 \\\"129.219.21.179\\\": 2900}:1gmS0t:00JYXGzwDNwY5JXOwKEYm-W_jqA\"")
                    .addHeader("x-ig-app-id", "1217981644879628")
                    //.addHeader("accept-encoding", "gzip, deflate, br")
                    .addHeader("accept-language", "en-IN,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Mobile Safari/537.36")
                    .addHeader("accept", "*/*")
                    .addHeader("referer", "https://www.instagram.com/h4ppyboys/following/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-instagram-gis", "92ab5f9c3cd65a60afdf60bcc232d667")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("Postman-Token", "5d8fcd96-4354-4e53-9e61-3f51511f3817")
                    .build();

                Response response = client.newCall(request).execute();
                String jsonresponse = response.body().string();
                JSONObject jsonObject = new JSONObject(jsonresponse);
                JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONObject("user").
                        getJSONObject("edge_follow").getJSONArray("edges");
                for(int i=0;i<10;i++){
                     JSONObject internalJsonObject =  jsonArray.getJSONObject(i);
                     JSONObject nodeObject = internalJsonObject.getJSONObject("node");
                     String userId = nodeObject.getString("id");
                     String userName = nodeObject.getString("username");
                     UserNameAndID userNameAndID = new UserNameAndID(0,userId,userName);
                     UnFollowUsers unFollowUsers = new UnFollowUsers();
                     unFollowUsers.fireRequest(userNameAndID);
                     Thread.sleep(12000);
                }

        } catch (Exception e){
            e.printStackTrace();
        }

        return "";
    }
}
