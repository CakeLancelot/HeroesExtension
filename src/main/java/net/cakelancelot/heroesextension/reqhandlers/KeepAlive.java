package net.cakelancelot.heroesextension.reqhandlers;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

public class KeepAlive extends BaseClientRequestHandler {
    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        // as far as I can tell from client disassembly, there is no response packet
        //trace("req_keep_alive: " + params.getDump());
    }
}
