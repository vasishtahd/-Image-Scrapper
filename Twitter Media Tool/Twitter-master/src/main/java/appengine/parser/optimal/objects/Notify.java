package appengine.parser.optimal.objects;

import com.google.gson.Gson;

import java.sql.Timestamp;
import java.text.DecimalFormat;

/**
 * Created by anand.kurapati on 22/01/18.
 */
public class Notify {
    public String coinlabel;
    public Timestamp timestamp;
    public Market frommarket;
    public Market tomarket;
    public NotifyType notifyType;
    public Double profit;
    public Double buyprice;
    public Double sellprice;


    public Notify(String coinlabel, Timestamp timestamp, Double profit, String frommarket, Double buyprice, String tomarket, Double sellprice,
                  NotifyType notifyType) {

        this.coinlabel = coinlabel;
        this.timestamp = timestamp;
        this.profit = profit;
        this.frommarket = Market.valueOf(frommarket);
        this.tomarket = Market.valueOf(tomarket);
        this.notifyType = notifyType;
        this.buyprice = buyprice;
        this.sellprice = sellprice;

    }

    public Notify(String coinlabel, Timestamp timestamp, Double profit, Market frommarket, Double buyprice, Market tomarket,
                  Double sellprice, NotifyType notifyType) {

        this.coinlabel = coinlabel;
        this.timestamp = timestamp;
        this.profit = profit;
        this.frommarket = frommarket;
        this.tomarket = tomarket;
        this.notifyType = notifyType;
        this.buyprice = buyprice;
        this.sellprice = sellprice;
    }

    public void setNotifyType(NotifyType notifyType) {
        this.notifyType = notifyType;
    }

    @Override
    public String toString() {
        String message = "";
        message = coinlabel + " Profit :" + profit + " " + notifyType + "\n" +
                "Buy From : " + frommarket + " Sell At : " + tomarket + "\n" +
                "Buy : " + priceFormatter(buyprice) + " Sell : " + priceFormatter(sellprice) + "\n\n";
        return message;

    }

    public String toJSON() {
        return new Gson().toJson(this, Notify.class);
    }

    private String priceFormatter(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        return df.format(value);
    }
}
