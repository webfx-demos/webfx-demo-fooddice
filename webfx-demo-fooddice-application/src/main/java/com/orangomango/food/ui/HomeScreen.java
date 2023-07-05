package com.orangomango.food.ui;

import com.orangomango.food.ui.editor.Editor;
import com.orangomango.food.ui.shared.MenuButton;
import com.orangomango.food.ui.shared.UiShared;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen{
	private Timeline loop;
	private List<MenuButton> buttons = new ArrayList<>();
	private boolean forward = true;
	private double extraY = 1;
	private Image background = UiShared.loadImage("background_home.jpg");
	private Image logo = UiShared.loadImage("logo.png");
	
	public Canvas getLayout(){
		//StackPane layout = new StackPane();
		
		Canvas canvas = new Canvas(UiShared.WIDTH, UiShared.HEIGHT);
		canvas.setOnMousePressed(e -> {
			for (MenuButton mb : this.buttons){
				mb.click(e.getX()/ UiShared.SCALE, e.getY()/ UiShared.SCALE);
			}
		});
		//layout.getChildren().add(canvas);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFont(UiShared.getFont(25));
		
		this.buttons.add(new MenuButton("Levels", () -> {
			this.loop.stop();
			LevelsScreen ls = new LevelsScreen();
			UiShared.setScreen(ls.getLayout());
		}, 160, 230, 75, 75, UiShared.loadImage("button_play.png")));
		this.buttons.add(new MenuButton("Help", () -> {
			this.loop.stop();
			UiShared.setScreen(new HelpScreen().getLayout());
		}, 290, 230, 75, 75, UiShared.loadImage("button_help.png")));
		this.buttons.add(new MenuButton("Credits", () -> {
			this.loop.stop();
			UiShared.setScreen(new CreditsScreen().getLayout());
		}, 430, 230, 75, 75, UiShared.loadImage("button_credits.png")));
		this.buttons.add(new MenuButton("Editor", () -> {
			this.loop.stop();
			Editor ed = new Editor();
			UiShared.setScreen(ed.getLayout());
		}, 570, 230, 75, 75, UiShared.loadImage("button_editor.png")));
		
		UiShared.onFontsImagesLoaded(() -> {
			update(gc);
			this.loop = new Timeline(new KeyFrame(Duration.millis(1000.0/ UiShared.FPS), e -> update(gc)));
			this.loop.setCycleCount(Animation.INDEFINITE);
			this.loop.play();
		});
		
		return canvas;
	}
	
	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, UiShared.WIDTH, UiShared.HEIGHT);
		gc.drawImage(this.background, 0, 0, UiShared.WIDTH, UiShared.HEIGHT);
		
		gc.save();
		gc.scale(UiShared.SCALE, UiShared.SCALE);
		gc.drawImage(this.logo, 165, 50);
		gc.translate(0, this.extraY);
		for (MenuButton mb : this.buttons){
			mb.render(gc);
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
