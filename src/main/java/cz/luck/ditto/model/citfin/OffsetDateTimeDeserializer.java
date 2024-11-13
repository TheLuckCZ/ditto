package cz.luck.ditto.model.citfin;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        String dateZoneString = jsonParser.getText();
        if (dateZoneString == null) {
            throw new IOException("OffsetDateTime argument is null.");
        }
        // Add default time component to the date string (setting time to 00:00)
        String dateTimeString = dateZoneString.substring(0, 10) + "T00:00" + dateZoneString.substring(10);

        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return offsetDateTime;
    }

}
