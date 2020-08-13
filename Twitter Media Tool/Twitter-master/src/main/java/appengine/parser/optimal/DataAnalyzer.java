package appengine.parser.optimal;

import appengine.parser.mysqlmodels.enums.OptimalnotifyNotifytype;
import appengine.parser.mysqlmodels.enums.OptimalupdateOperation;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.Notify;
import appengine.parser.optimal.objects.NotifyType;
import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.utils.DataBaseConnector;
import com.google.gson.Gson;
import org.jooq.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static appengine.parser.mysqlmodels.Tables.*;

/**
 * Created by anand.kurapati on 15/01/18.
 */
public class DataAnalyzer {

    String printString = "";

    public String coinAnalyzer(String[] coins, boolean isJson) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        for (String coin : coins) {
            coin = coin.toUpperCase();
            Result<Record2<String, Timestamp>> result = dslContext.select(OPTIMALJSON.JSON, OPTIMALJSON.TIME).from(OPTIMALJSON).
                    where(OPTIMALJSON.COINLABEL.eq(coin)).fetch();

            for (int i = 0; i < result.size(); i++) {
                ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
                resultOfCalculation.setTimestamp(result.get(i).value2());
                if (isJson) {
                    print(resultOfCalculation.toJSON(), isJson);
                } else {
                    print(resultOfCalculation.toString(), isJson);
                }
            }
        }
        return resultString(isJson);
    }

    public String getLastUpdatedTime() {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<Timestamp> updatedTimeRecord =
                dslContext.select(OPTIMALUPDATE.UPDATEDTIME).from(OPTIMALUPDATE)
                        .where(OPTIMALUPDATE.OPERATION.eq(OptimalupdateOperation.COINCALCULATOR)).fetchOne();

        return updatedTimeRecord.value1().toString();
    }

    public String getDataFromLastUpdateWithMinPercentage(boolean isJson,
                                                         int minimumPercentage) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<Timestamp> updatedTimeRecord =
                dslContext.select(OPTIMALUPDATE.UPDATEDTIME).from(OPTIMALUPDATE)
                        .where(OPTIMALUPDATE.OPERATION.eq(OptimalupdateOperation.COINCALCULATOR)).fetchOne();

        return getDataFromTime(updatedTimeRecord.value1(), isJson, minimumPercentage);
    }

    public List<ResultOfCalculation> getDataFromLastUpdateWithMinPercentage(int minimumPercentage) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<Timestamp> updatedTimeRecord =
                dslContext.select(OPTIMALUPDATE.UPDATEDTIME).from(OPTIMALUPDATE)
                        .where(OPTIMALUPDATE.OPERATION.eq(OptimalupdateOperation.COINCALCULATOR)).fetchOne();

        return getDataFromTime(updatedTimeRecord.value1(), minimumPercentage);
    }

    public String getDataFromLastUpdate(boolean isJson) {

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<Timestamp> updatedTimeRecord =
                dslContext.select(OPTIMALUPDATE.UPDATEDTIME).from(OPTIMALUPDATE)
                        .where(OPTIMALUPDATE.OPERATION.eq(OptimalupdateOperation.COINCALCULATOR)).fetchOne();

        return getDataFromTime(updatedTimeRecord.value1(), isJson);
    }

    public String getDataFromTime(Timestamp timestamp, boolean isJson) {
        return getDataFromTime(timestamp, isJson, Integer.MIN_VALUE);
    }


    public String getDataFromTime(Timestamp timeStamp, boolean isJson, int minPercentageProfit) {
        List<ResultOfCalculation> resultOfCalculations = getDataFromTime(timeStamp, minPercentageProfit);
        for (int i = 0; i < resultOfCalculations.size(); i++) {
            ResultOfCalculation resultOfCalculation = resultOfCalculations.get(i);
            if (isJson) {
                print(resultOfCalculation.toJSON(), isJson);
            } else {
                print(resultOfCalculation.toString(), isJson);
            }

        }
        return resultString(isJson);
    }

    public List<ResultOfCalculation> getDataFromTime(Timestamp timeStamp, int minPercentageProfit) {
        List<ResultOfCalculation> resultOfCalculations = new ArrayList<>();
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record2<String, Timestamp>> result = dslContext.select(OPTIMALJSON.JSON, OPTIMALJSON.TIME).from(OPTIMALJSON).
                where(OPTIMALJSON.TIME.greaterOrEqual(timeStamp)).and(OPTIMALJSON.COINLABEL.isNotNull()).fetch();
        for (int i = 0; i < result.size(); i++) {
            ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
            resultOfCalculation.setTimestamp(result.get(i).value2());
            if (resultOfCalculation.profitPercentage() > minPercentageProfit) {
                resultOfCalculations.add(resultOfCalculation);
            }
        }
        return resultOfCalculations;
    }

    public ArrayList<ResultOfCalculation> getDataFromLastUpdate() {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Record1<Timestamp> updatedTimeRecord =
                dslContext.select(OPTIMALUPDATE.UPDATEDTIME).from(OPTIMALUPDATE)
                        .where(OPTIMALUPDATE.OPERATION.eq(OptimalupdateOperation.COINCALCULATOR)).fetchOne();
        return getDataFromTime(updatedTimeRecord.value1());
    }

    public ArrayList<ResultOfCalculation> getDataFromTime(Timestamp timeStamp) {

        ArrayList<ResultOfCalculation> resultOfCalculations = new
                ArrayList<>();

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record2<String, Timestamp>> result = dslContext.select(OPTIMALJSON.JSON, OPTIMALJSON.TIME).from(OPTIMALJSON).
                where(OPTIMALJSON.TIME.greaterOrEqual(timeStamp)).and(OPTIMALJSON.COINLABEL.isNotNull()).fetch();
        for (int i = 0; i < result.size(); i++) {
            ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
            resultOfCalculation.setTimestamp(result.get(i).value2());
            resultOfCalculations.add(resultOfCalculation);
        }
        return resultOfCalculations;
    }

    public String getDataFromTimeAndCoin(Timestamp timeStamp, String[] coins, boolean isJson) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        for (String coin : coins) {
            coin = coin.toUpperCase();
            Result<Record2<String, Timestamp>> result = dslContext.select(OPTIMALJSON.JSON, OPTIMALJSON.TIME).from(OPTIMALJSON).
                    where(OPTIMALJSON.TIME.greaterThan(timeStamp)).and(OPTIMALJSON.COINLABEL.eq(coin)).fetch();
            for (int i = 0; i < result.size(); i++) {
                ResultOfCalculation resultOfCalculation = new Gson().fromJson(result.get(i).value1(), ResultOfCalculation.class);
                resultOfCalculation.setTimestamp(result.get(i).value2());
                if (isJson) {
                    print(resultOfCalculation.toJSON(), isJson);
                } else {
                    print(resultOfCalculation.toString(), isJson);
                }
            }
        }
        return resultString(isJson);
    }

    public ArrayList<Notify> getDataFromNotify(String coinlabel, Market firstmarket, Market secondmarket) {

        ArrayList<Notify> notifyArrayList = new ArrayList<>();

        DSLContext dslContext = DataBaseConnector.getDSLContext();


        for (int j = 0; j < 2; j++) {

            Market FIRSTMARKET = firstmarket;
            Market SECONDMARKET = secondmarket;

            if (j == 1) {
                FIRSTMARKET = secondmarket;
                SECONDMARKET = firstmarket;
            }

            Result<Record8<String, Timestamp, Double, String, Double, String, Double, OptimalnotifyNotifytype>> result =
                    dslContext.select(OPTIMALNOTIFY.COINLABEL, OPTIMALNOTIFY.TIME, OPTIMALNOTIFY.PROFIT,
                            OPTIMALNOTIFY.FROMMARKET, OPTIMALNOTIFY.BUYPRICE, OPTIMALNOTIFY.TOMARKET,
                            OPTIMALNOTIFY.SELLPRICE, OPTIMALNOTIFY.NOTIFYTYPE).from(OPTIMALNOTIFY).
                            where(OPTIMALNOTIFY.COINLABEL.eq(coinlabel).and(OPTIMALNOTIFY.FROMMARKET.eq(FIRSTMARKET.name())).
                                    and(OPTIMALNOTIFY.TOMARKET.eq(SECONDMARKET.name()))).fetch();

            for (int i = 0; i < result.size(); i++) {
                Record8<String, Timestamp, Double, String, Double, String, Double, OptimalnotifyNotifytype> record = result.get(i);
                Notify notify = new Notify(record.value1(), record.value2(), record.value3(), record.value4(), record.value5(),
                        record.value6(), record.value7(), NotifyType.valueOf(record.value8().toString()));

                notifyArrayList.add(notify);
            }

        }

        notifyArrayList.sort(new Comparator<Notify>() {
            @Override
            public int compare(Notify o1, Notify o2) {
                return o1.timestamp.compareTo(o2.timestamp);
            }
        });

        return notifyArrayList;
    }

    public Notify getDataFromLastNotify(Notify notify) {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record8<String, Timestamp, Double, String, Double, String, Double, OptimalnotifyNotifytype>> result =
                dslContext.select(OPTIMALNOTIFY.COINLABEL, OPTIMALNOTIFY.TIME, OPTIMALNOTIFY.PROFIT,
                        OPTIMALNOTIFY.FROMMARKET, OPTIMALNOTIFY.BUYPRICE, OPTIMALNOTIFY.TOMARKET,
                        OPTIMALNOTIFY.SELLPRICE, OPTIMALNOTIFY.NOTIFYTYPE).from(OPTIMALNOTIFY).
                        where(OPTIMALNOTIFY.COINLABEL.eq(notify.coinlabel).and(OPTIMALNOTIFY.FROMMARKET.eq(notify.frommarket.name())).
                                and(OPTIMALNOTIFY.TOMARKET.eq(notify.tomarket.name()))).fetch();

        if (result.size() < 1) {
            //should not occur here
            return null;
        }

        Record8<String, Timestamp, Double, String, Double, String, Double, OptimalnotifyNotifytype> record = result.get(result.size() - 1);
        Notify oldnotify = new Notify(record.value1(), record.value2(), record.value3(), record.value4(), record.value5(),
                record.value6(), record.value7(), NotifyType.valueOf(record.value8().toString()));

        return oldnotify;


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
