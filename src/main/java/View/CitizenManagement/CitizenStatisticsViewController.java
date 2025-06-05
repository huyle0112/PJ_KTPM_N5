package view.CitizenManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
public class CitizenStatisticsViewController {
    @FXML
    private TableView<StatisticRow> statisticsTable;
    @FXML
    private TableColumn<StatisticRow, String> typeColumn;
    @FXML
    private TableColumn<StatisticRow, Long> countColumn;
    @FXML
    private Button closeButton;
    @FXML
    private PieChart pieChart;

    @FXML
    public void initialize() {
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        countColumn.setCellValueFactory(cellData -> cellData.getValue().countProperty().asObject());
    }

    public void setStatistics(StatisticsResult result) {
        ObservableList<StatisticRow> data = FXCollections.observableArrayList(
                new StatisticRow("Total", result.getTotal()),
                new StatisticRow("Permanent", result.getCountPermanent()),
                new StatisticRow("Temporary", result.getCountTemporary()),
                new StatisticRow("Away", result.getCountAway()),
                new StatisticRow("Unknown", result.getCountUnknown())
        );
        statisticsTable.setItems(data);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Permanent", result.getCountPermanent()),
                new PieChart.Data("Temporary", result.getCountTemporary()),
                new PieChart.Data("Away", result.getCountAway()),
                new PieChart.Data("Unknown", result.getCountUnknown())
        );
        pieChart.setData(pieChartData);
        pieChart.setTitle("Proportion of Citizen Types");
        pieChart.setLegendVisible(true);
        pieChart.setLabelsVisible(true);
    }

    @FXML
    private void onClose() {
        ((Stage) closeButton.getScene().getWindow()).close();
    }

    public static class StatisticRow {
        private final javafx.beans.property.SimpleStringProperty type;
        private final javafx.beans.property.SimpleLongProperty count;

        public StatisticRow(String type, long count) {
            this.type = new javafx.beans.property.SimpleStringProperty(type);
            this.count = new javafx.beans.property.SimpleLongProperty(count);
        }
        public javafx.beans.property.SimpleStringProperty typeProperty() { return type; }
        public javafx.beans.property.SimpleLongProperty countProperty() { return count; }
    }
}
