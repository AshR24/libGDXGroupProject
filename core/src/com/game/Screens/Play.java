package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.actor.Base;
import com.game.actor.Platform;
import com.game.actor.Player;
import com.game.App;
import com.game.managers.ScreenManager;
import com.game.misc.Box2dUtils;
import com.game.misc.CameraUtils;
import com.game.misc.Vars;

import javax.xml.soap.Text;
import java.util.ArrayList;

import static com.game.misc.Vars.PPM;

/**
 * Created by Ash on 11/02/2016.
 */
public class Play extends AbstractScreen {

    private Skin skin;

    // TODO, remove
    public boolean isDebug = false;

    // Physics related
    private World world;
    private Box2DDebugRenderer b2dr; // TODO, remove
    private OrthographicCamera b2dCam; // TODO, remove

    // TileMap and Map Renderer
    private TiledMap tile1;
    private TiledMap tile2;
    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tmr;
    private float mapWidth, mapHeight;
    private Vector2 tileSize;

    // All Actors in level
    private Player player;
    private ArrayList<Platform> platforms = new ArrayList<Platform>();

    // Intro window
    private boolean isIntro;
    private Window introWindow;
    private Image introBackground;

    // Pause window
    private boolean isPaused;
    private Window pauseWindow;
    private Image pauseBackground;
    private Image pauseGlow;
    private Vector2 buttonSize;

    // Endgame window
    private boolean isEnd;
    private boolean isSuccess;
    private Window endgameWindow;
    private Image failureBackground, successBackground;

    // Progress bar
    private float percent;
    private Rectangle progressRect;

    private int levelNumber;

    private Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jumping.mp3"));
    private Sound colourchangeSound = Gdx.audio.newSound(Gdx.files.internal("sounds/colourchange.mp3"));

    public Play(App app, int levelNumber) {
        super(app);

        skin = new Skin();

        this.levelNumber = levelNumber;

        world = new World(new Vector2(0, Vars.GRAVITY.y), true);
        world.setContactListener(cl);

        b2dr = new Box2DDebugRenderer(); // TODO, remove

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Vars.SCREEN_WIDTH / PPM, Vars.SCREEN_HEIGHT / PPM);

        isIntro = true;
        isPaused = false;

        buttonSize = new Vector2(50, 50);

