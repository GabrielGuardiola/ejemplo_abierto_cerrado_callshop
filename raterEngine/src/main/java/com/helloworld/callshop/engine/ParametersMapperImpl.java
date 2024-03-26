package com.helloworld.callshop.engine;

import com.helloworld.callshop.rater.rate.factory.ParametersMapper;

import java.util.HashMap;

public class ParametersMapperImpl extends HashMap<String, String> implements ParametersMapper {
    @Override
    public Object getValue(String name) {
        return get(name);
    }
}
