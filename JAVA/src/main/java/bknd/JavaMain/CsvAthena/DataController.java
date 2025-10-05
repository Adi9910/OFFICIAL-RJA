package bknd.JavaMain.CsvAthena;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import bknd.JavaMain.Verify.EmailService;
import bknd.JavaMain.Verify.OTPService;
import bknd.JavaMain.CsvAthena.AthenaService;

import java.util.*;
import java.util.regex.Pattern;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private AthenaService athenaService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OTPService smsService;

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[0-9]{10}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s]{2,50}$");
    private static final int MAX_ID_GENERATION_ATTEMPTS = 10;

    @GetMapping("/all")
    public List<Map<String, String>> getAllData() {
        String query = String.format("SELECT * FROM %s ", athenaService.studentTable);
        List<Map<String, String>> result = athenaService.executeQuery(query);
        return result;
    }
    // REGISTER
    private String checkId() throws RuntimeException {
        int attempts = 0;
        while (attempts < MAX_ID_GENERATION_ATTEMPTS) {
            try {
                String accId = athenaService.generateUniqueId();
                String query = String.format("SELECT * FROM %s WHERE id='%s'",
                        athenaService.tableName, accId);
                List<Map<String, String>> result = athenaService.executeQuery(query);
                if (result.isEmpty()) {
                    return accId;
                }
                attempts++;
            } catch (Exception e) {
                throw new RuntimeException("Error checking ID uniqueness: " + e.getMessage(), e);
            }
        }
        throw new RuntimeException("Failed to generate unique ID after " + MAX_ID_GENERATION_ATTEMPTS + " attempts");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> addUser(@RequestBody Map<String, String> user) {
        try {
            // Input validation
            if (user == null || user.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Request body is empty"));
            }

            String name = user.get("name");
            String mobile = user.get("mobile");

            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Name is required"));
            }

            if (mobile == null || mobile.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Mobile number is required"));
            }

            // Sanitize inputs
            name = sanitizeInput(name.trim());
            mobile = sanitizeInput(mobile.trim());

            // Validate format
            if (!NAME_PATTERN.matcher(name).matches()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid name format. Only letters and spaces allowed (2-50 characters)"));
            }

            if (!MOBILE_PATTERN.matcher(mobile).matches()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid mobile number format. Must be 10 digits"));
            }

            // Check if mobile already exists
            String checkQuery = String.format("SELECT * FROM %s WHERE mobile='%s'",
                    athenaService.tableName, mobile);
            List<Map<String, String>> existingUsers = athenaService.executeQuery(checkQuery);

            if (!existingUsers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Mobile number already registered"));
            }

            // Generate unique ID
            String accID = checkId();

            // Build INSERT query
            String query = String.format(
                    "INSERT INTO %s (id, mobile, name) VALUES ('%s', '%s', '%s')",
                    athenaService.tableName, accID, mobile, name);

            athenaService.executeQuery(query);

            return ResponseEntity.ok(Map.of("valid", accID, "message", "User registered successfully"));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to register user: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    @PostMapping("/otp")
    public ResponseEntity<Map<String, String>> otp(@RequestBody Map<String, String> user) {
        try {
            // Input validation
            if (user == null || user.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Request body is empty"));
            }

            String mobile = user.get("mobile");

            if (mobile == null || mobile.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Mobile number is required"));
            }

            // Sanitize and validate
            mobile = sanitizeInput(mobile.trim());

            if (!MOBILE_PATTERN.matcher(mobile).matches()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid mobile number format. Must be 10 digits"));
            }

            // Generate OTP
            Random random = new Random();
            int otpValue = 100000 + random.nextInt(900000);
            String otp = String.valueOf(otpValue);

            // Query database
            String query = String.format("SELECT * FROM %s WHERE mobile='%s'",
                    athenaService.tableName, mobile);
            List<Map<String, String>> result = athenaService.executeQuery(query);

            System.out.println("Generated OTP: " + otp);

            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("valid", "NOT_FOUND", "otp", otp,
                                "message", "Mobile number not registered"));
            } else {
                Map<String, String> userRecord = result.get(0);
                String name = userRecord.get("name");
                String id = userRecord.get("id");

                if (name == null || id == null) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("error", "Incomplete user record found"));
                }

                return ResponseEntity.ok(Map.of("valid", name, "otp", otp, "id", id));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to generate OTP: " + e.getMessage()));
        }
    }

//    {
//        "id":"JKL012MNO",
//        "type":"student",
//        "highestQualification":"class X",
//        "InterestDomain":"engineer",
//        "InterestDegree":"b.tech",
//        "email":"rare@gmail.com" (optional)
//    }
    @PostMapping("/append/maindata")
    public ResponseEntity<Map<String, String>> appendDetail(@RequestBody Map<String, String> detail) {
        try {
            // Input validation
            if (detail == null || detail.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Request body is empty"));
            }

            String mobile = detail.get("mobile");
            String name = detail.get("name");
            String email = detail.get("email");
            
            String type = detail.get("type");
            String highestQualification = detail.get("highestQualification");
            String InterestDomain = detail.get("InterestDomain");
            String InterestDegree = detail.get("InterestDegree");
            String query = detail.get("query");

            // Insert student details
            String finalquery = String.format(
                    "INSERT INTO %s (name, email, mobile, type, highestQualification, InterestDomain, InterestDegree, date, query) " +
                            "VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    athenaService.studentTable, name, email, mobile, type,
                    highestQualification, InterestDomain, InterestDegree, new Date(), query);

            athenaService.executeQuery(finalquery);

            return ResponseEntity.ok(Map.of("valid", "yes"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to append details: " + e.getMessage()));
        }
    }

    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("'", "''");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An unexpected error occurred",
                        "details", e.getMessage()));
    }

}