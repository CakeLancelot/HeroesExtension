package net.cakelancelot.heroes.extension.reqhandlers;

import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;
import net.cakelancelot.heroes.extension.HeroesZoneExtension;

import java.util.ArrayList;
import java.util.List;

@MultiHandler
public class Attack extends BaseClientRequestHandler {
    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        /*
         This packet handler is only used for synchronizing animations
         with other clients and spending the correct amount of dextronium.
         If you're looking for hit registration, check HitActor
        */

        //trace(command + params.getDump());
        HeroesZoneExtension parentExt = (HeroesZoneExtension) getParentExtension();
        String command = params.getUtfString(SFSExtension.MULTIHANDLER_REQUEST_ID);
        int currentDextronium = sender.getVariable("dextronium").getIntValue();
        boolean specialAttack = false;

        ISFSObject respData = new SFSObject();
        List<UserVariable> newVars = new ArrayList<>();

        switch(command) {
            case "req_base_attack_actor":
                respData.putUtfString("id", params.getUtfString("id"));
                respData.putUtfString("target_id", params.getUtfString("target"));
                respData.putUtfString("attack_type", params.getUtfString("attack_type"));
                respData.putFloat("dest_x", 0);
                respData.putFloat("dest_y", 0);
                respData.putFloat("dest_z", 0);
                break;
            case "req_power_attack_actor":
                respData.putUtfString("id", params.getUtfString("id"));
                respData.putUtfString("target_id", String.valueOf(0));
                respData.putUtfString("attack_type", "power");
                respData.putFloat("dest_x", params.getFloat("dest_x"));
                respData.putFloat("dest_y", params.getFloat("dest_y"));
                respData.putFloat("dest_z", params.getFloat("dest_z"));
                newVars.add(new SFSUserVariable("dextronium", currentDextronium - 200));
                specialAttack = true;
                break;
            case "req_super_attack_actor":
                respData.putUtfString("id", params.getUtfString("id"));
                respData.putUtfString("target_id", String.valueOf(0));
                respData.putUtfString("attack_type", "super");
                respData.putFloat("dest_x", params.getFloat("dest_x"));
                respData.putFloat("dest_y", params.getFloat("dest_y"));
                respData.putFloat("dest_z", params.getFloat("dest_z"));
                newVars.add(new SFSUserVariable("dextronium", currentDextronium - 1000));
                specialAttack = true;
                break;
        }

        getApi().setUserVariables(sender, newVars);
        List<User> userList = sender.getLastJoinedRoom().getUserList();

        // some attacks can simultaneously move the player
        if (specialAttack) {
            ISFSObject moveData = new SFSObject();
            moveData.putUtfString("id", String.valueOf(sender.getId()));
            moveData.putFloat("dest_x", params.getFloat("dest_x"));
            moveData.putFloat("dest_y", params.getFloat("dest_y"));
            moveData.putFloat("dest_z", params.getFloat("dest_z"));
            moveData.putBool("orient", true);
            moveData.putFloat("speed", params.getFloat("speed"));

            parentExt.send("cmd_move_actor", parentExt.addTimeStamp(moveData), userList);
        }

        // the player who sent the packet doesn't care about this, send it to everyone else
        userList.remove(sender);
        getParentExtension().send("cmd_attack_actor", parentExt.addTimeStamp(respData), userList);
    }
}
