package appengine.parser.optimal.livecoinokex;

import appengine.parser.optimal.livecoinokex.enums.TransferState;
import appengine.parser.optimal.livecoinokex.utils.*;
import appengine.parser.optimal.livecoinokex.utils.livecoin.LivecoinUtil;
import appengine.parser.optimal.livecoinokex.utils.okex.OkexUtil;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.optimal.utils.DataAnalyzerUtil;
import okhttp3.*;

import java.util.ArrayList;
import java.util.Collections;

public class OkexLivecoinApi {

    String printString = "";


    public String automate() {

        return getLastData(false);
    }

    private String getLastData(boolean isJson) {

        DataAnalyzerUtil dataAnalyzerUtil = new DataAnalyzerUtil();
        ArrayList<ResultOfCalculation> filteredResultOfCalculation =
                dataAnalyzerUtil.fetchDataFromTwoExchanges(Market.LIVECOIN, Market.OKEX);

        ArrayList<TradeDepth> allTradeDepths = new LivecoinUtil().getOrderBookAll();

        OkexUtil okexUtil = new OkexUtil();

        ArrayList<CoinInfo> liveCoinInfoList = new LivecoinUtil().getCoinsInfo();


        ArrayList<Transfer> transfersList = new ArrayList<>();

        for (int i = 0; i < filteredResultOfCalculation.size(); i++) {
            ResultOfCalculation resultOfCalculation = filteredResultOfCalculation.get(i);
            CoinInfo coinInfo = getCoinInfoFromCoin(resultOfCalculation.getCoin(), liveCoinInfoList);
            if (resultOfCalculation.profitPercentage() > 0.5 && coinInfo != null && isCoinWalletStatusOk(coinInfo)) {

                print(resultOfCalculation.toJSON(), isJson);

                TradeDepth tradeDepth = getTradeDepthForCoin(resultOfCalculation.getCoin(), allTradeDepths);

                if (tradeDepth != null) {
                    Transfer transfer;
                    if (resultOfCalculation.getHighestSellCoin().getMarket() == Market.OKEX) {
                        transfer = getMaxProfitAmount(resultOfCalculation, tradeDepth, true);
                    } else {
                        transfer = getMaxProfitAmount(resultOfCalculation, tradeDepth, false);
                    }

                    String profitString = new DoubleUtil().priceFormatter(transfer.profitEstimatedInBTC);
                    if (transfer.profitEstimatedInBTC > 0.001) {
                        transfersList.add(transfer);
                        postOnSlack(transfer.toString());
                        //purchase(transfer, coinInfo);
                    }
                    print(transfer.toString(), isJson);
                } else {
                    print("Trade depth null", isJson);
                }
            }

        }

        Collections.sort(transfersList);

        for (int i = 0; i < transfersList.size(); i++) {

            Transfer transfer = transfersList.get(i);

            Double availableBtcInOkex = okexUtil.getBtcAvailable();
            CoinInfo coinInfo = getCoinInfoFromCoin(transfer.coin, liveCoinInfoList);

            boolean isPurchased = purchase(transfer, availableBtcInOkex, coinInfo);

            if (isPurchased) {
                transfer.currentState = TransferState.PURCHASED;
                transfer.insertInDB();
                postOnSlackTrade(transfer.toString());
            }

        }

        return "";
    }


    private Transfer getMaxProfitAmount(ResultOfCalculation resultOfCalculation, TradeDepth liveCointradedepth, boolean isSellingTradeOkex) {

        Transfer transfer = new Transfer();

        OkexUtil okexUtil = new OkexUtil();

        TradeDepth buyTradeDepth;
        TradeDepth sellTradeDepth;

        TradeDepth tradeDepthOkex = okexUtil.getOrderBook(resultOfCalculation.getUnderScoreBTCCoin());

        if (isSellingTradeOkex) {
            sellTradeDepth = tradeDepthOkex;
            buyTradeDepth = liveCointradedepth;
        } else {
            sellTradeDepth = liveCointradedepth;
            buyTradeDepth = tradeDepthOkex;
        }


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
                postOnSlack("Exceeded Order Book Profit " + buyTradeDepth.coin);
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

        transfer.coin = resultOfCalculation.getCoin();
        transfer.amount = amounttobepurchased;
        transfer.priceToBeSpentInBTC = amountinbtctobespent;
        transfer.profitEstimatedInBTC = profit;
        transfer.minSellPrice = minPriceToSell;
        transfer.maxBuyPrice = maxPriceToBuy;

        if (isSellingTradeOkex) {
            transfer.sellMarket = Market.OKEX;
            transfer.buyMarket = Market.LIVECOIN;
        } else {
            transfer.sellMarket = Market.LIVECOIN;
            transfer.buyMarket = Market.OKEX;
        }

        transfer.currentState = TransferState.PURCHASE_PENDING;


        return transfer;

    }

