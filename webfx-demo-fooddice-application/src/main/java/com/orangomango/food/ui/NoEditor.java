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
import javafx.scene.Cursor;
import javafx.geometry.Rectangle2D;

public class NoEditor{
	private Image background = MainApplication.loadImage("background_home.jpg");

	public Canvas getLayout(){
		Canvas canvas = new Canvas(MainApplication.WIDTH, MainApplication.HEIGHT);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		Rectangle2D bt = new Rectangle2D(50, 300, 75, 75);

		Image homeImage = MainApplication.loadImage("button_home.png");
		MenuButton home = new MenuButton(() -> {
			HomeScreen hs = new HomeScreen();
			MainApplication.setScreen(hs.getLayout());
		}, bt.getMinX(), bt.getMinY(), bt.getWidth(), bt.getHeight(), homeImage);

		canvas.setOnMousePressed(e -> {
			if (bt.contains(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE)){
				home.click(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE);
			} else {
				WindowLocation.assignHref("https://orangomango.itch.io/food-dice");
			}
		});

		canvas.setOnMouseMoved(e -> {
			if (bt.contains(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE)){
				canvas.setCursor(Cursor.DEFAULT);
			} else {
				canvas.setCursor(Cursor.HAND);
			}
		});

		MainApplication.onImagesLoaded(() -> {
			gc.drawImage(this.background, 0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
			gc.scale(MainApplication.SCALE, MainApplication.SCALE);
			gc.setFont(Font.loadFont(Resource.toUrl("/font/font.ttf", getClass()), 50));
			gc.setFill(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("Editor not available", 400, 100);
			gc.setFont(Font.loadFont(Resource.toUrl("/font/font.ttf", getClass()), 35));
			gc.fillText("Please download the game\nin order to use the editor", 400, 200);
			home.render(gc);
		});
		
		return canvas;
	}
}
