// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.demo.fooddice.application {

    // Direct dependencies modules
    requires java.base;
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.media;
    requires webfx.demo.fooddice.editor;
    requires webfx.demo.fooddice.ui.shared;
    requires webfx.platform.ast;
    requires webfx.platform.os;
    requires webfx.platform.resource;
    requires webfx.platform.scheduler;
    requires webfx.platform.storage;
    requires webfx.platform.windowlocation;

    // Exported packages
    exports com.orangomango.food;
    exports com.orangomango.food.ui;
    exports com.orangomango.food.ui.controls;

    // Resources packages
    opens audio;
    opens font;
    opens images;
    opens levels;

    // Provided services
    provides javafx.application.Application with com.orangomango.food.MainApplication;

}