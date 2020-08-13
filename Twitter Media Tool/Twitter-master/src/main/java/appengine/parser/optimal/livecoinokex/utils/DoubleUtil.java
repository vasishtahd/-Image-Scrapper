package appengine.parser.optimal.livecoinokex.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DoubleUtil {

    public String percentageFormatter(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(2);
        return df.format(value);
    }

    public String maxiumDigitsFormatter(Double value, int maxium) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(maxium);
        return df.format(value);
    }

    public String priceFormatter(String svalue) {
        if (svalue.startsWith(".")) {
            svalue = "0" + svalue;
        }
        return svalue;
    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.FLOOR);
        return bd.doubleValue();
    }


    public String priceFormatter(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        String svalue = df.format(value);
        if (svalue.startsWith(".")) {
            svalue = "0" + svalue;
        }
        return svalue;
    }

    public Double percentageOf(Double value , Double percentage){
        return (value/100)*percentage;
    }

}
