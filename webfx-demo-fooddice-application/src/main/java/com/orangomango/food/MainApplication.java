package com.orangomango.food;

import com.orangomango.food.ui.GameScreen;
import com.orangomango.food.ui.HomeScreen;
import com.orangomango.food.ui.shared.UiShared;
import dev.webfx.platform.os.OperatingSystem;
import dev.webfx.platform.storage.LocalStorage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApplication extends Application{

	public static Media BACKGROUND_MUSIC;
	public static AudioClip DIE_SOUND;
	public static AudioClip JUMP_SOUND;
	public static AudioClip NOTIFICATION_SOUND;
	public static AudioClip LEVEL_COMPLETE_SOUND;
	public static AudioClip CHECKPOINT_SOUND;
	public static AudioClip MOVE_SOUND;
	public static AudioClip COIN_SOUND;
	public static AudioClip PORTAL_SOUND;

	public static void main(String[] args){
		launch(args);
	}
	
	public void start(Stage stage){
		loadSounds();

		UiShared.playMusic(BACKGROUND_MUSIC, true);
		// For mobiles, we auto-scale to the whole window (which is the default behavior of ScalePane) because users
		// have no keyboard, but for desktops & laptops, we start with scale 2, and they can use the +/- keys to adjust
		// to their preferred scale.
		if (!OperatingSystem.isMobile()) {
			String scale = LocalStorage.getItem("scale");
			UiShared.setScale(scale == null ? 2 : Double.parseDouble(scale));
		}
		UiShared.homeScreenFactory = () -> new HomeScreen().getLayout();
		UiShared.gameScreenFactory = (l, ll) -> new GameScreen(l, ll).getLayout();

		UiShared.goToHomeScreen();
		Scene scene = new Scene(UiShared.scalePane, UiShared.WIDTH, UiShared.HEIGHT, Color.BLACK);
		scene.setOnKeyPressed(e -> UiShared.handleScalePress(e.getCode()));
		stage.setScene(scene);
		stage.setResizable(false);
		stage.getIcons().add(UiShared.loadImage("icon.png"));
		stage.setTitle("Food Dice");
		stage.show();

		// These images must be preloaded, because they are used by game objects that access their size in the constructor (see Player for example)
		UiShared.loadImages("player_1.png", "shooter_0.png", "laser.png", "activatorpad_0.png", "box.png", "checkpoint_on.png", "door_0.png", "fallingBlock.png", "jumppad.png", "portal.png", "propeller.png", "rotatingPlatform.png", "fire_0.png", "cactus_0.png", "spike.png");
	}

	private static void loadSounds(){
		BACKGROUND_MUSIC = new Media(UiShared.toUrl("/audio/background.mp3"));
		DIE_SOUND = new AudioClip(UiShared.toUrl("/audio/die.mp3"));
		JUMP_SOUND = new AudioClip(UiShared.toUrl("/audio/jump.mp3"));
		NOTIFICATION_SOUND = new AudioClip(UiShared.toUrl("/audio/notification.mp3"));
		LEVEL_COMPLETE_SOUND = new AudioClip(UiShared.toUrl("/audio/level_complete.mp3"));
		UiShared.CLICK_SOUND = new AudioClip(UiShared.toUrl("/audio/click.mp3"));
		CHECKPOINT_SOUND = new AudioClip(UiShared.toUrl("/audio/checkpoint.mp3"));
		MOVE_SOUND = new AudioClip(UiShared.toUrl("/audio/move.mp3"));
		COIN_SOUND = new AudioClip(UiShared.toUrl("/audio/coin.mp3"));
		PORTAL_SOUND = new AudioClip(UiShared.toUrl("/audio/portal.mp3"));
	}

}
