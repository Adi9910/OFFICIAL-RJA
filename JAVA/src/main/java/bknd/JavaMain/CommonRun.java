package bknd.JavaMain;

import bknd.JavaMain.Verify.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonRun {

    @Autowired
    public VerifyService VFS;


    public void RunProgram() {

//        API run itself

//        VFS.random(); //OTP Email + phone

//        user.executeQuery(); // SQL Query


    }

}
