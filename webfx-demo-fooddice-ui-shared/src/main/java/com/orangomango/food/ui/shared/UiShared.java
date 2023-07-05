package com.orangomango.food.ui.shared;

import dev.webfx.extras.scalepane.ScalePane;
import dev.webfx.kit.util.scene.DeviceSceneUtil;
import dev.webfx.platform.resource.Resource;
import dev.webfx.platform.storage.LocalStorage;
import javafx.animation.Animation;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * @author Bruno Salmon
 */
public class UiShared {
    public static final ScalePane scalePane = new ScalePane();
    public static final int WIDTH = 800;
    public static final int HEIGHT = 400;
    public static final double SCALE = 1;
    public static final int FPS = 40;
    public static AudioClip CLICK_SOUND;

    private static final Font font = Font.loadFont(toUrl("/font/font.ttf"), 16);
    private static final Map<String, Image> imagesCache = new HashMap<>();

    public static Supplier<Node> homeScreenFactory;
    public static BiFunction<Integer, String, Node> gameScreenFactory;

    public static String toUrl(String path) {
        return Resource.toUrl(path, UiShared.class);
    }

    public static void setScreen(Node screenNode) {
        onFontsImagesLoaded(() -> scalePane.setNode(screenNode));
    }

    public static void goToHomeScreen() {
        setScreen(homeScreenFactory.get());
    }

    public static void goToGameScreen(int l, String ll) {
        setScreen(gameScreenFactory.apply(l, ll));
    }

    public static void setScale(double newScale) {
        scalePane.setMaxScale(newScale);
        LocalStorage.setItem("scale", String.valueOf(newScale));
    }

    public static void increaseScale() {
        setScale(scalePane.getScale() * 1.1);
    }

    public static void decreaseScale() {
        setScale(scalePane.getScale() / 1.1);
    }

    public static boolean handleScalePress(KeyCode key) {
        if (key == KeyCode.PLUS || key == KeyCode.ADD) {
            increaseScale();
            return true;
        }
        if (key == KeyCode.MINUS || key == KeyCode.SUBTRACT) {
            decreaseScale();
            return true;
        }
        return false;
    }

    public static void onFontsImagesLoaded(Runnable runnable){
        DeviceSceneUtil.onFontsAndImagesLoaded(runnable, imagesCache.values().toArray(new Image[0]));
    }

    public static void playMusic(Media media, boolean rep){
        MediaPlayer player = new MediaPlayer(media);
        if (rep) player.setCycleCount(Animation.INDEFINITE);
        else player.setOnEndOfMedia(() -> player.dispose());
        player.play();
    }

    public static void playSound(AudioClip audioClip, boolean rep){
        if (rep) audioClip.setCycleCount(Animation.INDEFINITE);
        audioClip.play();
    }

    /**
     * Clockwise: positive rot
     * Counterclockwise: negative rot
     */
    public static Point2D rotatePoint(Point2D point, double rot, double px, double py){
        rot = Math.toRadians(rot);
        double x = point.getX();
        double y = point.getY();
        x -= px;
        y -= py;
        double nx = x*Math.cos(rot)-y*Math.sin(rot);
        double ny = y*Math.cos(rot)+x*Math.sin(rot);
        return new Point2D(nx+px, ny+py);
    }

    public static Font getFont(double size) {
        return new Font(font.getFamily(), size);
    }

    public static Image loadImage(String name){
        Image image = imagesCache.get(name);
        if (image == null)
            imagesCache.put(name, image = new Image(Resource.toUrl("/images/"+name, UiShared.class), true));
        return image;
    }

    public static void loadImages(String... names) {
        for (String name: names)
            loadImage(name);
    }
}
