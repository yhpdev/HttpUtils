package com.jun.httpUtils.json;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Created by jun on 16/7/6.
 */
public interface JsonProvider {
    /**
     * Serialize value to json, and write to writer
     */
    public void toJson(Writer writer, Object value);

    /**
     * Deserialize json from reader
     */
    public <T> T fromJson(Reader reader, Type type);

}
