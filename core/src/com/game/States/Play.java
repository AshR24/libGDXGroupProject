package com.game.States;

import static com.game.Misc.Vars.PPM;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.game.Actor.Base;
import com.game.Actor.Platform;
import com.game.Actor.Player;
import com.game.Misc.Vars;

import java.util.ArrayList;

/**
 * Created by Ash on 08/02/2016.
 */
public class Play extends State {

    // TODO, remove
    public boolean isDebug = true;

    // Physics related
    private World world;
    private Box2DDebugRenderer b2dr; // TODO, remove

    private OrthographicCamera b2dCam;

    private float tileSize;

    Player player;

    private TiledMap tileMap;
    private OrthogonalTiledMapRenderer tmr;

    ArrayList<Platform> platforms = new ArrayList<Platform>();

    private Sound jumpSound = Gdx.audio.newSound(Gdx.files.internal("sounds/jumping.mp3"));

    public Play(StateManager sm) {
        super(sm);
        world = new World(new Vector2(0, Vars.GRAVITY.y), true);
        ContactListener cl = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                player.setAction(Player.Action.IDLE);
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
        world.setContactListener(cl);

        b2dr = new Box2DDebugRenderer(); // TODO, remove

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Vars.SCREEN_WIDTH / PPM, Vars.SCREEN_HEIGHT / PPM);
    }

    @Override
    public void init() {
        setupLevel();
    }

    @Override
    public void update(float dt) {
        world.step(dt, 6, 2);
        cam.position.x = player.getPos().x * PPM;
        cam.update();

        b2dCam.position.x = player.getPos().x;
        b2dCam.update();


        player.update(dt);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!isDebug)
        {
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
            jumpSound.play();
            player.jump();
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.V)) { isDebug = !isDebug; }
    }

    @Override
    public void dispose() {

    }

    private void setupLevel()
    {
        player = new Player(world, new Vector2(500, 500), new Vector2(60, 60), Base.Colours.NONE);

        tileMap = new TmxMapLoader().load("levels/level2.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);

        TiledMapTileLayer layer = (TiledMapTileLayer)tileMap.getLayers().get("PLATFORM");
        tileSize = layer.getTileWidth();

        for(int row = 0; row < layer.getHeight(); row++)
        {
            for(int col = 0; col < layer.getWidth(); col++)
            {
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                if(cell == null) { continue; }
                if(cell.getTile() == null) { continue; }

                if(cell.getTile().getId() == 1) { platforms.add(new Platform(world, new Vector2((col + 0.5f) * tileSize, (row + 0.5f) * tileSize), Base.Colours.RED)); }
                else if(cell.getTile().getId() == 2) { platforms.add(new Platform(world, new Vector2((col + 0.5f) * tileSize, (row + 0.5f) * tileSize), Base.Colours.GREEN)); }
                else if(cell.getTile().getId() == 3) { platforms.add(new Platform(world, new Vector2((col + 0.5f) * tileSize, (row + 0.5f) * tileSize), Base.Colours.BLUE)); }
            }
        }
    }

    // Accessors

    // Mutators
}
