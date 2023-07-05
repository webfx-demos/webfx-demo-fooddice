package com.orangomango.food.ui.editor;

import com.orangomango.food.ui.shared.MenuButton;
import com.orangomango.food.ui.shared.UiShared;
import dev.webfx.platform.windowlocation.WindowLocation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class Editor {
	private Image background = UiShared.loadImage("background_home.jpg");

	public Node getLayout(){
		Canvas canvas = new Canvas(UiShared.WIDTH, UiShared.HEIGHT);

		GraphicsContext gc = canvas.getGraphicsContext2D();
		Rectangle2D bt = new Rectangle2D(50, 300, 75, 75);

		Image homeImage = UiShared.loadImage("button_home.png");
		MenuButton home = new MenuButton("", () -> {
			UiShared.goToHomeScreen();
		}, bt.getMinX(), bt.getMinY(), bt.getWidth(), bt.getHeight(), homeImage);

		canvas.setOnMousePressed(e -> {
			if (bt.contains(e.getX()/ UiShared.SCALE, e.getY()/ UiShared.SCALE)){
				home.click(e.getX()/ UiShared.SCALE, e.getY()/ UiShared.SCALE);
			} else {
				WindowLocation.assignHref("https://orangomango.itch.io/food-dice");
			}
		});

		canvas.setOnMouseMoved(e -> {
			if (bt.contains(e.getX()/ UiShared.SCALE, e.getY()/ UiShared.SCALE)){
				canvas.setCursor(Cursor.DEFAULT);
			} else {
				canvas.setCursor(Cursor.HAND);
			}
		});

		Font font50 = UiShared.getFont(50);
		Font font35 = UiShared.getFont(35);

		UiShared.onFontsImagesLoaded(() -> {
			gc.drawImage(this.background, 0, 0, UiShared.WIDTH, UiShared.HEIGHT);
			gc.scale(UiShared.SCALE, UiShared.SCALE);
			gc.setFont(font50);
			gc.setFill(Color.BLACK);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.fillText("Editor not available", 400, 100);
			gc.setFont(font35);
			gc.fillText("Please download the game\nin order to use the editor", 400, 200);
			home.render(gc);
		});
		
		return canvas;
	}
}
