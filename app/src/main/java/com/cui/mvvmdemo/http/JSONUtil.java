package com.cui.mvvmdemo.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by cuix on 2016/8/16.
 */
public final class JSONUtil {

    public static Gson gson = null;

    static {

       gson = new GsonBuilder().registerTypeAdapter(Integer.class,new IntegerTypeAdapter())
               .registerTypeAdapter(Double.class,new DoubleTypeAdapter())
               .registerTypeAdapter(Float.class,new FloatTypeAdapter())
               .registerTypeAdapter(Long.class,new LongTypeAdapter())
               .create();

    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        return gson.fromJson(text,clazz);
    }

    public static <T> T parseObject(String text, Type type) {
        return gson.fromJson(text,type);
    }

    public static String toJSONString(Object object) {
        return gson.toJson(object);
    }

    public static class DoubleTypeAdapter extends TypeAdapter<Number> {
        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number);
        }

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }

            String value = jsonReader.nextString();
            try{
                return Double.parseDouble(value);
            }catch (Exception e){
                return null;
            }
        }
    }

    public static class IntegerTypeAdapter extends TypeAdapter<Number> {
        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number);
        }

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }

            String value = jsonReader.nextString();
            try{
                return Integer.parseInt(value);
            }catch (Exception e){
                return null;
            }
        }
    }

    public static class FloatTypeAdapter extends TypeAdapter<Number> {
        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number);
        }

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }

            String value = jsonReader.nextString();
            try{
                return Float.parseFloat(value);
            }catch (Exception e){
                return null;
            }
        }
    }

    public static class LongTypeAdapter extends TypeAdapter<Number> {
        @Override
        public void write(JsonWriter jsonWriter, Number number) throws IOException {
            if (number == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(number);
        }

        @Override
        public Number read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }

            String value = jsonReader.nextString();
            try{
                return Long.parseLong(value);
            }catch (Exception e){
                return null;
            }
        }
    }


}
