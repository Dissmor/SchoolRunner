package ru.myitschool.schoolrunner;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class MyGdxGame extends ApplicationAdapter {
	public static final int SCR_HEIGHT = 720, SCR_WIDTH = 1280;

	SpriteBatch batch;
	OrthographicCamera camera;
	Vector3 touch;
	BitmapFont font;  // Start
	BitmapFont font2; // Pause
	BitmapFont font3; // You Dead
	BitmapFont font4; // Score
	BitmapFont font5; // Record
	Music gameMusic;
	Sound sndJump;
	Sound sndLose;

	long timeLevelUp,timeLevelUpInterval=1500;
	long timeScoreUp,timeScoreUpInterval=1000;
	long timeSave,timeSaveInterval=1000;

	Texture imgFon;
	TextureRegion imgFloor[] = new TextureRegion[4];
	TextureRegion imgMan[] = new TextureRegion[50];
	Texture imgAtlas;
	TextureRegion imgSoundOn, imgSoundOff;
	TextureRegion imgMusicOn, imgMusicOff;
	TextureRegion imgGamePlay, imgGamePause;

	Fon fon[] = new Fon[2];
	Array<Floor> floor = new Array<>();
	Man man;

	int gameScore = 0; // Счёт
	int gameRecord; // Рекорд

	static int gameState=4; // 0 - игра     1 - смерть     2 - ожидаем перезапуск   3 - пауза     4 - ожидание старта
	public static final int GAME_PLAY = 0;
	public static final int GAME_OVER = 1;
	public static final int WAIT_GAME_RESTART = 2;
	public static final int GAME_PAUSE = 3;
	public static final int WAIT_GAME_START = 4;

	boolean isPaused = false;
	boolean isOver = false;
	boolean isStart = true;
	boolean isMusic = true;
	boolean isSound = true;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);
		touch = new Vector3();

		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("gameMusic.mp3"));
		sndJump = Gdx.audio.newSound(Gdx.files.internal("jump.mp3"));
		sndLose = Gdx.audio.newSound(Gdx.files.internal("lose.mp3"));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("19319.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		parameter.size = 190;
		parameter.color = new Color(0, 1, 0.85f, 0.76f); // RGBA
		font = generator.generateFont(parameter);

		FreeTypeFontGenerator generator2 = new FreeTypeFontGenerator(Gdx.files.internal("19319.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter2.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		parameter2.size = 120;
		parameter2.color = new Color(0, 0.245f, 1, 0.46f); // RGBA
		font2 = generator2.generateFont(parameter2);

		FreeTypeFontGenerator generator3 = new FreeTypeFontGenerator(Gdx.files.internal("19319.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter3 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter3.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		parameter3.size = 150;
		parameter3.color = new Color(0.254f, 0f, 0.55f, 0.76f); // RGBA
		font3 = generator3.generateFont(parameter3);

		FreeTypeFontGenerator generator4 = new FreeTypeFontGenerator(Gdx.files.internal("19319.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter4 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter4.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		parameter4.size = 100;
		parameter4.color = new Color(0, 0.250f, 0.154f, 0.59f); // RGBA
		font4 = generator4.generateFont(parameter4);

		FreeTypeFontGenerator generator5 = new FreeTypeFontGenerator(Gdx.files.internal("19319.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter5 = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter5.characters = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяabcdefghijklmnopqrstuvwxyzАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";
		parameter5.size = 50;
		parameter5.color = new Color(0, 0.250f, 0.154f, 0.59f); // RGBA
		font5 = generator5.generateFont(parameter5);

		imgFon = new Texture("Wall.png");

		imgAtlas = new Texture("atlas.png");
		for(int i = 0; i<4; i++) imgFloor[i] = new TextureRegion(imgAtlas,i*256,1500,256,300);
		for(int i=0; i<10; i++) {
			imgMan[i] = new TextureRegion(imgAtlas,i*300,0,300,300);
			imgMan[i+10] = new TextureRegion(imgAtlas,i*300,300,300,300);
			imgMan[i+20] = new TextureRegion(imgAtlas,i*300,600,300,300);
			imgMan[i+30] = new TextureRegion(imgAtlas,i*300,900,300,300);
			imgMan[i+40] = new TextureRegion(imgAtlas,i*300,1200,300,300);
		}
		imgSoundOn = new TextureRegion(imgAtlas,1024,1500,200,200);
		imgSoundOff = new TextureRegion(imgAtlas,1224,1500,200,200);
		imgMusicOn = new TextureRegion(imgAtlas,1424,1500,200,200);
		imgMusicOff = new TextureRegion(imgAtlas,1624,1500,200,200);
		imgGamePlay = new TextureRegion(imgAtlas,1824,1500,200,200);
		imgGamePause = new TextureRegion(imgAtlas,2024,1500,200,200);


		fon[0] = new Fon(0, 0);
		fon[1] = new Fon(SCR_WIDTH, 0);
		for(int i=0; i<6; i++) floor.add(new Floor(i*SCR_WIDTH/5, MathUtils.random(0, 1)));
		man = new Man(SCR_WIDTH/10, SCR_HEIGHT/5);
		loadTableOfRecords();
	}

	@Override
	public void render () {
		// обработка нажатий и касаний
			if (Gdx.input.justTouched()) {
				touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touch);
				if (touch.x>SCR_WIDTH-112 && touch.x < SCR_WIDTH-62 && touch.y>SCR_HEIGHT-56 &&touch.y<SCR_HEIGHT-6) isMusic = !isMusic;
				else if (touch.x>SCR_WIDTH-56 && touch.x<SCR_WIDTH-6 && touch.y>SCR_HEIGHT-56 &&touch.y<SCR_HEIGHT-6) isSound = !isSound;
				else if (touch.x>SCR_WIDTH-168 && touch.x<SCR_WIDTH-118 && touch.y>SCR_HEIGHT-56 &&touch.y<SCR_HEIGHT-6 && !isOver && !isPaused) pause();
				else if (gameState==GAME_PLAY) {
						if(man.condition!=man.JUMP_UP && man.condition!=man.JUMP_DOWN && !isPaused){
							man.condition = man.JUMP_UP;
							if(isSound) sndJump.play();
						}
					} else if (gameState == WAIT_GAME_RESTART) {
						if (Gdx.input.justTouched()) {
							gameScore =0;
							isOver = false;
							gameState = GAME_PLAY;
							fon[0].x = 0;
							fon[1].x = SCR_WIDTH;
							floor.clear();
							for(int i=0; i<6; i++) floor.add(new Floor(i*SCR_WIDTH/5, MathUtils.random(0, 1)));
							man.x = SCR_WIDTH/10;
							man.y = SCR_HEIGHT/5;
							man.isAlive=true;
							man.condition=man.GO;
							man.phase = 0;
							floor.get(0).dx=-3.6f;
							fon[0].dx=-1.2f;
							if(isSound) if(isMusic) gameMusic.play();
						}
				} else if (gameState == GAME_PAUSE) {
						if (Gdx.input.justTouched()) {
							isPaused = false;
							gameState = GAME_PLAY;
						}
				}else if (gameState == WAIT_GAME_START) {
						if(Gdx.input.justTouched()){
							isStart = false;
							gameState = GAME_PLAY;
							isOver = false;
						}
				}
			}
		// События
		if(!isMusic) gameMusic.stop();
		if(!isPaused && !isStart) {
			if (gameState == GAME_OVER) {
				man.isAlive = false;
				gameState = WAIT_GAME_RESTART;
				isOver = true;
				saveTableOfRecords(); // Сохранение рекорда после смерти
				if(isSound) if(isMusic) gameMusic.stop();
 			}

			if(isSound||isMusic){
				gameMusic.setLooping(true);
				gameMusic.play();
			}

			if ((man.condition != man.FAIL_DESK) && (man.condition != man.FAIL_CHAIR)) {
				for (int i = 0; i < 2; i++) fon[i].move();
				for (int i = 0; i < floor.size; i++) floor.get(i).move();
				if (floor.get(0).x <= -floor.get(0).width) {
					floor.removeIndex(0);
					int r = (floor.get(floor.size - 1).type == 0 || floor.get(floor.size - 1).type == 1) ? MathUtils.random(0, 3) : MathUtils.random(0, 1);
					floor.add(new Floor(SCR_WIDTH, r));
				}
			}
			man.move();

			for (int i = 0; i < floor.size; i++) {
				if (man.x > floor.get(i).x-100 && man.x < floor.get(i).x + floor.get(i).width / 3 && floor.get(i).type == 3 && man.condition == man.GO){
					man.condition = man.FAIL_DESK;
					if(isSound) sndLose.play();
				}
				if (man.x > floor.get(i).x-100 && man.x < floor.get(i).x + floor.get(i).width / 3 && floor.get(i).type == 2 && man.condition == man.GO){
					man.condition = man.FAIL_CHAIR;
					if(isSound) sndLose.play();
				}
			}

			if(timeLevelUp+timeLevelUpInterval< TimeUtils.millis()) {
				timeLevelUp=TimeUtils.millis();
				fon[0].dx -= 0.01f;
				floor.get(0).dx -= 0.03f;
			}

			if(gameState==GAME_PLAY)
			if(timeScoreUp+timeScoreUpInterval < TimeUtils.millis()){
				timeScoreUp=TimeUtils.millis();
				gameScore++;
			}
		}

		// вывод изображений (отрисовка всей графики)
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(int i=0; i<2; i++) batch.draw(imgFon, fon[i].x, fon[i].y, fon[i].width+3, fon[i].height);
			if(gameState != GAME_OVER || gameState != WAIT_GAME_RESTART) for (int i = 0; i < floor.size; i++) batch.draw(imgFloor[floor.get(i).type], floor.get(i).x, floor.get(i).y, floor.get(i).width + 5, floor.get(i).height);
			batch.draw(imgMan[man.phase], man.x, man.y, man.width, man.height);

		if(isStart) font.draw(batch, "Старт", 0, SCR_HEIGHT/2, SCR_WIDTH, Align.center, false);
		if(isPaused) font2.draw(batch, "Пауза", 0, SCR_HEIGHT/2, SCR_WIDTH, Align.center, false);
		if(isOver) font3.draw(batch, "Ты проиграл", 0, SCR_HEIGHT/2, SCR_WIDTH, Align.center, false);
		font4.draw(batch, "Время: "+ gameScore, 6, SCR_HEIGHT-6, SCR_WIDTH, Align.topLeft, false);
		if(gameScore >= gameRecord) gameRecord = gameScore;
		font5.draw(batch, "Рекорд: "+ gameRecord, 6, SCR_HEIGHT-106, SCR_WIDTH, Align.topLeft, false);

		if(isSound) batch.draw(imgSoundOn, SCR_WIDTH-56, SCR_HEIGHT-56, 50, 50);
		else batch.draw(imgSoundOff, SCR_WIDTH-56, SCR_HEIGHT-56, 50, 50);
		if(isMusic) batch.draw(imgMusicOn, SCR_WIDTH-112, SCR_HEIGHT-56, 50, 50);
		else batch.draw(imgMusicOff, SCR_WIDTH-112, SCR_HEIGHT-56, 50, 50);
		if(isPaused) batch.draw(imgGamePause, SCR_WIDTH-168, SCR_HEIGHT-56, 50, 50);
		else batch.draw(imgGamePlay, SCR_WIDTH-168, SCR_HEIGHT-56, 50, 50);
		batch.end();

		// Сохранение рекорда каждую секунду.
		if(timeSave+timeSaveInterval < TimeUtils.millis()){
			timeSave=TimeUtils.millis();
			saveTableOfRecords();
		}
	}
	// Сохранение рекорда
	void saveTableOfRecords() {
		Preferences pf = Gdx.app.getPreferences("Records");
		try{
			pf.putInteger("Record-", gameRecord);
			pf.flush();
		}catch (Exception e) {}
	}
	// Загрузка рекорда
	void loadTableOfRecords() {
		Preferences pf = Gdx.app.getPreferences("Records");
		try{
			if(pf.contains("Record-"))
			gameRecord = pf.getInteger("Record-");
		}catch (Exception e) {}
	}

	@Override
	public void pause() {
		super.pause();
		if(!isOver&&!isStart){
			isPaused = true;
			gameState = GAME_PAUSE;
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		imgFon.dispose();
		imgAtlas.dispose();
	}
}
