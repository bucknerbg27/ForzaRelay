package wauz.forza.relay;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;


public class ForzaRelayController implements Initializable {
    @FXML
    Button buttonExit;
    @FXML
    Label labelIpLocal;
    @FXML
    Label labelIpRemote;
    @FXML
    Label labelPortLocal;
    @FXML
    Label labelPortRemote;

    public StringProperty spTargetAddress = new SimpleStringProperty();
    public IntegerProperty ipTargetPort = new SimpleIntegerProperty();
    public StringProperty spLocalAddress = new SimpleStringProperty();
    public IntegerProperty ipLocalPort = new SimpleIntegerProperty();

    public ForzaRelayController(int listeningPort, InetAddress targetIp, int targetPort, InetAddress localAddress){
        spTargetAddress.setValue(targetIp.toString());
        ipTargetPort.setValue(targetPort);
        ipLocalPort.setValue(listeningPort);
        spLocalAddress.setValue(localAddress.toString());
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Properties configuration = new Properties();
        InputStream streamConfig;
        String filePath = Paths.get("").toAbsolutePath().toString();
        labelIpLocal.textProperty().bind(spLocalAddress);
        labelIpRemote.textProperty().bind(spTargetAddress);
        labelPortLocal.textProperty().bind(ipLocalPort.asString());
        labelPortRemote.textProperty().bind(ipTargetPort.asString());
        try {
            streamConfig = new FileInputStream(filePath + "/ForzaConfig.properties");
            configuration.load(streamConfig);

            spLocalAddress.setValue(InetAddress.getByName(configuration.getProperty("sourceIP")).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        buttonExit.setOnAction(event -> {
            //todo: clean exit.
            System.exit(0);
        });
    }
}
