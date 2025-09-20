package bknd.Rnd.PlaywithJSON;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class controllerJSON {

    public List<Map<String,Object>> complexList = new ArrayList<>();
    public Map<String, Object> nestedObject = new HashMap<>();
    public List<String> simpleArray;

    public void initialiseSampleData(){
        simpleArray = Arrays.asList("item11", "item22", "item33");
    }

    public void initialiseNestedData() {

        Map<String, Object> nested = new HashMap<>();
        nested.putIfAbsent("oneNested", Arrays.asList("N1", "N2"));

        Map<String, Object> mainObj = new HashMap<>();
        mainObj.putIfAbsent("oneMnObj", Arrays.asList("MO11", "MO12"));
        mainObj.putIfAbsent("twoMnObj", Arrays.asList("MO21", "MO22"));

        nestedObject.putIfAbsent("data", mainObj);
    }


    public void initialiseComplexData(){

        Map<String, Object> item1 = new HashMap<>();
        item1.putIfAbsent("name", "John");
        item1.putIfAbsent("age", 30);
        item1.putIfAbsent("address", Map.of("street", "123 Main St", "city", "Boston", "location", Arrays.asList("locate1", "locate2")));

        Map<String, Object> item2 = new HashMap<>();
        item2.putIfAbsent("name", "Jane");
        item2.putIfAbsent("age", 25);
        item2.putIfAbsent("address", Map.of("street", "456 Oak St", "city", "New York", "location", Arrays.asList("locate11", "locate22")));

        Map<String, Object> item3 = new HashMap<>();
        item3.putIfAbsent("name","kikik");

        complexList.add(item1);
        complexList.add(item2);
        complexList.add(item3);
    }

}
