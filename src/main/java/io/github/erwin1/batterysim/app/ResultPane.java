package io.github.erwin1.batterysim.app;

import io.github.erwin1.batterysim.stats.MonthSummary;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class ResultPane extends ScrollPane {
    App parent;

    public ResultPane(App parent) {
        this.parent = parent;
    }

    public void initView() {
        BarChart barChartConsumption = createBarChartConsumption(parent.monthSummariesWithBattery);
        BarChart barChartInjection = createBarChartInjection(parent.monthSummariesWithBattery);
        BarChart barChartPrice = createBarChartPrice(parent.monthSummariesWithBattery, parent.monthSummariesWithoutBattery);

        Label lblWithoutBattery = new Label("Without battery");
        lblWithoutBattery.getStyleClass().add("heading");

        GridPane gridPane = new GridPane();
        gridPane.add(lblWithoutBattery, 0, 0, 2, 1);
        gridPane.add(new Label("Grid consumption (day)"), 0, 1, 1, 1);
        gridPane.add(new Label(formatKWh(parent.summaryWithoutBattery.getAmountInDay())), 1, 1, 1, 1);
        gridPane.add(new Label("Grid consumption (night)"), 0, 2, 1, 1);
        gridPane.add(new Label(formatKWh(parent.summaryWithoutBattery.getAmountInNight())), 1, 2, 1, 1);
        gridPane.add(new Label("Grid consumption (total)"), 0, 3, 1, 1);
        gridPane.add(new Label(formatKWh(parent.summaryWithoutBattery.getTotalAmountIn())), 1, 3, 1, 1);
        gridPane.add(new Label("Total price"), 0, 4, 1, 1);
        gridPane.add(new Label(formatPrice(parent.summaryWithoutBattery.getTotalPrice())), 1, 4, 1, 1);

        Label lblWithBattery = new Label("With simulated battery");
        lblWithBattery.getStyleClass().add("heading");

        GridPane gridPane2 = new GridPane();
        gridPane2.add(lblWithBattery, 0, 0, 2, 1);
        gridPane2.add(new Label("Grid consumption (day)"), 0, 1, 1, 1);
        gridPane2.add(new Label(formatKWh(parent.summaryWithBattery.getAmountInDay())), 1, 1, 1, 1);
        gridPane2.add(new Label("Grid consumption (night)"), 0, 2, 1, 1);
        gridPane2.add(new Label(formatKWh(parent.summaryWithBattery.getAmountInNight())), 1, 2, 1, 1);
        gridPane2.add(new Label("Grid consumption (total)"), 0, 3, 1, 1);
        gridPane2.add(new Label(formatKWh(parent.summaryWithBattery.getTotalAmountIn())), 1, 3, 1, 1);
        gridPane2.add(new Label("Total price"), 0, 4, 1, 1);
        gridPane2.add(new Label(formatPrice(parent.summaryWithBattery.getTotalPrice())), 1, 4, 1, 1);

        Label lblSavings = new Label("Savings");
        lblSavings.getStyleClass().add("heading");

        GridPane gridPane3 = new GridPane();
        gridPane3.add(lblSavings, 0, 0, 2, 1);
        gridPane3.add(new Label("Total in period"), 0, 1, 1, 1);
        gridPane3.add(new Label(formatPrice(parent.summaryWithoutBattery.getTotalPrice().subtract(parent.summaryWithBattery.getTotalPrice()))), 1, 1, 1, 1);
        gridPane3.add(new Label("Full year data"), 0, 2, 1, 1);
        gridPane3.add(new Label(format(parent.pct, "%")), 1, 2, 1, 1);
        gridPane3.add(new Label("Estimated saving in one year"), 0, 3, 1, 1);
        gridPane3.add(new Label(formatPrice(parent.yearEstimate)), 1, 3, 1, 1);
        gridPane3.add(new Label("Return on investment"), 0, 4, 1, 1);
        gridPane3.add(new Label(format(parent.batteryPrice.divide(parent.yearEstimate, 2, RoundingMode.HALF_UP), "years")), 1, 4, 1, 1);

        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane2.setHgap(20);
        gridPane2.setVgap(20);
        gridPane3.setHgap(20);
        gridPane3.setVgap(20);

        HBox hBox = new HBox(gridPane, gridPane2, gridPane3);
        hBox.setSpacing(50);
        HBox.setMargin(gridPane, new Insets(20, 20, 20, 20));
        HBox.setMargin(gridPane2, new Insets(20, 20, 20, 20));
        HBox.setMargin(gridPane3, new Insets(20, 20, 20, 20));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Label summaryLbl = new Label("Results for data from "+fmt.format(parent.first)+" to "+fmt.format(parent.last));
        summaryLbl.getStyleClass().add("title");
        VBox textSummary = new VBox(summaryLbl, hBox);

        VBox resultVBox = new VBox(
                textSummary,
                barChartConsumption,
                barChartInjection,
                barChartPrice);
        resultVBox.setAlignment(Pos.CENTER);

        VBox.setMargin(textSummary, new Insets(20, 20, 20, 20));

        setContent(resultVBox);
        fitToWidthProperty().set(true);

    }


    private BarChart createBarChartConsumption(List<MonthSummary> monthSummaries) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Months");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("kWh");

        BarChart<String, BigDecimal> barChart = new BarChart(xAxis, yAxis);
        barChart.setId("consumptionchart");

        XYChart.Series<String, BigDecimal> dataSeries1 = new XYChart.Series<>();
        dataSeries1.setName("Grid consumption without battery");
        XYChart.Series<String, BigDecimal> dataSeries2 = new XYChart.Series<>();
        dataSeries2.setName("Grid consumption with simulated battery");
        barChart.getData().add(dataSeries1);
        barChart.getData().add(dataSeries2);

        for(MonthSummary monthSummary : monthSummaries) {
            String label = monthSummary.getMonth().getMonth().toString() + " "+monthSummary.getMonth().getYear();
            dataSeries1.getData().add(new XYChart.Data<>(label,
                    monthSummary.getSummary().getTotalAmountIn().add(monthSummary.getSummary().getAmountInFromBattery())));
            dataSeries2.getData().add(new XYChart.Data<>(label,
                    monthSummary.getSummary().getTotalAmountIn()));
        }

        setTooltips(dataSeries1, "Without battery", "kWh");
        setTooltips(dataSeries2, "With simulated battery", "kWh");
        return barChart;
    }

    private void setTooltips(XYChart.Series<String, BigDecimal> series, String s, String unit) {
        for (final XYChart.Data<String, BigDecimal> data : series.getData()) {
            Tooltip tooltip = new Tooltip();
            tooltip.setText(s + "\n" + data.getXValue() + "\n" + format(data.getYValue(), unit));
            Tooltip.install(data.getNode(), tooltip);
        }
    }

    private BarChart createBarChartInjection(List<MonthSummary> monthSummaries) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Months");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("kWh");

        BarChart barChart = new BarChart(xAxis, yAxis);
        barChart.setId("gridinjectionchart");

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Grid injection without battery");
        XYChart.Series dataSeries2 = new XYChart.Series();
        dataSeries2.setName("Grid injection with simulated battery");
        barChart.getData().add(dataSeries1);
        barChart.getData().add(dataSeries2);

        for(MonthSummary monthSummary : monthSummaries) {
            String label = monthSummary.getMonth().getMonth().toString() + " "+monthSummary.getMonth().getYear();
            dataSeries1.getData().add(new XYChart.Data(label,
                    monthSummary.getSummary().getTotalAmountOut().add(monthSummary.getSummary().getAmountOutToBattery())));

            dataSeries2.getData().add(new XYChart.Data(label,
                    monthSummary.getSummary().getTotalAmountOut()));
        }
        setTooltips(dataSeries1, "Without battery", "kWh");
        setTooltips(dataSeries2, "With simulated battery", "kWh");

        return barChart;
    }


    private BarChart createBarChartPrice(List<MonthSummary> monthSummariesWithBattery, List<MonthSummary> monthSummariesWithoutBattery) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Months");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Price EUR");

        BarChart barChart = new BarChart(xAxis, yAxis);
        barChart.setId("pricechart");

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Price without battery");
        XYChart.Series dataSeries2 = new XYChart.Series();
        dataSeries2.setName("Price with simulated battery");
        barChart.getData().add(dataSeries1);
        barChart.getData().add(dataSeries2);

        for(MonthSummary monthSummary : monthSummariesWithBattery) {
            MonthSummary monthSummaryWithoutBattery = monthSummariesWithoutBattery.stream().filter(s -> s.getMonth().equals(monthSummary.getMonth())).findFirst().get();
            String label = monthSummary.getMonth().getMonth().toString() + " "+monthSummary.getMonth().getYear();
            dataSeries1.getData().add(new XYChart.Data(label,
                    monthSummaryWithoutBattery.getSummary().getTotalPrice()));

            dataSeries2.getData().add(new XYChart.Data(label,
                    monthSummary.getSummary().getTotalPrice()));
        }
        setTooltips(dataSeries1, "Without battery", "EUR");
        setTooltips(dataSeries2, "With simulated battery", "EUR");

        return barChart;
    }

    private String formatPrice(BigDecimal d) {
        return format(d, "EUR");
    }

    private String formatKWh(BigDecimal d) {
        return format(d, "kWh");
    }

    private String format(BigDecimal d, String unit) {
        return d.setScale(2, RoundingMode.HALF_UP)+" "+unit;
    }

}
