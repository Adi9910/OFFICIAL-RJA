package bknd.Rnd.SQLQueries;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;

@Service
public class User {

    @Autowired
    public ConnectDB cont;

    public void executeQuery(){
        System.out.println("Query 1: Users with name = 'Aditya'");
        List<Map<String, Object>> result1 = cont.runQuery(
                "SELECT * FROM users WHERE name = 'Aditya'"
        );
        result1.forEach(System.out::println);

        System.out.println("Query 1: Users");
        List<Map<String, Object>> result2 = cont.runQuery(
                "SELECT * FROM users"
        );
        result2.forEach(System.out::println);
    }

}
