package com.orangomango.food.ui;

import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;

import com.orangomango.food.MainApplication;

public class WinScreen{
	private Image background = MainApplication.loadImage("background_home.jpg");

	public Canvas getLayout(){
		//StackPane layout = new StackPane();
		
		Canvas canvas = new Canvas(MainApplication.WIDTH, MainApplication.HEIGHT);
		//layout.getChildren().add(canvas);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		MenuButton home = new MenuButton("", () -> {
			HomeScreen hs = new HomeScreen();
			MainApplication.setScreen(hs.getLayout());
		}, 50, 300, 75, 75, MainApplication.loadImage("button_home.png"));
		canvas.setOnMousePressed(e -> home.click(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE));

		MainApplication.onImagesLoaded(() -> {
			gc.drawImage(this.background, 0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
			gc.scale(MainApplication.SCALE, MainApplication.SCALE);
			gc.setFont(MainApplication.getFont(50));
			gc.setFill(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("YOU WIN!", 400, 100);
			gc.setFont(MainApplication.getFont(35));
			gc.fillText("Thanks for playing", 400, 200);
			home.render(gc);
		});

		return canvas;
	}
}
