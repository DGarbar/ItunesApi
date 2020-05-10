package com.dharbar.template.utils;

import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Method;

public class ShouldWhenUnderscoreNameGenerator extends DisplayNameGenerator.Standard {

    @Override
    public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
        return this.replaceCamelCase(testMethod.getName())
                + DisplayNameGenerator.parameterTypesAsString(testMethod);
    }

    private String replaceCamelCase(String methodName) {
        String[] splitedByUnderscore = methodName.split("_");
        if (splitedByUnderscore.length != 3) {
            return methodName;
        }

        return new StringBuilder(splitedByUnderscore[0])
                .append(" SHOULD ")
                .append(fromCamelCase(splitedByUnderscore[1]))
                .append(" WHEN ")
                .append(fromCamelCase(splitedByUnderscore[2]))
                .toString();
    }

    private StringBuilder fromCamelCase(String camelCase) {
        StringBuilder result = new StringBuilder();
        for (char c : camelCase.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append(' ').append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result;
    }
}
