package net.cakelancelot.heroes.extension.evthandlers;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.exceptions.SFSException;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;


public class LeaveZoneEventHandler extends BaseServerEventHandler {
    @Override
    public void handleServerEvent(ISFSEvent event) throws SFSException {
        // TODO: save player data on leave
    }
}
