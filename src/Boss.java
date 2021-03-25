import java.awt.*;
import java.util.Arrays;

public class Boss extends CollidingEnemy implements AttackIndication{

    public int level;
    public int attack;
    public int cooldown;
    public double speedmp = 1;
    public boolean isAtt;
    public Point pos;
    public Point towards;

    public Boss(int level) {
        super("boss");
        setSprite("entity/enemies/boss_{}.png".replace("{}", level%5+""));
        //img = img.replaceColor(Color.BLUE, Color.getHSBColor((float) (((level*30)%360)/360.0), 1,1));
        Weapon w;
        speed = 2;
        int range, speed;
        String dmgType, hitbox;
        boolean melee;
        hp = 5*level;
        maxhp = 5*level;
        type = "physical";
        dmg = level*4;

        pos = new Point(x,y);

        w = new Weapon("boss_weapon",
                "the bosses weapon",
                "The weapon, owned by the boss",
                2,
                level*4,
                "electric",
                10,
                300,
                "Invis.png",
                "entity/projectiles/zap.png",
                false);

        weapon = w;
    }

    @Override
    public Enemy clone() {
        return topClone(new Boss(level), this);
    }

    public void die() {
        world.objectsOf(Teleport.class).forEach(Teleport::unseal);
        world.removeObjects(Door.class);
        super.die();
    }

    public void tick() {
        cooldown--;
        if (cooldown % 10 == 1) {
            pos = new Point(x,y);
        }
        if (cooldown <= 0)
            attack();
        move();
    }


    public void attack() {
        if (isAtt) return;
        DungeonWorld world = (DungeonWorld) this.world;
        attack = world.random.nextInt(3);
        Player p = world.objectsOf(Player.class).get(0);
        double[] dir = new double[] {x-p.x, y-p.y};
        System.out.println(Arrays.toString(dir));
        switch(attack) {
            case 0:
                Indicator indi = new Indicator(Utils.loadImageFromAssets("entity/projectiles/indicator_point.png"), null, 50, this);
                world.addObject(indi, p.x, p.y);
                isAtt = true;
                break;
            case 1:
                for (int a = 0; a <= 0; a+=1) {
                    Projectile pr = new Projectile(new double[] {p.x-x,p.y-y}, dmg,"electric", 5, false, "entity/projectiles/zap.png", 300,this);
                    pr.move(200);
                    world.addObject(pr, x, y);
                }
                world.musichandler.playSound("atk", "boss_zap");
                cooldown = 200;
                break;
            case 2:

                break;
        }
    }

    public void action(int x, int y) {
        isAtt = false;
        switch (attack) {
            case 0:
                Wpn_hitbox w = new Wpn_hitbox(new double[]{0, 0}, dmg, "physical",0,false,"weapons/hitboxes/boss_point.png", this);
                w.lifespan = 15;
                world.addObject(w, x, y);
                cooldown = 200;
                ((DungeonWorld) world).musichandler.playSound("atk", "boss_fist");
                break;
        }
    }

    @Override
    public double[][] getMovement() {
        if (towards == null || isAt(towards.x, towards.y) || (cooldown % 10 == 0 && pos.equals(new Point(x,y)))){
            towards = new Point(world.random.nextInt(world.width-(4* world.pixelSize)+(2*world.pixelSize)),world.random.nextInt(world.height-(4* world.pixelSize)+(2*world.pixelSize)));
        }

        return new double[][] {{towards.x-x,towards.y-y},{speedmp}};
    }
}
