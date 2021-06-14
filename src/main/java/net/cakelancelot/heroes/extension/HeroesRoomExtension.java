package net.cakelancelot.heroes.extension;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.RoomVariable;
import com.smartfoxserver.v2.entities.variables.SFSRoomVariable;
import com.smartfoxserver.v2.extensions.ISFSExtension;
import com.smartfoxserver.v2.extensions.SFSExtension;
import net.cakelancelot.heroes.shared.Mission;
import net.cakelancelot.heroes.extension.evthandlers.JoinRoomEventHandler;

import java.util.*;

public class HeroesRoomExtension extends SFSExtension {
    Room room = null;

    @Override
    public void init() {

        room = getParentRoom();
        ISFSExtension zoneExt = room.getZone().getExtension();

        Mission mission = (Mission) zoneExt.handleInternalMessage("getMission", room.getProperty("missionID"));

        // set up everything needed for the game
        room.setProperty("searchable", true);
        room.setMaxUsers(mission.capacity);

        List<RoomVariable> roomVars = new ArrayList<>();
        roomVars.add(new SFSRoomVariable("Act", mission.name));
        roomVars.add(new SFSRoomVariable("tip", getRandomTip()));
        roomVars.add(new SFSRoomVariable("set", mission.assetBundle));
        roomVars.add(new SFSRoomVariable("soundtrack", mission.soundtrack));
        roomVars.add(new SFSRoomVariable("bossSoundtrack", mission.bossSoundtrack));
        getApi().setRoomVariables(null, room, roomVars);

        this.addEventHandler(SFSEventType.USER_JOIN_ROOM, JoinRoomEventHandler.class);
        trace("Room extension ready.");
    }


    public ISFSObject addTimeStamp(ISFSObject data) {
        Date date = new Date();
        data.putLong("timestamp", date.getTime());
        return data;
    }

    String[] tips = {
            "Hold the SHIFT key to stay in one place while attacking.",
            "You can use either the arrow keys or WASD to move around.",
            "You get unlimited lives! If you run out of health, just wait a second, and you'll respawn.",
            "You get a huge coin bonus if you can beat a mission without respawning.",
            "Stay close to your team! You'll beat the bad guys a lot quicker with teamwork.",
            "There are tons of different Heroes to discover! Which one will be your favorite?",
            "You build up Dextronium every time your attack hits an enemy.",
            "Reggular Eggs contain Common and Uncommon Heroes.",
            "Eggscellent Eggs contain Common and Uncommon Heroes, with a small chance of Rare Heroes.",
            "Megga Eggs have a good chance of containing Rare Heroes.",
            "Watch out for exploding barrels! They can hurt enemies, but they can also hurt you.",
            "Single coins are worth 5, stacks are worth 25, and bars are worth 50.",
            "Coins are yours as soon as you pick them up.",
            "Pick up food to recover health.",
            "Power Attacks use 1 unit of Dextronium, while Super Attacks use 5 units.",
            "You're completely invulnerable while you're doing a Super Attack.",
            "Break objects like crates, chests and barrels to find hidden goodies.",
            "If an Egg gives you a Hero you already have, it makes that Hero even stronger!",
            "Dextronium fuels your Power Attacks and Super Attacks.",
            "You can do Power Attacks and Super Attacks by right clicking or by hitting the space bar.",
            "You don't need a specific target to use your Super Attack.",
            "Open blue Dex crates to collect extra Dextronium.",
            "Many Heroes have special abilities that can turn the tide of battle.",
            "Some Power Attacks move in the direction of your cursor.",
            "Enemies will taunt you before they attack. Learn to predict their movements!",
            "You earn more coins for teaming up in a 4-player PARTY than you do playing a SOLO game by yourself."
    };

    Random random = new Random();
    private String getRandomTip() {
        int index = random.nextInt(tips.length);
        return tips[index];
    }
}