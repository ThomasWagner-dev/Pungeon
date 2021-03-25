import java.util.*;

/**
 * Superclass for all moving Objects.
 */
public abstract class Entity extends WorldObj {
    protected int hp, maxhp, dmg;
    protected double speed;
    protected String type;
    protected ArrayList<String> tickiveEffects;
    public String name;

    /**
     * creates objects for everything which is the same everywhere
     */
    public Entity() {
        AdvancedImage tmp = img;
        tmp = tmp.scale(DungeonWorld.pixelSize, DungeonWorld.pixelSize);
        setImage(tmp);
        tickiveEffects = new ArrayList<>();
    }

    /**
     * gets processed every tick. moves the entity and plays animation if present
     */
    public void tick() {
        move();
    }

    /**
     * Moves the entity.
     * If the entity is a Collider:
     * Moves the entity back till the entity is not colliding with another entity anymore.
     */
    protected void move() {
        // Fetch movementvalues as [[x,y],[speedmultiplier]].
        double[][] movementValues = getMovement();
        // Save speedmultiplier for later use.
        double speedmultiplier = movementValues[1][0];
        // Limit the movement to a distance of 1.
        double[] movementOriginal = getLimitedMovement(movementValues[0]);
        // Calculates the movementvector using the limited vector and speed.
        double[] movement = new double[]{movementOriginal[0] * speed * speedmultiplier,
                movementOriginal[1] * speed * speedmultiplier};
        //System.out.println(Arrays.toString(movementOriginal));
        movement = new double[]{Math.round(movement[0]), Math.round(movement[1])};
        int  oldX = x,
                oldY = y;
        double  newX = oldX + movement[0],
                newY = oldY + movement[1];

        // Move the Entity to assumed final location.
        setLocation((int) Math.round(newX), (int) Math.round(newY));
        // Skips collision check if the entity isn't a collider.
        if (!(this instanceof Collider)) return;
        // Move the entity backwards until it's not colliding with another Entity anymore.
        if (isTouching(Collider.class)) {
            setLocation((int)Math.round(newX), oldY);
            if (!isTouching(Collider.class)) return;
            setLocation(oldX, (int)Math.round(newY));
            if (!isTouching(Collider.class)) return;
            setLocation(oldX, oldY);
        }
    }

    /**
     * gets movement as a 2d vector, dependent on either player input or AI
     */
    protected abstract double[][] getMovement();

    protected void takeDamage(int amount, String dmgType) {
        DungeonWorld world = (DungeonWorld) this.world;
        if (this instanceof Projectile) return;
        double dmgMultiplier = (Double) (world.dmgMultiplier.get(dmgType).get(type));
        System.out.println("dmg: " + amount + " mulitplier: " + dmgMultiplier);
        System.out.println("hp before: " + hp);
        hp -= amount * dmgMultiplier;
        System.out.println("hp after: " + hp);
        if (hp <= 0) {
            die();
        } else {
            world.musichandler.playSound("dmg", name);
        }
    }

    protected void collide(Wpn_hitbox p) {
        takeDamage(p.dmg, p.dmgType);
    }

    /**
     * Basic collision physics. check {@link Projectile#tick()} for further information
     */
    protected void collide(Projectile p) {
        DungeonWorld world = (DungeonWorld) this.world;
        world.removeObject(p);
        if (this instanceof Projectile) {
            world.removeObject(this);
            return;
        }
        takeDamage(p.dmg, p.dmgType);
    }

    /**
     * makes the Entity take damage from the @param[attacking entity].
     * If hp is 0 or less afterwards, the dies and gets removed.
     */
    protected void takeDamage(Weapon w) {
        takeDamage(w.dmg, w.dmgType);
    }

    protected void takeDamage(Trap t) {
        takeDamage(t.dmg, t.dmgType);
    }

    /**
     * executed if the entity dies. Removes object from world (and plays animation, if redifined in subclasses)
     */
    protected void die() {
        DungeonWorld world = (DungeonWorld) this.world;
        System.out.println(name + " died and removing");
        try {
            setSprite("entity/enemies/"+name+"_dead.png");
            DeadBody db = new DeadBody();
            db.setImage(img);
            world.addObject(db, x, y);
        }
        catch(Exception e) {
            System.err.println("no dead body defined for " + name);
        }
        world.removeObject(this);
        world.musichandler.update();
        world.musichandler.playSound("die", name);
    }

    /**
     * Calculates the movment vector, that the distance is equal to 1, to avoid overspeeding, when moving diagonal and/or getting full vector to player
     */
    protected double[] getLimitedMovement(double[] movement) {
        double x = movement[0],
                y = movement[1],
                distance = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        if (distance <= 1) //returns if the distance is 1 or less.
            return movement;

        //calculates the multiplicator for x and y to get a distance of the added vectors, which is exaclty 1
        double r = Math.sqrt(Math.abs(1 / (Math.pow(x, 2) + Math.pow(y, 2))));
        //double r = Math.abs((Math.sqrt(Math.pow(x,2)*-1*Math.pow(y,2)+1)-x)/(Math.pow(y,2)+1));
        //System.out.println("r:"+r); //debug print statement
        return new double[]{x * r, y * r}; //returns the multiplied x and y values
    }
    //protected abstrtick void attack(Entity e);

    /**
     * sets the sprite to the given sprite. Includes propper scaling of the image to the overall game size
     */
    protected void setSprite(String spriteName, double scale) {
        AdvancedImage tmp = Utils.loadImageFromAssets(spriteName); //fetches image
        //tmp.scale((int) ((DungeonWorld.pixelSize*DungeonWorld.pixelSize*scale)/tmp.getWidth()),(int) ((DungeonWorld.pixelSize*DungeonWorld.pixelSize*scale)/tmp.getHeight())); //scales it to pixelsize times scale
        tmp = tmp.scale((int) (tmp.getWidth() * scale * DungeonWorld.globalScale), (int) (tmp.getHeight() * scale * DungeonWorld.globalScale));
        setImage(tmp);//sets image
    }

    /**
     * calls setSprite(String spritename, double scale) using scale 1
     */
    protected void setSprite(String spriteName) {
        setSprite(spriteName, 1);
    }

    protected void setSprite(AdvancedImage tmp, double scale) {
        tmp = tmp.scale((int) (tmp.getWidth() * scale * DungeonWorld.globalScale), (int) (tmp.getHeight() * scale * DungeonWorld.globalScale));
        setImage(tmp);//sets image        
    }

    /**
     * calls setSprite(String spritename, double scale) using scale 1
     */
    protected void setSprite(AdvancedImage spriteName) {
        setSprite(spriteName, 1);
    }
}