package com.beust;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.json.simple.JSONObject;

public class Jo {

    private final JSONObject o;

    public Jo(JSONObject o) {
        this.o = o;
    }

    public Optional<String> getString(String key) {
        Object v = o.get(key);
        if (v != null && v instanceof String) {
            return Optional.of((String) v);
        } else {
            return Optional.<String>empty();
        }
    }

    public Optional<JSONObject> getJSONObject(String key) {
        Object v = o.get(key);
        if (v != null && v instanceof JSONObject) {
            return Optional.of((JSONObject) v);
        } else {
            return Optional.<JSONObject>empty();
        }
    }

    public static <From, To> List<To> extractFromList(List<From> objects,
            Function<? super From, ? extends Optional<To>> lambda) {
//        List<To> result = objects.stream()
//                .map(lambda)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toList());
        return null;
    }
}
