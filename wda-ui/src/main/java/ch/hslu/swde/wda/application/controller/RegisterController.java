package ch.hslu.swde.wda.application.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.business.api.VerwaltungCustomer;
import ch.hslu.swde.wda.business.api.VerwaltungEmployee;
import ch.hslu.swde.wda.business.impl.VerwaltungCustomerImpl;
import ch.hslu.swde.wda.business.rmi.client.Connector;
import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.persister.impl.db.PersisterCustomerImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterController implements Initializable {

    private static final Logger LOG = LogManager.getLogger(RegisterController.class);

    @FXML
    private TextField txtSurName;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField pwdCustomer;

    @FXML
    private TextField txtStreet;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtZip;

    @FXML
    private Label lblResult;

    private int rmiPort;
    private String serverIP;

    @FXML
    public void registerCustomer() throws MalformedURLException, RemoteException, NotBoundException {
        lblResult.setText("");
        String name = txtName.getText();
        String surname = txtSurName.getText();
        String email = txtEmail.getText();
        String street = txtStreet.getText();
        String city = txtCity.getText();
        String txtzip = txtZip.getText();
        String pwd = pwdCustomer.getText();

        VerwaltungCustomer rmi = Connector.getCustomerStub(rmiPort, serverIP);

        if (!name.isBlank() && !surname.isBlank() && !email.isBlank() && !street.isBlank() && !city.isBlank()
                && !txtzip.isBlank() && !pwd.isBlank()) {
            try {
                int zip = Integer.parseInt(txtzip);
                rmi.registerCustomer(new Customer(name, surname, email, street, city, zip, pwd));

                FXMLLoader loader = new FXMLLoader(
                        getClass().getClassLoader().getResource("ch/hslu/swde/wda/application/view/LoginView.fxml"));
                Parent root = (Parent) loader.load();
                LoginController lgC = loader.getController();
                lgC.setResult("Sie wurden erfolgreich registriert und können sich jetzt einlogen.");

                Stage wdaGui = new Stage();
                wdaGui.setTitle("Wetterdaten App Login");
                wdaGui.setScene(new Scene(root, 850, 700));
                wdaGui.show();
                Stage stage = (Stage) txtName.getScene().getWindow();
                stage.close();

            } catch (Exception e) {
                LOG.info("Could not register new customer");
                lblResult.setText("Kunde konnte nicht registriert werden");
            }
        } else {
            lblResult.setText("Bitte alle Felder ausfüllen");
        }
    }

    @FXML
    public void back() {
        try {

            Parent root = null;
            root = FXMLLoader
                    .load(getClass().getClassLoader().getResource("ch/hslu/swde/wda/application/view/LoginView.fxml"));

            Stage wdaGui = (Stage) txtName.getScene().getWindow();
            wdaGui.setTitle("Wetterdaten App Login");
            wdaGui.setScene(new Scene(root, 850, 700));
            wdaGui.show();

        } catch (Exception e) {
            LOG.info("Could not load login page");
            lblResult.setText("Login Seite konnte nicht geladen werden");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.rmiPort = 1099;
        this.serverIP = "10.155.103.224";
        try {
            Connector.setSecurityManager();
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }

    }
}
