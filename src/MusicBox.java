import javax.sound.sampled.Clip;
import java.util.*;
/**
 * Handles the music of the game
 *
 * @author Commentator
 * @version 2020-12-26-23-21 202103181941
 */
public class MusicBox {
    public HashMap<String, Clip> musics, sounds, others;
    public final DungeonWorld world;
    public Clip currentSound;
    public int volume = 30;

    public MusicBox(DungeonWorld world) {
        System.out.println("Loading music...");
        HashMap<String, Clip> s = Utils.loadAllClips();
        musics = new HashMap<>();
        sounds = new HashMap<>();
        others = new HashMap<>();
        s.forEach((name, clip) -> {
            if (name.startsWith("snd")) sounds.put(name, clip);
            else if (name.startsWith("msc")) musics.put(name, clip);
            else others.put(name, clip);
        });
        System.out.println("Loaded sound tracks: " + musics);
//        assets.sounds = FileWork.loadAllSounds();
        System.out.println("Loaded sound effects: " + sounds);
        System.out.println("Loaded tracks:" + others);
        this.world = world;
        setVolume(30);
        update();
    }

    /**
     * updates the currently played music
     */
    public void update() {
        String name = "msc_";
        ArrayList<String> soundNameComposite = new ArrayList<>();
        //checks dungeon style(?)
        soundNameComposite.add("ow");
        name += "ow_";

        //checks if enemies are left
        if (world.objectsOf(Enemy.class).size() <= 0) {
            soundNameComposite.add("combat");
            name += "combat";
        } else {
            soundNameComposite.add("combat");
            name += "combat";
        }
        Clip song = musics.get(name);
        System.out.println(name + song+currentSound);
        if (song != null && !song.equals(currentSound)) {
            MusicHandler.setVolume(song, volume);
            System.out.println("here");
            if (currentSound != null)
                currentSound.stop();
            currentSound = song;
            MusicHandler.loop(currentSound);
            //MusicHandler.setVolume(currentSound, volume);
        }
    }

    public void playSound(String event, String caller) {
        String name = "snd_" + event + "_" + caller;
        switch (event) {
            case "wpn":
                name = name.replaceAll("_[a-zA-Z]$", "");
        }
        try {
            MusicHandler.play(sounds.get(name));
        } catch (Exception ex) {
            System.out.println("unknown sound: " + name);
        }
    }

    public void setVolume(int level) {
        volume = level;
        MusicHandler.setVolume(volume);
    }
}
