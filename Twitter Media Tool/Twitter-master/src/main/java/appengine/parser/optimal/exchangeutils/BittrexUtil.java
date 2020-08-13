package appengine.parser.optimal.exchangeutils;

import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.MarketUtil;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.bittrex.BittrexExchange;
import org.knowm.xchange.bittrex.dto.marketdata.BittrexMarketSummary;
import org.knowm.xchange.bittrex.service.BittrexMarketDataServiceRaw;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand.kurapati on 06/01/18.
 */
public class BittrexUtil implements MarketUtil {

    private List<CoinMarket> coinMarketList = new ArrayList<>();

    @Override
    public void fetch() {

        try {
            final Exchange bittrexExchange = createExchange();
            final BittrexMarketDataServiceRaw bittrexExchangeMarketDataService = (BittrexMarketDataServiceRaw) bittrexExchange.getMarketDataService();
            ArrayList<BittrexMarketSummary> bittrexMarketSummaryArrayList = bittrexExchangeMarketDataService.getBittrexMarketSummaries();
            for (BittrexMarketSummary bittrexMarketSummary : bittrexMarketSummaryArrayList) {
                if (bittrexMarketSummary.getMarketName().contains("BTC-")) {
                    CoinMarket coinMarket = toCoinMarket(bittrexMarketSummary);
                    if (coinMarket.getOurBuyPrice() > 0) {
                        coinMarketList.add(coinMarket);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Exchange createExchange() {
        final Exchange bittrexExchange = ExchangeFactory.INSTANCE.createExchange(BittrexExchange.class.getName());
        //bittrexExchange.getExchangeSpecification().setApiKey("FIXUGEQP-XBFWFV0K-2ZP478V3-NSZ9YESA-C2INRYOY");
        //bittrexExchange.getExchangeSpecification().setSecretKey("01d28dc810f72f95ef1797b3900600717432e469d1f1cafcf3ca9c65cf2f402b");
        //bittrexExchange.applySpecification(bittrexExchange.getExchangeSpecification());
        return bittrexExchange;
    }

    @Override
    public List<CoinMarket> getCoinList() {
        if (coinMarketList == null || coinMarketList.size() == 0) {
            fetch();
        }
        return coinMarketList;
    }

    @Override
    public CoinMarket toCoinMarket(Object... rawCoinMarket) {
        BittrexMarketSummary bittrexMarketSummary = (BittrexMarketSummary) rawCoinMarket[0];

        String label = bittrexMarketSummary.getMarketName().split("-")[1];

        CoinMarket coinMarket = new CoinMarket(Market.BITTREX, label, bittrexMarketSummary.getAsk(), bittrexMarketSummary.getBid(),
                bittrexMarketSummary.getVolume());

        return coinMarket;
    }
}

