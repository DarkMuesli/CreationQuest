package com.mygdx.game.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SimpleTextDrawer {

	private static final int BORDER = 4;

	private static ShapeRenderer shapeRenderer = new ShapeRenderer();

	private BitmapFont font;
	private GlyphLayout layout;
	private String text;
	private GameObject obj;

	public SimpleTextDrawer(GameObject obj) {
		this.font = new BitmapFont();
		this.layout = new GlyphLayout();
		this.obj = obj;
		this.text = "";
	}

	public void setText(String text) {
		this.text = text;
		this.layout.setText(font, text);
	}

	public void setAlpha(float a) {
		font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, a);
	}

	public void drawText(SpriteBatch batch) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(obj.getWorld().getCam().combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0f, 0f, 0f, font.getColor().a * 0.5f);
		shapeRenderer.rect(obj.getSprt().getX() + obj.getWorld().getTileWidth() / 2 - layout.width / 2 - (BORDER / 2),
				obj.getSprt().getY() + obj.getWorld().getTileHeight() + (BORDER / 2) - 5, layout.width + BORDER,
				layout.height + BORDER);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		batch.begin();
		font.draw(batch, text, obj.getSprt().getX() + obj.getWorld().getTileWidth() / 2 - layout.width / 2,
				obj.getSprt().getY() + obj.getWorld().getTileHeight() + layout.height + BORDER - 5);
		batch.end();
	}

	public float getAlpha() {
		return font.getColor().a;
	}

}
