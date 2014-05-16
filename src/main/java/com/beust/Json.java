package com.beust;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class Json {

    public static void main(String[] args) throws Exception {
        new Json().run();
    }

    private Optional<String> extractCityName(JSONObject j) {
        return new Jo(j).getJSONObject("location")
                .flatMap((o) -> new Jo(o).getJSONObject("city"))
                .flatMap((o) -> new Jo(o).getString("name"));
    }

    private void run() throws JsonParseException, JsonMappingException, IOException {
        List<JSONObject> m = (List<JSONObject>) JSONValue.parse(new FileReader("/tmp/a.json"));

        List<String> cityNames = Jo.extractFromList(m, j -> extractCityName(j));
//        Function<? super JSONObject, ? extends Optional<String>> lambda = j -> extractCityName(j);
//        List<String> r = m.stream()
//                .map(lambda)
//                .filter(Optional::isPresent)
//                .map(Optional::get)
//                .collect(Collectors.toList());
        System.out.println("Result: " + cityNames);
//        for (Object j : m) {
//            Jo jo = new Jo((JSONObject) j);
//            Optional<?> result = jo.getJSONObject("location")
//                    .flatMap((o) -> new Jo(o).getJSONObject("city"))
//                    .flatMap((o) -> new Jo(o).getString("name"));
//            System.out.println("Result: " + result);
//        }
//        JSONObject l = (JSONObject) m.get("location");
//        if (l != null) {
//            JSONObject city = (JSONObject) l.get("city");
//            if (city != null) {
//                String name = (String) city.get("name");
//                System.out.println("City name: " + name);
//            }
//        }
    }
}
