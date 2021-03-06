package com.kevlar.gravity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.kevlar.gravity.managers.PhysicsManager;
import com.kevlar.gravity.managers.ResourcesManager;
import com.kevlar.gravity.managers.SceneManager;
import com.kevlar.gravity.managers.SensorsManager;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;

import java.io.IOException;

public class MainActivity extends BaseGameActivity implements IOnSceneTouchListener {

    final private int CAMERA_WIDTH = 480;
    final private int CAMERA_HEIGHT = 640;
    final private int FPS = 60;
    final private boolean FULLSCREEN = true;

    private static int touch_count = 0;

    private ResourcesManager resourcesManager;
    private PhysicsManager physicsManager;
    private SceneManager sceneManager;
    private SensorsManager sensorsManager;
    private SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle pSavedInstance){
        super.onCreate(pSavedInstance);
        createManagers();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Engine onCreateEngine(EngineOptions pEngineOptions) {
        return new LimitedFPSEngine(pEngineOptions, FPS);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(FULLSCREEN, ScreenOrientation.PORTRAIT_FIXED,
                new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);

//        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
//        engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
        return engineOptions;
    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback onCreateResourcesCallback) throws IOException {

        resourcesManager.loadGameResources();

        onCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback onCreateSceneCallback) throws IOException {

        sceneManager.getMainScene().setOnSceneTouchListener(this);

        createGameScene();

        onCreateSceneCallback.onCreateSceneFinished(sceneManager.getMainScene());
    }

    @Override
    public void onPopulateScene(Scene scene, OnPopulateSceneCallback onPopulateSceneCallback) throws IOException {

        onPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorsManager.registerListener();
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorsManager.unregisterListener();
    }

    @Override
    public boolean onSceneTouchEvent(Scene scene, TouchEvent touchEvent) {

        return onGameTouchEvent(touchEvent);
    }

    //====================================================================
    // GAME METHODS
    //====================================================================

    private void createManagers(){

        resourcesManager = new ResourcesManager(this);
        physicsManager = new PhysicsManager();
        sceneManager = new SceneManager();

        sensorEventListener = new SensorEventListener() {

            public final float SCALE_FACTOR = 2;

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if(physicsManager.getWorld() != null){

                    final Vector2 gravity = Vector2Pool.obtain((sensorEvent.values[0])*(-1)*(SCALE_FACTOR), sensorEvent.values[1]*(SCALE_FACTOR));
                    physicsManager.getWorld().setGravity(gravity);

                    Vector2Pool.recycle(gravity);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
        SensorManager sm = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        sensorsManager = new SensorsManager(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), sensorEventListener);
    }

    private boolean onGameTouchEvent(TouchEvent touchEvent){

        if(physicsManager.getWorld() != null){

            if(touchEvent.isActionDown()){
                this.newCircle(touchEvent.getX(), touchEvent.getY(), touch_count);
                touch_count++;
                return true;
            }
        }
        return false;
    }

    private void newCircle(float pX, float pY, int count){

        Sprite playerSprite;

        switch (count%3){
            case 0:
                playerSprite = new Sprite(pX, pY,
                        resourcesManager.ballR,
                        getVertexBufferObjectManager());
                break;

            case 1:
                playerSprite = new Sprite(pX, pY,
                        resourcesManager.ballG,
                        getVertexBufferObjectManager());
                break;

            case 2:
                playerSprite = new Sprite(pX, pY,
                        resourcesManager.ballB,
                        getVertexBufferObjectManager());
                break;

            default:
                playerSprite = new Sprite(pX, pY,
                        resourcesManager.ballR,
                        getVertexBufferObjectManager());
                break;
        }

        sceneManager.getMainScene().attachChild(playerSprite);

        Body playerBody = physicsManager.createBody(playerSprite,
                PhysicsManager.BodyShape.CIRCLE,
                1.0f, 0.5f, 0.5f,
                BodyDef.BodyType.DynamicBody);

        boolean UPDATE_POSITION = true;
        boolean UPDATE_ROTATION = false;
        physicsManager.getWorld().registerPhysicsConnector(new PhysicsConnector(
                playerSprite,
                playerBody,
                UPDATE_POSITION,
                UPDATE_ROTATION));

    }

    private void createGameScene() {
        this.createWalls();
        sceneManager.getMainScene().registerUpdateHandler(physicsManager.getWorld());
    }

    private void createWalls() {

        final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, this.getVertexBufferObjectManager());
        final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, this.getVertexBufferObjectManager());
        final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, this.getVertexBufferObjectManager());
        final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, this.getVertexBufferObjectManager());

        final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
        PhysicsFactory.createBoxBody(physicsManager.getWorld(), ground, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(physicsManager.getWorld(), roof, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(physicsManager.getWorld(), left, BodyDef.BodyType.StaticBody, wallFixtureDef);
        PhysicsFactory.createBoxBody(physicsManager.getWorld(), right, BodyDef.BodyType.StaticBody, wallFixtureDef);

        this.sceneManager.getMainScene().attachChild(ground);
        this.sceneManager.getMainScene().attachChild(roof);
        this.sceneManager.getMainScene().attachChild(left);
        this.sceneManager.getMainScene().attachChild(right);

    }
}
