package com.orangomango.food.ui;

import com.orangomango.food.*;
import com.orangomango.food.ui.controls.JoyStick;
import dev.webfx.extras.scalepane.ScalePane;
import dev.webfx.platform.os.OperatingSystem;
import dev.webfx.platform.resource.Resource;
import dev.webfx.platform.scheduler.Scheduler;
import dev.webfx.platform.storage.LocalStorage;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.*;

public class GameScreen{
	private volatile List<GameObject> sprites = new ArrayList<>();
	private List<CollectableObject> collectables = new ArrayList<>();
	private List<Particle> effects = new ArrayList<>();
	private Map<KeyCode, Boolean> keys = new HashMap<>();
	private Player player;
	private static GameScreen instance;
	private GraphicsContext gc;
	public Timeline loop;
	private Exit exit;
	private int currentLevel;
	private Image backgroundTile = MainApplication.loadImage("background.png");
	private double levelWidth, levelHeight;
	private volatile boolean paused;
	private Image pausedImage;
	private boolean showCamera = true;
	private int[][] angles;
	private int currentFPS = 0;
	private volatile int framesDone = 0;
	private List<MenuButton> buttons = new ArrayList<>();
	private long levelStart, pausedStart, pausedTime;
	private int coinsCollected = 0;
	private SpecialEffect specialEffect;
	private Notification notification = new Notification();
	private double cameraShakeX, cameraShakeY;
	private boolean shaking;
	public int deaths;
	private String loadString;
	private Map<Integer, Integer> spritesID = new HashMap<>();
	private boolean showInfo;

	private JoyStick joystick;
	private ScalePane scalePane;

	private static Font FONT_20 = Font.loadFont(Resource.toUrl("/font/font.ttf", GameScreen.class), 20);
	private static Font FONT_55 = Font.loadFont(Resource.toUrl("/font/font.ttf", GameScreen.class), 55);

	public GameScreen(int l){
		this(l, null);
	}

	public GameScreen(int l, String ll){
		this.currentLevel = l;
		this.loadString = ll;
		GameScreen.instance = this;

		Scheduler.schedulePeriodic(1000, scheduled -> {
			if (GameScreen.instance != this)
				scheduled.cancel();
			else {
				this.currentFPS = this.framesDone;
				this.framesDone = 0;
			}
		});
	}

	public boolean isPaused(){
		return this.paused;
	}

	public static GameScreen getInstance(){
		return GameScreen.instance;
	}

	public List<GameObject> getSprites(){
		return this.sprites;
	}

	public List<Particle> getEffects(){
		return this.effects;
	}

	public List<CollectableObject> getCollectables(){
		return this.collectables;
	}

	public Map<KeyCode, Boolean> getKeys(){
		return this.keys;
	}

	public GraphicsContext getGC(){
		return this.gc;
	}

	public Player getPlayer(){
		return this.player;
	}

	public SpecialEffect getSpecialEffect(){
		return this.specialEffect;
	}

	public Notification getNotification(){
		return this.notification;
	}

	public double getLevelWidth(){
		return this.levelWidth;
	}

	public double getLevelHeight(){
		return this.levelHeight;
	}

	private void loadAngles(int w, int h){
		this.angles = new int[w][h];
		Random random = new Random();
		for (int i = 0; i < w; i++){
			for (int j = 0; j < h; j++){
				this.angles[i][j] = random.nextInt(4);
			}
		}
	}

