package appengine.parser.optimal;

import appengine.parser.mysqlmodels.enums.OptimalnotifyNotifytype;
import appengine.parser.optimal.objects.*;
import appengine.parser.optimal.utils.DataAnalyzerUtil;
import appengine.parser.utils.DataBaseConnector;
import okhttp3.*;
import org.jooq.DSLContext;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static appengine.parser.mysqlmodels.Tables.OPTIMALNOTIFY;
import static appengine.parser.utils.TimeUtils.getCurrentTime;

/**
 * Created by anand.kurapati on 17/01/18.
 */
public class Notifier {


    public String fetch() {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        ArrayList<ResultOfCalculation> resultOfCalculationList = dataAnalyzer.getDataFromLastUpdate();


        for (int i = 0; i < resultOfCalculationList.size(); i++) {

            ResultOfCalculation realResultOfCalculation = resultOfCalculationList.get(i);

            int otherMarketsSize = 0;
            if (realResultOfCalculation.getAllOtherMarkets() != null) {
                otherMarketsSize = realResultOfCalculation.getAllOtherMarkets().size();
            }

            ResultOfCalculation resultOfCalculation;

            if (otherMarketsSize > 0) {
                resultOfCalculation = realResultOfCalculation.clone();
            } else {
                resultOfCalculation = realResultOfCalculation;
            }

            int otherMarketIndex = -1;

            while (otherMarketIndex < otherMarketsSize) {

                // for -1 it is best seller
                if (otherMarketIndex >= 0) {
                    resultOfCalculation.setHighestSellCoin(
                            resultOfCalculation.getAllOtherMarkets().get(otherMarketIndex));
                }
                evaluateNewNotify(resultOfCalculation, dataAnalyzer);
                otherMarketIndex++;

            }

            if (otherMarketsSize > 0) {
                resultOfCalculation = realResultOfCalculation.clone();

                otherMarketIndex = 0;

                while (otherMarketIndex < otherMarketsSize) {

                    resultOfCalculation.setLowestBuyCoin(resultOfCalculation.getAllOtherMarkets()
                            .get(otherMarketIndex));

                    evaluateNewNotify(resultOfCalculation, dataAnalyzer);
                    otherMarketIndex++;

                }


                resultOfCalculation = realResultOfCalculation.clone();
                List<CoinMarket> otherMarkets = resultOfCalculation.getAllOtherMarkets();

                for (int p = 0; p < otherMarketsSize; p++) {
                    for (int q = 0; q < otherMarketsSize; q++) {
                        if (p != q) {
                            if (otherMarkets.get(p).getOurBuyPrice()
                                    < otherMarkets.get(q).getOurBuyPrice()) {
                                resultOfCalculation.setLowestBuyCoin(otherMarkets.get(p));
                                realResultOfCalculation.setHighestSellCoin(otherMarkets.get(q));
                            } else {
                                resultOfCalculation.setLowestBuyCoin(otherMarkets.get(q));
                                realResultOfCalculation.setHighestSellCoin(otherMarkets.get(p));
                            }

                            evaluateNewNotify(resultOfCalculation, dataAnalyzer);
                        }
                    }
                }
            }
        }

        return "ok";

    }

    private void evaluateNewNotify(ResultOfCalculation resultOfCalculation, DataAnalyzer dataAnalyzer) {
        Notify newnotify = new Notify(resultOfCalculation.getCoin(), resultOfCalculation.getTimeStamp(),
                resultOfCalculation.profitPercentage(), resultOfCalculation.getLowestBuyCoin().getMarket(),
                resultOfCalculation.getLowestBuyCoin().getOurBuyPrice(), resultOfCalculation.getHighestSellCoin().getMarket(),
                resultOfCalculation.getHighestSellCoin().getOurSellPrice(), null);

        Notify oldnotify = dataAnalyzer.getDataFromLastNotify(newnotify);

        modifyNotifyType(newnotify, oldnotify);

        if (newnotify.notifyType != null) {
            insertResultInDB(newnotify);
            if (newnotify.profit > 2) {
                //postOnSlack(newnotify.toString());
            }

        }
    }

    public String fetchOkexBinance() {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        ArrayList<ResultOfCalculation> resultOfCalculationList = dataAnalyzer.getDataFromLastUpdate();

        DataAnalyzerUtil dataAnalyzerUtil = new DataAnalyzerUtil();
        ArrayList<ResultOfCalculation> filterResultOfCalculationList =
                dataAnalyzerUtil.fetchDataFromTwoExchanges(Market.BINANCE, Market.OKEX);

        for (int i = 0; i < filterResultOfCalculationList.size(); i++) {

            ResultOfCalculation resultOfCalculation = filterResultOfCalculationList.get(i);

            if (resultOfCalculation.profitPercentage() > 1) {

                Notify newnotify = getNotifyFromResultOfCalculation(resultOfCalculation);

                Notify oldnotify = dataAnalyzer.getDataFromLastNotify(newnotify);

                modifyNotifyType(newnotify, oldnotify);

                if (newnotify.notifyType != null) {
                    insertResultInDB(newnotify);
                    //postOnSlacKOkexBinance(newnotify.toString());
                }
            }

        }

        return "ok";

    }

