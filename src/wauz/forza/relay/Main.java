package wauz.forza.relay;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Properties configuration = new Properties();
        InputStream streamConfig;
        int listeningPort = 1337;
        int targetPort = 1337;
        InetAddress targetAddress = InetAddress.getByName("10.0.0.192");

        String filePath = Paths.get("").toAbsolutePath().toString();
        try {
            streamConfig = new FileInputStream(filePath + "/ForzaConfig.properties");
            configuration.load(streamConfig);
            listeningPort = Integer.parseInt(configuration.getProperty("listeningPort"));
            targetPort = Integer.parseInt(configuration.getProperty("dataOutRelayTargetPort"));
            targetAddress = InetAddress.getByName(configuration.getProperty("dataOutRelayTargetIP"));
            System.out.println("Listening-Port: "+Integer.toString(listeningPort)+" Target: "+targetAddress.toString()+":"+Integer.toString(targetPort));

        } catch (Exception e) {
            System.out.println("Could not load config: " + e.toString());
        }
        ForzaDataOutReader reader = new ForzaDataOutReader();
        reader.startRelay(listeningPort, targetAddress, targetPort);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ForzaRelay.fxml"));
        ForzaRelayController controller = new ForzaRelayController(listeningPort,targetAddress,targetPort);
        fxmlLoader.setController(controller);
        Pane root = fxmlLoader.load();
        primaryStage.setTitle("Forza DataOut Relay");
        primaryStage.setScene(new Scene(root, 300, 120));
        primaryStage.show();


        primaryStage.setOnCloseRequest(event -> {
            reader.stopProcessing();
            //todo: clean exit
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
