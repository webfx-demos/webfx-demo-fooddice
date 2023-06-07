package com.orangomango.food;

import com.orangomango.food.ui.HomeScreen;
import dev.webfx.kit.util.scene.DeviceSceneUtil;
import dev.webfx.platform.resource.Resource;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class MainApplication extends Application{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 400;
	public static final double SCALE = 1;
	public static final int FPS = 40;
	public static Stage stage;
	
	public static Media BACKGROUND_MUSIC;
	public static AudioClip DIE_SOUND;
	public static AudioClip JUMP_SOUND;
	public static AudioClip NOTIFICATION_SOUND;
	public static AudioClip LEVEL_COMPLETE_SOUND;
	public static AudioClip CLICK_SOUND;
	public static AudioClip CHECKPOINT_SOUND;
	public static AudioClip MOVE_SOUND;
	public static AudioClip COIN_SOUND;
	
	public static void main(String[] args){
		launch(args);
	}
	
	public void start(Stage stage){
		loadSounds();
		
		playMusic(BACKGROUND_MUSIC, true);
		MainApplication.stage = stage;
		HomeScreen gs = new HomeScreen();
		stage.setScene(new Scene(gs.getLayout(), WIDTH, HEIGHT, Color.BLACK));
		stage.setResizable(false);
		stage.getIcons().add(loadImage("icon.png"));
		stage.setTitle("Food Dice");
		stage.show();
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
	}

	private static final Map<String, Image> imagesCache = new HashMap<>();
	
	public static Image loadImage(String name){
		Image image = imagesCache.get(name);
		if (image == null)
			imagesCache.put(name, image = new Image(Resource.toUrl("/images/"+name, MainApplication.class), true));
		return image;
	}

	public static void onImagesLoaded(Runnable runnable){
		DeviceSceneUtil.onImagesLoaded(runnable, imagesCache.values().toArray(new Image[0]));
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
}
