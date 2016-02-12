package com.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.game.Actor.Base;
import com.game.Actor.Platform;
import com.game.Actor.Player;
import com.game.App;
import com.game.Misc.CameraUtils;
import com.game.Misc.Vars;

import java.util.ArrayList;

import static com.game.Misc.Vars.PPM;

/**
 * Created by Ash on 11/02/2016.
 */
public class Play extends AbstractScreen {

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

    private Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jumping.mp3"));

    public Play(App app) {
        super(app);

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


        setupLevel();
    }

    @Override
    public void update(float dt) {
        world.step(dt, 6, 2);

        CameraUtils.lerpToTarget(cam, player.getPos().scl(PPM).x, 0);
        CameraUtils.lockOnTarget(b2dCam, player.getPos().x, player.getPos().y);

        Vector2 start = new Vector2(cam.viewportWidth / 2, cam.viewportHeight / 2);
        CameraUtils.setBoundary(cam, start, new Vector2(mapWidth * tileSize.x - start.x * 2, mapHeight * tileSize.y - start.y * 2));

        player.update(dt);
    }

    @Override
    public void render(float dt) {
        super.render(dt);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);

        if(!isDebug)
        {
            sb.begin();
            assets.get("badaboom25.ttf", BitmapFont.class).draw(sb,"Press M to go back to menu",  0, 0);
            player.render(sb);
            sb.end();

            tmr.setView(cam);
            tmr.render();
        }
        else
        {
            b2dr.render(world, b2dCam.combined);
        }
    }

    @Override
    public void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            //jumpSound.play(); //TODO, fix sound?
            player.jump();
        }



        if(Gdx.input.isKeyJustPressed(Input.Keys.V)) { isDebug = !isDebug; }
    }

    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
    }

    private void setupLevel()
    {
        tileMap = new TmxMapLoader().load("levels/level1.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        MapProperties mapProp = tileMap.getProperties();
        mapWidth = mapProp.get("width", Integer.class);
        mapHeight = mapProp.get("height", Integer.class);
        tileSize = new Vector2(mapProp.get("tilewidth", Integer.class), mapProp.get("tileheight", Integer.class));


        TiledMapTileLayer platformLayer = (TiledMapTileLayer)tileMap.getLayers().get("PLATFORM");

        MapLayer boundaryLayer = tileMap.getLayers().get("BOUNDARY");
        PolylineMapObject polylineObj = (PolylineMapObject)boundaryLayer.getObjects().get(0);
        buildBoundary(polylineObj);

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

                if(cell.getTile().getId() == 1) { platforms.add(new Platform(world, new Vector2((col + 0.5f) * tileSize.x, (row + 0.5f) * tileSize.y), new Vector2(tileSize.x, tileSize.y),  Base.Colours.RED)); }
                else if(cell.getTile().getId() == 2) { platforms.add(new Platform(world, new Vector2((col + 0.5f) * tileSize.x, (row + 0.5f) * tileSize.y), new Vector2(tileSize.x, tileSize.y), Base.Colours.GREEN)); }
                else if(cell.getTile().getId() == 3) { platforms.add(new Platform(world, new Vector2((col + 0.5f) * tileSize.x, (row + 0.5f) * tileSize.y), new Vector2(tileSize.x, tileSize.y), Base.Colours.BLUE)); }
            }
        }
    }

    private void buildBoundary(PolylineMapObject polylineObj)
    {
        Polyline r = polylineObj.getPolyline();
        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bd);

        FixtureDef fd = new FixtureDef();

        ChainShape chain = new ChainShape();

        float[] v = r.getTransformedVertices();
        Vector2[] finalV = new Vector2[v.length / 2];

        for(int i = 0; i < v.length / 2; ++i)
        {
            finalV[i] = new Vector2();
            finalV[i].x = v[i * 2] / PPM;
            finalV[i].y = v[i * 2 + 1] / PPM;
        }

        chain.createChain(finalV);
        fd.shape = chain;

        body.createFixture(fd).setUserData("boundary");
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
                }
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