    private void modifyNotifyType(Notify newnotify, Notify oldnotify) {

        if (oldnotify == null) {

            if (newnotify.profit < 0) {
                newnotify.setNotifyType(NotifyType.LOSS);
            } else {
                if (newnotify.profit > 0.2) {
                    newnotify.setNotifyType(NotifyType.NEWRAISE);
                } else {
                    newnotify.setNotifyType(NotifyType.EQUAL);
                }
            }

        } else {

            Double oldProfit = oldnotify.profit;
            Double newProfit = newnotify.profit;

            if (newProfit < 0) {
                if (oldnotify.notifyType != NotifyType.LOSS) {
                    newnotify.setNotifyType(NotifyType.LOSS);
                }
            } else if (newProfit < 0.2) {
                if (oldnotify.notifyType != NotifyType.EQUAL) {
                    newnotify.setNotifyType(NotifyType.EQUAL);
                }
            } else {

                if (newProfit > oldProfit + percentageOf(oldProfit, 10)) {

                    if (oldnotify.notifyType == NotifyType.LOSS) {

                        if (newProfit > 0.2) {
                            newnotify.setNotifyType(NotifyType.NEWRAISE);
                        } else {
                            newnotify.setNotifyType(NotifyType.EQUAL);
                        }

                    } else if (oldnotify.notifyType == NotifyType.EQUAL) {
                        newnotify.setNotifyType(NotifyType.NEWRAISE);
                    } else if (oldnotify.notifyType == NotifyType.NEWRAISE) {
                        newnotify.setNotifyType(NotifyType.RAISEINCREASE);
                    } else if (oldnotify.notifyType == NotifyType.RAISEINCREASE) {
                        if (newProfit > oldProfit + percentageOf(oldProfit, 20)) {
                            newnotify.setNotifyType(NotifyType.RAISEINCREASE);
                        }
                    }
                } else if (newProfit < oldProfit - percentageOf(oldProfit, 10)) {

                    if (oldnotify.notifyType == NotifyType.LOSS) {
                        // this should never occur
                    } else if (oldnotify.notifyType == NotifyType.EQUAL) {
                        // no need to update on this as it is still greater than 0
                    } else if (oldnotify.notifyType == NotifyType.NEWRAISE) {
                        newnotify.setNotifyType(NotifyType.RAISEDECREASE);
                    } else if (oldnotify.notifyType == NotifyType.RAISEINCREASE) {
                        newnotify.setNotifyType(NotifyType.RAISEDECREASE);
                    }

                }

            }

        }


    }

   /* private void modifyNotifyType(Notify newnotify, Notify oldnotify) {
        if (oldnotify == null) {
            if (newnotify.profit < 2) {
                newnotify.setNotifyType(NotifyType.EQUAL);
            } else {
                newnotify.setNotifyType(NotifyType.NEWRAISE);
            }
        } else {

            Double oldProfit = oldnotify.profit;
            Double newProfit = newnotify.profit;

            if (oldnotify.notifyType == NotifyType.EQUAL) {
                newnotify.setNotifyType(NotifyType.NEWRAISE);
            } else if (newProfit > oldProfit + percentageOf(oldProfit, 10)) {
                newnotify.setNotifyType(NotifyType.RAISEINCREASE);
            } else if (newProfit < oldProfit - percentageOf(oldProfit, 10)) {
                newnotify.setNotifyType(NotifyType.RAISEDECREASE);
            } else if (newProfit < 2) {
                newnotify.setNotifyType(NotifyType.EQUAL);
            }
        }
    }*/


    public Notify getNotifyFromResultOfCalculation(ResultOfCalculation resultOfCalculation) {
        Notify notify = new Notify(resultOfCalculation.getCoin(), resultOfCalculation.getTimeStamp(),
                resultOfCalculation.profitPercentage(), resultOfCalculation.getLowestBuyCoin().getMarket(),
                resultOfCalculation.getLowestBuyCoin().getOurBuyPrice(), resultOfCalculation.getHighestSellCoin().getMarket(),
                resultOfCalculation.getHighestSellCoin().getOurSellPrice(), null);
        return notify;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void postOnSlacKOkexBinance(String text) {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Double percentageOf(Double value, int times) {

        Double dtimes = new Double(times);
        return (value * dtimes) / 100;
    }


    public void fetchAndNotify() {

        long twoMinsBack = System.currentTimeMillis() - 60 * 1000 * 2;

        Timestamp timestamp = new Timestamp(twoMinsBack);

    }


    private void insertResultInDB(Notify notify) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(OPTIMALNOTIFY, OPTIMALNOTIFY.COINLABEL, OPTIMALNOTIFY.PROFIT, OPTIMALNOTIFY.FROMMARKET,
                OPTIMALNOTIFY.BUYPRICE, OPTIMALNOTIFY.TOMARKET, OPTIMALNOTIFY.SELLPRICE, OPTIMALNOTIFY.NOTIFYTYPE)
                .values(notify.coinlabel, notify.profit, notify.frommarket.name(), notify.buyprice, notify.tomarket.name(),
                        notify.sellprice, OptimalnotifyNotifytype.valueOf(notify.notifyType.name())).onDuplicateKeyUpdate()
                .set(OPTIMALNOTIFY.TIME, getCurrentTime())
                .set(OPTIMALNOTIFY.PROFIT, notify.profit)
                .set(OPTIMALNOTIFY.BUYPRICE, notify.buyprice)
                .set(OPTIMALNOTIFY.SELLPRICE, notify.sellprice)
                .execute();
    }
}
