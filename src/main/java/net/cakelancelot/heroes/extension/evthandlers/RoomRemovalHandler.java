package net.cakelancelot.heroes.extension.evthandlers;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import net.cakelancelot.heroes.extension.HeroesZoneExtension;

public class RoomRemovalHandler extends BaseServerEventHandler {

    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        Zone zone = (Zone) event.getParameter(SFSEventParam.ZONE);
        Room room = (Room) event.getParameter(SFSEventParam.ROOM);
        HeroesZoneExtension zoneExt = (HeroesZoneExtension) zone.getExtension();
        zoneExt.deregisterGame(room);
    }
}
