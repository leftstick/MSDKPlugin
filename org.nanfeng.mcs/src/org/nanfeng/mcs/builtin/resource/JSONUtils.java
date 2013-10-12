package org.nanfeng.mcs.builtin.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

/**
 * JSON utilities.
 */
public class JSONUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, true);
    }

    /** Standard view name to be used. */
    public static final String VIEW_NAME = "jsonView";

    /**
     * De-serialize from JSON to a specific type
     * 
     * @param <T> type to be converted to.
     * @param input json string to be converted.
     * @param clazz class
     * @return Object representation of the JSON input
     */
    public static <T> T fromJSON(String input, Class<T> clazz) {
        try {
            return mapper.readValue(input != null ? input : "null", clazz);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * De-serialize from JSON to a specific type
     * 
     * @param <T> type to be converted to.
     * @param input json string to be converted.
     * @param typeRef TypeReference
     * @return Object representation of the JSON input
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJSON(String input, TypeReference<T> typeRef) {
        try {
            return (T) mapper.readValue(input != null ? input : "null", typeRef);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * De-serialize from JSON to a specific type
     * 
     * @param <T> type to be converted to.
     * @param is json reader to be converted.
     * @param clazz class
     * @return Object representation of the JSON input
     */
    public static <T> T fromJSON(InputStream is, Class<T> clazz) {
        try {
            return mapper.readValue(is, clazz);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * De-serialize from JSON to a specific type
     * 
     * @param <T> type to be converted to.
     * @param is json reader to be converted.
     * @param typeRef TypeReference
     * @return Object representation of the JSON input
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromJSON(InputStream is, TypeReference<T> typeRef) {
        try {
            return (T) mapper.readValue(is, typeRef);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serialize to JSON
     * 
     * @param obj instance to be serialize
     * @return JSON representation
     */
    public static String toJSON(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serialize to JSON
     * 
     * @param output JSON formatted data will be written to this
     *            object
     * @param obj The objec to serialize to JSON
     */
    public static void toJSON(Writer output, Object obj) {
        try {
            mapper.writeValue(output, obj);
        } catch (JsonParseException e) {
            throw new RuntimeException(e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
