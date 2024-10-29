package com.service.runnersmap.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.service.runnersmap.entity.Path;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Converter
public class PathListConverter implements AttributeConverter<List<Path>, String> {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<Path> attribute) {
    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("convert error from list to db column", e);
    }
  }

  @Override
  public List<Path> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return new ArrayList<>();
    }

    try {
      return objectMapper.readValue(dbData, objectMapper.getTypeFactory().constructCollectionType(List.class, Path.class));
    } catch (IOException e) {
      throw new RuntimeException("convert error from db column to list", e);
    }
  }
}