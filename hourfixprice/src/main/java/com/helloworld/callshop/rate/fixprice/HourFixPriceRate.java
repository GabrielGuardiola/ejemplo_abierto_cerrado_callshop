package com.helloworld.callshop.rate.fixprice;

import com.helloworld.callshop.rater.rate.AbstractRate;
import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.RateableCall;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HourFixPriceRate extends AbstractRate implements Rate {

    private final static BigDecimal  SECONDS_PER_HOUR = new BigDecimal(3600);

    private final BigDecimal connectFee;
    private final BigDecimal hourPrice;

    public HourFixPriceRate(String name, BigDecimal connectFee, BigDecimal hourPrice) {
        super(name);
        this.connectFee = connectFee;
        this.hourPrice = hourPrice;
    }

    @Override
    public BigDecimal calculatePrice(RateableCall rateableCall) {
        BigDecimal duration = new BigDecimal(rateableCall.getDuration()); //duration in seconds
		return duration.divide(SECONDS_PER_HOUR, 6, RoundingMode.HALF_EVEN).multiply(hourPrice).add(connectFee);
    }

    @Override
    public String toString() {
        return "HourFixPriceRate{" +
                "name=" + getName() +
                ", connectFee=" + connectFee +
                ", hourPrice=" + hourPrice +
                '}';
    }
}
