package net.cakelancelot.heroes.extension.evthandlers;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import net.cakelancelot.heroes.extension.HeroesZoneExtension;

import java.util.concurrent.TimeUnit;

public class JoinRoomEventHandler extends BaseServerEventHandler {
    @Override
    public void handleServerEvent(ISFSEvent event) {
        Room room = (Room) event.getParameter(SFSEventParam.ROOM);
        if (!room.isFull() || !room.isGame()) {
            return;
        }

        if (!((boolean) room.getProperty("searchable"))) {
            return;
        }

        HeroesZoneExtension zoneExt = (HeroesZoneExtension) room.getZone().getExtension();
        zoneExt.getGameByRoom(room).startGame();
    }
}