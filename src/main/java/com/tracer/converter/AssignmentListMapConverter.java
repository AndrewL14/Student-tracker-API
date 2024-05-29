package com.tracer.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.tracer.model.assignments.AssignmentList;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Map;

@Converter
public class AssignmentListConverter implements AttributeConverter<Map<Integer, AssignmentList>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<Integer, AssignmentList> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting Map<Integer, AssignmentList> to JSON", e);
        }
    }

    @Override
    public Map<Integer, AssignmentList> convertToEntityAttribute(String dbData) {
        try {
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            return objectMapper.readValue(dbData, typeFactory.constructMapType(Map.class, Integer.class, AssignmentList.class));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to Map<Integer, AssignmentList>", e);
        }
    }
}