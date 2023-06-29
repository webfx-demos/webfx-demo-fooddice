package com.orangomango.food;

import dev.webfx.platform.scheduler.Scheduler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Door extends GameObject implements Turnable{
	private boolean opened;
	private static Image[] IMAGES = new Image[8];
	private boolean animating;
	private volatile boolean stopCurrentAnimation;

	static {
		for (int i = 0; i < 8; i++){
			IMAGES[i] = MainApplication.loadImage("door_"+i+".png");
		}
	}

	public Door(GraphicsContext gc, double x, double y){
		super(gc, x, y, IMAGES[0].getWidth(), IMAGES[0].getHeight());
	}
	
	@Override
	public void render(){
		this.solid = !this.opened;
		gc.drawImage(IMAGES[this.imageIndex], this.x, this.y, this.w, this.h);
	}
	
	@Override
	public void turnOn(){
		this.imageIndex = 0;
		if (!this.animating) {
			this.animating = true;
			Scheduler.schedulePeriodic(100, scheduled -> {
				if (!this.stopCurrentAnimation && this.imageIndex < 7)
					this.imageIndex++;
				else {
					if (this.imageIndex == 7) {
						this.opened = true;
						this.animating = false;
					}
					this.stopCurrentAnimation = false;
					scheduled.cancel();
				}
			});
		}
	}
	
	@Override
	public void turnOff(){
		this.imageIndex = 7;
		if (!this.animating) {
			this.animating = true;
			Scheduler.schedulePeriodic(100, scheduled -> {
				if (!this.stopCurrentAnimation && this.imageIndex > 0)
					this.imageIndex--;
				else {
					if (this.imageIndex == 0) {
						this.opened = false;
						this.animating = false;
					}
					this.stopCurrentAnimation = false;
					scheduled.cancel();
				}
			});
		}
	}
}
