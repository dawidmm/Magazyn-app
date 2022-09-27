package pl.ekookna.magazynapp.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter(autoApply = true)
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        String string;
        if (stringList != null) {
            try {
                string = objectMapper.writeValueAsString(stringList);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            string = null;
        }
        return string;
    }

    @Override
    public List<String> convertToEntityAttribute(String string) {
        List<String> stringList;
        if (string != null) {
            try {
                stringList = objectMapper.readValue(string, new TypeReference<List<String>>() {
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            stringList = null;
        }
        return stringList;
    }
}
