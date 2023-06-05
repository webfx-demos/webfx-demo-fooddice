package com.orangomango.food.ui;

import dev.webfx.kit.util.scene.DeviceSceneUtil;
import dev.webfx.platform.resource.Resource;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;

import com.orangomango.food.MainApplication;

public class WinScreen{
	private Image background = MainApplication.loadImage("background_home.jpg");

	public StackPane getLayout(){
		StackPane layout = new StackPane();
		
		Canvas canvas = new Canvas(MainApplication.WIDTH, MainApplication.HEIGHT);
		layout.getChildren().add(canvas);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();

		Image homeImage = MainApplication.loadImage("button_home.png");
		MenuButton home = new MenuButton(() -> {
			HomeScreen hs = new HomeScreen();
			MainApplication.stage.getScene().setRoot(hs.getLayout());
		}, 50, 300, 75, 75, homeImage);
		canvas.setOnMousePressed(e -> home.click(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE));

		DeviceSceneUtil.onImagesLoaded(() -> {
			gc.drawImage(this.background, 0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
			gc.scale(MainApplication.SCALE, MainApplication.SCALE);
			gc.setFont(Font.loadFont(Resource.toUrl("/font/font.ttf", getClass()), 50));
			gc.setFill(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("YOU WIN!", 400, 100);
			gc.setFont(Font.loadFont(Resource.toUrl("/font/font.ttf", getClass()), 35));
			gc.fillText("Thanks for playing", 400, 200);
			home.render(gc);
		}, background, homeImage);
		
		return layout;
	}
}
