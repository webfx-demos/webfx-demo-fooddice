// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.demo.fooddice.application {

    // Direct dependencies modules
    requires java.base;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;

    // Exported packages
    exports com.orangomango.food;
    exports com.orangomango.food.ui;
    exports com.orangomango.food.ui.controls;

    // Provided services
    provides javafx.application.Application with com.orangomango.food.MainApplication;

}