package com.orangomango.food.ui;

import com.orangomango.food.ui.shared.MenuButton;
import com.orangomango.food.ui.shared.UiShared;
import dev.webfx.platform.windowlocation.WindowLocation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;

public class CreditsScreen{
	private Image background = UiShared.loadImage("background_home.jpg");

	public Canvas getLayout(){
		//StackPane layout = new StackPane();
		
		Canvas canvas = new Canvas(UiShared.WIDTH, UiShared.HEIGHT);
		//layout.getChildren().add(canvas);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		Rectangle2D link = new Rectangle2D(150, 265, 500, 30);

		MenuButton home = new MenuButton("", () -> {
			UiShared.goToHomeScreen();
		}, 50, 300, 75, 75, UiShared.loadImage("button_home.png"));

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

		Font font = UiShared.getFont(30);
		UiShared.onFontsImagesLoaded(() -> {
			//gc.setFill(Color.web("#409B85"));
			//gc.fillRect(0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
			gc.drawImage(this.background, 0, 0, UiShared.WIDTH, UiShared.HEIGHT);
			gc.scale(UiShared.SCALE, UiShared.SCALE);
			home.render(gc);
			gc.setFill(Color.BLACK);
			gc.setFont(font);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("Game written in Java\nFramework used: JavaFX\nCode and images made by OrangoMango\nSounds from freesound.org\nGMTK Game Jam 2022\nPost-Jam update v3.0\n...\nhttps://github.com/OrangoMango/FoodDice", 400, 50);
		});

		return canvas;
	}
}
