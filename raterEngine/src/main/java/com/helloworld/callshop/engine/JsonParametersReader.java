package com.helloworld.callshop.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworld.callshop.rater.rate.factory.Parameter;
import com.helloworld.callshop.rater.rate.factory.ParametersMapper;
import com.helloworld.callshop.rater.rate.factory.ParametersReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonParametersReader implements ParametersReader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ParametersMapper readParameters(List<Parameter> parameters) {
        ParametersMapperImpl paramsMapper = new ParametersMapperImpl();

        try {
            File jsonFile = new File("parameters.json");
            JsonNode rootNode = objectMapper.readTree(jsonFile);

            for (Parameter parameter : parameters) {
                String paramName = parameter.getName();
                if (rootNode.has(paramName)) {
                    String paramValue = rootNode.get(paramName).asText();
                    paramsMapper.put(paramName, paramValue);
                } else {
                    throw new IllegalArgumentException("El archivo JSON no contiene el parámetro: " + paramName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return paramsMapper;
    }
}
