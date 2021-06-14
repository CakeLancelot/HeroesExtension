package net.cakelancelot.heroes.extension.reqhandlers;

import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import java.util.ArrayList;
import java.util.List;

public class HitActor extends BaseClientRequestHandler {
    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        //trace("req_hit_actor: " + params.getDump());

        // hit needs to be from a base attack to gain dextronium
        if (params.getUtfString("attackString").contains("attack")) {
            int currentDextronium = sender.getVariable("dextronium").getIntValue();
            int newDextronium;
            int increment = 50; // TODO: should be calculated using hero's dex gain stat and dmg result

            if ((currentDextronium + increment) > 1000) {
                newDextronium = 1000;
            } else {
                newDextronium = currentDextronium + increment;
            }

            List<UserVariable> vars = new ArrayList<>();
            vars.add(new SFSUserVariable("dextronium", newDextronium));
            getApi().setUserVariables(sender, vars);
        }
    }
}
