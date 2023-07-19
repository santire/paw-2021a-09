package ar.edu.itba.paw.webapp.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

import java.io.IOException;

public class JSONUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T jsonToObject(final String json, final Class<T> classToConvertTo) {
        try {
            return OBJECT_MAPPER.readValue(jsonObjectFrom(json).toString(), classToConvertTo);
        } catch (IOException e) {
            LOGGER.error("An Error occurred when trying to convert JSON to " + classToConvertTo.getName() + " object", e);
//            throw ApiException.of(HttpStatus.INTERNAL_SERVER_ERROR, "We are sorry, an unexpected error occurred, try again later" /*MessageConstants.SERVER_ERROR_GENERIC_MESSAGE*/);
            throw new RuntimeException("Unexpected server error");
        }
    }

    public static JSONObject jsonObjectFrom(final String jsonString) {
        return new JSONObject(jsonString);
    }
}
