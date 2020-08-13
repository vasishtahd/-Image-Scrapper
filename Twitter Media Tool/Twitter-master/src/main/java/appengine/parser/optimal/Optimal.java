package appengine.parser.optimal;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by anand.kurapati on 26/12/17.
 */
public class Optimal {

    Double DOLLAR_TO_RUPPE = 64.02;
    Double TOTAL_INVESTMENT = new Double(100000);

    Double BINANCE_CONVERSION_FEE = (0.1 / 100);
    Double KOINEX_BUYING_FEE = (0.25 / 100);

    final String ERROR = "ERROR";

    Map<String, Double> transferPriceMap = new HashMap<>();

    String BTCUSDT = "BTCUSDT";
    String ETHUSDT = "ETHUSDT";
    String BCCUSDT = "BCCUSDT";
    String LTCUSDT = "LTCUSDT";
    String XRPBTC = "XRPBTC";
    String XRPUSDT = "XRPUSDT";

    String ETH = "ETH";
    String BTC = "BTC";
    String BCH = "BCH";
    String XRP = "XRP";
    String LTC = "LTC";


    Map<String, String> usdindiansymbolmap = new HashMap<>();

    List<String> indianSymbols = Arrays.asList(BTC, ETH, BCH, LTC, XRP);

    List<String> usdsymbols = Arrays.asList(BTCUSDT, ETHUSDT, BCCUSDT, LTCUSDT, XRPBTC);

    Map<String, Double> usdsymbolPrice = new HashMap<>();
    //Map<String, Double> indiansymbolPrice = new HashMap<>();

    Map<String,Double> indianSymbolBuyPrice = new HashMap<>();
    Map<String,Double> indianSymbolSellPrice = new HashMap<>();

    Map<String, Double> usdToIndianSymbolPrice = new HashMap<>();

    Map<String,Double> percentagesBuyMap = new HashMap<>();
    Map<String, Double> percentagesSellMap = new HashMap<>();


    String highestlosspercentageUSDSymbol = "";
    Double highestprofitpercentage = new Double(-1);
    Double lowestlosspercentage = new Double(1);

    String lowestlosspercentageUSDSymbol = "";

    ArrayList<String> excludeList = new ArrayList<>();

    String printString = "\n";


    public String getData(String investment, String[] excludes) {
        TOTAL_INVESTMENT = Double.valueOf(investment);
        for (String exlcude : excludes) {
            excludeList.add(exlcude);
        }
        return getData();
    }

    public String getData(String investment) {
        TOTAL_INVESTMENT = Double.valueOf(investment);
        return getData();
    }

    public String getData() {
        try {

            fillBinancePrices();
            fillKoinexPrices();

            convertBinanceToRupees();

            fillUsdIndianMap();

            calculatePercentages();
            fillTransferPriceMap();

            calculateForProfitInINR(lowestlosspercentageUSDSymbol, highestlosspercentageUSDSymbol, TOTAL_INVESTMENT);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return printString;
    }

    public String getUSDToINDData(String investment, String symbol, String numberOfCoins, String value) {
        TOTAL_INVESTMENT = Double.valueOf(investment);
        return "";
    }

    public String getUSDToINDData(String investment, String symbol, String percentage) {
        TOTAL_INVESTMENT = Double.valueOf(investment);
        return getUSDToINDData(symbol, percentage);
    }

    public String getUSDToINDData(String symbol, String percentage) {
        fillBinancePrices();
        fillKoinexPrices();

        convertBinanceToRupees();

        fillUsdIndianMap();

        calculatePercentages();
        fillTransferPriceMap();

        lowestlosspercentageUSDSymbol = getUSDSymbolFromIndianSymbol(symbol);
        lowestlosspercentage = Double.valueOf(percentage);
        calculateForProfitInINR(lowestlosspercentageUSDSymbol, highestlosspercentageUSDSymbol, TOTAL_INVESTMENT);

        return printString;
    }

    private void fillUsdIndianMap() {
        usdindiansymbolmap.put(ETHUSDT, ETH);
        usdindiansymbolmap.put(BTCUSDT, BTC);
        usdindiansymbolmap.put(BCCUSDT, BCH);
        usdindiansymbolmap.put(LTCUSDT, LTC);
        usdindiansymbolmap.put(XRPUSDT, XRP);
    }

    private String getUSDSymbolFromIndianSymbol(String symbol) {
        Set<String> keys = usdindiansymbolmap.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String usdsymbol = iterator.next();
            if (usdindiansymbolmap.get(usdsymbol).equals(symbol)) {
                return usdsymbol;
            }
        }

        return ERROR;
    }

