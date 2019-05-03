package com.project.baseapp.common

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang.StringUtils

class ApplicationUtil {

  private static ObjectMapper objectMapper

  static {
    objectMapper = createObjectMapper()
  }


  static serialize(Object input) {
    String json = ""
    if (input != null) {
      json = objectMapper.writeValueAsString(input)
    }
    return json
  }

  static <T> T deserialize(String json, Class<T> type) {
    T deSerializedValue = null
    if (StringUtils.isNotBlank(json)) {
      deSerializedValue = objectMapper.readValue(json, type)
    }
    return deSerializedValue
  }

  static clone(Object input) {
    String ser = serialize(input)
    return deserialize(ser, input.class)
  }

  private static ObjectMapper createObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper()
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
    objectMapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
    return objectMapper
  }
}
