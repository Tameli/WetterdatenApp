package ch.hslu.swde.wda.application.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ch.hslu.swde.wda.business.api.VerwaltungCity;
import ch.hslu.swde.wda.business.api.VerwaltungCustomer;
import ch.hslu.swde.wda.business.api.VerwaltungEmployee;
import ch.hslu.swde.wda.business.api.VerwaltungWeatherData;
import ch.hslu.swde.wda.business.rmi.client.Connector;
import ch.hslu.swde.wda.domain.City;
import ch.hslu.swde.wda.domain.Customer;
import ch.hslu.swde.wda.domain.Employee;
import ch.hslu.swde.wda.domain.WeatherReading;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    private static final Logger LOG = LogManager.getLogger(LoginController.class);

    @FXML
    public TextField txtUser;

    @FXML
    public PasswordField pwdField;

    @FXML
    public Label lblError;

    @FXML
    private Label lblResult;

    @FXML
    private TextArea txtAreaResult;

    @FXML
    private ListView<String> lvCity;

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField pwdCustomer;

    private int rmiPort;
    private String serverIP;

    public void login() throws MalformedURLException, RemoteException, NotBoundException {
        String usrInput = txtUser.getText();
        String pwdInput = pwdField.getText();

        Long usInput = Long.parseLong(usrInput);

        VerwaltungEmployee rmi = Connector.getEmployeeStub(rmiPort, serverIP);

        Employee employee = null;

        if (!usrInput.isBlank()) {
            try {
                employee = rmi.findEmployee(usInput);
            } catch (Exception e) {
                LOG.error("Could not find employee in DB");
                lblError.setText("Mitarbeiter konnte nicht gefunden werden");
            }
        } else {
            lblError.setText("Bitte eine UserId eingeben");
        }

        if (pwdInput.equals(employee.getPassword())) {

            lblResult.setText("Login erfolgreich");
            Parent root = null;
            try {
                root = FXMLLoader.load(
                        getClass().getClassLoader().getResource("ch/hslu/swde/wda/application/view/MainView.fxml"));
            } catch (IOException e) {
                LOG.error("FXMLLoader could not load fxml file");
            }

            Stage wdaGui = new Stage();
            wdaGui.setTitle("Wetterdaten App");
            wdaGui.setScene(new Scene(root, 850, 700));
            wdaGui.show();
            Stage stage = (Stage) txtUser.getScene().getWindow();
            stage.close();

        } else {
            LOG.info("Loggin failed");
            lblError.setText("UserId oder Passwort falsch");
        }

    }

    @FXML
    public void loginCustomer() throws MalformedURLException, RemoteException, NotBoundException {
        String email = txtEmail.getText();
        String pwd = pwdCustomer.getText();

        VerwaltungCustomer rmi = Connector.getCustomerStub(rmiPort, serverIP);

        Customer customer = null;

        if (!email.isBlank()) {
            try {
                customer = rmi.findCustomer(email);
            } catch (Exception e) {
                LOG.error("Customer could not be found");
                lblError.setText("Email-Adresse konnte nicht in Datenbank gefunden werden");
            }

        } else {
            lblError.setText("Bitte eine Email-Adresse eingeben");
        }

        if (customer != null) {
            if (pwd.equals(customer.getPassword())) {

                Parent root = null;

                try {
                    root = FXMLLoader.load(getClass().getClassLoader()
                            .getResource("ch/hslu/swde/wda/application/view/CustomerView.fxml"));
                } catch (IOException e) {
                    LOG.error("FXMLLoader could not load fxml file");
                }

                Stage stage = (Stage) txtUser.getScene().getWindow();

                stage.setTitle("Wetterdaten App Customer");
                stage.setScene(new Scene(root, 850, 700));
                stage.show();

            } else {
                lblError.setText("Email oder Passwort falsch.");
            }
        } else {
            lblError.setText("Email falsch");

        }
    }

    @FXML
    public void register() {
        Parent root = null;
        try {
            root = FXMLLoader.load(
                    getClass().getClassLoader().getResource("ch/hslu/swde/wda/application/view/RegisterView.fxml"));
        } catch (IOException e) {
            LOG.error("FXMLLoader could not load fxml file");
        }

        Stage stage = (Stage) txtUser.getScene().getWindow();
        stage.setTitle("WDA Registrieren");
        stage.setScene(new Scene(root, 850, 700));
        stage.show();
    }

    @FXML
    public void showAllCities() {
        clearTextAreaAndLabels();

        try {
            int counter = 0;
            List<City> resultList = new ArrayList<>();
            VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);
            resultList = rmi.getAllCitites();
            Collections.sort(resultList);
            txtAreaResult.appendText(resultList.size() + " Elemente gefunden \n");
            for (City c : resultList) {
                txtAreaResult.appendText(resultList.get(counter).toString() + "\n");
                counter++;
            }

        } catch (Exception e) {
            LOG.info("Could not get all citites from database");
            lblError.setText("Die Städte konnten nicht aus der DB geholt werden");

        }
    }

    @FXML
    public void showLastDay() {
        clearTextAreaAndLabels();
        String ort = lvCity.getSelectionModel().getSelectedItem();

        try {
            if (!ort.isEmpty()) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                City city = rmi.findCityByName(ort);
                if (ort.toLowerCase().equals(city.getName().toLowerCase())) {
                    resultList = rmiWeather.getDataLast24Hour(city);
                    txtAreaResult.appendText(resultList.size() + " Elemente gefunden\n");
                    for (WeatherReading wr : resultList) {
                        txtAreaResult.appendText(wr.toString() + "\n");
                    }

                } else {
                    lblError.setText("Bitte einen Ort auswählen");

                }
            } else {
                lblError.setText("Für diese Anfrage wird eine Angabe im Feld Ort benötigt.");

            }
        } catch (Exception e) {
            LOG.info("Could not get the data from last 24 hours.");
            lblError.setText("Die Daten von den letzten 24 Stunden konnten nicht ausgegeben werden.");

        }
    }

    private void clearTextAreaAndLabels() {
        txtAreaResult.setText("");
        lblError.setText("");
    }

    @FXML
    private void initLvCity() throws RemoteException, MalformedURLException, NotBoundException {
        List<City> resultList = new ArrayList<>();
        VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);
        List<String> stringListResults = new ArrayList<String>();
        try {
            resultList = rmi.getAllCitites();
            for (City c : resultList) {
                stringListResults.add(c.getName());
            }
        } catch (Exception e) {
            LOG.info("ChoiceBox konnte nicht initialisiert werden");
        }
        Collections.sort(stringListResults);
        ObservableList<String> observableList = FXCollections.observableArrayList(stringListResults);
        lvCity.setItems(observableList);
        lvCity.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lvCity.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            List<String> cities = lvCity.getSelectionModel().getSelectedItems();
            txtAreaResult.setText("");
            for (String str : cities) {
                txtAreaResult.appendText(str + "\n");
            }
        });

    }

    @FXML
    public void setResult(String str) {
        txtAreaResult.setText(str);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.rmiPort = 1099;
            this.serverIP = "10.155.103.224";
            initLvCity();
        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }

    }

}
