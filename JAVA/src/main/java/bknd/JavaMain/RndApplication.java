package bknd.JavaMain;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RndApplication {

    @Autowired
    public CommonRun VFS;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(RndApplication.class, args);
        RndApplication app = context.getBean(RndApplication.class);
        app.Run();
    }

    public void Run() {
        VFS.RunProgram();
    }

}