    private boolean purchase(Transfer transfer, Double availableBTCinOkex, CoinInfo coinInfo) {

        if (transfer.buyMarket == Market.OKEX) {

            double amount = (availableBTCinOkex / transfer.maxBuyPrice) - coinInfo.withdrawFee;

            if (amount > coinInfo.minimumOrderAmount && amount > coinInfo.minimumWithdrawAmount) {

                Double averageBuyingPrice = (transfer.priceToBeSpentInBTC / transfer.amount);

                Double averageSellingPrice = (transfer.priceToBeSpentInBTC + transfer.profitEstimatedInBTC) /
                        (transfer.amount);

                if (averageSellingPrice * (amount - coinInfo.withdrawFee) > amount * averageBuyingPrice) {

                    OkexUtil okexUtil = new OkexUtil();
                    boolean purchaseValue = okexUtil.purchase(transfer);
                    if (purchaseValue) {
                        return true;
                    }

                } else {
                    return false;
                }

            }


        } else if (transfer.buyMarket == Market.LIVECOIN) {

            double amount = (availableBTCinOkex / transfer.maxBuyPrice) - coinInfo.withdrawFee;

            if (amount > coinInfo.minimumOrderAmount && amount > coinInfo.minimumWithdrawAmount) {

                Double averageBuyingPrice = (transfer.priceToBeSpentInBTC / transfer.amount);

                Double averageSellingPrice = (transfer.priceToBeSpentInBTC + transfer.profitEstimatedInBTC) /
                        (transfer.amount);

                if (averageSellingPrice * (amount - coinInfo.withdrawFee) > amount * averageBuyingPrice) {

                    LivecoinUtil livecoinUtil = new LivecoinUtil();

                    boolean purchaseValue = livecoinUtil.purchase(transfer);
                    if (purchaseValue) {
                        return true;
                    }

                } else {
                    return false;
                }

            }

        }

        return false;
    }

    private CoinInfo getCoinInfoFromCoin(String coin, ArrayList<CoinInfo> coinsInfoList) {
        for (int i = 0; i < coinsInfoList.size(); i++) {
            CoinInfo coinInfo = coinsInfoList.get(i);
            if (coinInfo.coin.equalsIgnoreCase(coin)) {
                return coinInfo;
            }
        }

        return null;
    }

    private boolean isCoinWalletStatusOk(CoinInfo coinInfo) {
        if (coinInfo.walletStatus.equalsIgnoreCase("normal")) {
            return true;
        } else {
            return false;
        }
    }


    private boolean isCoinWalletStatusOk(String coin, ArrayList<CoinInfo> coinsInfoList) {
        for (int i = 0; i < coinsInfoList.size(); i++) {
            CoinInfo coinInfo = coinsInfoList.get(i);
            if (coinInfo.coin.equalsIgnoreCase(coin)) {
                if (coinInfo.walletStatus.equalsIgnoreCase("normal")) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    private void postOnSlackTrade(String text) {

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{\"text\":\"" + text + "\"}");
            Request request = new Request.Builder()
                    .url("https://hooks.slack.com/services/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")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "8fa86be2-d201-9b05-5249-2be48eeb8a59")
                    .build();
            Response response = client.newCall(request).execute();
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void postOnSlack(String text) {

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, "{\"text\":\"" + text + "\"}");
            Request request = new Request.Builder()
                    .url("https://hooks.slack.com/services/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")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "8fa86be2-d201-9b05-5249-2be48eeb8a59")
                    .build();
            Response response = client.newCall(request).execute();
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private TradeDepth getTradeDepthForCoin(String coin, ArrayList<TradeDepth> allTradeDepths) {

        for (int i = 0; i < allTradeDepths.size(); i++) {

            TradeDepth tradeDepth = allTradeDepths.get(i);

            if (tradeDepth.coin.equalsIgnoreCase(coin)) {
                return tradeDepth;
            }

        }

        return null;
    }


    private String resultString(boolean isJson) {

        if (printString.length() > 2 && printString.charAt(printString.length() - 2) == ',') {
            printString = printString.substring(0, printString.length() - 2);
            printString += "\n";
        }

        if (isJson) {
            return "[" + printString + "]";
        } else {
            return printString;
        }
    }

    private void print(String text, boolean isJson) {
        printString += text;
        if (isJson) {
            printString += ",";
        }
        printString += "\n";
    }
}
