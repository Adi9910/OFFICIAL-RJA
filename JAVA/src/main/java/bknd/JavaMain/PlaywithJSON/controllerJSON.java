package bknd.JavaMain.PlaywithJSON;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class controllerJSON {

    public List<Map<String,Object>> complexList = new ArrayList<>();

    public void initialiseComplexData(){

        Map<String, Object> item1 = new HashMap<>();
        item1.putIfAbsent("name", "John");
        item1.putIfAbsent("age", 30);
        item1.putIfAbsent("address", Map.of("street", "123 Main St", "city", "Boston", "location", Arrays.asList("locate1", "locate2")));

        complexList.add(item1);
    }

}
