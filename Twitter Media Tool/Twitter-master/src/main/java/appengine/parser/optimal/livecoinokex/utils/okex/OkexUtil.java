package appengine.parser.optimal.livecoinokex.utils.okex;

import appengine.parser.optimal.livecoinokex.utils.AddressUtil;
import appengine.parser.optimal.livecoinokex.utils.SymbolUtil;
import appengine.parser.optimal.livecoinokex.utils.TradeDepth;
import appengine.parser.optimal.livecoinokex.utils.Transfer;
import appengine.parser.optimal.livecoinokex.utils.livecoin.LivecoinUtil;
import appengine.parser.optimal.objects.Market;
import org.apache.http.HttpException;
import org.json.JSONObject;

import java.io.IOException;

public class OkexUtil {

    String url_prex = "https://www.okex.com/";
    public static final String apiKey = "66e9ee07-3ac7-4c2f-932f-8324356b2c44";
    public static final String secretKey = "03C4524D6BD1C6041C0EC1E2A0E62111";

    StockRestApi stockRestApi;

    public OkexUtil() {
        stockRestApi = new StockRestApi(url_prex, apiKey, secretKey);
    }

    public TradeDepth getOrderBook(String symbol) {

        TradeDepth tradeDepth = null;

        if (stockRestApi == null) {
            stockRestApi = new StockRestApi(url_prex, apiKey, secretKey);
        }

        try {
            tradeDepth = TradeDepth.fromJSONString(stockRestApi.depth(symbol), new SymbolUtil().getCoin(symbol), true);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tradeDepth;
    }

    public void makeTransfer(Transfer transfer) {

        if (transfer.sellMarket == Market.LIVECOIN) {

            try {

                LivecoinUtil livecoinUtil = new LivecoinUtil();
                AddressUtil addressUtil = livecoinUtil.getAddress(transfer.coin.toUpperCase());
                String response = stockRestApi.withdraw(new SymbolUtil().getUnderScoreBTCCoin(transfer.coin),
                        addressUtil.wallet, transfer.amount, 2.0);
                System.out.println("Withdraw " + response);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public Double getBtcAvailable() {

        Double availableBtc = 0.0;
        if (stockRestApi == null) {
            stockRestApi = new StockRestApi(url_prex, apiKey, secretKey);
        }

        try {
            String jsonString = stockRestApi.userinfo();

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject infoJsonObject = jsonObject.getJSONObject("info");
            JSONObject fundsJsonObject = infoJsonObject.getJSONObject("funds");
            JSONObject freeJsonObject = fundsJsonObject.getJSONObject("free");

            availableBtc = getAvailableBTC(freeJsonObject);

        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return availableBtc;

    }

    public boolean purchase(Transfer transfer) {

        if (stockRestApi == null) {
            stockRestApi = new StockRestApi(url_prex, apiKey, secretKey);
        }

        try {
            String response = stockRestApi.trade(new SymbolUtil().getUnderScoreBTCCoin(transfer.coin), "buy",
                    String.valueOf(transfer.maxBuyPrice), String.valueOf(transfer.amount));

            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("result")) {
                String orderId = String.valueOf(jsonObject.getLong("order_id"));
                transfer.buyorderid = orderId;
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;

    }

    private Double getAvailableBTC(JSONObject freeJsonObject) {
        String samount = freeJsonObject.getString("btc");
        Double amount = Double.valueOf(samount);
        amount = amount - 0.0001;
        return amount;
    }


}
