package bknd.JavaMain.Verify;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.HashMap;
import java.util.Map;

@Service
public class OTPService {

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    private final Map<String, String> otpStorage = new HashMap<>();

    public String Call(String otp, String phoneNumber) {
        otpStorage.put(phoneNumber, otp);
        sendSMS(phoneNumber, otp);
        return otp;
    }

    public boolean verifyOTP(String phoneNumber, String otp) {
        String storedOTP = otpStorage.get(phoneNumber);
        return otp.equals(storedOTP);
    }

    private void sendSMS(String phoneNumber, String otp) {
        try {
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    "Your verification code is: " + otp + " at time" + new SimpleDateFormat("HH:mm:ss").format(new Date())
            ).create();
            System.out.println("SMS sent with SID: " + message.getSid());
        } catch (Exception e) {
            System.out.println(phoneNumber + " - " + accountSid + " - " + authToken + " - " + fromPhoneNumber);
            System.err.println("Failed to send SMS: " + e.getMessage());
        }
    }

    public void clearOTP(String phoneNumber) {
        otpStorage.remove(phoneNumber);
    }

    public String optGenerate(String otp, String phoneNumber){
        try {
            String generatedOTP = Call(otp, phoneNumber);
            Thread.sleep(1000);
            boolean isValid = verifyOTP(phoneNumber, generatedOTP);
            System.out.println("OTP verification result: " + (isValid ? "SUCCESS" : "FAILED"));
            clearOTP(phoneNumber);
            return generatedOTP;
        } catch(Exception error){
            System.out.println("error: " + error.getMessage());
            return error.getMessage();
        }
    }
}