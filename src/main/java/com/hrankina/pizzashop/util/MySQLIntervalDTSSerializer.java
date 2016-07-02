package com.hrankina.pizzashop.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
public class MySQLIntervalDTSSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(MySQLIntervalWrapper.getTimeFromInterval(s));
    }

}