        isEnd = false;
        isSuccess = false;
    }

    @Override
    public void show()
    {
        super.show();

        skin.add("default-font", app.assets.get("badaboom60.ttf", BitmapFont.class));
        skin.load(Gdx.files.internal("spritesheets/uiskin.json"));

        progressRect = new Rectangle(stage.getWidth() - 550, (stage.getHeight() - 50), 0, 25);

        initLevel();
        initIntroWindow();
        initPauseWindow();
        initEndgameWindow(false);
    }

    @Override
    public void update(float dt) {
        if(!isPaused && !isEnd && !isIntro)
        {
            world.step(dt, 6, 2);

            CameraUtils.lerpToTarget(cam, player.getPos().scl(PPM).x, 0);
            CameraUtils.lerpToTarget(b2dCam, player.getPos().x, player.getPos().y);
            b2dCam.zoom = 5f;

            Vector2 start = new Vector2(cam.viewportWidth / 2, cam.viewportHeight / 2);
            CameraUtils.setBoundary(cam, start, new Vector2(mapWidth * tileSize.x - start.x * 2, mapHeight * tileSize.y - start.y * 2));

            percent = Interpolation.linear.apply(percent, 500, 0.3f );
            System.out.println(mapWidth - player.getPos().x);
            progressRect.width = 0 + 500 * percent;
            if (progressRect.width >= 499) { progressRect.width = 500; }

            player.update(dt);
        }

        if(pauseWindow.isVisible() != isPaused)
        {
            pauseWindow.setVisible(isPaused);
            pauseGlow.setVisible(isPaused);
        }

        if(endgameWindow.isVisible() != isEnd)
        {
            initEndgameWindow(isSuccess);
            endgameWindow.setVisible(isEnd);
            pauseGlow.setVisible(isEnd);
        }

        stage.act(dt);
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        app.sb.setProjectionMatrix(cam.combined);

        if(!isDebug)
        {
            app.sb.begin();
            app.sb.draw(app.assets.get("textures/position0.png", Texture.class), cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2);
            app.sb.draw(app.assets.get("textures/position1.png", Texture.class), cam.position.x - cam.viewportWidth / 2, cam.position.y - (cam.viewportHeight / 2) + 75);
            app.sb.draw(app.assets.get("textures/position2.png", Texture.class), cam.position.x - cam.viewportWidth / 2, cam.position.y - (cam.viewportHeight / 2) - 150);
            player.render(app.sb);
            app.sb.end();

            tmr.setView(cam);
            tmr.render();

            // HUD related
            app.sb.setProjectionMatrix(hudCam.combined);

            app.sr.begin(ShapeRenderer.ShapeType.Filled);
            app.sr.setColor(1, 0, 0, 1);
            app.sr.rect(progressRect.x, progressRect.y, progressRect.width, progressRect.height); // Red loading bar
            app.sr.set(ShapeRenderer.ShapeType.Line);
            app.sr.rect(progressRect.x, progressRect.y, 500f, progressRect.height); // Outline
            app.sr.end();

            app.sb.begin();
            app.sb.draw(app.assets.get("spritesheets/platformSet.png", Texture.class), 100, 100);
            app.sb.end();
        }
        else
        {
            b2dr.render(world, b2dCam.combined);
        }

        stage.draw();
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            jumpSound.play();
            player.jump();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            isPaused = !isPaused;
            System.out.println("isPaused: " + isPaused);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
        {
            if(!isPaused) {
                colourchangeSound.play();
                player.setCurColour(Base.Colours.RED);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
        {
            if(!isPaused) {
                colourchangeSound.play();
                player.setCurColour(Base.Colours.GREEN);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            if (!isPaused) {
                colourchangeSound.play();
                player.setCurColour(Base.Colours.BLUE);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))
        {
            if(!isPaused) {
                player.setCurColour(Base.Colours.YELLOW);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.V)) { isDebug = !isDebug; }
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
        b2dr.dispose();
        tileMap.dispose();
        tmr.dispose();
    }

    private void initLevel()
    {
        tileMap = new TmxMapLoader().load("levels/level" + levelNumber + ".tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        MapProperties mapProp = tileMap.getProperties();
        mapWidth = mapProp.get("width", Integer.class);
        mapHeight = mapProp.get("height", Integer.class);
        tileSize = new Vector2(mapProp.get("tilewidth", Integer.class), mapProp.get("tileheight", Integer.class));


        TiledMapTileLayer platformLayer = (TiledMapTileLayer)tileMap.getLayers().get("PLATFORM");

        MapLayer boundaryLayer = tileMap.getLayers().get("BOUNDARY");
        PolylineMapObject polylineObj = (PolylineMapObject)boundaryLayer.getObjects().get(0);
        initBoundary(polylineObj, "BOUNDARY", false);

        boundaryLayer = tileMap.getLayers().get("FAILBOUNDARY");
        polylineObj = (PolylineMapObject)boundaryLayer.getObjects().get(0);
        initBoundary(polylineObj, "FAILBOUNDARY", true);

        boundaryLayer = tileMap.getLayers().get("PASSBOUNDARY");
        polylineObj = (PolylineMapObject)boundaryLayer.getObjects().get(0);
        initBoundary(polylineObj, "PASSBOUNDARY", true);

        MapLayer playerLayer = tileMap.getLayers().get("PLAYER");
        TextureMapObject playerObj = (TextureMapObject)playerLayer.getObjects().get(0);
        player = new Player(world, new Vector2(playerObj.getX(), playerObj.getY()), new Vector2(60, 60), Base.Colours.NONE);

        for(int row = 0; row < platformLayer.getHeight(); row++)
        {
            for(int col = 0; col < platformLayer.getWidth(); col++)
            {
                TiledMapTileLayer.Cell cell = platformLayer.getCell(col, row);

                if(cell == null) { continue; }
                if(cell.getTile() == null) { continue; }

                if(cell.getTile().getId() == 1) { platforms.add(new Platform(world, new Vector2((col + 0.5f) * tileSize.x, (row + 0.5f) * tileSize.y), new Vector2(tileSize.x, tileSize.y),  Base.Colours.RED, Vars.BIT_RED, Vars.BIT_PLAYER)); }
                else if(cell.getTile().getId() == 2) { platforms.add(new Platform(world, new Vector2((col + 0.5f) * tileSize.x, (row + 0.5f) * tileSize.y), new Vector2(tileSize.x, tileSize.y), Base.Colours.GREEN, Vars.BIT_GREEN, Vars.BIT_PLAYER)); }
                else if(cell.getTile().getId() == 3) { platforms.add(new Platform(world, new Vector2((col + 0.5f) * tileSize.x, (row + 0.5f) * tileSize.y), new Vector2(tileSize.x, tileSize.y), Base.Colours.BLUE, Vars.BIT_BLUE, Vars.BIT_PLAYER)); }
            }
        }
    }

    private void initBoundary(PolylineMapObject polylineObj, String userData, boolean isSensor)
    {
        Polyline r = polylineObj.getPolyline();
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bd);

        float[] v = r.getTransformedVertices();
        Vector2[] finalV = new Vector2[v.length / 2];

        for(int i = 0; i < v.length / 2; ++i)
        {
            finalV[i] = new Vector2();
            finalV[i].x = v[i * 2] / PPM;
            finalV[i].y = v[i * 2 + 1] / PPM;
        }

        Box2dUtils.makeChain(body, finalV, userData, isSensor, Vars.BIT_MISC, Vars.BIT_PLAYER);
    }

    private void initIntroWindow()
    {
        introWindow = new Window("Level "+levelNumber, skin);
        introWindow.getTitleLabel().setPosition(350, 500);
        introBackground = new Image(app.assets.get("textures/level1Intro.png", Texture.class));
        introWindow.setBackground(introBackground.getDrawable());
        introWindow.setSize(700, 500);
        introWindow.setPosition(280, 50);
        introWindow.setVisible(true);

        TextButton butProceed = new TextButton("PROCEED", skin, "default");
        butProceed.setPosition((introWindow.getWidth() / 4) * 3, buttonSize.y + 360);
        butProceed.setSize(buttonSize.x, buttonSize.y);
        butProceed.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                introWindow.setVisible(false);
                isIntro = false;
                isPaused = false;
            }
        });

        introWindow.addActor(butProceed);

        stage.addActor(introWindow);
    }

    private void initPauseWindow()
    {
        pauseWindow = new Window("Paused", skin);
        pauseWindow.getTitleLabel().setPosition(350, 500);
        pauseBackground = new Image(app.assets.get("textures/pauseBackground.png", Texture.class));
        pauseWindow.setBackground(pauseBackground.getDrawable());
        pauseWindow.setSize(700, 500);
        pauseWindow.setPosition(280, 50);
        pauseWindow.setVisible(false);

        TextButton butContinue = new TextButton("Continue", skin, "default");
        butContinue.setPosition((pauseWindow.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 240);
        butContinue.setSize(buttonSize.x, buttonSize.y);
        butContinue.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                isPaused = false;
            }
        });

        TextButton butReset = new TextButton("Reset", skin, "default");
        butReset.setPosition((pauseWindow.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 140);
        butReset.setSize(buttonSize.x, buttonSize.y);
        butReset.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setPlayScreen(levelNumber);
            }
        });

        TextButton butExit = new TextButton("Exit", skin, "default");
        butExit.setPosition((pauseWindow.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 40);
        butExit.setSize(buttonSize.x, buttonSize.y);
        butExit.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setScreen(ScreenManager.Screen.MENU);
            }
        });

        pauseGlow = new Image(app.assets.get("textures/pauseGlow.png", Texture.class));
        pauseGlow.setVisible(false);

        pauseWindow.addActor(butContinue);
        pauseWindow.addActor(butReset);
        pauseWindow.addActor(butExit);

        stage.addActor(pauseGlow);
        stage.addActor(pauseWindow);
    }

    private void initEndgameWindow(boolean success)
    {
        if (success) {
            endgameWindow = new Window("Success", skin);
            successBackground = new Image(app.assets.get("textures/successBackground.png", Texture.class));
            endgameWindow.setBackground(successBackground.getDrawable());

            TextButton butNext = new TextButton("Next", skin, "default");
            butNext.setPosition((pauseWindow.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 240);
            butNext.setSize(buttonSize.x, buttonSize.y);
            butNext.addListener(new ClickListener() {
                @Override
                public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                    app.sm.setPlayScreen(levelNumber+1);
                }
            });
            endgameWindow.addActor(butNext);
        } else {
            endgameWindow = new Window("Failure", skin);
            failureBackground = new Image(app.assets.get("textures/failureBackground.png", Texture.class));
            endgameWindow.setBackground(failureBackground.getDrawable());
        }
        endgameWindow.getTitleLabel().setPosition(350, 500);
        endgameWindow.setSize(700, 500);
        endgameWindow.setPosition(280, 50);
        endgameWindow.setVisible(false);

        TextButton butReset = new TextButton("Reset", skin, "default");
        butReset.setPosition((pauseWindow.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 140);
        butReset.setSize(buttonSize.x, buttonSize.y);
        butReset.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setPlayScreen(levelNumber);
            }
        });

        TextButton butExit = new TextButton("Exit", skin, "default");
        butExit.setPosition((pauseWindow.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 40);
        butExit.setSize(buttonSize.x, buttonSize.y);
        butExit.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setScreen(ScreenManager.Screen.MENU);
            }
        });

        endgameWindow.addActor(butReset);
        endgameWindow.addActor(butExit);

        stage.addActor(endgameWindow);
    }

    // Accessors

    // Mutators

    // Contact Listener
    ContactListener cl = new ContactListener() {
        @Override
        public void beginContact(Contact contact) {
            Fixture fa = contact.getFixtureA();
            Fixture fb = contact.getFixtureB();

            if(fa == null || fb == null) { return; }
            if(fa.getUserData() == null || fb.getUserData() == null) { return; }

            if(fa.getUserData().equals("PLAYER") && fb.getUserData().equals("PLATFORM") ||
                    fb.getUserData().equals("PLAYER") && fa.getUserData().equals("PLATFORM"))
            {
                if(player.getCurAction() != Player.Action.IDLE) {
                    player.setAction(Player.Action.IDLE);
                    return;
                }
            }

            if(fa.getUserData().equals("PLAYER") && fb.getUserData().equals("PASSBOUNDARY") ||
                    fb.getUserData().equals("PLAYER") && fa.getUserData().equals("PASSBOUNDARY"))
            {
                isEnd = true;
                isSuccess = true;
                return;
            }

            if(fa.getUserData().equals("PLAYER") && fb.getUserData().equals("FAILBOUNDARY") ||
                    fb.getUserData().equals("PLAYER") && fa.getUserData().equals("FAILBOUNDARY"))
            {
                isEnd = true;
                isSuccess = false;
                return;
            }

        }

        @Override
        public void endContact(Contact contact) {

        }

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {

        }

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {

        }
    };
}
