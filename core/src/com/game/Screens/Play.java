package com.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polyline;
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
    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tmr;
    private float mapWidth, mapHeight;
    private Vector2 tileSize;

    // All Actors in level
    private Player player;
    private ArrayList<Platform> platforms = new ArrayList<Platform>();

    // Pause window
    private boolean isPaused;
    private Window pauseWindow;
    private Image pauseBackground;
    private Image pauseGlow;
    private TextButton butContinue, butReset, butExit;
    private Vector2 buttonSize;


    private int levelNumber;

    private Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jumping.mp3"));

    public Play(App app, int levelNumber) {
        super(app);

        skin = new Skin();

        this.levelNumber = levelNumber;

        world = new World(new Vector2(0, Vars.GRAVITY.y), true);
        world.setContactListener(cl);

        b2dr = new Box2DDebugRenderer(); // TODO, remove

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Vars.SCREEN_WIDTH / PPM, Vars.SCREEN_HEIGHT / PPM);

        isPaused = false;
        buttonSize = new Vector2(50, 50);
    }

    @Override
    public void show()
    {
        super.show();

        skin.add("default-font", app.assets.get("badaboom60.ttf", BitmapFont.class));
        skin.load(Gdx.files.internal("spritesheets/uiskin.json"));

        initLevel();
        initPauseWindow();
    }

    @Override
    public void update(float dt) {
        if(!isPaused)
        {
            world.step(dt, 6, 2);

            CameraUtils.lerpToTarget(cam, player.getPos().scl(PPM).x, 0);
            CameraUtils.lerpToTarget(b2dCam, player.getPos().x, player.getPos().y);
            b2dCam.zoom = 5f;

            Vector2 start = new Vector2(cam.viewportWidth / 2, cam.viewportHeight / 2);
            CameraUtils.setBoundary(cam, start, new Vector2(mapWidth * tileSize.x - start.x * 2, mapHeight * tileSize.y - start.y * 2));

            player.update(dt);
        }

        if(pauseWindow.isVisible() != isPaused)
        {
            pauseWindow.setVisible(isPaused);
            pauseGlow.setVisible(isPaused);
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
            //jumpSound.play(); //TODO, fix sound?
            player.jump();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            isPaused = !isPaused;
            System.out.println("isPaused: " + isPaused);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
        {
            player.setCurColour(Base.Colours.RED);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
        {
            player.setCurColour(Base.Colours.GREEN);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))
        {
            player.setCurColour(Base.Colours.BLUE);
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

    private void initPauseWindow()
    {
        pauseWindow = new Window("Paused", skin);
        pauseWindow.getTitleLabel().setPosition(350, 500);
        pauseBackground = new Image(app.assets.get("textures/pauseBackground.png", Texture.class));
        pauseWindow.setBackground(pauseBackground.getDrawable());
        pauseWindow.setSize(700, 500);
        pauseWindow.setPosition(280, 50);
        pauseWindow.setVisible(false);

        butContinue = new TextButton("Continue", skin, "default");
        butContinue.setPosition((pauseWindow.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 240);
        butContinue.setSize(buttonSize.x, buttonSize.y);
        butContinue.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                isPaused = false;
            }
        });

        butReset = new TextButton("Reset", skin, "default");
        butReset.setPosition((pauseWindow.getWidth() / 2) - buttonSize.x / 2, buttonSize.y + 140);
        butReset.setSize(buttonSize.x, buttonSize.y);
        butReset.addListener(new ClickListener() {
            @Override
            public void clicked (com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                app.sm.setPlayScreen(levelNumber);
            }
        });

        butExit = new TextButton("Exit", skin, "default");
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
                isPaused = true;

                return;
            }

            if(fa.getUserData().equals("PLAYER") && fb.getUserData().equals("FAILBOUNDARY") ||
                    fb.getUserData().equals("PLAYER") && fa.getUserData().equals("FAILBOUNDARY"))
            {
                isPaused = true;
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
