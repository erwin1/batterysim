package io.github.erwin1.batterysim.app;

import io.github.erwin1.batterysim.stats.PriceConfig;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class ConfigPane extends Pane {
    private File file;
    private Label error;

    public ConfigPane(App parent, Stage stage) {
        TextField batterySize = createNumericField(parent.batteryCapacity.toString());
        TextField batterPrice = createNumericField(parent.batteryPrice.toString());
        TextField priceInNight = createNumericField(parent.priceConfig.getPriceInNight().toString());
        TextField priceInDay = createNumericField(parent.priceConfig.getPriceInDay().toString());
        TextField priceOutNight = createNumericField(parent.priceConfig.getPriceOutNight().toString());
        TextField priceOutDay = createNumericField(parent.priceConfig.getPriceOutDay().toString());
        TextField chargeEfficiency = createNumericField(parent.batteryChargeEfficiency.multiply(new BigDecimal(100)).toString());
        TextField dechargeEfficiency = createNumericField(parent.batteryDechargeEfficiency.multiply(new BigDecimal(100)).toString());
        Button start = new Button("Calculate");


        Button fileButton = new Button("Select csv...");
        Label fileLabel = new Label("no csv file selected");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open csv logfile");
        fileButton.setOnAction(e -> {
            file = fileChooser.showOpenDialog(stage);
            fileLabel.setText(file.getName());
        });


        start.setOnAction(e -> {
            if (file == null) {
                error.setText("Please select a CSV file.");
                error.setVisible(true);
            } else {
                error.setText("");
                error.setVisible(false);
                try {
                    parent.batteryCapacity = new BigDecimal(batterySize.getText());
                    parent.priceConfig = new PriceConfig(
                            new BigDecimal(priceInDay.getText()),
                            new BigDecimal(priceOutDay.getText()),
                            new BigDecimal(priceInNight.getText()),
                            new BigDecimal(priceOutNight.getText())
                    );
                    parent.batteryPrice = new BigDecimal(batterPrice.getText());
                    parent.batteryChargeEfficiency = new BigDecimal(chargeEfficiency.getText()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                    parent.batteryDechargeEfficiency = new BigDecimal(dechargeEfficiency.getText()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                    parent.csvLogFile = file.getAbsolutePath();
                    parent.initApp();
                } catch (Exception ex) {
                    error.setText("All fields are mandatory");
                    error.setVisible(true);
                    ex.printStackTrace();
                }
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.add(new Label("CSV logfile"), 0, 0, 1, 1);
        gridPane.add(fileButton, 1, 0, 1, 1);
        gridPane.add(fileLabel, 1, 1, 1, 1);

        gridPane.add(new Label("Battery size (kWh)"), 0, 2, 1, 1);
        gridPane.add(batterySize, 1, 2, 1, 1);
        gridPane.add(new Label("Battery price (EUR)"), 0, 3, 1, 1);
        gridPane.add(batterPrice, 1, 3, 1, 1);
        gridPane.add(new Label("Price consumption - day (EUR)"), 0, 4, 1, 1);
        gridPane.add(priceInDay, 1, 4, 1, 1);
        gridPane.add(new Label("Price consumption - night (EUR)"), 0, 5, 1, 1);
        gridPane.add(priceInNight, 1, 5, 1, 1);
        gridPane.add(new Label("Reimbursement grid injection - day (EUR)"), 0, 6, 1, 1);
        gridPane.add(priceOutDay, 1, 6, 1, 1);
        gridPane.add(new Label("Reimbursement grid injection - night (EUR)"), 0, 7, 1, 1);
        gridPane.add(priceOutNight, 1, 7, 1, 1);
        gridPane.add(new Label("Battery charge efficiency (%)"), 0, 8, 1, 1);
        gridPane.add(chargeEfficiency, 1, 8, 1, 1);
        gridPane.add(new Label("Battery decharge efficiency (%)"), 0, 9, 1, 1);
        gridPane.add(dechargeEfficiency, 1, 9, 1, 1);
        gridPane.add(start, 1, 10, 1, 1);

        gridPane.setHgap(15);
        gridPane.setVgap(15);

        Label title = new Label("Configuration");
        title.getStyleClass().add("title");

        error = new Label("");
        error.setVisible(false);
        error.getStyleClass().add("error");

        VBox vBox = new VBox(title, error, gridPane);
        VBox.setMargin(title, new Insets(20, 0, 0, 30));
        VBox.setMargin(error, new Insets(20, 0, 0, 30));
        VBox.setMargin(gridPane, new Insets(20, 0, 0, 30));

        getChildren().add(vBox);
    }

    private TextField createNumericField(String text) {
        TextField textField = new TextField();
        textField.setText(text);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d\\.]", ""));
            }
        });
        return textField;
    }
}
