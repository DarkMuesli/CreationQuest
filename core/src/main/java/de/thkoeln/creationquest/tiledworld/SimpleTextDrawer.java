package de.thkoeln.creationquest.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SimpleTextDrawer {

    private static final int BORDER = 4;
    private static final float DEFAULT_LIFETIME = 5;
    private static final float MAX_LIFETIME = 5;

    private static ShapeRenderer shapeRenderer = new ShapeRenderer();

    private BitmapFont font;
    private GlyphLayout layout;
    private String text;
    private GameObject obj;

    private float lifeTime;
    private float counter;

    private boolean fading;
    private float fadeTime;

    SimpleTextDrawer(GameObject obj, String text, float lifetime) {
        this(obj,text,lifetime, 0);
    }

    SimpleTextDrawer(GameObject obj, String text, float lifeTime, float fadeTime) {
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();
        this.obj = obj;
        this.text = text;
        this.layout.setText(font, text);
        this.fadeTime = fadeTime;
        this.fading = !(fadeTime == 0);
        this.lifeTime = lifeTime;
        this.counter = 0;
    }

    GameObject getObject() {
        return obj;
    }

    void setLifeTime(float lifetime) {
        if (lifetime < 0 || lifetime > MAX_LIFETIME)
            this.lifeTime = DEFAULT_LIFETIME;
        else
            this.lifeTime = lifetime;
    }

    void setText(String text) {
        this.text = text;
        this.layout.setText(font, text);
    }

    /**
     * Draws a complete text with background. No batch begin calls necessary.
     *
     * @param batch
     *            The {@link SpriteBatch} to draw the text with. Rectangle for
     *            background is drawn using static ShapeRenderer.
     */
    public void draw(SpriteBatch batch) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(obj.getWorld().getCam().combined);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(0f, 0f, 0f, font.getColor().a * 0.5f);
        shapeRenderer.rect(obj.getSprt().getX() + obj.getWorld().getMapProp().getTileWidth() / 2 - layout.width / 2 - (BORDER / 2),
                obj.getSprt().getY() + obj.getWorld().getMapProp().getTileHeight() + (BORDER / 2) - 5, layout.width + BORDER,
                layout.height + BORDER);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();
        font.draw(batch, text, obj.getSprt().getX() + obj.getWorld().getMapProp().getTileWidth() / 2 - layout.width / 2,
                obj.getSprt().getY() + obj.getWorld().getMapProp().getTileHeight() + layout.height + BORDER - 5);
        batch.end();
    }

    /**
     * Draws the background only. {@link #beginShapes()} needs to be called
     * beforehand, {@link #endShapes()} afterwards.
     *
     *
     */
    void drawBackground() {

        shapeRenderer.setProjectionMatrix(obj.getWorld().getCam().combined);
        shapeRenderer.setColor(0f, 0f, 0f, font.getColor().a * 0.5f);
        shapeRenderer.rect(obj.getSprt().getX() + obj.getWorld().getMapProp().getTileWidth() / 2 - layout.width / 2 - (BORDER / 2),
                obj.getSprt().getY() + obj.getWorld().getMapProp().getTileHeight() + (BORDER / 2) - 5, layout.width + BORDER,
                layout.height + BORDER);

    }

    static void beginShapes() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeType.Filled);
    }

    static void endShapes() {
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    void drawText(SpriteBatch batch) {
        font.draw(batch, text, obj.getSprt().getX() + obj.getWorld().getMapProp().getTileWidth() / 2 - layout.width / 2,
                obj.getSprt().getY() + obj.getWorld().getMapProp().getTileHeight() + layout.height + BORDER - 5);
    }

    void update(float deltaTime) {
        counter += deltaTime;
        if (fading && counter >= lifeTime - fadeTime) {
            float newTextAlpha = Math.max(0, getAlpha() - (1 / fadeTime) * deltaTime);
            setAlpha(newTextAlpha);
        }
    }

    public void setAlpha(float a) {
        font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, a);
    }

    public float getAlpha() {
        return font.getColor().a;
    }

    boolean isDead() {
        return counter > lifeTime;
    }

    void reset() {
        counter = 0;
    }

    void setFadeTime(float fadeTime) {

        fading = true;

        if (fadeTime > lifeTime || fadeTime < 0)
            this.fadeTime = this.lifeTime;
        else
            this.fadeTime = fadeTime;
    }
}
