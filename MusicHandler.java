import java.util.*;
import greenfoot.*;
/**
 * Handles the music of the game
 * 
 * @author Commentator
 * @version 2020-12-24-01-53
 */
public class MusicHandler  
{
    public final HashMap<String, GreenfootSound> musics, sounds;
    public final DungeonWorld world;
    public GreenfootSound currentSound;
    public MusicHandler(DungeonWorld world) {
        musics = FileWork.loadAllMusic();
        sounds = FileWork.loadAllSounds();
        System.out.println(musics.keySet());
        this.world = world;
        currentSound = musics.get("ow_combat.mp3");
        //currentSound.playLoop();
        currentSound.setVolume(50);
        update();
    }
    
    /**
     * updates the currently played music
     */
    public void update() {
        String name="";
        ArrayList<String> soundNameComposite = new ArrayList<>();
        //checks dungeon style(?)
        soundNameComposite.add("ow");
        name +="ow_";
        
        //checks if enemies are left
        if (world.getObjects(Enemy.class).size() <= 0) {
            soundNameComposite.add("test");
            name += "test";
        }
        else {
            soundNameComposite.add("combat");
            name += "combat";
        }
            
        
        name += ".mp3";
        GreenfootSound song = musics.get(name);
        if (!song.equals(currentSound)) {
            currentSound.stop();
            currentSound = song;
            currentSound.playLoop();
        }
    }
}
