package bknd.JavaMain.SQLQueries.Login;

import bknd.JavaMain.Verify.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import bknd.JavaMain.PlaywithJSON.controllerJSON;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@RestController
@RequestMapping()
public class LoginApi {

    @Autowired
    public JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> complexList = new ArrayList<>();

    @Autowired
    public controllerJSON check;

    @Autowired
    public VerifyService verifyotp;

    @GetMapping("/test-db")
    public String testConnection() {
        try {
            String result = jdbcTemplate.queryForObject("SELECT version()", String.class);
            return "Database connected: " + result;
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }

    @GetMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Map<String, Object>> getComplexList() {
        List<Map<String, Object>> resultQuery = jdbcTemplate.queryForList(
                "SELECT * FROM users"
        );
        return resultQuery;
    }

    @PostMapping(value="/user/append", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> appendToComplexList(@RequestBody Map<String, Object> newItem) {
        try {
            // Extract values from request body
            String name = (String) newItem.get("name");
            String email = (String) newItem.get("email");
            String mobile = (String) newItem.get("mobile");
            String domain = (String) newItem.get("domain");
            String degree = (String) newItem.get("degree");
            String password = (String) newItem.get("password");

            // Validate required fields
            if (name == null || email == null || password == null || domain == null) {
                return ResponseEntity.badRequest()
                        .body(List.of(Map.of("error", "Name, email, age and password are required")));
            }

            // Generate random account ID
            String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            Random random = new Random();
            StringBuilder idBuilder = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                idBuilder.append(characters.charAt(random.nextInt(characters.length())));
            }
            String accID = idBuilder.toString();

            // Execute INSERT query using JdbcTemplate
            int affectedRows = jdbcTemplate.update(
                    "INSERT INTO users (account_id, name, email, mobile, domain, degree, password) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    accID, name, email, mobile, domain, degree, password
            );

            if (affectedRows > 0) {
                return ResponseEntity.ok()
                        .body(List.of(Map.of(
                                "value", "Data inserted successfully",
                                "account_id", accID,
                                "affected_rows", affectedRows
                        )));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(List.of(Map.of("error", "Failed to insert data")));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(Map.of("error", "Server error: " + e.getMessage())));
        }
    }

    @PostMapping(value="/user/verify", produces = MediaType.APPLICATION_JSON_VALUE)
    public void verifyemail(@org.jetbrains.annotations.NotNull @RequestBody Map<String, Object> newItem) {
        String email = (String) ((Map<String, Object>) newItem.get("other")).get("email");
        verifyotp.VerifyUserThroughOTP(email);
    }

    @PostMapping(value="/user/remove", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> popFromComplexList(@RequestBody Map<String, Object> newItem) {
        try{
            String id = (String) newItem.get("id");
            if(id == null){
                return ResponseEntity.badRequest()
                        .body(List.of(Map.of("message", "Account Id cannot be null")));
            }
            int affectRow = jdbcTemplate.update("delete from users where account_id= ?", id);
            if(affectRow > 0){
                return ResponseEntity.status(200)
                        .body(List.of(Map.of(
                                "message", "successfully deleted the record",
                                "affected_rows", affectRow
                        )));
            }  else {
                return ResponseEntity.status(400)
                        .body(List.of(Map.of(
                                "message", "something went wrong"
                        )));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of(Map.of(
                            "message", "successfully deleted the record"
                    )));
        }
    }


}