    private void convertBinanceToRupees() {
        Set<String> keys = usdsymbolPrice.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String usdsymbol = iterator.next();
            Double usdprice = usdsymbolPrice.get(usdsymbol);
            Double inrprice = usdprice * DOLLAR_TO_RUPPE;
            usdToIndianSymbolPrice.put(usdsymbol, inrprice);
        }
    }

    private void calculatePercentages() {


        Set<String> keys = usdsymbolPrice.keySet();
        Iterator<String> iterator = keys.iterator();

        String usdsymbol = "";
        String indianSymbol = "";
        Double indianBuyPrice = new Double(0);
        Double indianSellPrice = new Double(0);
        Double usdindianprice = new Double(0);

        while (iterator.hasNext()) {
            try {
                usdsymbol = iterator.next();
                indianSymbol = usdindiansymbolmap.get(usdsymbol);

                indianBuyPrice = indianSymbolBuyPrice.get(indianSymbol);
                indianSellPrice = indianSymbolSellPrice.get(indianSymbol);
                usdindianprice = usdToIndianSymbolPrice.get(usdsymbol);

                if (indianBuyPrice== null || indianSellPrice==null) {
                    printString += ERROR + " : size of indiansymbolpricemap - ";
                }

                double losspercentage = (indianBuyPrice - usdindianprice) / indianBuyPrice;
                double profitpercentage = (indianSellPrice - usdindianprice)/indianSellPrice;


                printString += indianSymbol + " - " + losspercentage + " %" + "\n";

                System.out.println(indianSymbol + " - " + losspercentage + " %");

                if (losspercentage <= lowestlosspercentage && !excludeList.contains(indianSymbol)) {
                    lowestlosspercentage = losspercentage;
                    lowestlosspercentageUSDSymbol = usdsymbol;
                }

                if (profitpercentage >= highestprofitpercentage && !excludeList.contains(indianSymbol)) {
                    highestprofitpercentage = profitpercentage;
                    highestlosspercentageUSDSymbol = usdsymbol;
                }

                percentagesBuyMap.put(indianSymbol,losspercentage);
                percentagesSellMap.put(indianSymbol,profitpercentage);

            } catch (Exception e) {
                e.printStackTrace();
                printString += "ERROR OCCURED " + " : " + usdsymbol + ":" + usdindianprice + " - " + indianSymbol + " ";
                return;
            }
        }
    }

    private void fillTransferPriceMap() {
        transferPriceMap.put(BTC, 0.001);
        transferPriceMap.put(ETH, 0.01);
        transferPriceMap.put(BCH, 0.001);
        transferPriceMap.put(LTC, 0.01);
        transferPriceMap.put(XRP, 0.15);
    }

    private void calculateForProfitInINR(String usdlosssymbol, String usdProfitSymbol, Double totalInvestment) {

        String indianlossSymbol = usdindiansymbolmap.get(usdlosssymbol);
        Double indianlossprice = indianSymbolBuyPrice.get(indianlossSymbol);
        Double usdlossindianprice = usdToIndianSymbolPrice.get(usdlosssymbol);

        totalInvestment = totalInvestment - (KOINEX_BUYING_FEE) * totalInvestment;

        Double noOfIndianCoinsBeforeTransfer = totalInvestment / indianlossprice;

        Double noOfUSDCoins = noOfIndianCoinsBeforeTransfer - transferPriceMap.get(indianlossSymbol);

        Double usdProfitPrice = usdToIndianSymbolPrice.get(usdProfitSymbol);

        Double usdConvertedCoins = (noOfUSDCoins * usdlossindianprice) / usdProfitPrice;

        String indianProfitSymbol = usdindiansymbolmap.get(usdProfitSymbol);

        Double noOfIndianCoinsAfterTransfer = usdConvertedCoins - transferPriceMap.get(indianProfitSymbol);

        Double finalPrice = noOfIndianCoinsAfterTransfer * indianSymbolSellPrice.get(indianProfitSymbol) - ((BINANCE_CONVERSION_FEE * totalInvestment) * 2);

        Double profit = finalPrice - totalInvestment;

        printString += "Buy " + indianlossSymbol + " - " + noOfIndianCoinsBeforeTransfer + "\n";
        System.out.println("Buy " + indianlossSymbol + " - " + noOfIndianCoinsBeforeTransfer);
        printString += "Sell " + indianProfitSymbol + " - " + noOfIndianCoinsAfterTransfer + "\n";
        System.out.println("Sell " + indianProfitSymbol + " - " + noOfIndianCoinsAfterTransfer);
        printString += "Total Profit = " + profit + "\n";
        System.out.println("Total Profit = " + profit);

    }

    private void fillBinancePrices() {
        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://api.binance.com/api/v3/ticker/price")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "489fa3d1-a999-6b98-d60d-3cd6533ab7e3")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String symbol = jsonObject.getString("symbol");
                if (usdsymbols.contains(symbol)) {
                    String price = jsonObject.getString("price");

                    Double dprice;
                    if (symbol.equals(XRPBTC)) {
                        dprice = Double.valueOf(price) * Double.valueOf(usdsymbolPrice.get(BTCUSDT));
                        symbol = XRPUSDT;
                    } else {
                        dprice = Double.valueOf(price);
                    }
                    usdsymbolPrice.put(symbol, dprice);
                    printString += symbol + " - USD " + dprice + "\n";
                    System.out.println(symbol + " - USD " + dprice);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillKoinexPrices() {
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://koinex.in/api/ticker")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "97b83d16-a907-2c04-9468-34564e5fbf4e")
                    .build();

            Response response = client.newCall(request).execute();
            String jsonString = response.body().string();

            JSONObject jsonObject = new JSONObject(jsonString);
            jsonObject = jsonObject.getJSONObject("stats");
            for (int i = 0; i < indianSymbols.size(); i++) {
                String symbol = indianSymbols.get(i);
                JSONObject statsObject = jsonObject.getJSONObject(symbol);
                String lastTradedprice = String.valueOf(statsObject.getDouble("last_traded_price"));
                printString += symbol + "BuyPrice - Rs " + statsObject.getString("lowest_ask") +"SellPrice - " +statsObject.getString("highest_bid")+"\n";
                Double dlastTradedprice = Double.valueOf(lastTradedprice);
                indianSymbolBuyPrice.put(symbol,Double.valueOf(statsObject.getString("lowest_ask")));
                indianSymbolSellPrice.put(symbol,Double.valueOf(statsObject.getString("highest_bid")));
              //  indiansymbolPrice.put(symbol, dlastTradedprice);
            }

        } catch (Exception e) {
              e.printStackTrace();
        }

    }

}
