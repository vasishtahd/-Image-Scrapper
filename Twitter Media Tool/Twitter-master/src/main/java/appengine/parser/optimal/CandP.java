package appengine.parser.optimal;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.cryptopia.CryptopiaExchange;
import org.knowm.xchange.cryptopia.dto.marketdata.CryptopiaTicker;
import org.knowm.xchange.cryptopia.service.CryptopiaMarketDataServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.poloniex.dto.marketdata.PoloniexTicker;
import org.knowm.xchange.poloniex.service.PoloniexMarketDataServiceRaw;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.knowm.xchange.yobit.YoBitExchange;
import org.knowm.xchange.yobit.service.YoBitMarketDataService;

import java.io.IOException;
import java.util.List;

/**
 * Created by anand.kurapati on 31/12/17.
 */
public class CandP {

    String printString = "\n";

    List<org.knowm.xchange.cryptopia.dto.marketdata.CryptopiaTicker> cryptopiaTickerList;

    YoBitMarketDataService yoBitMarketDataService;


    public String calculateCandP() {

        fillCryptoData();
        fillPoloniex();
        return printString;
    }

    public String calculateCryptoYoBit() {
        fillCryptoData();
        calculateWithYoBit(cryptopiaTickerList);
        return printString;
    }

    private void fillCryptoData() {
        try {
            Exchange cryptopia = ExchangeFactory.INSTANCE.createExchange(CryptopiaExchange.class.getName());
            // Interested in the public market data feed (no authentication)
            MarketDataService marketDataService = cryptopia.getMarketDataService();

            rawCryptoPia((CryptopiaMarketDataServiceRaw) marketDataService);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rawCryptoPia(CryptopiaMarketDataServiceRaw marketDataService) throws IOException {
        cryptopiaTickerList = marketDataService.getCryptopiaMarkets(Currency.BTC);
    }

    private void fillPoloniex() {

        try {
            Exchange poloniex = ExchangeFactory.INSTANCE.createExchange(PoloniexExchange.class.getName());
            MarketDataService marketDataService = poloniex.getMarketDataService();

            for (int i = 0; i < cryptopiaTickerList.size(); i++) {

                CryptopiaTicker cryptopiaTicker = cryptopiaTickerList.get(i);
                printString += cryptopiaTicker.getLabel() + "\n";
                PoloniexTicker poloniexTicker;
                Double cryptopiaLast;
                Double poloniexLast;

                try {

                    poloniexTicker = ((PoloniexMarketDataServiceRaw) marketDataService).
                            getPoloniexTicker(getTradePairFromLabel(cryptopiaTicker.getLabel()));

                    cryptopiaLast = cryptopiaTicker.getLast().doubleValue();
                    poloniexLast = poloniexTicker.getPoloniexMarketData().getLast().doubleValue();
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                Double difference = cryptopiaLast - poloniexLast;

                Double poloneixProfit = (Math.abs(difference) / poloniexLast) * 100;
                Double cryptoProfit = (Math.abs(difference) / cryptopiaLast) * 100;

                if (poloneixProfit > 1 || cryptoProfit > 1) {
                    printString += cryptopiaTicker.getLabel() + "\n";

                    if (difference > 0) {
                        printString += "Buy from Poloneix Sell in Cryptopia . Percentage Profit : " + poloneixProfit + "\n";
                    } else {
                        printString += "Buy from Cryptopia Sell in Poloneix . Percentage Profit : " + cryptoProfit + "\n";
                    }

                    printString += "Cryptopia:" + cryptopiaLast + " Poloneix:" + poloniexLast + "\n" + "\n";
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fillPoloneixPostMan() {

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://www.poloniex.com/public?command=returnTicker")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "56b2b5f6-2cdc-5bd7-f464-c46cc1f7a742")
                    .build();
            Response response = client.newCall(request).execute();
            printString += response.body().string();


        } catch (Exception e) {

        }
    }

    private void initYoBit() {
        Exchange yoBitExchange = ExchangeFactory.INSTANCE.createExchange(YoBitExchange.class.getName(), "", "",
                "7ECABF40BE54588B162B0055FF4F7444",
                "28be9a9e22acc24fcb944e349b0adb11");
        yoBitMarketDataService = (YoBitMarketDataService) yoBitExchange.getMarketDataService();
    }

    private void calculateWithYoBit(List<org.knowm.xchange.cryptopia.dto.marketdata.CryptopiaTicker> cryptopiaTickerList) {
        if (yoBitMarketDataService == null) {
            initYoBit();
        }

        try {

            for (int i = 0; i < cryptopiaTickerList.size(); i++) {

                CryptopiaTicker cryptopiaTicker = cryptopiaTickerList.get(i);
                Double cryptopiaLast;
                Double yoBitLast;

                try {

                    Ticker yoBitTicker = yoBitMarketDataService.getTicker(getTradePairFromLabel(cryptopiaTicker.getLabel()));


                    cryptopiaLast = cryptopiaTicker.getLast().doubleValue();
                    yoBitLast = yoBitTicker.getLast().doubleValue();

                    Double difference = cryptopiaLast - yoBitLast;

                    Double yoBitProfit = (Math.abs(difference) / yoBitLast) * 100;
                    Double cryptoProfit = (Math.abs(difference) / cryptopiaLast) * 100;

                    if (yoBitLast > 1 || cryptoProfit > 1) {
                        printString += cryptopiaTicker.getLabel() + "\n";

                        if (difference > 0) {
                            printString += "Buy from YoBit Sell in Cryptopia . Percentage Profit : " + yoBitProfit + "\n";
                        } else {
                            printString += "Buy from Cryptopia Sell in YoBit . Percentage Profit : " + cryptoProfit + "\n";
                        }

                        printString += "Cryptopia:" + cryptopiaLast + " YoBit:" + yoBitLast + "\n" + "\n";
                    }

                } catch (Exception e) {
                    continue;
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private CurrencyPair getTradePairFromLabel(String label) {
        String[] pair = label.split("/");
        return new CurrencyPair(pair[0], pair[1]);
    }


}
