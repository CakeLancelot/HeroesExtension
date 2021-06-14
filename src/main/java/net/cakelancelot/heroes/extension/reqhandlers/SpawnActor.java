package net.cakelancelot.heroes.extension.reqhandlers;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import net.cakelancelot.heroes.extension.HeroesZoneExtension;

import java.util.ArrayList;
import java.util.List;

public class SpawnActor extends BaseClientRequestHandler {
    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        trace("req_spawn_actor: " + params.getDump());

        HeroesZoneExtension parentExt = (HeroesZoneExtension) getParentExtension();
        Room currentRoom = sender.getLastJoinedRoom();

        ISFSObject createActorData = new SFSObject();
        createActorData.putUtfString("id", String.valueOf(sender.getId()));
        createActorData.putUtfString("actor", sender.getVariable("actor").getStringValue());

        ISFSObject spawnPoint = new SFSObject(); // TODO: get a spawn point from current mission data
        spawnPoint.putFloat("x", -2);
        spawnPoint.putFloat("y", 0);
        spawnPoint.putFloat("z", -12);
        spawnPoint.putFloat("rotation", 0);
        createActorData.putSFSObject("spawn_point", spawnPoint);

        parentExt.send("cmd_create_actor", parentExt.addTimeStamp(createActorData), currentRoom.getUserList());

        ISFSObject setSpeedData = new SFSObject();
        setSpeedData.putFloat("speed", 5); // TODO: read speed from actor definition
        setSpeedData.putUtfString("id", String.valueOf(sender.getId()));

        parentExt.send("cmd_set_speed", parentExt.addTimeStamp(setSpeedData), sender);

        List<UserVariable> vars = new ArrayList<>();
        vars.add(new SFSUserVariable("health", 500));
        vars.add(new SFSUserVariable("p_health", 1.0)); // hp percentage (used for bar ui)
        vars.add(new SFSUserVariable("dextronium", 0)); // each segment is 200, full is 1000
        getApi().setUserVariables(sender, vars);
    }
}
