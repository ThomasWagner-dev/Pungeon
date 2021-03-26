public class Door extends WorldObj implements Collider{

    public Door() {
        setImage(Utils.loadImageFromAssets("map/wall/door.png").scale(64,64));
    }

    public void tick() {}

    public static void close(DungeonWorld world) {
        world.addObject(new Door(), DungeonWorld.pixelSize/2, DungeonWorld.pixelSize*7-DungeonWorld.pixelSize/2);
        world.addObject(new Door(), DungeonWorld.pixelSize/2, DungeonWorld.pixelSize*6-DungeonWorld.pixelSize/2);
        world.addObject(new Door(), DungeonWorld.pixelSize*11-DungeonWorld.pixelSize/2, DungeonWorld.pixelSize/2);
        world.addObject(new Door(), DungeonWorld.pixelSize*12-DungeonWorld.pixelSize/2, DungeonWorld.pixelSize/2);
        world.addObject(new Door(), DungeonWorld.pixelSize*DungeonWorld.width-DungeonWorld.pixelSize/2, DungeonWorld.pixelSize*7-DungeonWorld.pixelSize/2);
        world.addObject(new Door(), DungeonWorld.pixelSize*DungeonWorld.width-DungeonWorld.pixelSize/2, DungeonWorld.pixelSize*6-DungeonWorld.pixelSize/2);
        world.addObject(new Door(), DungeonWorld.pixelSize*11-DungeonWorld.pixelSize/2, DungeonWorld.pixelSize*DungeonWorld.height-DungeonWorld.pixelSize/2);
        world.addObject(new Door(), DungeonWorld.pixelSize*12-DungeonWorld.pixelSize/2, DungeonWorld.pixelSize*DungeonWorld.height-DungeonWorld.pixelSize/2);
    }
}
