package bknd.Rnd.PlaywithJSON;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import bknd.Rnd.PlaywithJSON.controllerJSON;
import java.util.*;

@RestController
@RequestMapping("/api")
public class apiJSON {

    @Autowired
    public controllerJSON check;

    @GetMapping(value="/simple" , produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getSimpleArray() {
        check.initialiseSampleData();
        return Arrays.asList("item1", "item2", "item3");
    }

    @GetMapping(value="/nested", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> getNestedObject() {

        check.initialiseNestedData();
        return check.nestedObject;
    }

    @GetMapping(value="/complex", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> getComplexList() {
        if(check.complexList.isEmpty()){
            check.initialiseComplexData();
        }
        return check.complexList;
    }

    @PostMapping(value="/complex/append", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> appendToComplexList(@RequestBody Map<String, Object> newItem) {
        if(check.complexList.isEmpty()) {
            check.initialiseComplexData();
        }
        check.complexList.add(newItem);
        return check.complexList;
    }

    @PostMapping(value="/complex/pop", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> popFromComplexList() {
        if(check.complexList.isEmpty()) {
            check.initialiseComplexData();
        }
        if (check.complexList.isEmpty()) {
            return Map.of("error", "List is empty");
        }
        return check.complexList.remove(check.complexList.size() - 1);
    }

    @PostMapping(value="/nested/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> updateNestedObject(@RequestBody Map<String, Object> updates) {
        check.initialiseNestedData();
        check.nestedObject.putAll(updates);
        return check.nestedObject;
    }

}
