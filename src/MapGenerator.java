import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapGenerator {
    public Random random;
    public DungeonWorld world;
    public List<RawScreen> screens;
    public Map<String, Screen> currentMap = new HashMap<>();
    public Map<String, RawScreen> rawMap = new HashMap<>();
    public int openCons = 1;
    public int level, x, y;
    public boolean hasEnd, hasBoss;
    //String name, ArrayList<ArrayList<String>> rawMap, HashMap<Enemy, int[]> enemies, Block background, HashMap<String, Block> blocks, HashMap<String, String> adjacentScreens, ImageGenerator imgGen, DungeonWorld world
    public MapGenerator(Random random, List<RawScreen> screens, DungeonWorld world, int level) {
        this.world = world;
        this.random = random;
        this.screens = screens;
        this.level = level;
    }

    public Screen getAt(int x, int y) {
        Screen s = currentMap.get(x+","+y);
        if (s == null)
            s = generateAt(x,y);
        this.x = x;
        this.y = y;
        return s;
    }

    public Screen generateAt(int x, int y) {
        boolean hu=false, hl=false, hr=false, hd=false, gu=false, gl=false, gr=false, gd=false;
        RawScreen tmp;
        System.out.println(rawMap.get(x-1+","+y));
        if ((tmp = rawMap.get(x-1+","+y)) != null) {
            hl = tmp.r;
            gl = true;
        }
        if ((tmp = rawMap.get(x+1+","+y)) != null) {
            hr = tmp.l;
            gr = true;
        }
        if ((tmp = rawMap.get(x+","+(y-1))) != null) {
            hu = tmp.d;
            gu = true;
        }
        if ((tmp = rawMap.get(x+","+(y+1))) != null) {
            hd = tmp.u;
            gd = true;
        }

        boolean u = !gu,d = !gd,l = !gl,r = !gr;
        boolean finalHu = hu;
        boolean finalHd = hd;
        boolean finalHl = hl;
        boolean finalHr = hr;
        List<RawScreen> options = screens.stream()
                .filter(s -> u || finalHu == s.u)
                .filter(s -> d || finalHd == s.d)
                .filter(s -> l || finalHl == s.l)
                .filter(s -> r || finalHr == s.r)
                .collect(Collectors.toList());
        System.out.println("Selecting random from " + options.size() + " rooms");
        tmp = options.get((int)(Math.random()*options.size()));
        openCons += tmp.conCnt;
        openCons--;
        if (hu) openCons--;
        if (hd) openCons--;
        if (hl) openCons--;
        if (hr) openCons--;
        System.out.println("leftover connections: " + openCons);
        Screen compiled = tmp.compile(x, y, world, this);
        rawMap.put(x+","+y,tmp);
        currentMap.put(x+","+y, compiled);
        return compiled;
    }

    public List<Enemy> getEnemies() {
        if (hasBoss) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
    }

    public void transition(String dir) {
        Screen s;
        switch (dir) {
            case "up":
                s = getAt(x,y-1);
                break;
            case "down":
                s = getAt(x, y+1);
                break;
            case "left":
                s = getAt(x-1, y);
                break;
            case "right":
                s = getAt(x+1, y);
                break;
            default:
                s = getAt(x,y);
        }
        s.load(world);
    }
}
