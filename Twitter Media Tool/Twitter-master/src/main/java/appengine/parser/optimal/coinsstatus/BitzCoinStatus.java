package appengine.parser.optimal.coinsstatus;

import appengine.parser.optimal.objects.CoinStatus;
import appengine.parser.optimal.objects.CoinsStatusUtil;
import appengine.parser.optimal.objects.Market;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BitzCoinStatus implements CoinsStatusUtil {

    List<CoinStatus> coinInfoList = new ArrayList<>();

    @Override
    public void fetch() {
        try {
            File input = new File("bitz_balance.html");
            Document doc = Jsoup.parse(input, "UTF-8", "https://www.bit-z.com/");
            Elements coinRowElements = doc.select("tr");
            for (Element coinRowElement : coinRowElements) {

                try {
                    String coin = coinRowElement.attr("coin").toUpperCase();
                    String name = coinRowElement.attr("display");

                    Elements aElements = coinRowElement.getElementsByTag("a");

                    boolean isWalletActive = false;

                    if (aElements.get(0).text().trim().equalsIgnoreCase("Deposit")) {

                        if (aElements.get(1).text().trim().equalsIgnoreCase("Withdraw")) {
                            isWalletActive = true;
                        }
                    }

                    boolean isListingActive = true;

                    CoinStatus coinStatus = toCoinStatus(name, coin, isWalletActive, isListingActive);
                    coinInfoList.add(coinStatus);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<CoinStatus> getCoinsStatusList() {
        if (coinInfoList == null || coinInfoList.size() == 0) {
            fetch();
        }
        return coinInfoList;
    }

    @Override
    public CoinStatus toCoinStatus(Object... rawCoinInfo) {
        String coinName = (String) rawCoinInfo[0];
        String label = (String) rawCoinInfo[1];
        boolean isWalletActive = (boolean) rawCoinInfo[2];
        boolean isListingActive = (boolean) rawCoinInfo[3];

        CoinStatus coinInfo = new CoinStatus(Market.BITZ, coinName, label, isWalletActive, isListingActive);
        return coinInfo;
    }
}
