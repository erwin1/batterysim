package io.github.erwin1.batterysim.app;

import io.github.erwin1.batterysim.usage.Usage;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class DetailsPane extends ScrollPane {
    App parent;
    private ObservableList<XYChart.Data> dataInjection;
    private ObservableList<XYChart.Data> dataCharging;
    private ObservableList<XYChart.Data> dataDecharging;
    private ObservableList<XYChart.Data> dataConsumption;
    private ObservableList<XYChart.Data> dataBatteryLevel;


    public DetailsPane(App parent) {
        this.parent = parent;
    }

    public void initView() {
        final DatePicker datePicker = new DatePicker(parent.first.toLocalDate());
        datePicker.setOnAction(event -> {
            dateChanged(datePicker.getValue());
        });
        LineChart lineChartBatteryLevel = getLineChartBattery(parent.usagesWithBattery);
        StackedAreaChart stackedLineChart = createStackedAreaChart(parent.usages, parent.usagesWithBattery);
        StackedAreaChart stackedLineChartInjection = createStackedAreaChartInjection(parent.usages, parent.usagesWithBattery);
        Label lblDetail = new Label("Daily details with simulated battery");
        lblDetail.getStyleClass().add("title");

        Button bckButton = new Button("  <  ");
        bckButton.setOnAction(e -> {
            datePicker.setValue(datePicker.getValue().plus(-1, ChronoUnit.DAYS));
            dateChanged(datePicker.getValue());
        });
        Button fwdButton = new Button("  >  ");
        fwdButton.setOnAction(e -> {
            datePicker.setValue(datePicker.getValue().plus(1, ChronoUnit.DAYS));
            dateChanged(datePicker.getValue());
        });

        HBox datePickerBox = new HBox(bckButton, datePicker, fwdButton);
        datePickerBox.setSpacing(50);
        datePickerBox.setAlignment(Pos.CENTER);

        VBox vbox2 = new VBox(lblDetail, datePickerBox, stackedLineChart, stackedLineChartInjection, lineChartBatteryLevel);
        VBox.setMargin(datePickerBox, new Insets(30, 30, 30, 30));
        vbox2.setAlignment(Pos.CENTER);

        setContent(vbox2);
        fitToWidthProperty().set(true);

        dateChanged(datePicker.getValue());
    }


    private void dateChanged(LocalDate localDate) {
        dataInjection.clear();
        dataCharging.clear();
        dataDecharging.clear();
        dataConsumption.clear();
        dataBatteryLevel.clear();


        final AtomicInteger counter = new AtomicInteger(0);
        parent.usagesWithBattery.stream().filter(u -> u.getDateTimeInterval().getStartDate().toLocalDate().equals(localDate))
                .forEach(u -> {
                    BigDecimal relativePerHour = u.getDateTimeInterval().relativePerHour();
                    double hour = counter.incrementAndGet() * relativePerHour.doubleValue();
                    dataInjection.add(new XYChart.Data(hour, u.getAmountOut().multiply(new BigDecimal(1000)).divide(relativePerHour, 2, RoundingMode.HALF_UP).doubleValue()));
                    dataCharging.add(new XYChart.Data(hour, u.getAmountOutToBattery().multiply(new BigDecimal(1000)).divide(relativePerHour, 2, RoundingMode.HALF_UP).doubleValue()));

                    dataDecharging.add(new XYChart.Data(hour, u.getAmountInFromBattery().multiply(new BigDecimal(1000)).divide(relativePerHour, 2, RoundingMode.HALF_UP).doubleValue()));
                    dataConsumption.add(new XYChart.Data(hour, u.getAmountIn().multiply(new BigDecimal(1000)).divide(relativePerHour, 2, RoundingMode.HALF_UP).doubleValue()));

                    dataBatteryLevel.add(new XYChart.Data(hour, u.getBatteryLevel().multiply(new BigDecimal(1000)).doubleValue()));

                });
    }


    private LineChart getLineChartBattery(List<Usage> usages) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Wh");

        LineChart lineChart = new LineChart(xAxis, yAxis);

        XYChart.Series dataSeries1 = null;
        dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Battery level");

        dataBatteryLevel = dataSeries1.getData();

        lineChart.getData().add(dataSeries1);
        return lineChart;
    }


    private StackedAreaChart createStackedAreaChart(List<Usage> usages, List<Usage> usagesWithBattery) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Power (W)");

        StackedAreaChart stackedAreaChart = new StackedAreaChart(xAxis, yAxis);
        stackedAreaChart.setId("consumptiondetailschart");

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Battery consumption");
        stackedAreaChart.getData().add(dataSeries1);

        XYChart.Series dataSeries2 = new XYChart.Series();
        dataSeries2.setName("Grid consumption");
        stackedAreaChart.getData().add(dataSeries2);

        dataDecharging = dataSeries1.getData();
        dataConsumption = dataSeries2.getData();

        return stackedAreaChart;
    }


    private StackedAreaChart createStackedAreaChartInjection(List<Usage> usages, List<Usage> usagesWithBattery) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Power (W)");

        StackedAreaChart stackedAreaChart = new StackedAreaChart(xAxis, yAxis);
        stackedAreaChart.setId("injectiondetailschart");

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Battery charging");
        stackedAreaChart.getData().add(dataSeries1);

        XYChart.Series dataSeries2 = new XYChart.Series();
        dataSeries2.setName("Grid Injection");
        stackedAreaChart.getData().add(dataSeries2);

        dataCharging = dataSeries1.getData();
        dataInjection = dataSeries2.getData();

        return stackedAreaChart;
    }


}
