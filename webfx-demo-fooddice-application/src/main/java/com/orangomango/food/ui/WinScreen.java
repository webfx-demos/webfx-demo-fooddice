package com.orangomango.food.ui;

import com.orangomango.food.ui.shared.MenuButton;
import com.orangomango.food.ui.shared.UiShared;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class WinScreen{
	private Image background = UiShared.loadImage("background_home.jpg");

	public Canvas getLayout(){
		//StackPane layout = new StackPane();
		
		Canvas canvas = new Canvas(UiShared.WIDTH, UiShared.HEIGHT);
		//layout.getChildren().add(canvas);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		MenuButton home = new MenuButton("", () -> {
			UiShared.goToHomeScreen();
		}, 50, 300, 75, 75, UiShared.loadImage("button_home.png"));
		canvas.setOnMousePressed(e -> home.click(e.getX()/ UiShared.SCALE, e.getY()/ UiShared.SCALE));

		Font font50 = UiShared.getFont(50);
		Font font35 = UiShared.getFont(35);

		UiShared.onFontsImagesLoaded(() -> {
			gc.drawImage(this.background, 0, 0, UiShared.WIDTH, UiShared.HEIGHT);
			gc.scale(UiShared.SCALE, UiShared.SCALE);
			gc.setFont(font50);
			gc.setFill(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("YOU WIN!", 400, 100);
			gc.setFont(font35);
			gc.fillText("Thanks for playing", 400, 200);
			home.render(gc);
		});

		return canvas;
	}
}
