package com.orangomango.food;

import com.orangomango.food.ui.HomeScreen;
import dev.webfx.extras.scalepane.ScalePane;
import dev.webfx.kit.util.scene.DeviceSceneUtil;
import dev.webfx.platform.os.OperatingSystem;
import dev.webfx.platform.resource.Resource;
import dev.webfx.platform.storage.LocalStorage;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class MainApplication extends Application{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 400;
	public static final double SCALE = 1;
	public static final int FPS = 40;
	private static Stage stage;
	private static ScalePane scalePane;

	public static Media BACKGROUND_MUSIC;
	public static AudioClip DIE_SOUND;
	public static AudioClip JUMP_SOUND;
	public static AudioClip NOTIFICATION_SOUND;
	public static AudioClip LEVEL_COMPLETE_SOUND;
	public static AudioClip CLICK_SOUND;
	public static AudioClip CHECKPOINT_SOUND;
	public static AudioClip MOVE_SOUND;
	public static AudioClip COIN_SOUND;
	public static AudioClip PORTAL_SOUND;

	public static void main(String[] args){
		launch(args);
	}
	
	public void start(Stage stage){
		loadSounds();

		playMusic(BACKGROUND_MUSIC, true);
		MainApplication.stage = stage;
		MainApplication.scalePane = new ScalePane();
		// For mobiles, we auto-scale to the whole window (which is the default behavior of ScalePane) because users
		// have no keyboard, but for desktops & laptops, we start with scale 2, and they can use the +/- keys to adjust
		// to their preferred scale.
		if (!OperatingSystem.isMobile()) {
			String scale = LocalStorage.getItem("scale");
			MainApplication.scalePane.setMaxScale(scale == null ? 2 : Double.parseDouble(scale));
		}
		HomeScreen gs = new HomeScreen();
		setScreen(gs.getLayout());
		Scene scene = new Scene(MainApplication.scalePane, WIDTH, HEIGHT, Color.BLACK);
		scene.setOnKeyPressed(e -> handleScalePress(e.getCode()));
		stage.setScene(scene);
		stage.setResizable(false);
		stage.getIcons().add(loadImage("icon.png"));
		stage.setTitle("Food Dice");
		stage.show();

		// These images must be preloaded, because they are used by game objects that access their size in the constructor (see Player for example)
		loadImages("player_1.png", "shooter_0.png", "laser.png", "activatorpad_0.png", "box.png", "checkpoint_on.png", "door_0.png", "fallingBlock.png", "jumppad.png", "portal.png", "propeller.png", "rotatingPlatform.png", "fire_0.png", "cactus_0.png", "spike.png");
	}

	public static void setScreen(Node screenNode) {
		MainApplication.onFontsImagesLoaded(() -> scalePane.setNode(screenNode));
	}

	public static void setScale(double newScale) {
		MainApplication.scalePane.setMaxScale(newScale);
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

	private static void loadSounds(){
		BACKGROUND_MUSIC = new Media(Resource.toUrl("/audio/background.mp3", MainApplication.class));
		DIE_SOUND = new AudioClip(Resource.toUrl("/audio/die.mp3", MainApplication.class));
		JUMP_SOUND = new AudioClip(Resource.toUrl("/audio/jump.mp3", MainApplication.class));
		NOTIFICATION_SOUND = new AudioClip(Resource.toUrl("/audio/notification.mp3", MainApplication.class));
		LEVEL_COMPLETE_SOUND = new AudioClip(Resource.toUrl("/audio/level_complete.mp3", MainApplication.class));
		CLICK_SOUND = new AudioClip(Resource.toUrl("/audio/click.mp3", MainApplication.class));
		CHECKPOINT_SOUND = new AudioClip(Resource.toUrl("/audio/checkpoint.mp3", MainApplication.class));
		MOVE_SOUND = new AudioClip(Resource.toUrl("/audio/move.mp3", MainApplication.class));
		COIN_SOUND = new AudioClip(Resource.toUrl("/audio/coin.mp3", MainApplication.class));
		PORTAL_SOUND = new AudioClip(Resource.toUrl("/audio/portal.mp3", MainApplication.class));
	}

	private static final Map<String, Image> imagesCache = new HashMap<>();

	public static Image loadImage(String name){
		Image image = imagesCache.get(name);
		if (image == null)
			imagesCache.put(name, image = new Image(Resource.toUrl("/images/"+name, MainApplication.class), true));
		return image;
	}

	public static void loadImages(String... names) {
		for (String name: names)
			loadImage(name);
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

	private static final Font font = Font.loadFont(Resource.toUrl("/font/font.ttf", Application.class), 16);

	public static Font getFont(double size) {
		return new Font(font.getFamily(), size);
	}

}
