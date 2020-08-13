package appengine.parser.optimal.livecoinokex.utils.livecoin;

import appengine.parser.optimal.livecoinokex.utils.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class LivecoinUtil {

    final String BASE_URL = "https://api.livecoin.net";

    FetchFromRequest requestMaker;

    public LivecoinUtil() {
        requestMaker = new FetchFromRequest();
    }

    public ArrayList<TradeDepth> getOrderBookAll() {

        ArrayList<TradeDepth> tradeDepthsList = new ArrayList<>();

        if (requestMaker == null) {
            requestMaker = new FetchFromRequest();
        }

        String jsonString = requestMaker.getAllOrderBook();

        JSONObject jsonObject = new JSONObject(jsonString);

        Set<String> keys = jsonObject.keySet();
        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.contains("/BTC")) {
                JSONObject coinJsonObject = jsonObject.getJSONObject(key);

                TradeDepth tradeDepth = TradeDepth.fromJSONString(coinJsonObject.toString(), new SymbolUtil().getCoin(key));
                tradeDepthsList.add(tradeDepth);
            }
        }

        return tradeDepthsList;
    }

    public AddressUtil getAddress(String coinInCaps) {
        FetchFromRequest fetchFromRequest = new FetchFromRequest();
        String totalAddress = fetchFromRequest.getAddress(coinInCaps);

        AddressUtil addressUtil = new AddressUtil();

        if (totalAddress.contains("::")) {
            String[] array = totalAddress.split("::");
            addressUtil.wallet = array[0];
            addressUtil.extraId = array[1];
        } else {
            addressUtil.wallet = totalAddress;
        }
        return addressUtil;
    }

    public ArrayList<CoinInfo> getCoinsInfo() {

        ArrayList<CoinInfo> coinsInfoList = new ArrayList<>();

        String result = "";

        try {

            result = new FetchFromRequest().getCoinsInfo().body().string();

            JSONObject jsonObject = new JSONObject(result);
            JSONArray infoArray = jsonObject.getJSONArray("info");

            for (int i = 0; i < infoArray.length(); i++) {

                JSONObject coinJsonObject = infoArray.getJSONObject(i);
                CoinInfo coinInfo = CoinInfo.fromJson(coinJsonObject);
                coinsInfoList.add(coinInfo);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return coinsInfoList;
    }

    public boolean purchase(Transfer transfer) {

        Map<String, String> postData = new TreeMap<>();
        postData.put("currencyPair", new SymbolUtil().getSlashBTCCoin(transfer.coin));
        postData.put("price", String.valueOf(transfer.maxBuyPrice));
        postData.put("quantity", String.valueOf(transfer.amount));

        String url = BASE_URL + "/exchange/buylimit";

        FetchFromRequest fetchFromRequest = new FetchFromRequest();
        String response = fetchFromRequest.makePostRequest(postData, url);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("success")) {
                transfer.buyorderid = String.valueOf(jsonObject.getLong("orderId"));
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    public TradeDepth getOrderBook(String symbol) {

        TradeDepth tradeDepth = null;


        return tradeDepth;
    }


}
