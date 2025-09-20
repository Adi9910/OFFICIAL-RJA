package bknd.Rnd;

import bknd.Rnd.PlaywithJSON.controllerJSON;
import bknd.Rnd.SQLQueries.User;
import bknd.Rnd.Verify.VerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonRun {

    @Autowired
    public VerifyService VFS;

    @Autowired
    public User user;

    @Autowired
    public controllerJSON check;

    public void RunProgram() {

//        API run itself

//        VFS.random(); //OTP Email + phone

//        user.executeQuery(); // SQL Query


    }

}
