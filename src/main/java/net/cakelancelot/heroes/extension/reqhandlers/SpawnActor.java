package net.cakelancelot.heroes.extension.reqhandlers;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import net.cakelancelot.heroes.extension.HeroesZoneExtension;
import net.cakelancelot.heroes.extension.models.Game;
import net.cakelancelot.heroes.extension.models.Vector3;

import java.util.ArrayList;
import java.util.List;

public class SpawnActor extends BaseClientRequestHandler {
    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        trace("req_spawn_actor: " + params.getDump());

        HeroesZoneExtension parentExt = (HeroesZoneExtension) getParentExtension();
        Room currentRoom = sender.getLastJoinedRoom();
        Game runningGame = parentExt.getGameByRoom(currentRoom);

        ISFSObject createActorData = new SFSObject();
        createActorData.putUtfString("id", String.valueOf(sender.getId()));
        createActorData.putUtfString("actor", sender.getVariable("actor").getStringValue());

        Vector3 selectedSpawn = runningGame.getNextSpawnPoint();
        ISFSObject spawnPoint = new SFSObject();
        spawnPoint.putFloat("x", (float) selectedSpawn.x);
        spawnPoint.putFloat("y", (float) selectedSpawn.y);
        spawnPoint.putFloat("z", (float) selectedSpawn.z);
        spawnPoint.putFloat("rotation", 0);
        createActorData.putSFSObject("spawn_point", spawnPoint);

        parentExt.send("cmd_create_actor", parentExt.addTimeStamp(createActorData), currentRoom.getUserList());

        ISFSObject setSpeedData = new SFSObject();
        setSpeedData.putFloat("speed", 5); // TODO: read speed from actor definition
        setSpeedData.putUtfString("id", String.valueOf(sender.getId()));

        parentExt.send("cmd_set_speed", parentExt.addTimeStamp(setSpeedData), sender);

        List<UserVariable> vars = new ArrayList<>();
        vars.add(new SFSUserVariable("health", 500)); // TODO: read health from  actor definition
        vars.add(new SFSUserVariable("p_health", 1.0)); // hp percentage (used for bar gui)
        vars.add(new SFSUserVariable("dextronium", 0)); // each segment is 200, full is 1000
        getApi().setUserVariables(sender, vars);
    }
}
