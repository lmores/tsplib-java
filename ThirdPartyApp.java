// Compile once the project and then run me:
//   mvn clean package
//   java -cp target/tsplib-0.0.1.jar ThirdPartyApp.java

import java.io.IOException;

import io.github.lmores.tsplib.TsplibArchive;
import io.github.lmores.tsplib.tsp.TspInstance;

public class ThirdPartyApp {
    public static void main(String[] args) throws IOException {
        System.out.println(System.getProperty("java.class.path"));
        final TspInstance inst = TsplibArchive.loadTspInstance("a280.tsp");
        System.out.println(inst);
    }
}
