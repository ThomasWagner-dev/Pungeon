import java.util.*;

import greenfoot.*;

/**
 * Handles the music of the game
 *
 * @author Commentator
 * @version 2020-12-26-23-21
 */
public class MusicHandler {
    public final HashMap<String, GreenfootSound> musics, sounds;
    public final DungeonWorld world;
    public GreenfootSound currentSound;
    public int volume = 30;

    public MusicHandler(DungeonWorld world) {
        System.out.println("Loading music...");
        musics = FileWork.loadAllMusic();
        System.out.println("Loaded sound tracks: " + musics.keySet());
        sounds = FileWork.loadAllSounds();
        System.out.println("Loaded sound effects: " + sounds.keySet());
        this.world = world;
        update();
    }

    /**
     * updates the currently played music
     */
    public void update() {
        String name = "";
        ArrayList<String> soundNameComposite = new ArrayList<>();
        //checks dungeon style(?)
        soundNameComposite.add("ow");
        name += "ow_";

        //checks if enemies are left
        if (world.getObjects(Enemy.class).size() <= 0) {
            soundNameComposite.add("combat");
            name += "combat";
        } else {
            soundNameComposite.add("combat");
            name += "combat";
        }
        GreenfootSound song = musics.get(name);
        if (song != null && !song.equals(currentSound)) {
            if (currentSound != null)
                currentSound.stop();
            currentSound = song;
            currentSound.playLoop();
            currentSound.setVolume(volume);
        }
    }

    public void playSound(String event, String caller) {
        String name = "snd_" + event + "_" + caller;
        try {
            sounds.get(name).play();
        } catch (Exception ex) {
            System.out.println("unknown sound: " + name);
        }
    }

    public void setVolume(int level) {
        volume = level;
        currentSound.setVolume(level);
    }
}
