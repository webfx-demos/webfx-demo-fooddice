package com.orangomango.food.ui;

import com.orangomango.food.MainApplication;
import dev.webfx.platform.resource.Resource;
import dev.webfx.platform.windowlocation.WindowLocation;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;

public class CreditsScreen{
	private Image background = MainApplication.loadImage("background_home.jpg");

	public Canvas getLayout(){
		Canvas canvas = new Canvas(MainApplication.WIDTH, MainApplication.HEIGHT);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		Rectangle2D link = new Rectangle2D(150, 280, 500, 25);

		Image homeImage = MainApplication.loadImage("button_home.png");
		MenuButton home = new MenuButton(() -> {
			HomeScreen hs = new HomeScreen();
			MainApplication.setScreen(hs.getLayout());
		}, 50, 300, 75, 75, homeImage);

		canvas.setOnMousePressed(e -> {
			if (link.contains(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE)){
				WindowLocation.assignHref("https://github.com/OrangoMango/FoodDice");
			} else {
				home.click(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE);
			}
		});

		canvas.setOnMouseMoved(e -> {
			if (link.contains(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE)){
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
			gc.setFont(Font.loadFont(Resource.toUrl("/font/font.ttf", getClass()), 30));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("Game written in Java\nFramework used: JavaFX\nCode and images made by OrangoMango\nSounds from freesound.org\nGMTK Game Jam 2022\nPost-Jam update v3.0\n...\nhttps://github.com/OrangoMango/FoodDice", 400, 50);
		});

		return canvas;
	}
}
