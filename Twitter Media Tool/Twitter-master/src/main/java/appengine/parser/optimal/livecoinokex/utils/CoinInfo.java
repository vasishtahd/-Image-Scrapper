package appengine.parser.optimal.livecoinokex.utils;

import org.json.JSONObject;

public class CoinInfo {

    public String name;
    public String coin;
    public String walletStatus;
    public Double withdrawFee;
    public Double minimumDepositAmount;
    public Double minimumWithdrawAmount;
    public Double minimumOrderAmount;


    public static CoinInfo fromJson(JSONObject jsonObject) {
        CoinInfo coinInfo = new CoinInfo();
        coinInfo.name = jsonObject.getString("name");
        coinInfo.coin = jsonObject.getString("symbol").toUpperCase();
        coinInfo.walletStatus = jsonObject.getString("walletStatus");
        coinInfo.withdrawFee = jsonObject.getDouble("withdrawFee");
        coinInfo.minimumDepositAmount = jsonObject.getDouble("minDepositAmount");
        coinInfo.minimumWithdrawAmount = jsonObject.getDouble("minWithdrawAmount");
        coinInfo.minimumOrderAmount = jsonObject.getDouble("minOrderAmount");
        return coinInfo;
    }


}
