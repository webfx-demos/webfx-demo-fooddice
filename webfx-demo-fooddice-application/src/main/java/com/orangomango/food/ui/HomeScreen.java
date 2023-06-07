package com.orangomango.food.ui;

import com.orangomango.food.MainApplication;
import dev.webfx.platform.resource.Resource;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen{
	private Timeline loop;
	private List<MenuButton> buttons = new ArrayList<>();
	private boolean forward = true;
	private double extraY = 1;
	private Image background = MainApplication.loadImage("background_home.jpg");
	private Image logo = MainApplication.loadImage("logo.png");
	
	public StackPane getLayout(){		
		StackPane layout = new StackPane();
		
		Canvas canvas = new Canvas(MainApplication.WIDTH, MainApplication.HEIGHT);
		canvas.setOnMousePressed(e -> {
			for (MenuButton mb : this.buttons){
				mb.click(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE);
			}
		});
		layout.getChildren().add(canvas);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFont(Font.loadFont(Resource.toUrl("/font/font.ttf", getClass()), 25));

		Image playButtonImage = MainApplication.loadImage("button_play.png");
		this.buttons.add(new MenuButton(() -> {
			this.loop.stop();
			LevelsScreen ls = new LevelsScreen();
			MainApplication.stage.getScene().setRoot(ls.getLayout());
		}, 148, 230, 75, 75, playButtonImage));
		Image helpButtonImage = MainApplication.loadImage("button_help.png");
		this.buttons.add(new MenuButton(() -> {
			this.loop.stop();
			HelpScreen hs = new HelpScreen();
			MainApplication.stage.getScene().setRoot(hs.getLayout());
		}, 363, 230, 75, 75, helpButtonImage));
		Image creditsButtonImage = MainApplication.loadImage("button_credits.png");
		this.buttons.add(new MenuButton(() -> {
			this.loop.stop();
			CreditsScreen cs = new CreditsScreen();
			MainApplication.stage.getScene().setRoot(cs.getLayout());
		}, 578, 230, 75, 75, creditsButtonImage));
		/*this.buttons.add(new MenuButton(() -> {
			this.loop.stop();
			Editor ed = new Editor();
			MainApplication.stage.getScene().setRoot(ed.getLayout());
		}, 570, 230, 75, 75, MainApplication.loadImage("button_editor.png")));*/

		MainApplication.onImagesLoaded(() -> {
			this.loop = new Timeline(new KeyFrame(Duration.millis(1000.0/MainApplication.FPS), e -> update(gc)));
			this.loop.setCycleCount(Animation.INDEFINITE);
			this.loop.play();
		});

		return layout;
	}
	
	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
		gc.drawImage(this.background, 0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
		
		gc.save();
		gc.scale(MainApplication.SCALE, MainApplication.SCALE);
		gc.drawImage(this.logo, 165, 50);
		gc.translate(0, this.extraY);
		String[] texts = new String[]{"Levels", "Help", "Credits"/*, "Editor"*/};
		int c = 0;
		for (MenuButton mb : this.buttons){
			mb.render(gc);
			gc.setFill(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText(texts[c++], mb.getX(), mb.getY()+30);
		}
		gc.restore();
		
		this.extraY += this.forward ? 0.1 : -0.1;
		if (this.extraY >= 3){
			this.forward = false;
		} else if (this.extraY <= 0){
			this.forward = true;
		}
	}
}
