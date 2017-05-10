package com.crest.driver.ModelClasses;

/**
 * Created by annie on 27/4/17.
 */

public class GetExtraAmountModel {

    public String getAmountType() {
        return AmountType;
    }

    public void setAmountType(String amountType) {
        AmountType = amountType;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    String AmountType;

    String Amount;
}
