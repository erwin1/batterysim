package io.github.erwin1.batterysim.app;

import io.github.erwin1.batterysim.logimporter.FluviusLog;
import io.github.erwin1.batterysim.logimporter.FluviusLogParser;
import io.github.erwin1.batterysim.logimporter.FluviusLogToUsageConverter;
import io.github.erwin1.batterysim.simulator.BatterySimulator;
import io.github.erwin1.batterysim.stats.MonthSummary;
import io.github.erwin1.batterysim.stats.YearBreakdown;
import io.github.erwin1.batterysim.stats.PriceConfig;
import io.github.erwin1.batterysim.stats.Summary;
import io.github.erwin1.batterysim.usage.Usage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {
    List<Usage> usages;
    List<Usage> usagesWithBattery;
    List<MonthSummary> monthSummariesWithoutBattery;
    List<MonthSummary> monthSummariesWithBattery;
    Summary summaryWithoutBattery;
    Summary summaryWithBattery;
    LocalDateTime first;
    LocalDateTime last;
    BigDecimal pct;
    BigDecimal yearEstimate;
    BigDecimal batteryCapacity = new BigDecimal("10.0");
    BigDecimal batteryDechargeEfficiency = new BigDecimal("0.9");
    BigDecimal batteryChargeEfficiency = new BigDecimal("0.9");
    BigDecimal batteryPrice = new BigDecimal("4000");
    PriceConfig priceConfig = new PriceConfig(
                new BigDecimal("0.35"),
                new BigDecimal("0.045"),
                new BigDecimal("0.25"),
                new BigDecimal("0.0225"));
    ResultPane resultPane;
    DetailsPane detailsPane;
    TabPane tabPane;
    String csvLogFile;

    @Override
    public void start(Stage stage) throws IOException {
        ConfigPane configPane = new ConfigPane(this, stage);
        resultPane = new ResultPane(this);
        detailsPane = new DetailsPane(this);

        tabPane = new TabPane();
        Tab configTab = new Tab("Configuration", configPane);
        Tab resultsTab = new Tab("Results", resultPane);
        Tab detailsTab = new Tab("Details" , detailsPane);
        configTab.setClosable(false);
        resultsTab.setClosable(false);
        detailsTab.setClosable(false);

        tabPane.getTabs().add(configTab);
        tabPane.getTabs().add(resultsTab);
        tabPane.getTabs().add(detailsTab);

        resultsTab.setDisable(true);
        detailsTab.setDisable(true);

        VBox vBox = new VBox(tabPane);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("style.css");
        stage.setTitle("Battery simulator");

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public void initApp() throws IOException {
        BatterySimulator batterySimulator = new BatterySimulator(batteryCapacity, batteryChargeEfficiency, batteryDechargeEfficiency);
        FluviusLogParser logParser = new FluviusLogParser();
        FluviusLogToUsageConverter logToUsageConverter = new FluviusLogToUsageConverter();

        List<FluviusLog> logs = logParser.parseUsages(Paths.get(csvLogFile));

        usages = logToUsageConverter.convert(logs);
        usagesWithBattery = batterySimulator.simulateBattery(usages);

        monthSummariesWithoutBattery = MonthSummary.calculateMonthSummary(usages, priceConfig);
        monthSummariesWithBattery = MonthSummary.calculateMonthSummary(usagesWithBattery, priceConfig);

        summaryWithoutBattery = Summary.calculateSummary(usages, priceConfig);
        summaryWithBattery = Summary.calculateSummary(usagesWithBattery, priceConfig);

        first = usages.get(0).getDateTimeInterval().getStartDate();
        last = usages.get(usages.size() - 1).getDateTimeInterval().getEndDate();
        YearBreakdown yearBreakdown = new YearBreakdown();
        pct = yearBreakdown.calculatePercentageOfFullYear(first, last);
        yearEstimate = summaryWithoutBattery.getTotalPrice().subtract(summaryWithBattery.getTotalPrice())
                .divide(pct, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));

        resultPane.initView();
        detailsPane.initView();

        tabPane.getTabs().get(1).setDisable(false);
        tabPane.getTabs().get(2).setDisable(false);

        tabPane.getSelectionModel().select(1);
    }


    public static void main(String[] args) {
        launch();
    }

}