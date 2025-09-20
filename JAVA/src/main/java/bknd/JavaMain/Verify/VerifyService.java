package bknd.JavaMain.Verify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class VerifyService {

    //VerifyUserThroughOTP
    @Autowired
    public OTPService otpServe;
    @Autowired
    public EmailService emailServe;

    private final Random random = new Random();

    public void VerifyUserThroughOTP(String email){
//        String phoneNumber = "+919910282537";
//        String email = "info9adi@gmail.com";

        int otpValue = 100000 + random.nextInt(900000);
        String otp = String.valueOf(otpValue);
//        new Thread(()-> otpServe.optGenerate(otp, phoneNumber)).start();
        new Thread(()-> emailServe.emailGenerate(otp, email)).start();
    }

}
