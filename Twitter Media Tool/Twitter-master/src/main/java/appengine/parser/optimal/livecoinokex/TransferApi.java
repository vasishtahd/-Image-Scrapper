package appengine.parser.optimal.livecoinokex;

import appengine.parser.mysqlmodels.enums.CointransferCurrentstate;
import appengine.parser.optimal.livecoinokex.enums.TransferState;
import appengine.parser.optimal.livecoinokex.utils.Transfer;
import appengine.parser.optimal.livecoinokex.utils.okex.OkexUtil;
import appengine.parser.optimal.objects.Market;
import appengine.parser.utils.DataBaseConnector;
import org.jooq.DSLContext;
import org.jooq.Record12;
import org.jooq.Result;

import java.sql.Timestamp;
import java.util.ArrayList;

import static appengine.parser.mysqlmodels.Tables.COINTRANSFER;

public class TransferApi {


    public void transferOrder() {

        ArrayList<Transfer> transferList = getPurchasedOrders();

        for (int i = 0; i < transferList.size(); i++) {

            Transfer transfer = transferList.get(i);

            if (transfer.buyMarket == Market.OKEX) {

                OkexUtil okexUtil = new OkexUtil();
                okexUtil.makeTransfer(transfer);


            }
        }


    }


    public void sellOrders() {

        ArrayList<Transfer> transferList = getPurchasedOrders();

        for (int i = 0; i < transferList.size(); i++) {
            Transfer transfer = transferList.get(i);

            if (transfer.sellMarket == Market.LIVECOIN) {

            }

        }


    }


    public ArrayList<Transfer> getPurchasedOrders() {
        ArrayList<Transfer> transfersList = new ArrayList<>();

        DSLContext dslContext = DataBaseConnector.getDSLContext();
        Result<Record12<String, Double, Double, Double, String, String, Double, Double, CointransferCurrentstate, Timestamp,
                String, String>> result = dslContext.select(COINTRANSFER.COIN, COINTRANSFER.AMOUNT, COINTRANSFER.PRICETOBESPENTINBTC,
                COINTRANSFER.PROFITESTIMATEDINBTC, COINTRANSFER.BUYMARKET, COINTRANSFER.SELLMARKET,
                COINTRANSFER.MAXBUYPRICE, COINTRANSFER.MINSELLPRICE, COINTRANSFER.CURRENTSTATE,
                COINTRANSFER.UPDATEDTIME, COINTRANSFER.BUYORDERID, COINTRANSFER.SELLORDERID)
                .from(COINTRANSFER)
                .where(COINTRANSFER.CURRENTSTATE.eq(CointransferCurrentstate.PURCHASED)).fetch();

        for (int i = 0; i < result.size(); i++) {
            Record12<String, Double, Double, Double, String, String, Double, Double, CointransferCurrentstate, Timestamp,
                    String, String> record12 = result.get(i);

            Transfer transfer = new Transfer();
            transfer.coin = record12.value1();
            transfer.amount = record12.value2();
            transfer.priceToBeSpentInBTC = record12.value3();
            transfer.profitEstimatedInBTC = record12.value4();
            transfer.buyMarket = Market.valueOf(record12.value5());
            transfer.sellMarket = Market.valueOf(record12.value6());
            transfer.maxBuyPrice = record12.value7();
            transfer.minSellPrice = record12.value8();
            transfer.currentState = TransferState.valueOf(record12.value9().toString());
            transfer.buyorderid = record12.value11();
            transfer.sellorderid = record12.value12();

            transfersList.add(transfer);
        }

        return transfersList;
    }

}
