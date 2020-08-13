package appengine.parser.optimal.livecoinokex.utils;

public class SymbolUtil {

    public String getCoinLowerCase(String symbol) {

        if (symbol.contains("_")) {

            String[] array = symbol.split("_");
            symbol = array[0];
        }
        return symbol.toLowerCase();

    }

    public String getUnderScoreBTCCoin(String coin) {

        return coin.toLowerCase() + "_btc";


    }

    public String getSlashBTCCoin(String coin){
        return coin.toUpperCase()+"/BTC";
    }

    public String getCoin(String coinString) {

        if (coinString.contains("_")) {

            String[] array = coinString.split("_");
            return array[0].toUpperCase();

        }
        if (coinString.contains("/")) {
            String[] array = coinString.split("/");
            return array[0].toUpperCase();
        }

        return coinString.toUpperCase();

    }
}