	private void loadLevel(GraphicsContext gc, int level){
		loadLevel(gc, level, null);
	}
	private void loadLevel(GraphicsContext gc, int levelN, String[] level){
		for (GameObject go : sprites){
			go.destroy();
		}
		sprites.clear();
		collectables.clear();
		effects.clear();
		spritesID.clear();
		this.player = null;
		this.exit = null;
		this.levelWidth = 0;
		this.levelHeight = 0;
		this.levelStart = System.currentTimeMillis();
		this.pausedTime = 0;
		this.coinsCollected = 0;
		this.deaths = 0;
		this.specialEffect = new SpecialEffect();
		switch (levelN){
			case -1:
				this.levelWidth = Double.parseDouble(level[0].split("x")[0]);
				this.levelHeight = Double.parseDouble(level[0].split("x")[1]);
				this.showCamera = Boolean.parseBoolean(level[0].split("x")[2]);

				for (int i = 1; i < level.length; i++){
					String line = level[i];
					int type = Integer.parseInt(line.split(",")[0].split(";")[0]);
					double px = Double.parseDouble(line.split(",")[1]);
					double py = Double.parseDouble(line.split(",")[2]);
					double pw = Double.parseDouble(line.split(",")[3]);
					double ph = Double.parseDouble(line.split(",")[4]);
					switch (type){
						case 0:
							sprites.add(new Platform(gc, px, py, Platform.PlatformType.SMALL));
							break;
						case 1:
							sprites.add(new Platform(gc, px, py, Platform.PlatformType.MEDIUM));
							break;
						case 2:
							sprites.add(new Platform(gc, px, py, pw, ph, MainApplication.loadImage("ground.png")));
							break;
						case 3:
							sprites.add(new Platform(gc, px, py, pw, ph, MainApplication.loadImage("wood.png")));
							break;
						case 4:
							sprites.add(new Spike(gc, px, py, "fire"));
							break;
						case 5:
							sprites.add(new Spike(gc, px, py, "cactus"));
							break;
						case 6:
							Laser laser = new Laser(gc, px, py, pw, ph);
							if (line.split(",").length == 6){
								String txt = line.split(",")[5];
								laser.setTimeOff(Integer.parseInt(txt));
							}
							sprites.add(laser);
							break;
						case 7:
							Shooter shooter = new Shooter(gc, px, py, Boolean.parseBoolean(line.split(",")[5].split("-")[0]));
							shooter.setTimeOff(Integer.parseInt(line.split(",")[5].split("-")[1]));
							sprites.add(shooter);
							break;
						case 8:
							sprites.add(new Box(gc, px, py));
							break;
						case 9:
							sprites.add(new JumpPad(gc, px, py));
							break;
						case 10:
							if (line.split(",").length == 6){
								String txt = line.split(",")[5];
								sprites.add(new ActivatorPad(gc, px, py, () -> {
									for (String part : txt.split("-")){
										int n = Integer.parseInt(part);
										((Turnable)sprites.get(spritesID.get(n))).turnOn();
									}
								}, () -> {
									for (String part : txt.split("-")){
										int n = Integer.parseInt(part);
										((Turnable)sprites.get(spritesID.get(n))).turnOff();
									}
								}));
							} else {
								sprites.add(new ActivatorPad(gc, px, py, () -> System.out.println("On"), () -> System.out.println("Off")));
							}
							break;
						case 11:
							sprites.add(new Door(gc, px, py));
							break;
						case 12:
							collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, px, py));
							break;
						case 13:
							sprites.add(new CheckPoint(gc, px, py));
							break;
						case 14:
							this.player = new Player(gc, px, py, Player.SIZE, Player.SIZE);
							sprites.add(player);
							break;
						case 15:
							this.exit = new Exit(gc, px, py);
							break;
						case 16:
						case 17:
							double moveX = 0;
							double moveY = 0;
							double maxX = 0;
							double maxY = 0;
							int moveTime = 0;
							if (line.split(",").length == 6){
								String[] data = line.split(",")[5].split("-");
								moveX = Double.parseDouble(data[0]);
								moveY = Double.parseDouble(data[1]);
								maxX = Double.parseDouble(data[2]);
								maxY = Double.parseDouble(data[3]);
								moveTime = Integer.parseInt(data[4]);
							}
							sprites.add(new MovablePlatform(gc, px, py, type == 16 ? Platform.PlatformType.SMALL : Platform.PlatformType.MEDIUM, moveX, moveY, maxX, maxY, moveTime));
							break;
					}
					spritesID.put(Integer.parseInt(line.split(",")[0].split(";")[1]), sprites.size()-1);
				}

