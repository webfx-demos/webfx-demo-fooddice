package com.orangomango.food.ui.shared;

import javafx.scene.image.Image;

/**
 * @author Bruno Salmon
 */
public enum PlatformType {
    SMALL(68, 20, "platform_small.png"),
    MEDIUM(100, 20, "platform_medium.png");

    private double width;
    private double height;
    private Image image;

    private PlatformType(double w, double h, String imageName) {
        this.width = w;
        this.height = h;
        this.image = UiShared.loadImage(imageName);
    }

    public double getHeight() {
        return this.height;
    }

    public double getWidth() {
        return this.width;
    }

    public Image getImage() {
        return this.image;
    }
}
