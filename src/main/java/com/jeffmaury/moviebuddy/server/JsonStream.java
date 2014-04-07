package com.jeffmaury.moviebuddy.server;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class JsonStream {
  public interface JsonItem {
    int getInt(String name);
    String getString(String name);
    String toJSON();
  }
  
  public interface ItemFactory<T> {
  	public T build(JsonItem item);
  }
  
  private static class MapJsonItem implements JsonItem {
    private Map<String, Object> map;

    private MapJsonItem(Map<String, Object> map) {
      this.map = map;
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public int getInt(String name) {
      return (Integer)map.get(name);
    }

    /**
     * {@inheritedDoc}
     */
    @Override
    public String getString(String name) {
      return (String)map.get(name);
         }

    /**
     * {@inheritedDoc}
     */
    @Override
    public String toJSON() {
      StringBuilder builder = new StringBuilder();
      builder.append('{');
      for(String key: map.keySet()) {
        Object value = map.get(key);
        builder.append('"').append(key).append("\":");
        if (value instanceof String) {
          builder.append('"').append(value).append('"');
        } else {
          builder.append(value);
        }
        builder.append(',');
      }
      return builder.append('}').toString();
    }
  }
  
  public static <T> List<T> asStream(InputStream stream, ItemFactory<T> factory) throws IOException {
    LinkedHashMap<String, Object> slotMap = new LinkedHashMap<String, Object>();
    List<T> results = new ArrayList<T>();
    JsonFactory jfactory = new JsonFactory();
    JsonParser parser = jfactory.createParser(stream);
    parser.nextToken();
    JsonItem item = null;
    
    while (parser.hasCurrentToken()) {
    	JsonToken token = parser.getCurrentToken();
    	switch (token) {
    	case START_OBJECT:
    		slotMap = new LinkedHashMap<String, Object>();
    		item = new MapJsonItem(slotMap);
    		break;
    	case END_OBJECT:
    		results.add(factory.build(item));
    		break;
    	case FIELD_NAME:
    		String name = parser.getCurrentName();
    		switch (parser.nextToken()) {
    		case VALUE_STRING:
					slotMap.put(name, parser.getValueAsString());
					break;
				case VALUE_NUMBER_INT:
					slotMap.put(name, parser.getValueAsInt());
					break;
					default:
    		}
    		break;
    		default:
    	}
    	parser.nextToken();
    }
    return results;
  }
  
}
