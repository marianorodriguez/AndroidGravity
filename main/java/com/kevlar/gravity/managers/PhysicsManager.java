package com.kevlar.gravity.managers;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;

public class PhysicsManager {

    public enum BodyShape{

        CIRCLE,
    }

    private PhysicsWorld physicsWorld;

    public PhysicsManager(){
        physicsWorld = new PhysicsWorld(new Vector2(0.0f, SensorManager.GRAVITY_EARTH), false);

    }

    public PhysicsWorld getWorld(){
        return physicsWorld;
    }

    private FixtureDef setFixtureDef(float density, float elasticity, float friction){
        return PhysicsFactory.createFixtureDef(density, elasticity, friction);
    }

    public Body createBody(Sprite sprite, BodyShape shape, float density, float elasticity, float friction, BodyDef.BodyType type){

        FixtureDef fDef = setFixtureDef(density, elasticity, friction);

        switch(shape){
            case CIRCLE:
                return PhysicsFactory.createCircleBody(physicsWorld, sprite, type, fDef);

            default:
                return null;
        }
    }
}
