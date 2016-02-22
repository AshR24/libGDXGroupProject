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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.actor.Base;
import com.game.actor.Platform;
import com.game.actor.Player;
import com.game.App;
import com.game.actor.Spike;
import com.game.managers.ScreenManager;
import com.game.misc.*;
import com.game.misc.myWindow;

import java.util.ArrayList;
import java.util.HashMap;

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
    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tmr;
    private float mapWidth, mapHeight;
    private Vector2 tileSize;

    // All Actors in level
    private Player player;
    private ArrayList<Platform> platforms = new ArrayList<Platform>();
    private ArrayList<Spike> spikes = new ArrayList<Spike>();

    // Windows
    private HashMap<GameState, myWindow> windows = new HashMap<GameState, myWindow>();

    private GameState curGameState;
    public enum GameState
    {
        PLAYING,
        INTRO,
        PAUSED,
        SUCCESS,
        FAILURE,
    }

    // Progress bar
    private Rectangle progressRect;
    private Texture progressTexture;
    private float percent;
    private float progressX;

    private int levelNumber;

    public Play(App app, int levelNumber) {
        super(app);

        skin = new Skin();

        this.levelNumber = levelNumber;

        world = new World(new Vector2(0, Vars.GRAVITY.y), true);
        world.setContactListener(cl);

        b2dr = new Box2DDebugRenderer(); // TODO, remove

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Vars.SCREEN_WIDTH / PPM, Vars.SCREEN_HEIGHT / PPM);
    }

    @Override
    public void show()
    {
        super.show();

        skin.add("default-font", app.assets.get("badaboom60.ttf", BitmapFont.class));
        skin.load(Gdx.files.internal("spritesheets/uiskin.json"));

        progressRect = new Rectangle(stage.getWidth() - 550, (stage.getHeight() - 50), 0, 25);
        progressTexture = app.assets.get("textures/player_red.png", Texture.class);

        initLevel();
        System.out.println("Finished initLevel");
        initWindows();
        System.out.println("Finished initWindows");
        setCurGameState(GameState.INTRO);
    }

    @Override
    public void update(float dt) {

        if(curGameState == GameState.PLAYING)
        {
            world.step(dt, 6, 2);

            CameraUtils.lerpToTarget(cam, player.getPos().scl(PPM).x, 0);
            CameraUtils.lerpToTarget(b2dCam, player.getPos().x, player.getPos().y);
            b2dCam.zoom = 5f;

            Vector2 start = new Vector2(cam.viewportWidth / 2, cam.viewportHeight / 2);
            CameraUtils.setBoundary(cam, start, new Vector2(mapWidth * tileSize.x - start.x * 2, mapHeight * tileSize.y - start.y * 2));

            percent = Interpolation.linear.apply(percent, (player.getPos().x * PPM) / (mapWidth * tileSize.x), 0.2f );
            progressX = (progressRect.x + 500 * percent) - player.getSize().x / 2;

            player.update(dt);
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
            app.sb.draw(app.assets.get("textures/position0.png", Texture.class), (cam.position.x - cam.viewportWidth / 2), cam.position.y - cam.viewportHeight / 2);
            app.sb.draw(app.assets.get("textures/position1.png", Texture.class), (cam.position.x - cam.viewportWidth / 2) * .1f, cam.position.y - (cam.viewportHeight / 2) + 75);
            app.sb.draw(app.assets.get("textures/position2.png", Texture.class), (cam.position.x - cam.viewportWidth / 2) * .01f, cam.position.y - (cam.viewportHeight / 2) - 150);
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
            app.sb.draw(app.assets.get("spritesheets/platformSet.png", Texture.class), 100, (stage.getHeight() - 50));
            app.sb.draw(progressTexture, progressX, progressRect.y, 30, 30);
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

        if(curGameState == GameState.PLAYING)
        {
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
            {
                player.jump();
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            {
                player.setCurColour(Base.Colours.RED);
                progressTexture = app.assets.get("textures/player_red.png", Texture.class);
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            {
                player.setCurColour(Base.Colours.GREEN);
                progressTexture = app.assets.get("textures/player_green.png", Texture.class);
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
                player.setCurColour(Base.Colours.BLUE);
                progressTexture = app.assets.get("textures/player_blue.png", Texture.class);
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_4))
            {
                player.setCurColour(Base.Colours.YELLOW);
                progressTexture = app.assets.get("textures/player_yellow.png", Texture.class);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            if(curGameState == GameState.PAUSED) { setCurGameState(GameState.PLAYING); }
            else { setCurGameState(GameState.PAUSED); }
        }

        if(curGameState == GameState.INTRO)
        {
            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) { setCurGameState(GameState.PLAYING); }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.X)) { isDebug = !isDebug; }
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
        TiledMapTileLayer spikeLayer = (TiledMapTileLayer)tileMap.getLayers().get("SPIKES");

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

        /*for(int row = 0; row < spikeLayer.getHeight(); row++)
        {
            for(int col = 0; col < spikeLayer.getWidth(); col++)
            {
                TiledMapTileLayer.Cell cell = spikeLayer.getCell(col, row);

                if(cell == null) { continue; }
                if(cell.getTile() == null) { continue; }

                if(cell.getTile().getId() == 0) { spikes.add(new Spike(world, new Vector2((col + 0.5f) * tileSize.x, (row + 0.5f) * tileSize.y), new Vector2(tileSize.x, tileSize.y), Base.Colours.RED, Vars.BIT_RED)); }
            }
        }*/
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

        Box2dUtils.makeChain(body, finalV, userData, isSensor, Vars.BIT_PRISMATIC, Vars.BIT_PLAYER);
    }

    /**
     * Method that creates the overlay windows for the intro, pause, sucess and failure states
     */
    private void initWindows()
    {
        Vector2 winPos = new Vector2(280, 50);
        Vector2 winSize = new Vector2(700, 500);

        windows.put(GameState.PLAYING, new myWindow("", new Vector2(0, 0), new Vector2(0, 0), skin, app.assets.get("textures/player_red.png", Texture.class)));
        windows.put(GameState.INTRO, new myWindow("Level " + levelNumber, winPos, winSize, skin, app.assets.get("textures/level" + levelNumber + "Intro.png", Texture.class)));
        windows.put(GameState.PAUSED, new myWindow("", winPos, winSize, skin, app.assets.get("textures/pauseBackground.png", Texture.class)));
        windows.put(GameState.SUCCESS, new myWindow("", winPos, winSize, skin, app.assets.get("textures/successBackground.png", Texture.class)));
        windows.put(GameState.FAILURE, new myWindow("", winPos, winSize, skin, app.assets.get("textures/failureBackground.png", Texture.class)));

        // Init INTRO buttons
        myWindow tempWindow = windows.get(GameState.INTRO);
        tempWindow.addButton(new myButton("Continue", new Vector2((tempWindow.getX() * 2) - 5, tempWindow.getHeight() - 50), skin, "default", new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                setCurGameState(GameState.PLAYING);
            }
        }));

        // Init PAUSED buttons
        tempWindow = windows.get(GameState.PAUSED);
        tempWindow.addButton(new myButton("Continue", new Vector2((tempWindow.getX() / 2) - 25, 290), skin, "default", new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                setCurGameState(GameState.PLAYING);
            }
        }));
        tempWindow.addButton(new myButton("Reset", new Vector2((tempWindow.getX() / 2) - 25, 190), skin, "default", new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setPlayScreen(levelNumber);
            }
        }));
        tempWindow.addButton(new myButton("Exit", new Vector2((tempWindow.getX() / 2) - 25, 90), skin, "default", new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setScreen(ScreenManager.Screen.MENU);
            }
        }));

        // Init SUCCESS buttons
        tempWindow = windows.get(GameState.SUCCESS);
        tempWindow.addButton(new myButton("Continue", new Vector2((tempWindow.getX() / 2) - 25, 290), skin, "default", new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setPlayScreen(levelNumber + 1);
            }
        }));
        tempWindow.addButton(new myButton("Reset", new Vector2((tempWindow.getX() / 2) - 25, 190), skin, "default", new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setPlayScreen(levelNumber);
            }
        }));
        tempWindow.addButton(new myButton("Exit", new Vector2((tempWindow.getX() / 2) - 25, 90), skin, "default", new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setScreen(ScreenManager.Screen.MENU);
            }
        }));

        // Init FAILURE buttons
        tempWindow = windows.get(GameState.FAILURE);
        tempWindow.addButton(new myButton("Reset", new Vector2((tempWindow.getX() / 2) - 25, 190), skin, "default", new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setPlayScreen(levelNumber);
            }
        }));
        tempWindow.addButton(new myButton("Exit", new Vector2((tempWindow.getX() / 2) - 25, 90), skin, "default", new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setScreen(ScreenManager.Screen.MENU);
            }
        }));

        for(myWindow w : windows.values())
        {
            stage.addActor(w);
        }
    }

    // Accessors

    // Mutators
    private void setCurGameState(GameState newGameState)
    {
        if(curGameState != null) {
            windows.get(curGameState).setVisible(false); // hide last window
        }

        curGameState = newGameState;
        System.out.println("Showing: " + curGameState.name() + " window");
        windows.get(curGameState).setVisible(true); // show new window
    }

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

            if(fa.getUserData().equals("PLAYER") && fb.getUserData().equals("SPIKES") ||
                    fb.getUserData().equals("PLAYER") && fa.getUserData().equals("SPIKES"))
            {
                setCurGameState(GameState.FAILURE);
                System.out.println("FAILURE - TOUCHED SPIKE");
                return;
            }

            if(fa.getUserData().equals("PLAYER") && fb.getUserData().equals("PASSBOUNDARY") ||
                    fb.getUserData().equals("PLAYER") && fa.getUserData().equals("PASSBOUNDARY"))
            {
                setCurGameState(GameState.SUCCESS);
                System.out.println("SUCCESS");
                return;
            }

            if(fa.getUserData().equals("PLAYER") && fb.getUserData().equals("FAILBOUNDARY") ||
                    fb.getUserData().equals("PLAYER") && fa.getUserData().equals("FAILBOUNDARY"))
            {
                setCurGameState(GameState.FAILURE);
                System.out.println("FAILURE - TOUCHED FAILBOUNDARY");
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