				break;
			case 0:
				this.levelWidth = 800;
				this.levelHeight = 800;
				this.showCamera = true;

				this.player = new Player(gc, 40, 240, Player.SIZE, Player.SIZE);
				sprites.add(player);

				sprites.add(new Platform(gc, 0, 256, 96, this.levelHeight-256-150, MainApplication.loadImage("ground.png")));
				sprites.add(new Box(gc, 65, 30));
				sprites.add(new Box(gc, 65, 0));
				Door door = new Door(gc, 385, 206);
				Laser laser = new Laser(gc, 450, 20, 30, 30);
				MovablePlatform mob = new MovablePlatform(gc, 130, 270, Platform.PlatformType.SMALL, 2, 0, 50, 0, 100);
				sprites.add(door);
				sprites.add(new ActivatorPad(gc, 0, -30, () -> {
					door.open();
					mob.turnOff();
				}, () -> {
					door.close();
					mob.turnOn();
				}));
				sprites.add(new ActivatorPad(gc, 420, 180, () -> laser.turnOff(), () -> laser.turnOn()));
				sprites.add(new Platform(gc, 385, 256, 224, this.levelHeight-256-150, MainApplication.loadImage("wood.png")));
				sprites.add(mob);
				sprites.add(new Platform(gc, 260, 270, Platform.PlatformType.MEDIUM));
				sprites.add(new Platform(gc, 150, 150, Platform.PlatformType.MEDIUM));
				sprites.add(new Platform(gc, 300, 100, Platform.PlatformType.MEDIUM));
				sprites.add(new JumpPad(gc, 350, 50));
				sprites.add(new MovablePlatform(gc, 625, 250, Platform.PlatformType.SMALL, 0, 5, 0, 500, 50));
				sprites.add(new JumpPad(gc, 270, 252));
				sprites.add(new CheckPoint(gc, 50, 750));

				for (int i = 0; i < 9; i++){
					if (i % 3 == 0 || i > 6) sprites.add(new Spike(gc, 120+i*25, 375, "cactus"));
				}

				//sprites.add(new Shooter(gc, 410, 236, true));
				sprites.add(new Shooter(gc, 460, 236, false));

				sprites.add(laser);
				sprites.add(new Laser(gc, 500, 20, 30, 30));

				collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, 180, 235));
				collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, 320, 235));

				//effects.add(new Particle(gc, 100, 100, "tail", 40, true));

				this.exit = new Exit(gc, 175, 110);
				break;
			case 1:
				this.levelWidth = 800;
				this.levelHeight = 400;
				this.showCamera = false;

				this.player = new Player(gc, 20, 240, Player.SIZE, Player.SIZE);
				sprites.add(new GameText(gc, 35, 190, 300, 25, "You get special effect every 15s based on your dice's position"));
				sprites.add(this.player);
				sprites.add(new Platform(gc, 0, 256, 192, 400-256, MainApplication.loadImage("ground.png")));
				sprites.add(new Platform(gc, 348, 256, 192, 400-256, MainApplication.loadImage("ground.png")));
				sprites.add(new Liquid(gc, 348+192, 350, 800-348-192, 50));
				sprites.add(new Platform(gc, 0, 0, 200, 125, MainApplication.loadImage("ground.png")));
				sprites.add(new Platform(gc, 220, 256, Platform.PlatformType.MEDIUM));
				sprites.add(new Platform(gc, 700, 175, Platform.PlatformType.SMALL));
				sprites.add(new Platform(gc, 600, 115, Platform.PlatformType.SMALL));
				sprites.add(new MovablePlatform(gc, 390, 85, Platform.PlatformType.SMALL, 2, 0, 100, 0, 50));
				sprites.add(new Platform(gc, 250, 80, Platform.PlatformType.MEDIUM));
				for (int i = 0; i < 6; i++){
					sprites.add(new Spike(gc, 192+i*25, 375, "fire"));
				}
				sprites.add(new Platform(gc, 570, 220, Platform.PlatformType.MEDIUM));

				collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, 255, 215));
				collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, 480, 215));
				collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, 490, 40));

				this.exit = new Exit(gc, 270, 40);
				break;
			case 2:
				loadLevel(gc, -1, getLevelData(2));
				sprites.add(new GameText(gc, 460, 435, 300, 20, "JumpPads can be pushed"));
				return;
			case 3:
				loadLevel(gc, -1, getLevelData(3));
				sprites.add(new GameText(gc, 400, 390, 300, 25, "Boxes can be pushed"));
				return;
			case 4:
				this.levelWidth = 800;
				this.levelHeight = 800;
				this.showCamera = true;

				this.player = new Player(gc, 5, 700, Player.SIZE, Player.SIZE);
				this.player.setRespawnX(40);
				this.player.setRespawnY(240);
				sprites.add(this.player);

				// Grounds
				sprites.add(new Platform(gc, 0, 0, 800, 72, MainApplication.loadImage("ground.png")));
				sprites.add(new Platform(gc, 0, 728, 800, 72, MainApplication.loadImage("ground.png")));
				sprites.add(new Platform(gc, 0, 512, 290, 72, MainApplication.loadImage("ground.png")));

				sprites.add(new Laser(gc, 20, 584, 30, 30));
				sprites.add(new Laser(gc, 120, 584, 30, 30));
				sprites.add(new Laser(gc, 240, 584, 30, 30));
				sprites.add(new Platform(gc, 300, 700, Platform.PlatformType.MEDIUM));
				sprites.add(new Platform(gc, 415, 650, Platform.PlatformType.MEDIUM));
				sprites.add(new Spike(gc, 407, 703, "cactus"));
				sprites.add(new Spike(gc, 437, 703, "cactus"));
				sprites.add(new Spike(gc, 467, 703, "cactus"));
				Door door_l4 = new Door(gc, 547, 170);
				sprites.add(new Spike(gc, 530, 703, "fire"));
				sprites.add(new ActivatorPad(gc, 450, 630, () -> door_l4.open(), () -> door_l4.close()));
				sprites.add(new Spike(gc, 575, 703, "fire"));
				sprites.add(new Spike(gc, 615, 703, "cactus"));
				sprites.add(new Spike(gc, 655, 703, "cactus"));
				sprites.add(new Spike(gc, 695, 703, "cactus"));
				sprites.add(new Spike(gc, 735, 703, "cactus"));
				sprites.add(new Spike(gc, 775, 703, "cactus"));
				sprites.add(new MovablePlatform(gc, 530, 650, Platform.PlatformType.SMALL, 2, 0, 100, 0, 50));
				sprites.add(new Platform(gc, 700, 615, Platform.PlatformType.MEDIUM));
				sprites.add(new CheckPoint(gc, 730, 565));
				sprites.add(new Platform(gc, 570, 585, Platform.PlatformType.MEDIUM));
				sprites.add(new Shooter(gc, 570, 565, false));
				sprites.add(new Platform(gc, 700, 537, Platform.PlatformType.MEDIUM));
				sprites.add(new MovablePlatform(gc, 470, 510, Platform.PlatformType.SMALL, 2, 0, 144, 0, 50));
				sprites.add(new Platform(gc, 300, 490, 143, 40, MainApplication.loadImage("wood.png")));
				for (int i = 0; i < 11; i++){
					sprites.add(new Spike(gc, i*25, 487, "fire"));
				}
				sprites.add(new CheckPoint(gc, 340, 440));
				sprites.add(new Platform(gc, 190, 450, Platform.PlatformType.MEDIUM));
				sprites.add(new MovablePlatform(gc, 110, 315, Platform.PlatformType.SMALL, 0, 2, 0, 120, 50));
				sprites.add(new Platform(gc, 5, 290, Platform.PlatformType.MEDIUM));
				sprites.add(new Platform(gc, 180, 290, Platform.PlatformType.MEDIUM));
				sprites.add(new Platform(gc, 300, 290, 140, 40, MainApplication.loadImage("wood.png")));
				sprites.add(new Laser(gc, 300, 330, 30, 30));
				sprites.add(new Laser(gc, 390, 330, 30, 30));
				sprites.add(new Laser(gc, 300, 72, 30, 30));
				sprites.add(new Laser(gc, 390, 72, 30, 30));
				sprites.add(new Laser(gc, 60, 72, 30, 30));
				sprites.add(new Laser(gc, 235, 72, 30, 30));
				sprites.add(new Platform(gc, 475, 270, Platform.PlatformType.SMALL));
				sprites.add(new Box(gc, 480, 235));
				sprites.add(new Platform(gc, 547, 220, 33, 150, MainApplication.loadImage("wood.png")));
				sprites.add(door_l4);
				sprites.add(new Platform(gc, 580, 328, Platform.PlatformType.MEDIUM));
				sprites.add(new Platform(gc, 733, 328, Platform.PlatformType.SMALL));
				sprites.add(new Shooter(gc, 753, 310, true));
				sprites.add(new Platform(gc, 580, 268, Platform.PlatformType.SMALL));
				sprites.add(new Laser(gc, 590, 72, 30, 30));

				collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, 13, 255));
				collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, 193, 255));
				collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, 760, 690));
				collectables.add(new CollectableObject(CollectableObject.CollectableType.COIN, gc, 550, 435));

				this.exit = new Exit(gc, 585, 288);
				break;
		}
		loadAngles((int)this.levelWidth/25, (int)this.levelHeight/25);
	}

	private String[] getLevelData(int n){
		try {
			String fileContent = Resource.getText(Resource.toUrl("/levels/level"+n+".lvl", getClass())); // Text returned immediately because embed
			return fileContent.split("\n");
		} catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	public ScalePane getLayout(){
		Canvas canvas = new Canvas(MainApplication.WIDTH, MainApplication.HEIGHT);
		this.gc = canvas.getGraphicsContext2D();

		//String testString = "800x800\n1;0,44.00,116.00,100.00,20.00\n1;1,169.00,141.00,100.00,20.00\n15;2,229.00,102.00,35.00,40.00\n14;3,52.00,93.00,16.00,16.00\n1;4,20.00,228.00,100.00,20.00\n8;5,77.00,202.00,25.00,25.00";

		//loadLevel(gc, -1, testString.split("\n"));
		if (this.loadString != null){
			loadLevel(gc, -1, this.loadString.split("\n"));
		} else {
			loadLevel(gc, this.currentLevel);
		}
		this.joystick = new JoyStick(gc);

		canvas.setFocusTraversable(true);
		canvas.setOnMousePressed(e -> {
			for (int i = 0; i < this.buttons.size(); i++){
				MenuButton mb = this.buttons.get(i);
				mb.click(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE);
			}
			KeyCode k = this.joystick.clicked(e.getX()/MainApplication.SCALE, e.getY()/MainApplication.SCALE);
			if (k != null){
				handlePress(k, canvas);
			}
		});

		this.loop = new Timeline(new KeyFrame(Duration.millis(1000.0/MainApplication.FPS), e -> update(gc)));
		loop.setCycleCount(Animation.INDEFINITE);
		loop.play();

		canvas.setOnKeyPressed(e -> handlePress(toKeyCode(e), canvas));
		canvas.setOnKeyReleased(e -> keys.put(toKeyCode(e), false));
		canvas.setOnMouseReleased(e -> {
			KeyCode k = this.joystick.clicked(e.getX(), e.getY());
			if (k != null){
				keys.put(k, false);
			}
		});

		AnimationTimer fTimer = new AnimationTimer(){
			@Override
			public void handle(long time){
				GameScreen.this.framesDone++;
			}
		};
		fTimer.start();

		//MainApplication.sizeOnResize(canvas);

		this.scalePane = new ScalePane();
		// For mobiles, we auto-scale to the whole window (which is the default behavior of ScalePane) because users
		// have no keyboard, but for desktops & laptops, we start with scale 2, and they can use the +/- keys to adjust
		// to their preferred scale.
		if (!OperatingSystem.isMobile()) {
			String scale = LocalStorage.getItem("scale");
			this.scalePane.setMaxScale(scale == null ? 2 : Double.parseDouble(scale));
		}
		// Restoring showInfo if persisted
		String showInfo = LocalStorage.getItem("showInfo");
		if (showInfo != null)
			this.showInfo = Boolean.parseBoolean(showInfo);

		MainApplication.onImagesLoaded(() -> scalePane.setNode(canvas));

		return this.scalePane;
	}

	private KeyCode toKeyCode(KeyEvent keyEvent) {
		KeyCode keyCode = null;
		// The HelpScreen tells the users to press keys such as 'A' or 'M', but the keyCode returned by JavaFX is the
		// physical key, so if the user presses what he sees as 'A' on an AZERTY keyboard, the KeyCode received here
		// will be actually Q and not A. The user would actually need to press what he sees as 'Q' to generate the
		// keyCode 'A' expected by the game. Same issue with 'M' and '?' on AZERTY keyboards. To remove this confusion,
		// we prioritize the text property of the KeyEvent that better matches what the user sees on his keyboard.
		String letterText = keyEvent.getText(); // May be null for non letter keys such as arrows
		if (letterText != null) {
			// Checking if there is a KeyCode associated with that letter
			KeyCode letterCode = KeyCode.getKeyCode(letterText.toUpperCase());
			// If yes, this is the keyCode that we take (should match what the user sees on his keyboard)
			if (letterCode != null) {
				keyCode = letterCode;
			}
		}
		// If we didn't find a KeyCode with the previous code (ex: arrows), we just take the physical KeyCode from
		// the KeyEvent
		if (keyCode == null)
			keyCode = keyEvent.getCode();
		return keyCode;
	}

	private void setScale(double newScale) {
		this.scalePane.setMaxScale(newScale);
		LocalStorage.setItem("scale", String.valueOf(newScale));
	}

	private void setShowInfo(boolean newShowInfo) {
		this.showInfo = newShowInfo;
		LocalStorage.setItem("showInfo", String.valueOf(newShowInfo));
	}

	private void handlePress(KeyCode key, Canvas canvas){
		if (key == KeyCode.PLUS || key == KeyCode.ADD)
			setScale(scalePane.getScale() * 1.1);
		else if (key == KeyCode.MINUS || key == KeyCode.SUBTRACT)
			setScale(scalePane.getScale() / 1.1);
		else if (key == KeyCode.I)
			setShowInfo(!this.showInfo);
		else if (key == KeyCode.P || key == KeyCode.ESCAPE){
			this.paused = !this.paused;
			if (this.paused){
				this.pausedStart = System.currentTimeMillis();
				this.pausedImage = canvas.snapshot(null, new WritableImage(MainApplication.WIDTH, MainApplication.HEIGHT));
				this.buttons.add(new MenuButton(() -> {
					clearEverything();
					/*if (this.currentLevel < 0){
						Editor ed = new Editor(Editor.lastFile);
						MainApplication.stage.getScene().setRoot(ed.getLayout());
					} else*/ {
						HomeScreen hs = new HomeScreen();
						MainApplication.stage.getScene().setRoot(hs.getLayout());
					}
				}, 250, 200, 75, 75, MainApplication.loadImage("button_home.png")));
				this.buttons.add(new MenuButton(() -> {
					if (this.currentLevel < 0){
						loadLevel(gc, -1, this.loadString.split("\n"));
					} else {
						loadLevel(gc, this.currentLevel);
					}
					this.paused = false;
					this.pausedImage = null;
					this.buttons.clear();
				}, 350, 200, 75, 75, MainApplication.loadImage("button_restart.png")));
				this.buttons.add(new MenuButton(() -> handlePress(KeyCode.P, canvas), 450, 200, 75, 75, MainApplication.loadImage("button_continue.png")));
			} else {
				this.pausedImage = null;
				this.pausedTime += System.currentTimeMillis()-this.pausedStart;
				this.buttons.clear();
			}
		} else {
			keys.put(key, true);
		}
	}

	private void clearEverything(){
		this.loop.stop();
		for (GameObject go : sprites){
			go.destroy();
		}
		sprites.clear();
		spritesID.clear();
		collectables.clear();
		effects.clear();
		this.player = null;
		this.exit = null;
	}

	public void shakeCamera(){
		if (this.shaking) return;
		this.shaking = true;
		this.cameraShakeX = -7;
		Scheduler.scheduleDelay(100, () -> this.cameraShakeX = 0);
		Scheduler.scheduleDelay(150, () -> this.cameraShakeY = -7);
		Scheduler.scheduleDelay(250, () -> {this.cameraShakeY = 0; this.shaking = false; });
	}

	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
		gc.setFill(Color.web("#00694F"));
		gc.fillRect(0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);

		if (this.paused){
			gc.drawImage(this.pausedImage, 0, 0);
			gc.save();
			gc.setGlobalAlpha(0.6);
			gc.setFill(Color.BLACK);
			gc.fillRect(0, 0, MainApplication.WIDTH, MainApplication.HEIGHT);
			gc.setGlobalAlpha(1);
			gc.setFill(Color.WHITE);
			gc.scale(MainApplication.SCALE, MainApplication.SCALE);
			gc.setFont(FONT_55);
			gc.fillText("PAUSED", 310, 150);
			for (MenuButton mb : this.buttons){
				mb.render(gc);
			}
			gc.restore();
			return;
		}

		gc.save();
		if (this.showCamera){
			gc.scale(MainApplication.SCALE, MainApplication.SCALE);
			gc.translate(-this.player.getX()+200-this.cameraShakeX, -this.player.getY()+175-this.cameraShakeY);
		} else {
			gc.scale(MainApplication.WIDTH/this.levelWidth, MainApplication.HEIGHT/this.levelHeight);
		}

		// Make background
		for (int x = 0; x < this.levelWidth; x += 25){
			for (int y = 0; y < this.levelHeight; y += 25){
				gc.save();
				switch (this.angles[x/25][y/25]){
					case 0:
						gc.translate(x+25, y);
						gc.rotate(90);
						break;
					case 1:
						gc.translate(x+25, y+25);
						gc.rotate(180);
						break;
					case 2:
						gc.translate(x, y+25);
						gc.rotate(270);
						break;
					case 3:
						gc.translate(x, y);
						gc.rotate(360);
						break;
				}
				gc.drawImage(this.backgroundTile, 0, 0, 25, 25);
				gc.restore();
			}
		}

		for (GameObject go : sprites){
			go.render();
			if (go instanceof Spike || go instanceof Liquid){
				if (go.collided(this.player)){
					this.player.die(false);
				}
			}
		}
		for (int i = 0; i < collectables.size(); i++){
			CollectableObject co = collectables.get(i);
			co.render();
			if (co.collided(this.player)){
				if (co.getType() == CollectableObject.CollectableType.COIN) this.coinsCollected++;
				MainApplication.playSound(MainApplication.COIN_SOUND, false);
				collectables.remove(co);
				i--;
			}
		}
		this.exit.render();
		for (int i = 0; i < effects.size(); i++){
			Particle ef = effects.get(i);
			ef.render();
			if (ef.isFinished()){
				effects.remove(ef);
				i--;
			}
		}
		if (this.player.collided(this.exit.x, this.exit.y, Exit.WIDTH, Exit.HEIGHT)){
			MainApplication.playSound(MainApplication.LEVEL_COMPLETE_SOUND, false);
			if (this.currentLevel == 4){ // Final level
				clearEverything();
				WinScreen ws = new WinScreen();
				MainApplication.stage.getScene().setRoot(ws.getLayout());
				return;
			} /*else if (this.currentLevel < 0){
				clearEverything();
				Editor ed = new Editor(Editor.lastFile);
				MainApplication.stage.getScene().setRoot(ed.getLayout());
				return;
			}*/ else {
				loadLevel(gc, ++this.currentLevel);
			}
		}
		if (keys.getOrDefault(KeyCode.LEFT, false) || keys.getOrDefault(KeyCode.S, false)){
			this.player.moveLeft(Player.X_SPEED);
		}
		if (keys.getOrDefault(KeyCode.RIGHT, false) || keys.getOrDefault(KeyCode.F, false)){
			this.player.moveRight(Player.X_SPEED);
		}
		if (keys.getOrDefault(KeyCode.SPACE, false) || keys.getOrDefault(KeyCode.UP, false)){
			this.player.moveUp(this.specialEffect.specialJump ? Player.Y_SPEED+50 : Player.Y_SPEED);
		}
		if (keys.getOrDefault(KeyCode.K, false)){
			this.player.die(true);
			keys.put(KeyCode.K, false);
		}
		if (keys.getOrDefault(KeyCode.L, false)){
			if (this.currentLevel < 0){
				loadLevel(gc, -1, this.loadString.split("\n"));
			} else {
				loadLevel(gc, this.currentLevel);
			}
			keys.put(KeyCode.L, false);
		}
		if (keys.getOrDefault(KeyCode.F1, false)){
			loadLevel(gc, 0);
			this.currentLevel = 0;
			keys.put(KeyCode.F1, false);
		}
		if (keys.getOrDefault(KeyCode.F3, false)){
			gc.setStroke(Color.GREEN);
			gc.setLineWidth(1);
			gc.strokeRect(this.player.getX(), this.player.getY(), this.player.getWidth(), this.player.getHeight());
			gc.strokeRect(this.player.getX()-Player.X_SPEED, this.player.getY(), this.player.getWidth()+Player.X_SPEED, this.player.getHeight());
			gc.strokeRect(this.player.getX(), this.player.getY(), this.player.getWidth()+Player.X_SPEED, this.player.getHeight());
			gc.strokeRect(this.player.getX(), this.player.getY()-Player.Y_SPEED, this.player.getWidth(), this.player.getHeight()+Player.Y_SPEED);
		}
		if (keys.getOrDefault(KeyCode.F4, false)){
			this.showCamera = !this.showCamera;
			keys.put(KeyCode.F4, false);
		}

		if (keys.getOrDefault(KeyCode.F2, false)){
			// Display nearest objects to the player
			gc.save();
			gc.setStroke(Color.RED);
			GameObject red = this.player.getNearestBottomObject(this.player);
			gc.setLineWidth(4);
			if (red != null) gc.strokeRect(red.getX(), red.getY(), red.getWidth(), red.getHeight());
			gc.restore();
		}
		gc.restore();

		gc.save();
		gc.scale(MainApplication.SCALE, MainApplication.SCALE);

		if (this.showInfo){
			gc.setFill(Color.WHITE);
			gc.fillText("Player at X: " + format2d(this.player.getX()) + " Y:" + format2d(this.player.getY()) +"\nCamera available: " + this.showCamera + "\nFPS: " + this.currentFPS + "\nGravity: "+ format2d(this.player.getGravity()) + "\nLevel: " + this.currentLevel + "\nRunning tasks: " + Scheduler.tasksCount(), 50, 30);
		}

		gc.save();
		gc.setGlobalAlpha(0.6);
		gc.setFill(Color.BLACK);
		gc.fillRect(690, 10, 90, 75);
		gc.setGlobalAlpha(1);
		gc.setFill(Color.WHITE);
		gc.setFont(FONT_20);
		long difference = System.currentTimeMillis()-this.levelStart-this.pausedTime;
		gc.fillText(difference/60000 + ":" + difference/1000%60 + "\nCoins: " + this.coinsCollected +  "\nDeaths: " + this.deaths, 695, 30);
		gc.restore();

		this.notification.render(gc);
		// For mobile
		if (OperatingSystem.isMobile())
			this.joystick.render();
		gc.restore();
	}

	private static String format2d(double d) {
		String s = String.valueOf(d);
		int p = s.indexOf('.');
		if (p == -1) s += ".00";
		else if (p == s.length() - 2) s += "0";
		else s = s.substring(0, p + 3);
		return s;
	}
}
