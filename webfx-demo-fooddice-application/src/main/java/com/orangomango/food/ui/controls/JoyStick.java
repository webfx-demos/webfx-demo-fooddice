package com.orangomango.food.ui.controls;

import com.orangomango.food.ui.shared.UiShared;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.image.Image;

import static com.orangomango.food.ui.shared.UiShared.WIDTH;
import static com.orangomango.food.ui.shared.UiShared.HEIGHT;

public class JoyStick{
	private final GraphicsContext gc;
	private final Image[] images = {
			UiShared.loadImage("control_left.png"),
			UiShared.loadImage("control_right.png"),
			UiShared.loadImage("control_jump.png"),
			UiShared.loadImage("control_pause.png"),
			UiShared.loadImage("control_minimap.png"),
			UiShared.loadImage("control_kill.png")
	};

	private final Rectangle2D moveLeft = new Rectangle2D(40, HEIGHT-90, 70, 70);
	private final Rectangle2D moveRight = new Rectangle2D(130, HEIGHT-90, 70, 70);
	private final Rectangle2D jump = new Rectangle2D(WIDTH-115, HEIGHT-90, 70, 70);
	private final Rectangle2D pause = new Rectangle2D(WIDTH-270, 25, 35, 35);
	private final Rectangle2D map = new Rectangle2D(WIDTH-225, 25, 35, 35);
	private final Rectangle2D kill = new Rectangle2D(WIDTH-180, 25, 35, 35);

	public JoyStick(GraphicsContext gc){
		this.gc = gc;
	}
	
	public void render(){
		gc.save();
		gc.setGlobalAlpha(0.7);
		drawImage(this.images[0], moveLeft);
		drawImage(this.images[1], moveRight);

		drawImage(this.images[2], jump);

		drawImage(this.images[3], pause);
		drawImage(this.images[4], map);
		drawImage(this.images[5], kill);
		gc.restore();
	}

	private void drawImage(Image image, Rectangle2D r) {
		gc.drawImage(image, r.getMinX(), r.getMinY(), r.getWidth(), r.getHeight());
	}
	
	public KeyCode clicked(double x, double y){
		if (moveLeft.contains(x, y)) return KeyCode.S;
		if (moveRight.contains(x, y)) return KeyCode.F;
		if (jump.contains(x, y)) return KeyCode.SPACE;
		if (pause.contains(x, y)) return KeyCode.P;
		if (map.contains(x, y)) return KeyCode.M;
		if (kill.contains(x, y)) return KeyCode.K;
		return null;
	}
}
