package appengine.parser.instagram.follow4follow;

import okhttp3.*;
import org.jooq.util.derby.sys.Sys;
import org.json.JSONArray;
import org.json.JSONObject;

public class EarnPoints {


    public void fetchInLoop() {
        try {
            for (int i = 0; i < 100; i++) {
                if(!fetch()){
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean fetch() {


        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "pkId=5704301242&hashCode=mr6qvgMrkTEdQH4B0Y8Qm%2BIKeiScPVA925gG9A%2B%2BAk4%3D&count=10&interests=%5B%22All%22%5D");
            Request request = new Request.Builder()
                    .url("http://follow4follow.globusdemos.com/api/order/pending/fetch")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("user-agent", "Dalvik/2.1.0 (Linux; U; Android 8.0.0; moto g(6) Build/OPS27.82-19-4)")
                    .addHeader("host", "follow4follow.globusdemos.com")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "9e2f1bff-cf3c-4e8d-b299-141b30561ecb")
                    .build();

            Response response = client.newCall(request).execute();

            String jsonResponse = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject dataJSONObject = jsonObject.getJSONObject("data");
            JSONArray ordersJSONArray = dataJSONObject.getJSONArray("orders");
            for (int i = 0; i < ordersJSONArray.length(); i++) {

                String orderId = ordersJSONArray.getJSONObject(i).getString("id");

                earnPoints(orderId);
                Thread.sleep(1000);
            }

            makeOrder();


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void earnPoints(String orderId) {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "pkId=5704301242&orderId=" + orderId + "&hashCode=1K5wdDqQuhFi3SXJx3vXR3kMdLXikjL3aUttC5EOk0c%3D");
            Request request = new Request.Builder()
                    .url("http://follow4follow.globusdemos.com/api/order/follow")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("user-agent", "Dalvik/2.1.0 (Linux; U; Android 8.0.0; moto g(6) Build/OPS27.82-19-4)")
                    .addHeader("host", "follow4follow.globusdemos.com")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "e438be07-5ef1-1b91-2850-34d0354acdb2")
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            System.out.println(responseString);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void makeOrder(){
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "pkId=5704301242&followRequested=10&hashCode=5VFv5H9JhN66CbPAB4gDJ2LJR7RIqDO%2B3gjuavAFxd0%3D&profilePic=https%3A%2F%2Finstagram.fblr4-1.fna.fbcdn.net%2Fvp%2F80e4a3ea34a215b9683116f933796a0e%2F5BA243E1%2Ft51.2885-19%2F32224814_198152794139533_7258373710946500608_n.jpg");
            Request request = new Request.Builder()
                    .url("http://follow4follow.globusdemos.com/api/order/create")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("user-agent", "Dalvik/2.1.0 (Linux; U; Android 8.0.0; moto g(6) Build/OPS27.82-19-4)")
                    .addHeader("host", "follow4follow.globusdemos.com")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "53bd543a-1ebd-72f9-5e15-9c4067eafc24")
                    .build();

            Response response = client.newCall(request).execute();
            String responseString = response.body().string();
            System.out.println(responseString);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
