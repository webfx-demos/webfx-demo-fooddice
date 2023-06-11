package com.orangomango.food.ui;

import com.orangomango.food.MainApplication;
import dev.webfx.platform.resource.Resource;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class NoEditor{
	private Image background = MainApplication.loadImage("background_home.jpg");

	public Canvas getLayout(){
		Canvas canvas = new Canvas(MainApplication.WIDTH, MainApplication.HEIGHT);

		GraphicsContext gc = canvas.getGraphicsContext2D();

		Image homeImage = MainApplication.loadImage("button_home.png");
		MenuButton home = new MenuButton(() -> {
			HomeScreen hs = new HomeScreen();
			MainApplication.setScreen(hs.getLayout());
		}, 50, 300, 75, 75, homeImage);
		canvas.setOnMousePressed(e -> home.click(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE));

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