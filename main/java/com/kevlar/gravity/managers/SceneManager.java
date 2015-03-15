package com.kevlar.gravity.managers;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;

public class SceneManager {

    private static Scene mainScene;

    public SceneManager(){
        mainScene = new Scene();              //valores RGB
        mainScene.setBackground(new Background(255, 255, 255));
    }

    public Scene getMainScene(){
        return mainScene;
    }
}
