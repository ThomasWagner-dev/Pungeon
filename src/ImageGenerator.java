import java.util.*;

/**
 * I'm just not gonna comment this pice of code.
 * This code was made by Scisneromam#9346 on discord. If you do have any questions about this, please ask them.
 * This is a connected textures class and generates a wall image from the given position and the raw map.
 */
public class ImageGenerator {
    //corners, together they form a complete part
    private AdvancedImage wallCorner = Utils.loadImageFromAssets("map/wall/connectedTextures/WallCorner.png");
    private AdvancedImage wallEnd = Utils.loadImageFromAssets("map/wall/connectedTextures/WallEnd.png");
    private AdvancedImage wallMiddle = Utils.loadImageFromAssets("map/wall/connectedTextures/WallMiddle.png");

    private HashMap<WallType, HashMap<WallConnection[], AdvancedImage>> cacheMap = new HashMap<>();

    public WallType getWallType(String string) {
        string = string.toLowerCase();
        if (!string.startsWith("wall_")) {
            return null;
        }
        if (string.contains("crack")) {
            return WallType.CRACKED;
        }
        if (string.contains("hole")) {
            return WallType.HOLE;
        }
        return WallType.NORMAL;
    }

    public WallType getWallType(int x, int y, ArrayList<ArrayList<String>> blocks) {
        //check if x and y are in bounds
        if (y < 0 || y >= blocks.size()) {
            return null;
        }
        if (x < 0 || x >= blocks.get(y).size()) {
            return null;
        }
        return getWallType(blocks.get(y).get(x));
    }

    public AdvancedImage generateWallImage(int x, int y, ArrayList<ArrayList<String>> blocks) {
        wallCorner.imgpos = AdvancedImage.ImagePosition.TOP_LEFT;
        wallEnd.imgpos = AdvancedImage.ImagePosition.TOP_LEFT;
        wallMiddle.imgpos = AdvancedImage.ImagePosition.TOP_LEFT;
        //check if x and y are in bounds
        if (y < 0 || y >= blocks.size()) {
            return null;
        }
        if (x < 0 || x >= blocks.get(y).size()) {
            return null;
        }

        HashSet<WallConnection> connections = new HashSet<>();
        WallType wallType = getWallType(blocks.get(y).get(x));

        if (wallType == null) {
            return null;
        }

        boolean left = getWallType(x - 1, y, blocks) != null;
        boolean right = getWallType(x + 1, y, blocks) != null;
        boolean top = getWallType(x, y - 1, blocks) != null;
        boolean bottom = getWallType(x, y + 1, blocks) != null;

        //block is encased by other walls
        if (top && left) {
            if (getWallType(x - 1, y - 1, blocks) == null) {
                connections.add(WallConnection.CORNER_TOP_LEFT);
            }
        }
        if (top && right) {
            if (getWallType(x + 1, y - 1, blocks) == null) {
                connections.add(WallConnection.CORNER_TOP_RIGHT);
            }
        }
        if (bottom && left) {
            if (getWallType(x - 1, y + 1, blocks) == null) {
                connections.add(WallConnection.CORNER_BOTTOM_LEFT);
            }
        }
        if (bottom && right) {
            if (getWallType(x + 1, y + 1, blocks) == null) {
                connections.add(WallConnection.CORNER_BOTTOM_RIGHT);
            }
        }

        //block is at edge of map
        if (y == 0) {
            connections.add(WallConnection.CORNER_TOP_LEFT);
            connections.add(WallConnection.TOP);
            connections.add(WallConnection.CORNER_TOP_RIGHT);
        }
        if (y == blocks.size() - 1) {
            connections.add(WallConnection.CORNER_BOTTOM_LEFT);
            connections.add(WallConnection.BOTTOM);
            connections.add(WallConnection.CORNER_BOTTOM_RIGHT);
        }
        if (x == 0) {
            connections.add(WallConnection.CORNER_TOP_LEFT);
            connections.add(WallConnection.LEFT);
            connections.add(WallConnection.CORNER_BOTTOM_LEFT);
        }
        if (x == blocks.get(y).size() - 1) {
            connections.add(WallConnection.CORNER_TOP_RIGHT);
            connections.add(WallConnection.RIGHT);
            connections.add(WallConnection.CORNER_BOTTOM_RIGHT);
        }

        //block is in map
        if (!left) {
            connections.add(WallConnection.CORNER_TOP_LEFT);
            connections.add(WallConnection.LEFT);
            connections.add(WallConnection.CORNER_BOTTOM_LEFT);
        }
        if (!right) {
            connections.add(WallConnection.CORNER_TOP_RIGHT);
            connections.add(WallConnection.RIGHT);
            connections.add(WallConnection.CORNER_BOTTOM_RIGHT);
        }
        if (!top) {
            connections.add(WallConnection.CORNER_TOP_LEFT);
            connections.add(WallConnection.TOP);
            connections.add(WallConnection.CORNER_TOP_RIGHT);
        }
        if (!bottom) {
            connections.add(WallConnection.CORNER_BOTTOM_LEFT);
            connections.add(WallConnection.BOTTOM);
            connections.add(WallConnection.CORNER_BOTTOM_RIGHT);
        }

        return generateWallImage(wallType, connections);
    }

