import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawScreen {
    public ArrayList<ArrayList<String>> map;
    public boolean u,d,l,r;
    public final int conCnt;

    public RawScreen(ArrayList<ArrayList<String>> map) {
        this.map = map;
        setCon();
        conCnt = getConCnt();
    }

    public Screen compile(int x, int y, DungeonWorld world, MapGenerator mg) {
        Screen s = new Screen(x+","+y, map, createEnemies(mg), world.blocks.get("tile_ground"), world.blocks, new HashMap<>(), world.imgG, world);
        generateConnections(mg, x, y, s);
        return s;
    }

    public HashMap<Enemy, int[]> createEnemies(MapGenerator mg) {
        List<Enemy> enemies = mg.getEnemies();
        HashMap<Enemy, int[]> e = new HashMap<>();
        int w = map.get(0).size(), h = map.size();
        enemies.forEach(en -> {
            int x, y;
            do {
                x = (int) (Math.random() * (w - 4)) + 2;
                y = (int) (Math.random() * (h - 4)) + 2;
                int fx = x;
                int fy = y;
                if (e.values().stream().noneMatch(pos -> pos[0] == fx && pos[1] == fy))
                    break;
            }while (true);
            e.put(en, new int[] {x,y});
        });
        return e;
    }

    public void setCon() {
        int width = map.get(0).size()-1, height = map.size()-1;
        u = !map.get(0).get(width/2).startsWith("wall");
        d = !map.get(height).get(width/2).startsWith("wall");
        l = !map.get(height/2).get(0).startsWith("wall");
        r = !map.get(height/2).get(width).startsWith("wall");
    }

    public int getConCnt() {
        int cnt = 0;
        if (u) cnt++;
        if (d) cnt++;
        if (l) cnt++;
        if (r) cnt++;
        return cnt;
    }

    public void generateConnections(MapGenerator mg, int x, int y, Screen s) {
        Screen ad;
        String name = x+","+y;
        mg.currentMap.put(name, s);
        ad = mg.currentMap.get(x-1+","+y);
        if (ad != null) {
            ad.adjacentScreens.put("right", name);
            s.adjacentScreens.put("left", ad.name);
        }
        ad = mg.currentMap.get(x+1+","+y);
        if (ad != null) {
            ad.adjacentScreens.put("left", name);
            s.adjacentScreens.put("right", ad.name);
        }
        ad = mg.currentMap.get(x+","+(y-1));
        if (ad != null) {
            ad.adjacentScreens.put("down", name);
            s.adjacentScreens.put("up", ad.name);
        }
        ad = mg. currentMap.get(x+","+(y+1));
        if (ad != null) {
            ad.adjacentScreens.put("up", name);
            s.adjacentScreens.put("down", ad.name);
        }
    }
}
