package com.orangomango.food;

import dev.webfx.platform.scheduler.Scheduler;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.animation.*;
import javafx.util.Duration;

import java.util.*;
import com.orangomango.food.ui.GameScreen;

public class Shooter extends GameObject{
	private static class Bullet{
		private double x, y;
		private boolean left;
		private static final double speed = 1.5;
		public static final double SIZE = 12;
		private static Image image = MainApplication.loadImage("shooter_bullet.png");
		
		public Bullet(double x, double y, boolean l){
			this.x = x;
			this.y = y;
			this.left = l;
		}
		
		public void render(GraphicsContext gc){
			gc.drawImage(image, this.x, this.y, SIZE, SIZE);
			this.x += this.left ? -speed : speed;
		}
		
		public double getX(){
			return this.x;
		}
		
		public double getY(){
			return this.y;
		}
	}
	
	private static final double SIZE = 20;
	private Image[] images = new Image[2];
	private boolean left;
	private List<Bullet> bullets = new ArrayList<>();
	private int timeOff = 1300;
	
	public Shooter(GraphicsContext gc, double x, double y, boolean left){
		super(gc, x, y, SIZE, SIZE);
		this.images[0] = MainApplication.loadImage("shooter.png");
		this.images[1] = MainApplication.loadImage("shooter_1.png");
		this.solid = true;
		this.left = left;
		long[] lastTime = { System.currentTimeMillis() };
		Scheduler.schedulePeriodic(200, scheduled -> {
			if (this.stopThread)
				scheduled.cancel();
			else if (!GameScreen.getInstance().isPaused()) {
				long now = System.currentTimeMillis();
				if (now < lastTime[0] + this.timeOff)
					this.imageIndex = 0;
				else {
					this.imageIndex = 1;
					this.bullets.add(new Bullet(this.left ? this.x : this.x+this.w, this.y, this.left));
					lastTime[0] = now;
				}
			}
		});
	}
	
	public void setTimeOff(int time){
		this.timeOff = time;
	}
	
	@Override
	public void render(){
		if (this.left){
			gc.drawImage(this.images[this.imageIndex], this.x, this.y, SIZE, SIZE);
		} else {
			gc.drawImage(this.images[this.imageIndex], this.x+SIZE, this.y, -SIZE, SIZE);
		}
		for (int i = 0; i < this.bullets.size(); i++){
			Bullet b = this.bullets.get(i);
			b.render(gc);
			boolean col = false;
			if (b.getX() >= GameScreen.getInstance().getLevelWidth() || b.getX()+Bullet.SIZE <= 0) col = true;
			for (GameObject go : GameScreen.getInstance().getSprites()){
				if (go.collided(b.getX(), b.getY(), Bullet.SIZE, Bullet.SIZE) && go != this && go.isSolid()){
					if (go instanceof Player) GameScreen.getInstance().getPlayer().die(false);
					col = true;
					break;
				}
			}
			if (col){
				this.bullets.remove(b);
				i--;
			}
		}
	}
}
