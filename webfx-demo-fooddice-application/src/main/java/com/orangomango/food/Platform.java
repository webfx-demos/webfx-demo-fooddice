package com.orangomango.food;

import com.orangomango.food.ui.shared.PlatformType;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Platform extends GameObject{

	protected Image type;
	private boolean repeat;
	
	public Platform(double x, double y, double w, double h, Image image){
		super(x, y, w, h);
		this.type = image;
		this.repeat = true;
		this.solid = true;
	}
	
	public Platform(double x, double y, PlatformType type){
		this(x, y, type.getWidth(), type.getHeight(), type.getImage());
		this.repeat = false;
	}
	
	@Override
	public void render(GraphicsContext gc){
		if (this.repeat){
			for (int i = 0; i < this.h/32; i++){
				for (int j = 0; j < this.w/32; j++){
					double tempW = j*32+32 < this.w ? 32 : this.w-j*32;
					double tempH = i*32+32 < this.h ? 32 : this.h-i*32;
					gc.drawImage(this.type, 0, 0, tempW, tempH, this.x+j*32, this.y+i*32, tempW, tempH);
				}
			}
		} else {
			gc.drawImage(this.type, this.x, this.y, this.w, this.h);
		}
	}
}
