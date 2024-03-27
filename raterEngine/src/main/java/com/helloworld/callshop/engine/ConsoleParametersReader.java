package com.helloworld.callshop.engine;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.helloworld.callshop.rater.rate.factory.ParametersReader;
import com.helloworld.callshop.rater.rate.factory.Parameter;
import com.helloworld.callshop.rater.rate.factory.ParametersMapper;

import java.util.List;
import java.util.Scanner;

public class ConsoleParametersReader implements ParametersReader {



    @Override
    public ParametersMapper readParameters(List<Parameter> parameters) {

        ParametersMapperImpl paramsMapper = new ParametersMapperImpl();

        Scanner consoleInput = new Scanner(System.in);

        for (Parameter parameter: parameters) {
            System.out.print(parameter.getDescription() + ":");
            String parameterValue = consoleInput.nextLine();
            while (!parameter.getValidator().test(parameterValue)) {
                System.out.print("El valor introducido es incorrecto. Repita por favor" + ":");
                parameterValue = consoleInput.nextLine();
            }
            paramsMapper.put(parameter.getName(), parameterValue);
        }

        /*
        ParametersMapperImpl paramsMapper = new ParametersMapperImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        for (Parameter parameter: parameters) {
            String parameterValue = rootNode.get(parameter.getName()).asText();
            while (!parameter.getValidator().test(parameterValue)) {
                // You may want to handle invalid input within the JSON differently,
                // such as throwing an exception or logging a message.
                // For simplicity, this example prompts the user to enter valid input.
                System.out.print("Invalid value for " + parameter.getDescription() + ". Please enter a valid value:");
                parameterValue = consoleInput.nextLine();
            }
            paramsMapper.put(parameter.getName(), parameterValue);
        }
        */

        return paramsMapper;
    }


}
