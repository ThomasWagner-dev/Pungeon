import java.awt.*;

public class Boss extends CollidingEnemy implements AttackIndication{

    public int level;
    public int attack;
    public int atick;

    public Boss(int level) {
        super("boss");
        setSprite("entity/enemies/boss_{}.png".replace("{}", level%5+""));
        //img = img.replaceColor(Color.BLUE, Color.getHSBColor((float) (((level*30)%360)/360.0), 1,1));
        Weapon w;
        speed = 2;
        int range, dmg, speed, cooldown;
        String dmgType, hitbox;
        boolean melee;
        hp = 5*level;
        maxhp = 5*level;
        type = "physical";





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
        super.die();
    }

    public void attack() {
        switch(attack) {
            case 0:

                break;
            case 1:

                break;

            case 2:

                break;
        }
    }

    public void action() {
        switch (attack) {

        }
    }

}
