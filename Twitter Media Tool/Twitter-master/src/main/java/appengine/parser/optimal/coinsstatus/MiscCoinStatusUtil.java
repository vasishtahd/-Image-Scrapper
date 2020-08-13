package appengine.parser.optimal.coinsstatus;


import appengine.parser.optimal.constants.ExcludeList;
import appengine.parser.optimal.objects.CoinStatus;
import appengine.parser.optimal.objects.Market;
import appengine.parser.utils.DataBaseConnector;
import org.jooq.DSLContext;
import org.jooq.Record5;
import org.jooq.Result;

import java.util.*;

import static appengine.parser.mysqlmodels.Tables.COINSTATUS;
import static appengine.parser.utils.OtherUtils.byteToBool;

public class MiscCoinStatusUtil {

    public String printSameCoinWithDifferentLabels() {

        DSLContext dslContext = DataBaseConnector.getDSLContext();

        Result<Record5<String, String, String, Byte, Byte>> result =
                dslContext.select(COINSTATUS.NAME, COINSTATUS.LABEL, COINSTATUS.MARKET,
                        COINSTATUS.ISWALLETACTIVE, COINSTATUS.ISLISTINGACTIVE).from(COINSTATUS).
                        where(COINSTATUS.NAME.notEqual("")).fetch();

        List<CoinStatus>
                coinStatusList = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            Record5<String, String, String, Byte, Byte> record =
                    result.get(i);
            CoinStatus coinStatus = new CoinStatus(Market.valueOf(record.value3()),
                    record.value1(), record.value2(), byteToBool(record.value4()),
                    byteToBool(record.value5()));

            coinStatusList.add(coinStatus);
        }

        Map<String, List<CoinStatus>> coinNameAndStatus =
                new HashMap<>();


        for (int i = 0; i < coinStatusList.size(); i++) {
            CoinStatus coinStatus = coinStatusList.get(i);
            String coinName =
                    ExcludeList.convertToGenericName(coinStatus.getCoinName());

            if (coinNameAndStatus.containsKey(coinName)) {
                coinNameAndStatus.get(coinName).add(coinStatus);
            } else {
                List<CoinStatus> coinStatuses = new ArrayList<>();
                coinStatuses.add(coinStatus);
                coinNameAndStatus.put(coinName, coinStatuses);
            }
        }

        Set<String> keySet = coinNameAndStatus.keySet();
        Iterator<String> keyIterator = keySet.iterator();

        while (keyIterator.hasNext()) {

            String key = keyIterator.next();

            List<CoinStatus> coinStatuses =
                    coinNameAndStatus.get(key);

            String label = "";

            if (coinStatuses.size() > 1) {

                label = coinStatuses.get(0).getLabel();

                for (int i = 0; i < coinStatuses.size(); i++) {
                    CoinStatus coinStatus = coinStatuses.get(i);

                    if (!label.equalsIgnoreCase(coinStatus.getLabel())) {
                        print(coinStatuses);
                    }
                }
            }
        }


        return printString;
    }

    private String printString = "";

    private void print(List<CoinStatus> coinStatusList) {

        for (CoinStatus coinStatus : coinStatusList) {
            printString += "\n" + coinStatus.getCoinName() + " - "
                    + coinStatus.getLabel() + " - " + coinStatus.getMarket().name();
        }

        printString += "\n-----------\n";

    }

}