    public AdvancedImage generateWallImage(WallType wallType, Collection<WallConnection> connections) {
        return generateWallImage(wallType, connections.toArray(new WallConnection[0]));
    }

    public AdvancedImage generateWallImage(WallType wallType, WallConnection[] connections) {
        if (wallType == null) {
            return null;
        }

        HashMap<WallConnection[], AdvancedImage> map = cacheMap.get(wallType);
        if (map != null) {
            AdvancedImage image = map.get(connections);
            if (image != null) {
                return image;
            }
        }

        AdvancedImage base = new AdvancedImage(wallType.getImage());
        AdvancedImage corner = new AdvancedImage(wallCorner);
        AdvancedImage end = new AdvancedImage(wallEnd);
        AdvancedImage middle = new AdvancedImage(wallMiddle);
        AdvancedImage tmp;

        //maybe create copies instead of rotating?
        for (WallConnection connection : connections) {
            boolean left = false;
            boolean right = false;
            boolean top = false;
            boolean bottom = false;
            switch (connection) {
                case CORNER_TOP_LEFT:
                    left = Arrays.stream(connections).anyMatch(it -> it == WallConnection.LEFT);
                    top = Arrays.stream(connections).anyMatch(it -> it == WallConnection.TOP);
                    if (left && top) {
                        base.drawImage(corner, 0, 0);
                    } else if (left) {
                        base.drawImage(end.rotate(270), 0, 0);
                    } else if (top) {
                        base.drawImage(end.rotate(180), 0, 0);
                    } else {
                        base.drawImage(corner, 0, 0);
                    }
                    break;
                case TOP:
                    base.drawImage(middle, 4, 0);
                    base.drawImage(middle, 8, 0);
                    break;
                case CORNER_TOP_RIGHT:
                    right = Arrays.stream(connections).anyMatch(it -> it == WallConnection.RIGHT);
                    top = Arrays.stream(connections).anyMatch(it -> it == WallConnection.TOP);
                    if (right && top) {
                        base.drawImage(corner.rotate(90), 12, 0);
                    } else if (right) {
                        base.drawImage(end.rotate(270), 12, 0);
                    } else if (top) {
                        base.drawImage(end, 12, 0);
                    } else {
                        base.drawImage(corner.rotate(90), 12, 0);
                    }
                    break;
                case RIGHT:
                    tmp = middle.rotate(90);
                    base.drawImage(tmp, 12, 4);
                    base.drawImage(tmp, 12, 8);
                case CORNER_BOTTOM_RIGHT:
                    right = Arrays.stream(connections).anyMatch(it -> it == WallConnection.RIGHT);
                    bottom = Arrays.stream(connections).anyMatch(it -> it == WallConnection.BOTTOM);
                    if (right && bottom) {
                        base.drawImage(corner.rotate(180), 12, 12);
                    } else if (right) {
                        base.drawImage(end.rotate(90), 12, 12);
                    } else if (bottom) {
                        base.drawImage(end, 12, 12);
                    } else {
                        base.drawImage(corner.rotate(180), 12, 12);
                    }
                    break;
                case BOTTOM:
                    tmp = middle.rotate(180);
                    base.drawImage(tmp, 4, 12);
                    base.drawImage(tmp, 8, 12);
                    break;
                case CORNER_BOTTOM_LEFT:
                    left = Arrays.stream(connections).anyMatch(it -> it == WallConnection.LEFT);
                    bottom = Arrays.stream(connections).anyMatch(it -> it == WallConnection.BOTTOM);
                    if (left && bottom) {
                        base.drawImage(corner.rotate(270), 0, 12);
                    } else if (left) {
                        base.drawImage(end.rotate(90).mirrorHorizontally(), 0, 12);
                    } else if (bottom) {
                        base.drawImage(end.rotate(180), 0, 12);
                    } else {
                        base.drawImage(corner.rotate(270), 0, 12);
                    }
                    break;
                case LEFT:
                    tmp = middle.rotate(270);
                    base.drawImage(tmp, 0, 4);
                    base.drawImage(tmp, 0, 8);
                    break;
            }
        }

        cacheMap.putIfAbsent(wallType, new HashMap<>());
        cacheMap.get(wallType).put(connections, base);
        return base;
    }

    public enum WallConnection {
        CORNER_TOP_LEFT,
        TOP,
        CORNER_TOP_RIGHT,
        RIGHT,
        CORNER_BOTTOM_RIGHT,
        BOTTOM,
        CORNER_BOTTOM_LEFT,
        LEFT
    }

    public enum WallType {
        NORMAL(Utils.loadImageFromAssets("map/wall/wall1.png")),
        CRACKED(Utils.loadImageFromAssets("map/wall/wall1_crack.png")),
        HOLE(Utils.loadImageFromAssets("map/wall/wall1_hole.png"));

        private AdvancedImage image;

        WallType(AdvancedImage image) {
            this.image = image;
            image.imgpos = AdvancedImage.ImagePosition.TOP_LEFT;
        }

        public AdvancedImage getImage() {
            return image;
        }
    }
}
