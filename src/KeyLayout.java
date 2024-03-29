/**
 * Saves the key mapping for (controller and) keyboard.
 * TODO: save in SFO
 *
 * @author Commentator
 * @version 2020-11-30
 */
public class KeyLayout {
    public char
            up = 'w',
            down = 's',
            left = 'a',
            right = 'd',
            use = 'e',
            attack = ' ',
            w_cycle_f = 'y',
            w_cycle_b = 'x',
            i_cycle_f = 'f',
            i_cycle_b = 'r',
            map = 9,
            pause = 27;

    public String
            run = "shift",
            sneak = "ctrl";

    enum tickionGroup {
        MOVEMENT,
        USE,
        ATTACK,
        ITEMS,
        WEAPONS,
        PAUSE,
        MAP
    }

    /**
     * TODO: open remap window for remapping
     */
    public void remap() {
    }

    /**
     * returns a string of the keybinds of the tickiongroup
     * @param ag the tickiongroup the keybinds are requested of
     * @return a string of all keybinds in the tickiongroup
     */
    public String getKeysOftickionGroup(tickionGroup ag) {
        switch (ag) {
            case MOVEMENT:
                return String.join(", ", new String[]{up+"", down+"", left+"", right+"", run, sneak});
            case USE:
                return use+"";
            case ATTACK:
                return attack == ' '? "space" : attack+"";
            case ITEMS:
                return String.join(", ", new String[]{i_cycle_f+"", i_cycle_b+""});
            case WEAPONS:
                return String.join(", ", new String[]{w_cycle_f+"", w_cycle_b+""});
            case PAUSE:
                return pause+"";
            case MAP:
                return "tab";
            default:
                return "";
        }
    }
}
