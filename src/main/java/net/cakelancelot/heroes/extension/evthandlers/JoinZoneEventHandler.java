package net.cakelancelot.heroes.extension.evthandlers;

import com.smartfoxserver.v2.core.ISFSEvent;
import com.smartfoxserver.v2.core.SFSEventParam;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.data.*;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.extensions.BaseServerEventHandler;
import net.cakelancelot.heroes.extension.HeroesZoneExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JoinZoneEventHandler extends BaseServerEventHandler {
    @Override
    public void handleServerEvent(ISFSEvent event) {
        User user = (User) event.getParameter(SFSEventParam.USER);
        HeroesZoneExtension parentExt = (HeroesZoneExtension) getParentExtension();

        String[] actorArray = {"rigby", "finn", "dexter", "feedback","fionna","mojojojo","mordecai","johnny","fourarms","marceline","gumball"};

        ISFSArray actorTypes = new SFSArray();

        for (int i = 0; i < actorArray.length; i++) {
            ISFSObject actor = new SFSObject();
            actor.putUtfString("actor", actorArray[i]);
            actor.putUtfString("type", actorArray[i].toUpperCase(Locale.ROOT));
            actorTypes.addSFSObject(actor);
        }

        List<UserVariable> vars = new ArrayList<>();

        String allActors = "{\"finn\":1,\"dexter\":1,\"mordecai\":1,\"fourarms\":1,\"gumball\":1,\"rigby\":1,\"mojojojo\":1,\"fionna\":1,\"marceline\":1,\"feedback\":1,\"johnny\":1,\"gumball\":1}";

        vars.add(new SFSUserVariable("actors", allActors)); // json string, current heroes?
        vars.add(new SFSUserVariable("actorTypes", actorTypes)); // array of sfs objects, hero variants
        vars.add(new SFSUserVariable("defaultActors", allActors)); // json string
        vars.add(new SFSUserVariable("actor", "dexter")); // selected hero

        int startingCoins = 100000;

        vars.add(new SFSUserVariable("coins", startingCoins));
        vars.add(new SFSUserVariable("level", 1));
        vars.add(new SFSUserVariable("percentLevel", 0.0 )); // to next level
        vars.add(new SFSUserVariable("displayName", user.getName()));

        getApi().setUserVariables(user, vars);

        ISFSObject data = new SFSObject();
        data.putUtfString("actorMap", allActors.toUpperCase(Locale.ROOT)); // total amount of each hero type, json string

        trace("Sending cmd_player_loaded to " + user.getName());
        parentExt.send("cmd_player_loaded", parentExt.addTimeStamp(data), user);
    }
}
