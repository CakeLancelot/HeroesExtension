package net.cakelancelot.heroes.extension.evthandlers;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import net.cakelancelot.heroes.extension.HeroesRoomExtension;

import java.util.concurrent.TimeUnit;

public class JoinRoomEventHandler extends BaseServerEventHandler {
    @Override
    public void handleServerEvent(ISFSEvent event) {
        Room room = (Room) event.getParameter(SFSEventParam.ROOM);
        if (!room.isFull()) {
            return;
        }

        HeroesRoomExtension parentExt = (HeroesRoomExtension) getParentExtension();
        room.setProperty("searchable", false);
        int startDelay = 3;

        ISFSObject matchStartData = new SFSObject();
        matchStartData.putInt("delay", startDelay);
        parentExt.send("cmd_match_starting", parentExt.addTimeStamp(matchStartData), parentExt.getParentRoom().getUserList());

        try {
            TimeUnit.SECONDS.sleep(startDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ISFSObject roomReadyData = new SFSObject();
        parentExt.send("cmd_room_ready", parentExt.addTimeStamp(roomReadyData), parentExt.getParentRoom().getUserList());
    }
}