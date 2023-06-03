package com.orangomango.food;

import dev.webfx.platform.scheduler.Scheduler;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;

public class Door extends GameObject implements Turnable{
	private boolean opened;
	private Image[] images = new Image[8];
	private boolean animating;
	private volatile boolean stopCurrentAnimation;

	public Door(GraphicsContext gc, double x, double y){
		super(gc, x, y, 20, 50);
		for (int i = 0; i < 8; i++){
			this.images[i] = MainApplication.loadImage("door_"+i+".png");
		}
	}
	
	@Override
	public void render(){
		this.solid = !this.opened;
		gc.drawImage(this.images[this.imageIndex], this.x, this.y, this.w, this.h);
	}
	
	@Override
	public void turnOn(){
		this.open();
	}
	
	@Override
	public void turnOff(){
		this.close();
	}
	
	public void open(){
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
	
	public void close(){
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
