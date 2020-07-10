package ch.hslu.swde.wda.application.controller;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
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
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class WDAController implements Initializable {
    private static final Logger LOG = LogManager.getLogger(WDAController.class);

    @FXML
    private TextField txtSurName;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPasswort;

    @FXML
    private TextField txtStreet;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtZip;

    @FXML
    private TextField txtAllCities;

    @FXML
    private Label lblError;

    @FXML
    private TextArea txtAreaResult;

    @FXML
    private TextField txtEmployeeVorname;

    @FXML
    private TextField txtEmployeeName;

    @FXML
    private TextField txtPwdEmployee;

    @FXML
    private TextField txtIdEmployee;

    @FXML
    private DatePicker fromDate;

    @FXML
    private DatePicker untillDate;

    @FXML
    private ListView<String> lvCity;

    private List<WeatherReading> listWeatherReading = new ArrayList<WeatherReading>();

    private List<String> listString = new ArrayList<>();

    private int rmiPort;
    private String serverIP;

    @FXML
    public void registerCustomer() throws MalformedURLException, RemoteException, NotBoundException {
        clearTextAreaAndLabels();
        String name = txtName.getText();
        String surname = txtSurName.getText();
        String email = txtEmail.getText();
        String street = txtStreet.getText();
        String city = txtCity.getText();

        VerwaltungCustomer rmi = Connector.getCustomerStub(rmiPort, serverIP);
        try {
            int zip = Integer.parseInt(txtZip.getText());
            rmi.registerCustomer(new Customer(name, surname, email, street, city, zip, ""));
            txtAreaResult.setText("Kunde wurde registriert");
        } catch (Exception e) {
            LOG.info("Could not register new customer");
            lblError.setText("Kunde konnte nicht registriert werden");
        }
    }

    @FXML
    public void updateCustomer() throws MalformedURLException, RemoteException, NotBoundException {
        clearTextAreaAndLabels();
        String name = txtName.getText();
        String surname = txtSurName.getText();
        String email = txtEmail.getText();
        String street = txtStreet.getText();
        String city = txtCity.getText();

        VerwaltungCustomer rmi = Connector.getCustomerStub(rmiPort, serverIP);

        try {
            int zip = Integer.parseInt(txtZip.getText());
            boolean transactionResult = rmi
                    .updateCustomer(new Customer(name, surname, email, street, city, zip, ""));
            if (transactionResult) {
                txtAreaResult.appendText("Kundendaten wurden erfolgreich aktualisiert \n");
                txtAreaResult.appendText(rmi.findCustomer(email).toString());
            } else {
                txtAreaResult.appendText("Kundendaten konnten nicht aktualisiert werden.");
            }
        } catch (Exception e) {
            LOG.info("Kunde konnte nicht aktualisiert werden");
        }
    }

    @FXML
    public void findCustomer() throws MalformedURLException, RemoteException, NotBoundException {
        clearTextAreaAndLabels();
        String email = txtEmail.getText();

        VerwaltungCustomer rmi = Connector.getCustomerStub(rmiPort, serverIP);

        try {
            Customer customer = rmi.findCustomer(email);
            txtAreaResult.setText("Kunde " + customer.toString() + "\n wurde gefunden");
        } catch (Exception e) {
            LOG.info("Kunde konnte nicht gefunden werden");
            lblError.setText("Kunde konnte nicht gefunden werden");
        }
    }

    @FXML
    public void deleteCustomer() throws MalformedURLException, RemoteException, NotBoundException {
        clearTextAreaAndLabels();

        String email = txtEmail.getText();

        boolean successfull = false;

        String customer = "";

        VerwaltungCustomer rmi = Connector.getCustomerStub(rmiPort, serverIP);

        try {
            customer = rmi.findCustomer(email).toString();
            successfull = rmi.deletCustomer(email);
            System.out.println(successfull);

        } catch (Exception e) {
            LOG.info("Kunde konnte nicht gelöscht werden");
            lblError.setText("Kunde konnte nicht gelöscht werden");
        }

        if (successfull) {
            try {
                txtAreaResult.appendText("Kunde " + customer + "\n wurde gelöscht");
            } catch (Exception e) {
                LOG.info(e);
                lblError.setText("Error");
            }
        } else {
            txtAreaResult.appendText("Kunde mit email: \n" + email + " konnte nicht gelöscht werden");
        }
    }

    @FXML
    public void showAllCities() {
        clearTextAreaAndLabels();

        try {
            int counter = 0;
            List<City> resultList = new ArrayList<>();
            VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);
            resultList = rmi.getAllCitites();
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
    public void dbUpdate() {

        try {
            VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
            rmiWeather.updateDatabase();
            ProgressBar progressBar = new ProgressBar();
            FlowPane root = new FlowPane();
            root.setPadding(new Insets(10));
            root.setHgap(10);
            root.getChildren().addAll(progressBar);
            txtAreaResult.setText("DB aktualisiert");
        } catch (RemoteException e) {
            LOG.error("Remote Exception", e);
            lblError.setText("Remote Exception");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            LOG.error("MalformedURL", e);
            lblError.setText("Falsche Server URL");
            e.printStackTrace();
        } catch (NotBoundException e) {
            LOG.error("NotBoundException", e);
            lblError.setText("Verbindung mit dem entfernten Server konnte nicht aufgebaut werden");
            e.printStackTrace();
        } catch (Exception e) {
            LOG.error(e);
            e.printStackTrace();
        }

    }

    @FXML
    public void showLastDay() {
        clearTextAreaAndLabels();
        clearLists();
        String ort = lvCity.getSelectionModel().getSelectedItem();

        try {
            if (!ort.isEmpty()) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);
                City city = rmi.findCityByName(ort);

                if (ort.toLowerCase().equals(city.getName().toLowerCase())) {
                    resultList = rmiWeather.getDataLast24Hour(city);
                    listWeatherReading.addAll(resultList);
                    txtAreaResult.appendText(resultList.size() + " Elemente gefunden\n");
                    for (WeatherReading wr : resultList) {
                        txtAreaResult.appendText(wr.toString() + "\n");
                    }

                } else {
                    lblError.setText("Bitte einen Ort auswählen");

                }
            } else {
                lblError.setText("Bitte einen Ort auswählen");

            }
        } catch (Exception e) {
            LOG.info("Could not get the data from last 24 hours.");
            lblError.setText("Die Daten von den letzten 24 Stunden konnten nicht ausgegeben werden.");

        }
    }

    @FXML
    public void periodOfTime() {
        clearTextAreaAndLabels();
        clearLists();
        LocalDate fromThisDate = fromDate.getValue();
        LocalDate untilDate = untillDate.getValue();
        String ort = lvCity.getSelectionModel().getSelectedItem();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();
        try {
            if (checkIfStartBiggerThanEnd() && !ort.isEmpty() && !(fromThisDate.getMonthValue() == 0)
                    && !(untilDate.getMonthValue() == 0) && cities.size() == 1) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);
                City city = rmi.findCityByName(ort);
                if (ort.toLowerCase().equals(city.getName().toLowerCase())) {
                    resultList = rmiWeather.getDataForSpecificCityAndTimeInterval(fromThisDate, untilDate, ort);
                    txtAreaResult.appendText(resultList.size() + " Elemente gefunden \n");
                    txtAreaResult.setText(ort + "\n");
                    listWeatherReading.addAll(resultList);
                    for (WeatherReading wr : resultList) {
                        txtAreaResult.appendText(wr.toString() + "\n");
                    }

                } else {
                    lblError.setText("Ort ist nicht in dem Datenbank.");

                }

            } else if (!checkIfStartBiggerThanEnd()) {
                lblError.setText("Das Enddatum muss nach dem Startdatum sein");

            } else if (cities.size() > 1) {
                lblError.setText("Bitte nur eine Stadt anwählen");
            } else {
                lblError.setText("Für diese Anfrage werden noch weitere Angaben benötigt.");
            }

        } catch (Exception e) {

            LOG.info("Could not get the data from last 24 hours.");
            lblError.setText("Es muss ein Ort, sowie ein Startdatum und ein Enddatum ausgewählt sein");

        }

    }

    @FXML
    public void temperaturAverage() {
        clearTextAreaAndLabels();
        clearLists();
        LocalDate fromThisDate = fromDate.getValue();
        LocalDate untilDate = untillDate.getValue();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();

        try {
            if (checkIfStartBiggerThanEnd() && !cities.isEmpty() && !(fromThisDate.getMonthValue() == 0)
                    && !(untilDate.getMonthValue() == 0)) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);

                for (String strCity : cities) {
                    City city = rmi.findCityByName(strCity);

                    if (strCity.toLowerCase().equals(city.getName().toLowerCase())) {
                        resultList = rmiWeather.getDataForSpecificCityAndTimeInterval(fromThisDate, untilDate, strCity);
                        float average = rmiWeather.getAverageTempForTimeInterval(resultList);
                        if (average != -274) {
                            System.out.println(average);
                            String averageString = Float.toString(average);
                            listString.add(strCity);
                            listString.add(averageString);
                            txtAreaResult.appendText(strCity + "\n");
                            txtAreaResult.appendText(averageString + "\n");
                        } else {
                            lblError.setText("Der Wert konnte nich berechnet werden.");
                        }

                    } else {
                        lblError.setText("Ort ist nicht in dem Datenbank.");

                    }
                }
            } else if (!checkIfStartBiggerThanEnd()) {
                lblError.setText("Das Enddatum muss nach dem Startdatum sein");

            } else {
                lblError.setText("Für diese Anfrage werden noch weitere Angaben benötigt.");
            }

        } catch (Exception e) {
            LOG.info("Could not get the data from last 24 hours.");
            lblError.setText("Die Daten von den letzten 24 Stunden konnten nicht ausgegeben werden.");

        }

    }

    @FXML
    public void maxAndMinTemp() {
        clearTextAreaAndLabels();
        clearLists();

        LocalDate fromThisDate = fromDate.getValue();
        LocalDate untilDate = untillDate.getValue();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();
        try {
            if (checkIfStartBiggerThanEnd() && !cities.isEmpty() && !(fromThisDate.getMonthValue() == 0)
                    && !(untilDate.getMonthValue() == 0)) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);

                for (String strCities : cities) {

                    City city = rmi.findCityByName(strCities);
                    if (strCities.toLowerCase().equals(city.getName().toLowerCase())) {
                        resultList = rmiWeather.getDataForSpecificCityAndTimeInterval(fromThisDate, untilDate,
                                strCities);
                        float maxTemp = rmiWeather.getMaxTempSpecificCities(resultList);
                        String maxString = Float.toString(maxTemp);
                        listString.add(strCities);
                        listString.add("Max: " + maxString);
                        txtAreaResult.appendText(strCities + "\n");
                        txtAreaResult.appendText(maxString + "\n");
                        float minTemp = rmiWeather.getMinTempSpecificCities(resultList);
                        String minString = Float.toString(minTemp);
                        listString.add("Min: " + minString);
                        txtAreaResult.appendText(minString + "\n");

                    } else {
                        lblError.setText("Ort ist nicht in dem Datenbank.");

                    }
                }
            } else if (!checkIfStartBiggerThanEnd()) {
                lblError.setText("Das Enddatum muss nach dem Startdatum sein");

            } else {
                lblError.setText("Für diese Anfrage werden noch weitere Angaben benötigt.");
            }

        } catch (Exception e) {
            LOG.info("Could not get the data from last 24 hours.");
            lblError.setText("Die Daten von den letzten 24 Stunden konnten nicht ausgegeben werden.");

        }

    }

    @FXML
    private void avgPressure() {
        clearTextAreaAndLabels();
        clearLists();

        LocalDate fromThisDate = fromDate.getValue();
        LocalDate untilDate = untillDate.getValue();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();
        try {
            if (checkIfStartBiggerThanEnd() && !cities.isEmpty() && !(fromThisDate.getMonthValue() == 0)
                    && !(untilDate.getMonthValue() == 0)) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);

                for (String strCities : cities) {

                    City city = rmi.findCityByName(strCities);
                    if (strCities.toLowerCase().equals(city.getName().toLowerCase())) {
                        resultList = rmiWeather.getDataForSpecificCityAndTimeInterval(fromThisDate, untilDate,
                                strCities);
                        float avgPressure = rmiWeather.getAveragePressureForTimeInterval(resultList);
                        String strAvgPressure = Float.toString(avgPressure);
                        listString.add(strCities);
                        listString.add("Durschnittlicher Luftdruck: " + strAvgPressure);
                        txtAreaResult.appendText(strCities + "\n");
                        txtAreaResult.appendText(strAvgPressure + "\n");

                    } else {
                        lblError.setText("Ort ist nicht in dem Datenbank.");

                    }
                }
            } else if (!checkIfStartBiggerThanEnd()) {
                lblError.setText("Das Enddatum muss nach dem Startdatum sein");

            } else {
                lblError.setText("Für diese Anfrage werden noch weitere Angaben benötigt.");
            }

        } catch (Exception e) {
            LOG.info("Could not get the data from last 24 hours.");
            lblError.setText("Die Daten von den letzten 24 Stunden konnten nicht ausgegeben werden.");

        }
    }

    @FXML
    private void progressTemperatur() {
        clearTextAreaAndLabels();
        clearLists();

        LocalDate fromThisDate = fromDate.getValue();
        LocalDate untilDate = untillDate.getValue();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();
        List<Float> temperaturList = new ArrayList<>();
        try {
            if (checkIfStartBiggerThanEnd() && !cities.isEmpty() && !(fromThisDate.getMonthValue() == 0)
                    && !(untilDate.getMonthValue() == 0)) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);

                for (String strCities : cities) {

                    City city = rmi.findCityByName(strCities);
                    if (strCities.toLowerCase().equals(city.getName().toLowerCase())) {
                        resultList = rmiWeather.getDataForSpecificCityAndTimeInterval(fromThisDate, untilDate,
                                strCities);
                        temperaturList = rmiWeather.getProgressTemp(resultList, fromThisDate, untilDate, strCities);
                        listString.add(strCities);
                        txtAreaResult.appendText(strCities + "\n");

                        for (Float flt : temperaturList) {
                            txtAreaResult.appendText(flt + "\n");
                            listString.add(Float.toString(flt));
                        }
                    } else {
                        lblError.setText("Ort ist nicht in dem Datenbank.");

                    }
                }
            } else if (!checkIfStartBiggerThanEnd()) {
                lblError.setText("Das Enddatum muss nach dem Startdatum sein");

            } else {
                lblError.setText("Für diese Anfrage werden noch weitere Angaben benötigt.");
            }

        } catch (Exception e) {
            LOG.info("Could not get the data for progress Temperatur");
            lblError.setText("Die Daten konnten nicht ausgegeben werden");

        }
    }

    @FXML
    private void progressHumidity() {
        clearTextAreaAndLabels();
        clearLists();

        LocalDate fromThisDate = fromDate.getValue();
        LocalDate untilDate = untillDate.getValue();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();
        List<Integer> humidityList = new ArrayList<>();
        try {
            if (checkIfStartBiggerThanEnd() && !cities.isEmpty() && !(fromThisDate.getMonthValue() == 0)
                    && !(untilDate.getMonthValue() == 0)) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);

                for (String strCities : cities) {

                    City city = rmi.findCityByName(strCities);
                    if (strCities.toLowerCase().equals(city.getName().toLowerCase())) {
                        resultList = rmiWeather.getDataForSpecificCityAndTimeInterval(fromThisDate, untilDate,
                                strCities);
                        humidityList = rmiWeather.getProgressHumidity(resultList, fromThisDate, untilDate, strCities);
                        listString.add(strCities);
                        txtAreaResult.appendText(strCities + "\n");

                        for (Integer el : humidityList) {
                            txtAreaResult.appendText(el + "\n");
                            listString.add(Integer.toString(el));
                        }
                    } else {
                        lblError.setText("Ort ist nicht in dem Datenbank.");

                    }
                }
            } else if (!checkIfStartBiggerThanEnd()) {
                lblError.setText("Das Enddatum muss nach dem Startdatum sein");

            } else {
                lblError.setText("Für diese Anfrage werden noch weitere Angaben benötigt.");
            }

        } catch (Exception e) {
            LOG.info("Could not get the data for progress humidity");
            lblError.setText("Die Daten konnten nicht ausgegeben werden");

        }
    }

    @FXML
    private void progressPressure() {

        clearTextAreaAndLabels();
        clearLists();

        LocalDate fromThisDate = fromDate.getValue();
        LocalDate untilDate = untillDate.getValue();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();
        List<Integer> pressureList = new ArrayList<>();
        try {
            if (checkIfStartBiggerThanEnd() && !cities.isEmpty() && !(fromThisDate.getMonthValue() == 0)
                    && !(untilDate.getMonthValue() == 0)) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);

                for (String strCities : cities) {
                    VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);
                    City city = rmi.findCityByName(strCities);
                    if (strCities.toLowerCase().equals(city.getName().toLowerCase())) {
                        resultList = rmiWeather.getDataForSpecificCityAndTimeInterval(fromThisDate, untilDate,
                                strCities);
                        pressureList = rmiWeather.getProgressPressure(resultList, fromThisDate, untilDate, strCities);
                        listString.add(strCities);
                        txtAreaResult.appendText(strCities + "\n");

                        for (Integer el : pressureList) {
                            txtAreaResult.appendText(el + "\n");
                            listString.add(Integer.toString(el));
                        }
                    } else {
                        lblError.setText("Ort ist nicht in dem Datenbank.");

                    }
                }
            } else if (!checkIfStartBiggerThanEnd()) {
                lblError.setText("Das Enddatum muss nach dem Startdatum sein");

            } else {
                lblError.setText("Für diese Anfrage werden noch weitere Angaben benötigt.");
            }

        } catch (Exception e) {
            LOG.info("Could not get the data for progress pressure");
            lblError.setText("Die Daten konnten nicht ausgegeben werden");
        }
    }

    // Abfrage 07
    @FXML
    public void minMaxPressure() {
        clearTextAreaAndLabels();
        clearLists();

        LocalDate fromThisDate = fromDate.getValue();
        LocalDate untilDate = untillDate.getValue();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();
        try {
            if (checkIfStartBiggerThanEnd() && !cities.isEmpty() && !(fromThisDate.getMonthValue() == 0)
                    && !(untilDate.getMonthValue() == 0)) {
                List<WeatherReading> resultList = new ArrayList<>();
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);

                for (String strCities : cities) {

                    City city = rmi.findCityByName(strCities);
                    if (strCities.toLowerCase().equals(city.getName().toLowerCase())) {
                        resultList = rmiWeather.getDataForSpecificCityAndTimeInterval(fromThisDate, untilDate,
                                strCities);
                        int maxPressure = rmiWeather.getMaxPressure(resultList);
                        String maxString = Integer.toString(maxPressure);
                        listString.add(strCities);
                        listString.add("Max: " + maxString);
                        txtAreaResult.appendText(strCities + "\n");
                        txtAreaResult.appendText(maxString + "\n");
                        int minPressure = rmiWeather.getMinPressure(resultList);
                        String minString = Integer.toString(minPressure);
                        listString.add("Min: " + minString);
                        txtAreaResult.appendText(minString + "\n");

                    } else {
                        lblError.setText("Ort ist nicht in dem Datenbank.");

                    }
                }
            } else if (!checkIfStartBiggerThanEnd()) {
                lblError.setText("Das Enddatum muss nach dem Startdatum sein");

            } else {
                lblError.setText("FÃ¼r diese Anfrage werden noch weitere Angaben benÃ¶tigt.");
            }

        } catch (Exception e) {
            LOG.info("Could not get the data from last 24 hours.");
            lblError.setText("Die Daten von den letzten 24 Stunden konnten nicht ausgegeben werden.");

        }
    }

    @FXML
    private void printAsText() throws RemoteException, MalformedURLException, NotBoundException {
        if (listWeatherReading.isEmpty() ^ listString.isEmpty()) {
            FileChooser fileChooser = new javafx.stage.FileChooser();

            // Set extension filter for text files
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            FileChooser.ExtensionFilter extFilter2 = new FileChooser.ExtensionFilter("JSON files (*.json", "*.json");
            fileChooser.getExtensionFilters().add(extFilter);
            fileChooser.getExtensionFilters().add(extFilter2);

            // Show save file dialog
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                String json;
                if (!listWeatherReading.isEmpty()) {
                    json = rmiWeather.converstListtoJSON(listWeatherReading);
                } else {
                    json = rmiWeather.convertStringListToJSON(listString);
                }

                rmiWeather.getTxtFilefromList(file.getAbsolutePath(), json);
                txtAreaResult.appendText("Daten wurden unter " + file + "  gespeichert");
            }

        } else {
            lblError.setText("Es wurden keine Daten ausgewählt");
        }
    }

    private boolean checkIfStartBiggerThanEnd() {
        LocalDate fromThisDate = fromDate.getValue();
        LocalDate untilDate = untillDate.getValue();
        int compare = untilDate.compareTo(fromThisDate);
        if (compare >= 0) {
            return true;
        } else {
            return false;
        }

    }
    
  //Go back to the login page
    @FXML
    private void backToLogin() {
        try {
            
            Parent root = null;
            root = FXMLLoader.load(getClass().getClassLoader().getResource("ch/hslu/swde/wda/application/view/LoginView.fxml"));

            Stage wdaGui = (Stage) txtName.getScene().getWindow();
            wdaGui.setTitle("Wetterdaten App Login");
            wdaGui.setScene(new Scene(root, 850, 700));
            wdaGui.show();
            
        } catch (Exception e) {
            LOG.info("Could not load login page");
            lblError.setText("Login Seite konnte nicht geladen werden");
        }
        
    }

    // Method clears all output fields
    @FXML
    private void clearTextAreaAndLabels() {
        txtAreaResult.setText("");
        lblError.setText("");
    }

    // Method clears all lists with the latest results
    private void clearLists() {
        listString.clear();
        listWeatherReading.clear();
    }

    @FXML
    private void initChoiceBox() throws RemoteException {
        List<City> resultList = new ArrayList<>();
        List<String> stringListResults = new ArrayList<String>();
        try {
            VerwaltungCity rmi = Connector.getCityStub(rmiPort, serverIP);
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

    // Anfrage A08
    public void averageHumidity() throws RemoteException, MalformedURLException, NotBoundException {
        clearLists();
        clearTextAreaAndLabels();

        LocalDate lStart = fromDate.getValue();
        LocalDate lEnd = untillDate.getValue();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();

        try {
            if (!cities.isEmpty() && !(lStart.getMonthValue() == 0) && !(lEnd.getMonthValue() == 0)) {
                if (checkIfStartBiggerThanEnd()) {
                    VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                    for (String s : cities) {
                        List<WeatherReading> listTemp = new ArrayList<>();
                        listTemp = rmiWeather.getDataForSpecificCityAndTimeInterval(lStart, lEnd, s);
                        float averageHumidity = rmiWeather.getAverageHumidityForTimeInterval(listTemp);
                        txtAreaResult.appendText(s + "\n");
                        txtAreaResult.appendText("Durchschnittliche Luftfeuchtigkeit: " + averageHumidity + "\n");
                        listString.add(s);
                        listString.add(Float.toString(averageHumidity));
                        
                        
                    }

                    
                } else {
                    lblError.setText("Startdatum muss vor Enddatum sein");
                }

            } else {
                lblError.setText("Bitte Ort und Daten auswählen");
            }
        } catch (Exception e) {
            LOG.info("Could not get data");
            lblError.setText("Daten konnten nicht geladen werden");
        }

    }

    // Anfrage A09
    public void minMaxHumidity() throws RemoteException, MalformedURLException, NotBoundException {
        clearLists();
        clearTextAreaAndLabels();

//        List<WeatherReading> list = new ArrayList<>();
        LocalDate lStart = fromDate.getValue();
        LocalDate lEnd = untillDate.getValue();
        List<String> cities = lvCity.getSelectionModel().getSelectedItems();

        try {
            if (!cities.isEmpty() && !(lStart.getMonthValue() == 0) && !(lEnd.getMonthValue() == 0)) {
                if (checkIfStartBiggerThanEnd()) {
                    VerwaltungWeatherData rmiWeather = Connector.getWeatherDataStub(rmiPort, serverIP);
                    for (String s : cities) {
                        List<WeatherReading> listTemp = new ArrayList<>();
                        listTemp = rmiWeather.getDataForSpecificCityAndTimeInterval(lStart, lEnd, s);
//                        list.addAll(listTemp);
                        float minHumidity = 101;
                        float maxHumidity = -1;

                        minHumidity = rmiWeather.getMinHumidity(listTemp);
                        maxHumidity = rmiWeather.getMaxHumidity(listTemp);
                        txtAreaResult.appendText(s + "\n");
                        txtAreaResult.appendText("Min: " + minHumidity + "%" + " " + "Max: " + maxHumidity + "%" + "\n");
                        listString.add(s + "\n");
                        listString.add("Min: " + minHumidity + "%" + " " + "Max: " + maxHumidity + "%" + "\n");
                    }

                    
                } else {
                    lblError.setText("Startdatum muss vor Enddatum sein");
                }
            } else {
                lblError.setText("Bitte Ort und Daten auswählen");
            }
        } catch (Exception e) {
            LOG.info("Could not get data");
            lblError.setText("Daten konnten nicht geladen werden");
        }
    }

    // Employee --------------------

    @FXML
    public void registerEmployee() throws MalformedURLException, RemoteException, NotBoundException {
        clearTextAreaAndLabels();
        String name = txtEmployeeVorname.getText();
        String surname = txtEmployeeName.getText();
        String password = txtPwdEmployee.getText();
        String id = txtIdEmployee.getText();

        VerwaltungEmployee rmi = Connector.getEmployeeStub(rmiPort, serverIP);

        try {
            rmi.registerEmployee(new Employee(name, surname, password));
            txtAreaResult.appendText("Mitarbeiter wurde registriert" + "\n");
            Employee newUser = rmi.findEmployeeByPassword(surname, password);
            String userId = Long.toString(newUser.getId());
            txtAreaResult.appendText("Dies ist ihre Mitarbeiter ID: " + userId);
        } catch (Exception e) {
            LOG.info("Could not register new customer");
            lblError.setText("Kunde konnte nicht registriert werden");
        }
    }

    @FXML
    public void updateEmployee() throws MalformedURLException, RemoteException, NotBoundException {
        clearTextAreaAndLabels();
        String name = txtEmployeeVorname.getText();
        String surname = txtEmployeeName.getText();
        String password = txtPwdEmployee.getText();
        String id = txtIdEmployee.getText();

        VerwaltungEmployee rmi = Connector.getEmployeeStub(rmiPort, serverIP);

        try {
            long userLongId = Long.parseLong(id);
            rmi.updateEmployee(new Employee(name, surname, password), userLongId);
            txtAreaResult.appendText("Mitarbeiter wurde aktualisiert" + "\n");
            Employee newUser = rmi.findEmployeeByPassword(surname, password);
            String userId = Long.toString(newUser.getId());
            txtAreaResult.appendText("Dies ist ihre Mitarbeiter ID: " + userId);
        } catch (Exception e) {
            LOG.info("Could not update customer");
            lblError.setText("Kunde konnte nicht aktualisiert werden");
        }
    }

    @FXML
    public void searchEmployee() throws MalformedURLException, RemoteException, NotBoundException {
        clearTextAreaAndLabels();
        String name = txtEmployeeVorname.getText();
        String surname = txtEmployeeName.getText();
        String password = txtPwdEmployee.getText();
        String id = txtIdEmployee.getText();

        VerwaltungEmployee rmi = Connector.getEmployeeStub(rmiPort, serverIP);

        try {
            long userLongId = Long.parseLong(id);
            Employee findEmployee = rmi.findEmployee(userLongId);
            txtAreaResult.setText(findEmployee.toString());
        } catch (Exception e) {
            LOG.info("Could not find customer");
            lblError.setText("Mitarbeiter konnte nicht gefunden werden");
        }
    }

    @FXML
    public void deleteEmployee() throws MalformedURLException, RemoteException, NotBoundException {
        clearTextAreaAndLabels();
        String id = txtIdEmployee.getText();

        VerwaltungEmployee rmi = Connector.getEmployeeStub(rmiPort, serverIP);

        try {
            long userLongId = Long.parseLong(id);
            rmi.deletEmployee(userLongId);
            txtAreaResult.setText("Mitarbeiter wurde gelöscht");
        } catch (Exception e) {
            LOG.info("Could not delete customer");
            lblError.setText("Mitarbeiter konnte nicht gelöscht werden.");
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.rmiPort = 1099;
            this.serverIP = "10.155.103.224";
            Connector.setSecurityManager();
            initChoiceBox();

        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }

    }

}
