package net.cakelancelot.heroesextension.reqhandlers;

import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import net.cakelancelot.heroesextension.HeroesZoneExtension;

public class MoveActor extends BaseClientRequestHandler {
    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        Room currentRoom = sender.getLastJoinedRoom();
        HeroesZoneExtension parentExt = (HeroesZoneExtension) getParentExtension();

        ISFSObject data = new SFSObject();
        data.putUtfString("id", String.valueOf(sender.getId()));
        data.putFloat("dest_x", params.getFloat("dest_x"));
        data.putFloat("dest_y", params.getFloat("dest_y"));
        data.putFloat("dest_z", params.getFloat("dest_z"));
        data.putBool("orient", params.getBool("orient"));
        data.putFloat("speed", params.getFloat("speed"));

        parentExt.send("cmd_move_actor", parentExt.addTimeStamp(data), currentRoom.getUserList());
    }
}
