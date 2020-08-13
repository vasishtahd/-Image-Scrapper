package appengine.parser.optimal.utils;

import appengine.parser.optimal.exchangeutils.BitzUtil;
import appengine.parser.optimal.exchangeutils.CobinhoodUtil;
import appengine.parser.optimal.exchangeutils.CoinExchangeUtil;
import appengine.parser.optimal.exchangeutils.SouthXchangeUtil;
import appengine.parser.optimal.livecoinokex.enums.TransferState;
import appengine.parser.optimal.livecoinokex.utils.Ask;
import appengine.parser.optimal.livecoinokex.utils.Bid;
import appengine.parser.optimal.livecoinokex.utils.TradeDepth;
import appengine.parser.optimal.livecoinokex.utils.Transfer;
import appengine.parser.optimal.livecoinokex.utils.okex.OkexUtil;
import appengine.parser.optimal.objects.Market;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.binance.BinanceExchange;
import org.knowm.xchange.bittrex.BittrexExchange;
import org.knowm.xchange.cryptopia.CryptopiaExchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.hitbtc.v2.HitbtcExchange;
import org.knowm.xchange.liqui.LiquiExchange;
import org.knowm.xchange.livecoin.LivecoinExchange;
import org.knowm.xchange.poloniex.PoloniexExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.io.IOException;
import java.util.List;

public class OrderBookCalculator {

    String coin;
    String buyMarket;
    String sellMarket;

    TradeDepth buyOrderBook;
    TradeDepth sellOrderBook;


    public OrderBookCalculator(String coin, String buyMarket, String sellMarket) {
        this.coin = coin;
        this.buyMarket = buyMarket;
        this.sellMarket = sellMarket;
    }

    public Transfer calculate() {

        buyOrderBook = getOrderBook(buyMarket);
        sellOrderBook = getOrderBook(sellMarket);

        Transfer transfer = getMaxProfitAmount(buyOrderBook, sellOrderBook);

        return transfer;
    }

    private Transfer getMaxProfitAmount(TradeDepth buyTradeDepth, TradeDepth sellTradeDepth) {

        Transfer transfer = new Transfer();

        int buytradeIndex = 0;
        int selltradeIndex = 0;

        boolean noMoreProfitCanbeMade = false;

        Double profit = 0.0;
        Double amounttobepurchased = 0.0;
        Double amountinbtctobespent = 0.0;
        Double maxPriceToBuy = 0.0;
        Double minPriceToSell = 10.0;

        while (!noMoreProfitCanbeMade) {

            if (buytradeIndex >= buyTradeDepth.askList.size() || selltradeIndex >= sellTradeDepth.bidList.size()) {
                //profit = profit * 3;
                break;
            }


            Ask ourBuyTrade = buyTradeDepth.askList.get(buytradeIndex);
            Bid ourSellTrade = sellTradeDepth.bidList.get(selltradeIndex);


            if (ourBuyTrade.price > ourSellTrade.price) {
                noMoreProfitCanbeMade = true;
                continue;
            }


            if (ourBuyTrade.price > maxPriceToBuy) {
                maxPriceToBuy = ourBuyTrade.price;
            }

            if (ourSellTrade.price < minPriceToSell) {
                minPriceToSell = ourSellTrade.price;
            }


            if (ourBuyTrade.amount < ourSellTrade.amount) {

                Double amount = ourBuyTrade.amount;
                profit += (ourSellTrade.price - ourBuyTrade.price) * amount;

                amounttobepurchased += ourBuyTrade.amount;
                amountinbtctobespent += ourBuyTrade.price * ourBuyTrade.amount;

                ourSellTrade.amount = ourSellTrade.amount - ourBuyTrade.amount;

                buytradeIndex++;

            }

            if (ourBuyTrade.amount > ourSellTrade.amount) {

                Double amount = ourSellTrade.amount;
                profit += (ourSellTrade.price - ourBuyTrade.price) * amount;

                amounttobepurchased += ourSellTrade.amount;
                amountinbtctobespent += ourBuyTrade.price * ourSellTrade.amount;

                ourBuyTrade.amount = ourBuyTrade.amount - ourSellTrade.amount;
                selltradeIndex++;
            }


        }

        transfer.coin = coin;
        transfer.amount = amounttobepurchased;
        transfer.priceToBeSpentInBTC = amountinbtctobespent;
        transfer.profitEstimatedInBTC = profit;
        transfer.minSellPrice = minPriceToSell;
        transfer.maxBuyPrice = maxPriceToBuy;


        transfer.sellMarket = Market.valueOf(sellMarket);
        transfer.buyMarket = Market.valueOf(buyMarket);


        transfer.currentState = TransferState.PURCHASE_PENDING;


        return transfer;

    }


