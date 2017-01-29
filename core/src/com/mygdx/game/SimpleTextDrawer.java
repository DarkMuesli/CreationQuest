package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class SimpleTextDrawer {

	BitmapFont font;
	GlyphLayout layout;
	ShapeRenderer shapeRenderer;
	String text;
	GameObject obj;

	public SimpleTextDrawer(GameObject obj) {
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		layout = new GlyphLayout();
		this.obj = obj;
		text = "";
	}

	public void setText(String text) {
		this.text = text;
		layout.setText(font, text);
	}

	public void drawText(SpriteBatch batch) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.setProjectionMatrix(obj.getWorld().getCam().combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0f, 0f, 0f, 0.5f);
		shapeRenderer.rect(obj.getPixelPosition().x + obj.getWorld().getTileWidth() / 2 - layout.width / 2 - 2,
				obj.getPixelPosition().y + obj.getWorld().getTileHeight() + 2, layout.width + 4, layout.height + 4);
		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);

		batch.begin();
		font.draw(batch, text,
				obj.getPixelPosition().x + obj.getWorld().getTileWidth() / 2 - layout.width / 2,
				obj.getPixelPosition().y + obj.getWorld().getTileHeight() + layout.height + 4);
		batch.end();
	}

}
