import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.*;

/**
 * I'm just not gonna comment this pice of code.
 * This code was made by Scisneromam#9346 on discord. If you do have any questions about this, please ask them.
 */
public class ImageGenerator {
    //corners, together they form a complete part
    private GreenfootImage wallCorner = new GreenfootImage("map/wall/connectedTextures/WallCorner.png");
    private GreenfootImage wallEnd = new GreenfootImage("map/wall/connectedTextures/WallEnd.png");
    private GreenfootImage wallMiddle = new GreenfootImage("map/wall/connectedTextures/WallMiddle.png");

    private HashMap<WallType, HashMap<WallConnection[], GreenfootImage>> cacheMap = new HashMap<>();

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

    public void addAllToWorld(World world, ArrayList<ArrayList<String>> blocks) {
        for (int y = 0; y < blocks.size(); y++) {
            for (int x = 0; x < blocks.size(); x++) {
                GreenfootImage image = generateWallImage(x, y, blocks);
                if (image == null) {
                    continue;
                }
                image.scale(world.getCellSize(), world.getCellSize()); //removed due to scaling in Screen
                Block block = new Wall();
                block.setImage(image);
                world.addObject(block, x, y);
            }
        }
    }

    public GreenfootImage generateWallImage(int x, int y, ArrayList<ArrayList<String>> blocks) {
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

    public GreenfootImage generateWallImage(WallType wallType, Collection<WallConnection> connections) {
        return generateWallImage(wallType, connections.toArray(new WallConnection[0]));
    }

    public GreenfootImage generateWallImage(WallType wallType, WallConnection[] connections) {
        if (wallType == null) {
            return null;
        }

        HashMap<WallConnection[], GreenfootImage> map = cacheMap.get(wallType);
        if (map != null) {
            GreenfootImage image = map.get(connections);
            if (image != null) {
                return image;
            }
        }

        GreenfootImage base = new GreenfootImage(wallType.getImage());
        GreenfootImage corner = new GreenfootImage(wallCorner);
        GreenfootImage end = new GreenfootImage(wallEnd);
        GreenfootImage middle = new GreenfootImage(wallMiddle);


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
                        end.rotate(270); // or -90
                        base.drawImage(end, 0, 0);
                        end.rotate(90);
                    } else if (top) {
                        end.rotate(180);
                        base.drawImage(end, 0, 0);
                        end.rotate(180);
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
                        corner.rotate(90);
                        base.drawImage(corner, 12, 0);
                        corner.rotate(270);
                    } else if (right) {
                        end.rotate(270);
                        base.drawImage(end, 12, 0);
                        end.rotate(90);
                    } else if (top) {
                        base.drawImage(end, 12, 0);
                    } else {
                        corner.rotate(90);
                        base.drawImage(corner, 12, 0);
                        corner.rotate(270);
                    }
                    break;
                case RIGHT:
                    middle.rotate(90);
                    base.drawImage(middle, 12, 4);
                    base.drawImage(middle, 12, 8);
                    middle.rotate(270);
                case CORNER_BOTTOM_RIGHT:
                    right = Arrays.stream(connections).anyMatch(it -> it == WallConnection.RIGHT);
                    bottom = Arrays.stream(connections).anyMatch(it -> it == WallConnection.BOTTOM);
                    if (right && bottom) {
                        corner.rotate(180);
                        base.drawImage(corner, 12, 12);
                        corner.rotate(180);
                    } else if (right) {
                        end.rotate(90);
                        base.drawImage(end, 12, 12);
                        end.rotate(270);
                    } else if (bottom) {
                        base.drawImage(end, 12, 12);
                    } else {
                        corner.rotate(180);
                        base.drawImage(corner, 12, 12);
                        corner.rotate(180);
                    }
                    break;
                case BOTTOM:
                    middle.rotate(180);
                    base.drawImage(middle, 4, 12);
                    base.drawImage(middle, 8, 12);
                    middle.rotate(180);
                    break;
                case CORNER_BOTTOM_LEFT:
                    left = Arrays.stream(connections).anyMatch(it -> it == WallConnection.LEFT);
                    bottom = Arrays.stream(connections).anyMatch(it -> it == WallConnection.BOTTOM);
                    if (left && bottom) {
                        corner.rotate(270);
                        base.drawImage(corner, 0, 12);
                        corner.rotate(90);
                    } else if (left) {
                        end.rotate(90);
                        end.mirrorHorizontally();
                        base.drawImage(end, 0, 12);
                        end.mirrorHorizontally();
                        end.rotate(270);
                    } else if (bottom) {
                        end.rotate(180);
                        base.drawImage(end, 0, 12);
                        end.rotate(180);
                    } else {
                        corner.rotate(270);
                        base.drawImage(corner, 0, 12);
                        corner.rotate(90);
                    }
                    break;
                case LEFT:
                    middle.rotate(270);
                    base.drawImage(middle, 0, 4);
                    base.drawImage(middle, 0, 8);
                    middle.rotate(90);
                    break;
            }
        }

        cacheMap.putIfAbsent(wallType, new HashMap<>());
        cacheMap.get(wallType).put(connections, base);
        return base;
    }

    public void GenerationTest(World world, int scale) {
        int mod = scale / world.getCellSize() + scale / world.getCellSize() / 4;
        int x = mod;
        int y = mod;
        WallType baseType = WallType.NORMAL;

        WallConnection[] allConnections = WallConnection.values();
        int amount = 1 << allConnections.length; // equal to 2^allConnections.length

        //generrate powerset
        for (int i = 0; i < amount; i++) {
            WallConnection[] connections = new WallConnection[Integer.bitCount(i)];
            for (int j = 0, bit = i, k = 0; j < allConnections.length; ++j, bit >>= 1) {
                if ((bit & 1) != 0) {
                    connections[k++] = allConnections[j];
                }
            }

            Block block = new Wall();
            GreenfootImage image = generateWallImage(baseType, connections);
            image.scale(scale, scale);
            block.setImage(image);
            world.addObject(block, x, y);
            x += mod;
            if (x >= world.getWidth() - (scale / world.getCellSize())) {
                y += mod;
                x = mod;
            }
        }
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
        NORMAL(new GreenfootImage("map/wall/wall1.png")),
        CRACKED(new GreenfootImage("map/wall/wall1_crack.png")),
        HOLE(new GreenfootImage("map/wall/wall1_hole.png"));

        private GreenfootImage image;

        WallType(GreenfootImage greenfootImage) {
            this.image = greenfootImage;
        }

        public GreenfootImage getImage() {
            return image;
        }
    }
}