    private TradeDepth getOrderBook(String market) {

        OrderBook orderBook = null;
        TradeDepth tradeDepth = null;

        Exchange exchange = null;

        switch (Market.valueOf(market)) {
            case BINANCE:
                exchange = ExchangeFactory.INSTANCE.createExchange(BinanceExchange.class.getName());
                break;
            case OKEX:
                tradeDepth = new OkexUtil().getOrderBook(coin.toLowerCase() + "_btc");
                break;
            case LIVECOIN:
                exchange = ExchangeFactory.INSTANCE.createExchange(LivecoinExchange.class.getName());
                break;
            case COBINHOOD:
                tradeDepth = new CobinhoodUtil().getTradeDepth(coin);
                break;
            case BITZ:
                tradeDepth = new BitzUtil().getTradeDepth(coin);
                break;
            case LIQUI:
                exchange = ExchangeFactory.INSTANCE.createExchange(LiquiExchange.class.getName());
                break;
            case HitBTC:
                exchange = ExchangeFactory.INSTANCE.createExchange(HitbtcExchange.class.getName());
                break;
            case POLONEIX:
                exchange = ExchangeFactory.INSTANCE.createExchange(PoloniexExchange.class.getName());
                break;
            case CRYPTOPIA:
                exchange = ExchangeFactory.INSTANCE.createExchange(CryptopiaExchange.class.getName());
                break;
            case COINEXCHANGE:
                tradeDepth = new CoinExchangeUtil().getTradeDepth(coin);
                break;
            case SOUTHXCHANGE:
                tradeDepth = new SouthXchangeUtil().getTradeDepth(coin);
                break;
            case BITTREX:
                exchange = ExchangeFactory.INSTANCE.createExchange(BittrexExchange.class.getName());

        }

        if (exchange != null) {
            MarketDataService marketDataService = exchange.getMarketDataService();
            try {
                orderBook = generic(marketDataService, new CurrencyPair(coin, "btc"));
                tradeDepth = orderBookToTradeDepth(orderBook);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {


        }

        return tradeDepth;
    }

    public TradeDepth orderBookToTradeDepth(OrderBook orderBook) {

        TradeDepth tradeDepth = new TradeDepth();
        tradeDepth.coin = coin;

        List<LimitOrder> asksLimitOrder = orderBook.getAsks();
        List<LimitOrder> bidLimitOrder = orderBook.getBids();

        for (int i = 0; i < asksLimitOrder.size(); i++) {

            LimitOrder limitOrder = asksLimitOrder.get(i);
            Ask ask = new Ask(limitOrder.getLimitPrice().doubleValue(), limitOrder.getRemainingAmount().doubleValue());
            tradeDepth.askList.add(ask);
        }

        for (int i = 0; i < bidLimitOrder.size(); i++) {

            LimitOrder limitOrder = bidLimitOrder.get(i);
            Bid bid = new Bid(limitOrder.getLimitPrice().doubleValue(), limitOrder.getRemainingAmount().doubleValue());
            tradeDepth.bidList.add(bid);
        }

        return tradeDepth;
    }

    private static OrderBook generic(MarketDataService marketDataService, CurrencyPair pair) throws IOException {
        OrderBook orderBook = marketDataService.getOrderBook(pair, new Object[0]);
        return orderBook;
    }
}
