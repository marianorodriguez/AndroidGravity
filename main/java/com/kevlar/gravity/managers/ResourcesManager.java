package com.kevlar.gravity.managers;
import com.kevlar.gravity.MainActivity;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

public class ResourcesManager {

    public BitmapTextureAtlas playerTextureR;
    public BitmapTextureAtlas playerTextureG;
    public BitmapTextureAtlas playerTextureB;

    public ITextureRegion ballR;
    public ITextureRegion ballG;
    public ITextureRegion ballB;

    private MainActivity activity;

    public ResourcesManager(MainActivity act)
    {
        activity = act;
    }

    public void loadMenuResources()
    {
        loadMenuGraphics();
        loadMenuAudio();
    }

    public void loadGameResources()
    {
        loadGameGraphics();
        loadGameFonts();
        loadGameAudio();
    }

    private void loadMenuGraphics(){}

    private void loadMenuAudio(){}

    private void loadGameGraphics() {

        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

        //creo una textura de 64 x 64 (tiene q ser potencia de 2)
        playerTextureR = new BitmapTextureAtlas(activity.getTextureManager(), 64, 64);
        //extraigo la imagen de mi sprite sheet

        ballR = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(playerTextureR, activity, "playerR.png", 0, 0);

        //cargo la textura en VRAM
        playerTextureR.load();

        //creo una textura de 64 x 64 (tiene q ser potencia de 2)
        playerTextureG = new BitmapTextureAtlas(activity.getTextureManager(), 64, 64);
        //extraigo la imagen de mi sprite sheet

        ballG = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(playerTextureG, activity, "playerG.png", 0, 0);

        //cargo la textura en VRAM
        playerTextureG.load();

        //creo una textura de 64 x 64 (tiene q ser potencia de 2)
        playerTextureB = new BitmapTextureAtlas(activity.getTextureManager(), 64, 64);
        //extraigo la imagen de mi sprite sheet

        ballB = BitmapTextureAtlasTextureRegionFactory.
                createFromAsset(playerTextureB, activity, "playerB.png", 0, 0);

        //cargo la textura en VRAM
        playerTextureB.load();

    }

    private void loadGameFonts(){}

    private void loadGameAudio(){}

    public void loadSplashScreen(){}

    public void unloadSplashScreen(){}
}
