package appengine.parser.optimal.utils;

import appengine.parser.optimal.DataAnalyzer;
import appengine.parser.optimal.objects.CoinMarket;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.ResultOfCalculation;

import java.util.ArrayList;
import java.util.List;

public class DataAnalyzerUtil {

    public ArrayList<ResultOfCalculation> fetchDataFromTwoExchanges (Market FIRSTMARKET,
                                                                    Market SECONDMARKET) {

        ArrayList<ResultOfCalculation> filteredresultOfCalculationList = new
                ArrayList<>();

        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        ArrayList<ResultOfCalculation> resultOfCalculationList = dataAnalyzer.getDataFromLastUpdate();

        for (int i = 0; i < resultOfCalculationList.size(); i++) {

            ResultOfCalculation resultOfCalculation = resultOfCalculationList.get(i);

            boolean isPaired = false;

            if (resultOfCalculation.getLowestBuyCoin().getMarket() == FIRSTMARKET) {
                if (resultOfCalculation.getHighestSellCoin().getMarket() == SECONDMARKET) {
                    isPaired = true;
                } else {
                    List<CoinMarket> otherCoinMarketsList = resultOfCalculation.getAllOtherMarkets();
                    for (int j = 0; j < otherCoinMarketsList.size(); j++) {
                        CoinMarket otherCoinMarket = otherCoinMarketsList.get(j);

                        if (otherCoinMarket.getMarket() == SECONDMARKET) {
                            resultOfCalculation.setHighestSellCoin(otherCoinMarket);
                            isPaired = true;
                            break;
                        }
                    }
                }
            } else if (resultOfCalculation.getLowestBuyCoin().getMarket() == SECONDMARKET) {
                if (resultOfCalculation.getHighestSellCoin().getMarket() == FIRSTMARKET) {
                    isPaired = true;
                } else {
                    List<CoinMarket> otherCoinMarketsList = resultOfCalculation.getAllOtherMarkets();

                    for (int j = 0; j < otherCoinMarketsList.size(); j++) {
                        CoinMarket otherCoinMarket = otherCoinMarketsList.get(j);
                        if (otherCoinMarket.getMarket() == FIRSTMARKET) {
                            resultOfCalculation.setHighestSellCoin(otherCoinMarket);
                            isPaired = true;
                            break;
                        }
                    }
                }
            } else {
                List<CoinMarket> otherCoinMarketsList = resultOfCalculation.getAllOtherMarkets();
                CoinMarket firstCoinMarket = null;
                CoinMarket secondCoinMarket = null;
                for (int j = 0; j < otherCoinMarketsList.size(); j++) {
                    CoinMarket coinMarket = otherCoinMarketsList.get(j);
                    if (coinMarket.getMarket() == SECONDMARKET) {
                        firstCoinMarket = coinMarket;
                    }
                    if (coinMarket.getMarket() == FIRSTMARKET) {
                        secondCoinMarket = coinMarket;
                    }
                }
                if (firstCoinMarket != null && secondCoinMarket != null) {

                    resultOfCalculation.setLowestBuyCoin(firstCoinMarket);
                    resultOfCalculation.setHighestSellCoin(secondCoinMarket);

                    Double firstProfitPercentage = resultOfCalculation.profitPercentage();

                    resultOfCalculation.setLowestBuyCoin(secondCoinMarket);
                    resultOfCalculation.setHighestSellCoin(firstCoinMarket);

                    Double secondProfitPercentage = resultOfCalculation.profitPercentage();

                    if (firstProfitPercentage > secondProfitPercentage) {
                        resultOfCalculation.setLowestBuyCoin(firstCoinMarket);
                        resultOfCalculation.setHighestSellCoin(secondCoinMarket);
                    } else {
                        //its already set previously
                    }
                    isPaired = true;
                }
            }

            resultOfCalculation.profitPercentage();

            if (isPaired) {
                filteredresultOfCalculationList.add(resultOfCalculation);
            }
        }

        return filteredresultOfCalculationList;

    }
}
