package net.cakelancelot.heroesextension.reqhandlers;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.annotations.MultiHandler;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;
import com.smartfoxserver.v2.extensions.SFSExtension;

@MultiHandler
public class Stub extends BaseClientRequestHandler {
    public void handleClientRequest(User sender, ISFSObject params) {
        /*
         req_delayed_login: used for registering after the client has launched
         req_admin_command: seemingly no way to trigger from within the client, unused?
         req_spam: likely for testing - not used for anything important
        */

        String command = params.getUtfString(SFSExtension.MULTIHANDLER_REQUEST_ID);
        trace(command + params.getDump());
    }
}
