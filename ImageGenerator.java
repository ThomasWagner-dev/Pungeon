import greenfoot.GreenfootImage;
import greenfoot.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ImageGenerator {
    //corners, together they form a complete part
    private GreenfootImage wallCorner = new GreenfootImage("map/wall/connectedTextures/WallCorner.png");
    private GreenfootImage wallEnd = new GreenfootImage("map/wall/connectedTextures/WallEnd.png");
    private GreenfootImage wallPart1 = new GreenfootImage("map/wall/connectedTextures/WallPart1.png");
    private GreenfootImage wallPart2 = new GreenfootImage("map/wall/connectedTextures/WallPart2.png");

    private HashMap<WallType, HashMap<WallConnection[], GreenfootImage>> cacheMap = new HashMap<>(); //todo caching

    public GreenfootImage generateWallImage(int x, int y, List<List<Block>> blocks) {
        //todo
        return null;
    }

    public GreenfootImage generateWallImage(WallType wallType, WallConnection[] connections) {
        GreenfootImage base = new GreenfootImage(wallType.getImage());
        GreenfootImage corner = new GreenfootImage(wallCorner);
        GreenfootImage end = new GreenfootImage(wallEnd);
        GreenfootImage part1 = new GreenfootImage(wallPart1);
        GreenfootImage part2 = new GreenfootImage(wallPart2);


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
                    base.drawImage(part1, 4, 0);
                    base.drawImage(part2, 8, 0);
                    break;
                case CORNER_TOP_RIGHT:
                    right = Arrays.stream(connections).anyMatch(it -> it == WallConnection.RIGHT);
                    top = Arrays.stream(connections).anyMatch(it -> it == WallConnection.TOP);
                    if (right && top) {
                        corner.rotate(90);
                        base.drawImage(corner, 12, 0);
                        corner.rotate(270);
                    } else if (right) {
                        end.rotate(90);
                        base.drawImage(end, 12, 0);
                        end.rotate(270);
                    } else if (top) {
                        base.drawImage(end, 12, 0);
                    } else {
                        corner.rotate(90);
                        base.drawImage(corner, 12, 0);
                        corner.rotate(270);
                    }
                    break;
                case RIGHT:
                    part1.rotate(90);
                    part2.rotate(90);
                    part2.mirrorVertically();
                    base.drawImage(part2, 12, 4);
                    base.drawImage(part1, 12, 8);
                    part1.rotate(270);
                    part2.rotate(270);
                    part2.mirrorVertically();
                    break;
                case CORNER_BOTTOM_RIGHT:
                    right = Arrays.stream(connections).anyMatch(it -> it == WallConnection.RIGHT);
                    bottom = Arrays.stream(connections).anyMatch(it -> it == WallConnection.BOTTOM);
                    if (right && bottom) {
                        corner.rotate(180);
                        base.drawImage(corner, 12, 12);
                        corner.rotate(180);
                    } else if (right) {
                        end.rotate(270);
                        base.drawImage(end, 12, 12);
                        end.rotate(90);
                    } else if (bottom) {
                        base.drawImage(end, 12, 12);
                    } else {
                        corner.rotate(180);
                        base.drawImage(corner, 12, 12);
                        corner.rotate(180);
                    }
                    break;
                case BOTTOM:
                    part1.rotate(180);
                    part2.rotate(180);
                    base.drawImage(part1, 4, 12);
                    base.drawImage(part2, 8, 12);
                    part1.rotate(180);
                    part2.rotate(180);
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
                        corner.rotate(180);
                        base.drawImage(corner, 0, 12);
                        corner.rotate(180);
                    }
                    break;
                case LEFT:
                    part1.rotate(270);
                    part2.rotate(270);
                    part2.mirrorVertically();
                    base.drawImage(part1, 0, 4);
                    base.drawImage(part2, 0, 8);
                    part1.rotate(270);
                    part2.rotate(270);
                    part2.mirrorVertically();
                    break;
            }
        }


        return base;
    }

    public void GenerationTest(World world) {
        int x = 20;
        int y = 20;
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
            block.setImage(image);
            world.addObject(block, x, y);
            x += 20;
            if (x >= world.getWidth() - 16) {
                y += 20;
                x = 20;
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
