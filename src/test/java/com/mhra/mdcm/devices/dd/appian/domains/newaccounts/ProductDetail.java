package com.mhra.mdcm.devices.dd.appian.domains.newaccounts;

/**
 * Created by TPD_Auto on 21/12/2016.
 */
public class ProductDetail {
    public String name;
    public String make;
    public String model;
    public String manCode;

    public ProductDetail(String detail){
        String[] data = detail.split(",");
        for(String dataPair: data){
            String[] dp = dataPair.split(":");
            setProductDetails(dp);
        }
    }



    private void setProductDetails(String[] dp) {
        String key = dp[0];
        String value = dp[1];

        if(key.equals("name")){
            name = value;
        }else if(key.equals("make")){
            make = value;
        }else if(key.equals("model")){
            model = value;
        }else if(key.equals("manCode")){
            manCode = value;
        }
    }

}
