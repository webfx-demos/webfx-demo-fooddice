package com.orangomango.food.ui;

import com.orangomango.food.ui.shared.MenuButton;
import com.orangomango.food.ui.shared.UiShared;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class HelpScreen{
	private Timeline loop;
	private MenuButton home;
	private Image[] images = new Image[14];
	private boolean forward = true;
	private double extraY = 1;
	private Image background = UiShared.loadImage("background_home.jpg");
	
	public HelpScreen(){
		this.images[0] = UiShared.loadImage("key_s.png");
		this.images[1] = UiShared.loadImage("key_f.png");
		this.images[2] = UiShared.loadImage("key_left.png");
		this.images[3] = UiShared.loadImage("key_right.png");
		this.images[4] = UiShared.loadImage("key_esc.png");
		this.images[5] = UiShared.loadImage("key_p.png");
		this.images[6] = UiShared.loadImage("key_k.png");
		this.images[7] = UiShared.loadImage("key_l.png");
		this.images[8] = UiShared.loadImage("key_f1.png");
		this.images[9] = UiShared.loadImage("key_f2.png");
		this.images[10] = UiShared.loadImage("key_f3.png");
		this.images[11] = UiShared.loadImage("key_f4.png");
		this.images[12] = UiShared.loadImage("key_i.png");
		this.images[13] = UiShared.loadImage("key_up.png");
	}

	public Canvas getLayout(){
		//StackPane layout = new StackPane();
		
		Canvas canvas = new Canvas(UiShared.WIDTH, UiShared.HEIGHT);
		//layout.getChildren().add(canvas);
		
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		this.home = new MenuButton("", () -> {
			UiShared.goToHomeScreen();
		}, 50, 300, 75, 75, UiShared.loadImage("button_home.png"));
		canvas.setOnMousePressed(e -> home.click(e.getX()/ UiShared.SCALE, e.getY()/ UiShared.SCALE));

		UiShared.onFontsImagesLoaded(() -> {
			this.loop = new Timeline(new KeyFrame(Duration.millis(1000.0/ UiShared.FPS), e -> update(gc)));
			this.loop.setCycleCount(Animation.INDEFINITE);
			this.loop.play();
		});

		return canvas;
	}
	
	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, UiShared.WIDTH, UiShared.HEIGHT);
		//gc.setFill(Color.web("#409B85"));
		//gc.fillRect(0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
		gc.drawImage(this.background, 0, 0, UiShared.WIDTH, UiShared.HEIGHT);
		gc.save();
		gc.scale(UiShared.SCALE, UiShared.SCALE);
		this.home.render(gc);

		// TODO add spacebar key image

		gc.drawImage(this.images[0], 75, 55+this.extraY);
		gc.drawImage(this.images[1], 120, 55+this.extraY);
		gc.drawImage(this.images[2], 40, 110+this.extraY);
		gc.drawImage(this.images[3], 85, 110+this.extraY);
		gc.drawImage(this.images[13], 130, 110+this.extraY);
		gc.drawImage(this.images[4], 75, 200+this.extraY);
		gc.drawImage(this.images[5], 130, 200+this.extraY);
		gc.drawImage(this.images[6], 500, 55+this.extraY);
		gc.drawImage(this.images[7], 500, 110+this.extraY);
		gc.drawImage(this.images[8], 490, 215+this.extraY);
		gc.drawImage(this.images[9], 490, 260+this.extraY);
		gc.drawImage(this.images[10], 490, 305+this.extraY);
		gc.drawImage(this.images[11], 490, 350+this.extraY);
		gc.drawImage(this.images[12], 170, 310+this.extraY);
		
		this.extraY += this.forward ? 0.1 : -0.1;
		if (this.extraY >= 2.5){
			this.forward = false;
		} else if (this.extraY <= 0){
			this.forward = true;
		}
		
		gc.setFill(Color.BLACK);
		gc.setFont(UiShared.getFont(20));
		gc.fillText("Use S/F or arrow keys to move\nSpace or up arrow to jump", 187, 85);
		gc.fillText("Use ESC or P to pause/resume", 187, 230);
		gc.fillText("Use K to kill yourself", 550, 75);
		gc.fillText("Use L to reload the level", 550, 130);
		gc.fillText("Use I to toggle game info", 227, 330);
		gc.fillText("Load the test level", 550, 240);
		gc.fillText("View nearest bottom object", 550, 290);
		gc.fillText("View player hitboxes", 550, 335);
		gc.fillText("Turn on/off camera", 550, 380);
		gc.setFill(Color.RED);
		gc.fillText("Debug keys", 460, 190);
		gc.restore();
	}
}
