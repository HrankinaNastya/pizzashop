package com.hrankina.pizzashop.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * creation date 25.06.2016
 *
 * @author A.Hrankina
 */
public class DateTimeJSDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try {
            DateFormat dateFormat;
            String parseText = jsonParser.getText();
            if (parseText.toUpperCase().endsWith("Z")) {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
            } else {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            }
            return dateFormat.parse(jsonParser.getText());
        } catch (ParseException e) {
            return null;
        }
    }

}
