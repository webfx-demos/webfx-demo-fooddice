package com.orangomango.food;

import dev.webfx.platform.scheduler.Scheduler;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import com.orangomango.food.ui.GameScreen;

public class Laser extends GameObject implements Turnable{
	private volatile boolean shooting = false;
	private Image image = MainApplication.loadImage("laser.png");
	private double drawAmount;
	private volatile boolean on = true;
	private int timeOff = 1400;
	
	public Laser(GraphicsContext gc, double x, double y, double w, double h){
		super(gc, x, y, w, h);
		if (w != h) throw new IllegalArgumentException("W and H must be equal");
		this.solid = true;
		int[] i = {0};
		Scheduler.schedulePeriodic(40, scheduled -> {
			if (this.stopThread)
				scheduled.cancel();
			else if (this.on && !GameScreen.getInstance().isPaused()) {
				this.shooting = true;
				if (++i[0] <= 20)
					this.drawAmount += 0.05;
				else {
					if (i[0] >= 20 + 1600 / 40) {
						this.shooting = false;
						if (i[0] >= 20 + 1600 / 40 + this.timeOff / 40) {
							this.drawAmount = 0;
							i[0] = 0;
						}
					}
				}
			}
		});
	}
	
	public void setTimeOff(int time){
		this.timeOff = time;
	}
	
	@Override
	public void turnOn(){
		this.on = true;
	}
	
	@Override
	public void turnOff(){
		this.on = false;
		if (this.shooting) this.shooting = false;
	}
	
	@Override
	public void render(){
		gc.drawImage(this.image, this.x, this.y, this.w, this.h);
		if (this.shooting){
			GameObject found = getNearestBottomObject(GameScreen.getInstance().getPlayer());
			gc.setFill(Color.RED);
			double height = ((found == null ? GameScreen.getInstance().getLevelHeight() : found.getY())-(this.y+this.h))*this.drawAmount;
			gc.fillRect(this.x+this.w/2-1.5, this.y+this.h, 3, height);
			if (GameScreen.getInstance().getPlayer().collided(this.x+this.w/2-1.5, this.y+this.h, 3, height)){
				GameScreen.getInstance().getPlayer().die(false);
			}
		}
	}
}
