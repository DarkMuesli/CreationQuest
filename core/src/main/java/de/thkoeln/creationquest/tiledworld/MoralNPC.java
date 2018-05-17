package de.thkoeln.creationquest.tiledworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoralNPC extends MonologNPC {

    @SuppressWarnings("unused")
    private static final String TAG = MoralNPC.class.getName();

    private List<List<String>> wordLists = new ArrayList<>(3);

    MoralNPC(MapObject mapObject, TiledWorld tiledWorld) {
        super(mapObject, tiledWorld);
        FileHandle handle = Gdx.files.internal("text/Moral.txt");

        String tmpText = handle.readString();
        String[] parts = tmpText.split("###");

        for (int i = 0; i < parts.length; i++) {
            String[] word = parts[i].split("\\r?\\n");
            wordLists.add(new ArrayList<>());
            for (String aWord : word) {
                if (!aWord.isEmpty())
                    wordLists.get(i).add(aWord.trim());
            }
        }
    }

    @Override
    public boolean onInteract(GameObject obj) {

        text = getWord();

        return super.onInteract(obj);
    }

    private String getWord() {
        StringBuilder result = new StringBuilder(32);
        for (List<String> list : wordLists) {
            result.append(" ").append(list.get(new Random().nextInt(list.size())));
        }
        return result.toString().trim();
    }

}
