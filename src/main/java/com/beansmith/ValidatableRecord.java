package com.beansmith;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
public class ValidatableRecord {

    private final UUID id = UUID.randomUUID();
    private final LinkedHashMap<String, Object> data = new LinkedHashMap<>();

    public ValidatableRecord() {
        super();
    }

    public Object get(String key) {
        return data.getOrDefault(key, "");
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }


}
