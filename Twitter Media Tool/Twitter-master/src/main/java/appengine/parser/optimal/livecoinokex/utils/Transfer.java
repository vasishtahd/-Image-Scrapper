package appengine.parser.optimal.livecoinokex.utils;

import appengine.parser.mysqlmodels.enums.CointransferCurrentstate;
import appengine.parser.optimal.livecoinokex.enums.TransferState;
import appengine.parser.optimal.objects.Market;
import appengine.parser.optimal.objects.ResultOfCalculation;
import appengine.parser.utils.DataBaseConnector;
import com.google.gson.Gson;
import org.jooq.DSLContext;

import java.sql.Timestamp;

import static appengine.parser.mysqlmodels.Tables.COINTRANSFER;

public class Transfer implements Comparable<Transfer> {

    public String coin;
    public Double amount;
    public Double priceToBeSpentInBTC;
    public Double profitEstimatedInBTC;
    public Market buyMarket;
    public Market sellMarket;
    public String buyorderid;
    public String sellorderid = "";
    public Double maxBuyPrice;
    public Double minSellPrice;
    public TransferState currentState;
    public Timestamp updatedtime;

    @Override
    public String toString() {
        if (coin != null) {
            return "Coin " + coin + " Amount:" + new DoubleUtil().priceFormatter(amount) + coin + " Investment:" + new DoubleUtil().priceFormatter(priceToBeSpentInBTC) + " BTC" +
                    " Profit:" + new DoubleUtil().priceFormatter(profitEstimatedInBTC) + " BTC" + " BuyMarket:" + buyMarket.name() + " SellMarket:" + sellMarket.name() +
                    " MaxBuyPrice:" + new DoubleUtil().priceFormatter(maxBuyPrice) + "BTC" + " MinSellPrice:" + new DoubleUtil().priceFormatter(minSellPrice) + "BTC"
                    + " BuyOrderid: " + buyorderid + " SellorderId: " + sellorderid + "\n CurrentState:" + currentState.name() + " UpdatedTime:" + updatedtime;
        }
        return "";
    }

    public String toJSON() {
        return new Gson().toJson(this, Transfer.class);
    }


    @Override
    public int compareTo(Transfer comparableTransfer) {


        if (priceToBeSpentInBTC - comparableTransfer.priceToBeSpentInBTC > 0.2) {
            return -1;
        }

        if (comparableTransfer.priceToBeSpentInBTC - priceToBeSpentInBTC > 0.2) {
            return 1;
        }

        if ((profitEstimatedInBTC / priceToBeSpentInBTC) - (comparableTransfer.profitEstimatedInBTC / comparableTransfer.priceToBeSpentInBTC) > 0) {
            return -1;
        }

        if ((comparableTransfer.profitEstimatedInBTC / comparableTransfer.priceToBeSpentInBTC) - (profitEstimatedInBTC / priceToBeSpentInBTC) > 0) {
            return 1;
        }

        return 0;

    }

    public void insertInDB() {
        DSLContext dslContext = DataBaseConnector.getDSLContext();
        dslContext.insertInto(COINTRANSFER, COINTRANSFER.COIN, COINTRANSFER.AMOUNT, COINTRANSFER.PRICETOBESPENTINBTC,
                COINTRANSFER.PROFITESTIMATEDINBTC, COINTRANSFER.BUYMARKET, COINTRANSFER.SELLMARKET, COINTRANSFER.MAXBUYPRICE,
                COINTRANSFER.MINSELLPRICE, COINTRANSFER.CURRENTSTATE)
                .values(coin, amount, priceToBeSpentInBTC, profitEstimatedInBTC, buyMarket.name(), sellMarket.name(),
                        maxBuyPrice, minSellPrice, CointransferCurrentstate.valueOf(currentState.name()))
                .execute();

    }
}
