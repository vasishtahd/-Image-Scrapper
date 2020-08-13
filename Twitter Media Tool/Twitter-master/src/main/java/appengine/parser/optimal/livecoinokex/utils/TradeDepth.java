package appengine.parser.optimal.livecoinokex.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static org.knowm.xchange.btcmarkets.dto.trade.BTCMarketsOrder.Side.Bid;

public class TradeDepth {


    public String coin;

    public List<Ask> askList = new ArrayList<Ask>();

    public List<Bid> bidList = new ArrayList<Bid>();


    public static TradeDepth fromJSONString(String jsonString, String coin) {
        return fromJSONString(jsonString, coin, false);
    }


    public static TradeDepth fromJSONString(String jsonString, String coin, boolean addAskInreverse) {
        TradeDepth tradeDepth = new TradeDepth();

        try {
            tradeDepth.coin = coin;


            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray asksArray = jsonObject.getJSONArray("asks");

            tradeDepth.askList = new ArrayList<Ask>();

            for (int i = 0; i < asksArray.length(); i++) {
                JSONArray askArrayObject = asksArray.getJSONArray(i);

                Double price = askArrayObject.getDouble(0);
                Double amount = askArrayObject.getDouble(1);

                Ask ask = new Ask(price, amount);
                if (addAskInreverse) {
                    tradeDepth.askList.add(0, ask);
                } else {
                    tradeDepth.askList.add(ask);
                }
            }

            JSONArray bidsArray = jsonObject.getJSONArray("bids");

            tradeDepth.bidList = new ArrayList<Bid>();

            for (int i = 0; i < bidsArray.length(); i++) {
                JSONArray bidArrayObject = bidsArray.getJSONArray(i);

                Double price = bidArrayObject.getDouble(0);
                Double amount = bidArrayObject.getDouble(1);

                Bid bid = new Bid(price, amount);
                tradeDepth.bidList.add(bid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Parsing error at tradedepth");
        }

        return tradeDepth;

    }





}
