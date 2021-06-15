package net.cakelancelot.heroes.extension.reqhandlers;

import com.smartfoxserver.v2.api.CreateRoomSettings;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.User;
import com.smartfoxserver.v2.entities.Zone;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.variables.SFSUserVariable;
import com.smartfoxserver.v2.entities.variables.UserVariable;
import com.smartfoxserver.v2.exceptions.SFSCreateRoomException;
import com.smartfoxserver.v2.exceptions.SFSJoinRoomException;
import com.smartfoxserver.v2.extensions.BaseClientRequestHandler;

import java.util.*;

public class FindRoom extends BaseClientRequestHandler {
    @Override
    public void handleClientRequest(User sender, ISFSObject params) {
        Zone zone = getParentExtension().getParentZone();
        Random random = new Random();
        Room roomToJoin;

        // update selected hero
        List<UserVariable> vars = new ArrayList<>();
        vars.add(new SFSUserVariable("actor", params.getUtfString("actor")));
        getApi().setUserVariables(sender, vars);

        // determine which button they pressed
        if (params.containsKey("roomName")) {
            trace(sender.getName() + " has requested to create a dev match.");
            roomToJoin = createNewGame(params.getUtfString("roomName"));
        }
        else if (params.getInt("numberOfPlayers") == 4) {
            trace(sender.getName() + " has queued for a party match.");
            List<Room> roomList = zone.getRoomList();
            // remove rooms that don't have the searchable property, as well as those that are full or not game rooms
            roomList.removeIf(i -> i.containsProperty("searchable") && i.getProperty("searchable").equals(false));
            roomList.removeIf(i -> i.isFull() || !i.isGame());
            if (roomList.size() > 0) {
                roomToJoin = roomList.get(0); // put the player into the first available room
            } else {
                int index = random.nextInt(partyMissions.length);
                String selected = partyMissions[index];
                roomToJoin = createNewGame(selected);
            }
        }
        else if (params.getInt("numberOfPlayers") == 1) {
            trace(sender.getName() + " has queued for a solo match.");
            int index = random.nextInt(soloMissions.length);
            String selected = soloMissions[index];
            roomToJoin = createNewGame(selected);
        }
        else {
            trace("Bad find room request");
            return;
        }

        try {
            getApi().joinRoom(sender, roomToJoin);
        } catch (SFSJoinRoomException e) {
            e.printStackTrace();
        }
    }

    private Room createNewGame(String missionID) {
        CreateRoomSettings settings = new CreateRoomSettings();
        Zone zone = getParentExtension().getParentZone();

        // this is what the client expects for room name according to the AspenTracker class
        // the mission name, a colon, and a unique game ID - ex. m_ruins_1p:849778179420
        settings.setName(String.format("%s:%d", missionID, new Date().getTime()));
        settings.setGame(true);
        settings.setDynamic(true);

        String classPath = "net.cakelancelot.heroes.extension.HeroesRoomExtension";
        settings.setExtension(new CreateRoomSettings.RoomExtensionSettings("Heroes", classPath));

        try {
            return zone.createRoom(settings, null);
        } catch(SFSCreateRoomException e) {
            e.printStackTrace();
            return null;
        }
    }

    // not sure if there is a way to determine which missions are meant for matchmaking
    // and which are meant to be accessible via dev menu only, so I'm hardcoding this for now
    private final String[] soloMissions = {
            "m_ruins_1p",
            "m_junkyard_1p",
            "m_graveyard_1p",
            "m_dexlabs_1p",
            "m_street_1p",
            "m_ruins_2_1p",
            "m_junkyard_2_1p",
            "m_park_1p",
            "m_graveyard_2_1p"
    };

    // same here
    private final String[] partyMissions = {
            "m_ruins_4p",
            "m_junkyard_4p",
            "m_graveyard_4p",
            "m_dexlabs_4p",
            "m_street_4p",
            "m_ruins_2_4p",
            "m_junkyard_2_4p",
            "m_park_4p",
            "m_graveyard_2_4p"
    };
}
