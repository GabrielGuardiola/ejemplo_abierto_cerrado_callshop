package com.helloworld.callshop.rate.hourfixprice;

import com.helloworld.callshop.rater.rate.Rate;
import com.helloworld.callshop.rater.rate.factory.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import java.util.List;
import java.util.function.Predicate;

public class HourFixPriceRateFactory implements RateFactory {

    private static final String DESCRIPTION = "Tarifa de precio fijo (establecimiento y precio por hora)";

    private static final String ID = "HOUR";
    private static final String CONNECT_FEE_NAME = "connectFee";
    private static final String HOUR_PRICE_NAME = "hourPrice";


    private final Predicate<Object> priceParameterValidator = parameter -> {
        try {
            BigDecimal value = parseBigDecimalParameter(parameter);
            return isValidPriceParameter(value);
        } catch (InvalidParameterValueException | NullPointerException e) {
            return false;
        }
    };

    private final Parameter connectParam = new Parameter(CONNECT_FEE_NAME, "Establecimiento de llamada", priceParameterValidator);
    private final Parameter hourPriceParam = new Parameter(HOUR_PRICE_NAME, "Precio por hora", priceParameterValidator);


    @Override
    public Rate makeRate(ParametersReader parametersReader) throws InvalidParameterValueException {

        List<Parameter> parameters = getBasicParameterList();
        parameters.addAll(Arrays.asList(connectParam, hourPriceParam));

        ParametersMapper parametersMapper = parametersReader.readParameters(parameters);

        BigDecimal connectFee = parseBigDecimalParameter(parametersMapper.getValue(CONNECT_FEE_NAME));
        BigDecimal minutePrice = parseBigDecimalParameter(parametersMapper.getValue(HOUR_PRICE_NAME));

        if (!isValidPriceParameter(connectFee)) {
            throw new InvalidParameterValueException("Establecimiento no válido");
        }

        if (!isValidPriceParameter(minutePrice)) {
            throw new InvalidParameterValueException("Precio por hora no válido");
        }

        Object name = parametersMapper.getValue(RATE_NAME_NAME);

        if (!getNameValidator().test(name)) {
            throw new InvalidParameterValueException("Nombre no válido");
        }

        return new HourFixPriceRate((String)name, connectFee, minutePrice);
    }

    private BigDecimal parseBigDecimalParameter(Object parameter) {

        if (parameter == null) {
            throw new NullPointerException("Valor del parametro a null");
        }
        if (parameter instanceof BigDecimal value) {
            return value;
        }
        if (parameter instanceof Double value) {
            return BigDecimal.valueOf(value);
        }
        if (parameter instanceof Long value) {
            return BigDecimal.valueOf(value);
        }
        if (parameter instanceof Integer value) {
            return new BigDecimal(value);
        }
        if (parameter instanceof BigInteger value) {
            return new BigDecimal(value);
        }
        if (parameter instanceof String value) {
            try {
                return new BigDecimal(value);
            } catch (NumberFormatException e) {
                throw new InvalidParameterValueException("Número decimal no válido", e);
            }
        }


        throw new InvalidParameterValueException("Número decimal no válido");
    }

    private boolean isValidPriceParameter(BigDecimal price) {
        return price.compareTo(BigDecimal.ZERO) >= 0;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    public String getId() {
        return ID;
    }


}
