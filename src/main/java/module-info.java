module MediaLab.Assistant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.json;
	requires javafx.graphics;
	requires javafx.base;

    opens MediaLab.Assistant to javafx.fxml;
    exports MediaLab.Assistant;
    exports Models;
    exports Services;
    exports Utils;
}
