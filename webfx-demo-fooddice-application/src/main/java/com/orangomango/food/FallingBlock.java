package com.orangomango.food;

import com.orangomango.food.ui.shared.UiShared;
import dev.webfx.platform.scheduler.Scheduler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import com.orangomango.food.ui.GameScreen;

public class FallingBlock extends GameObject{
	private static Image IMAGE = UiShared.loadImage("fallingBlock.png");
	private boolean falling = false;
	private int time = 1000;

	public FallingBlock(double x, double y){
		super(x, y, IMAGE.getWidth(), IMAGE.getHeight());
		this.solid = true;
	}
	
	public void setFallingTime(int time){
		this.time = time;
	}
	
	@Override
	public void render(GraphicsContext gc){
		gc.drawImage(IMAGE, this.x, this.y, this.w, this.h);
		if (GameScreen.getInstance().getPlayer().collided(this.x, this.y-4, this.w, 4)){
			startFalling();
		}
	}
	
	private void startFalling(){
		if (this.falling) return;
		this.falling = true;
		Scheduler.scheduleDelay(this.time, () -> {
			makeGravity();
			this.movable = true;
		});
	}
}
