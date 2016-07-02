package com.hrankina.pizzashop.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
public class MySQLIntervalDTSDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        return MySQLIntervalWrapper.getDayToSecondInterval(jsonParser.getText());
    }

}