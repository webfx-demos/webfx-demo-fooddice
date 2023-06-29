package com.orangomango.food.ui;

import dev.webfx.platform.windowlocation.WindowLocation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;

import com.orangomango.food.MainApplication;

public class CreditsScreen{
	private Image background = MainApplication.loadImage("background_home.jpg");

	public Canvas getLayout(){
		//StackPane layout = new StackPane();
		
		Canvas canvas = new Canvas(MainApplication.WIDTH, MainApplication.HEIGHT);
		//layout.getChildren().add(canvas);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Rectangle2D link = new Rectangle2D(150, 265, 500, 30);

		MenuButton home = new MenuButton("", () -> {
			HomeScreen hs = new HomeScreen();
			MainApplication.setScreen(hs.getLayout());
		}, 50, 300, 75, 75, MainApplication.loadImage("button_home.png"));

		canvas.setOnMousePressed(e -> {
			if (link.contains(e.getX(), e.getY())){
				WindowLocation.assignHref("https://github.com/OrangoMango/FoodDice");
			} else {
				home.click(e.getX(), e.getY());
			}
		});

		canvas.setOnMouseMoved(e -> {
			if (link.contains(e.getX(), e.getY())){
				canvas.setCursor(Cursor.HAND);
			} else {
				canvas.setCursor(Cursor.DEFAULT);
			}
		});

		MainApplication.onImagesLoaded(() -> {
			//gc.setFill(Color.web("#409B85"));
			//gc.fillRect(0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
			gc.drawImage(this.background, 0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
			gc.scale(MainApplication.SCALE, MainApplication.SCALE);
			home.render(gc);
			gc.setFill(Color.BLACK);
			gc.setFont(MainApplication.getFont(30));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("Game written in Java\nFramework used: JavaFX\nCode and images made by OrangoMango\nSounds from freesound.org\nGMTK Game Jam 2022\nPost-Jam update v3.0\n...\nhttps://github.com/OrangoMango/FoodDice", 400, 50);
		});

		return canvas;
	}
}
