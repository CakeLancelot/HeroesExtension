package net.cakelancelot.heroes.shared;

public class Actor {
    int HP;
    int maxHP;
    int type;
    float speed;
    String tag;
    // TODO other stats I can't remember rn

    Vector3 position;
    float rotation;

    Attack[] baseAttacks;
    Attack[] powerAttacks;
    Attack[] superAttacks;

    public Actor(String definition) {
        /*
         TODO: depending on which actor definition is passed, set all the fields in this constructor
         For Mobs: speed, health, damage, etc. can just be set as-is.
         For Heroes: some math is required to get the level adjusted stats.
         From what I can tell, the formula is just originalValue / (level * 10),
         where originalValue is what you get from the definition/xml.
        */
    }

    public void setHP(int value) {
        if (value > maxHP) {
            HP = maxHP;
        } else {
            HP = value;
        }
    }
}

class Attack {
    // TODO: find out what is actually necessary to keep from the xml
}

enum ActorType {
    BOSS,
    BREAKABLE,
    HERO,
    MELEE,
    MINION,
    PICKUP,
    RANGED
}