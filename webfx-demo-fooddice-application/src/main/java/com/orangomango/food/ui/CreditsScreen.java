package com.orangomango.food.ui;

import com.orangomango.food.MainApplication;
import dev.webfx.platform.resource.Resource;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class CreditsScreen{
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

		return layout;
	}
}
