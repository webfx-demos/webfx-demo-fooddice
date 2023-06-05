package com.orangomango.food;

import com.orangomango.food.ui.HomeScreen;
import dev.webfx.platform.resource.Resource;
import javafx.animation.Animation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainApplication extends Application{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 400;
	public static final double SCALE = 1;
	public static final int FPS = 40;
	public static Stage stage;
	
	public static Media BACKGROUND_MUSIC;
	public static Media DIE_SOUND;
	public static Media JUMP_SOUND;
	public static Media NOTIFICATION_SOUND;
	public static Media LEVEL_COMPLETE_SOUND;
	public static Media CLICK_SOUND;
	public static Media CHECKPOINT_SOUND;
	public static Media MOVE_SOUND;
	public static Media COIN_SOUND;
	
	public static void main(String[] args){
		launch(args);
	}
	
	public void start(Stage stage){
		loadSounds();
		
		playSound(BACKGROUND_MUSIC, true);
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
		DIE_SOUND = new Media(Resource.toUrl("/audio/die.wav", MainApplication.class));
		JUMP_SOUND = new Media(Resource.toUrl("/audio/jump.wav", MainApplication.class));
		NOTIFICATION_SOUND = new Media(Resource.toUrl("/audio/notification.wav", MainApplication.class));
		LEVEL_COMPLETE_SOUND = new Media(Resource.toUrl("/audio/level_complete.wav", MainApplication.class));
		CLICK_SOUND = new Media(Resource.toUrl("/audio/click.wav", MainApplication.class));
		CHECKPOINT_SOUND = new Media(Resource.toUrl("/audio/checkpoint.wav", MainApplication.class));
		MOVE_SOUND = new Media(Resource.toUrl("/audio/move.wav", MainApplication.class));
		COIN_SOUND = new Media(Resource.toUrl("/audio/coin.wav", MainApplication.class));
	}
	
	public static Image loadImage(String name){
		return new Image(Resource.toUrl("/images/"+name, MainApplication.class), true);
	}
	
	public static void playSound(Media media, boolean rep){
		MediaPlayer player = new MediaPlayer(media);
		if (rep) player.setCycleCount(Animation.INDEFINITE);
		else player.setOnEndOfMedia(() -> player.dispose());
		player.play();
	}
}
