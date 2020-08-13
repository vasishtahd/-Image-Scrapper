package appengine.parser.optimal;

import appengine.parser.optimal.livecoinokex.utils.Transfer;
import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.optimal.utils.OrderBookCalculator;
import okhttp3.*;

import java.util.List;

public class AppNotifier {


    public void calculateAndNotify() {

        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        List<ResultOfCalculation> resultOfCalculations =
                dataAnalyzer.getDataFromLastUpdateWithMinPercentage(0);


        for (int i = 0; i < resultOfCalculations.size(); i++) {
            try {
                ResultOfCalculation resultOfCalculation = resultOfCalculations.get(i);
                OrderBookCalculator orderBookCalculator = new OrderBookCalculator(resultOfCalculation.getCoin(),
                        resultOfCalculation.getLowestBuyCoin().getMarket().name(), resultOfCalculation.getHighestSellCoin()
                        .getMarket().name());
                Transfer transfer = orderBookCalculator.calculate();
                if (transfer.profitEstimatedInBTC > 0.001) {
                    postOnSlacKAppNotifier(transfer.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void postOnSlacKAppNotifier(String text) {

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
}
