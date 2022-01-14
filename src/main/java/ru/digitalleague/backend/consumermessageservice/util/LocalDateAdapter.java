package ru.digitalleague.backend.consumermessageservice.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class LocalDateAdapter implements JsonSerializer<LocalDate>,JsonDeserializer<LocalDate>{
    public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)); // "yyyy-mm-dd"
    }
    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        Instant instant = Instant.now();
        return LocalDate.ofInstant(instant, ZoneId.systemDefault());
    }
}
