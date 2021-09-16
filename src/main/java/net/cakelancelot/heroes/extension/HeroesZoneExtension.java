package net.cakelancelot.heroes.extension;

import com.smartfoxserver.v2.core.SFSEventType;
import com.smartfoxserver.v2.entities.Room;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.extensions.SFSExtension;
import net.cakelancelot.heroes.extension.evthandlers.JoinRoomEventHandler;
import net.cakelancelot.heroes.extension.evthandlers.LeaveZoneEventHandler;
import net.cakelancelot.heroes.extension.evthandlers.JoinZoneEventHandler;
import net.cakelancelot.heroes.extension.evthandlers.RoomRemovalHandler;
import net.cakelancelot.heroes.extension.models.Game;
import net.cakelancelot.heroes.extension.reqhandlers.*;
import net.cakelancelot.heroes.extension.models.Mission;
import net.cakelancelot.heroes.extension.models.Vector3;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

public class HeroesZoneExtension extends SFSExtension {

    HashMap<String, Element> actorDefinitions = new HashMap<>();
    HashMap<String, Mission> missions = new HashMap<>();
    HashMap<Room, Game> runningGames = new HashMap<>();

    @Override
    public void init() {
        trace("Registering event handlers...");
        this.addEventHandler(SFSEventType.USER_JOIN_ZONE, JoinZoneEventHandler.class);
        this.addEventHandler(SFSEventType.USER_JOIN_ROOM, JoinRoomEventHandler.class);
        this.addEventHandler(SFSEventType.USER_LOGOUT, LeaveZoneEventHandler.class);
        this.addEventHandler(SFSEventType.USER_DISCONNECT, LeaveZoneEventHandler.class);
        this.addEventHandler(SFSEventType.ROOM_REMOVED, RoomRemovalHandler.class);

        trace("Registering lobby request handlers...");
        this.addRequestHandler("req_keep_alive", KeepAlive.class);
        this.addRequestHandler("req_find_room", FindRoom.class);
        this.addRequestHandler("req_buy_item", BuyItem.class);
        this.addRequestHandler("req_spam", Stub.class);
        this.addRequestHandler("req_delayed_login", Stub.class);
        this.addRequestHandler("req_admin_command", Stub.class);

        trace("Registering game request handlers...");
        this.addRequestHandler("req_spawn_actor", SpawnActor.class);
        this.addRequestHandler("req_move_actor", MoveActor.class);
        this.addRequestHandler("req_pickup_item", PickupItem.class);
        this.addRequestHandler("req_hit_actor", HitActor.class);
        this.addRequestHandler("req_base_attack_actor", Attack.class);
        this.addRequestHandler("req_power_attack_actor", Attack.class);
        this.addRequestHandler("req_super_attack_actor", Attack.class);

        trace("Loading definition files...");
        try {
            loadDefinitions();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        trace("Attempting database connection...");
        try {
            databaseInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        trace("Zone extension ready.");
    }

    public void registerGame(Room room, Game game) {
        runningGames.put(room, game);
    }

    public void deregisterGame(Room room) {
        runningGames.remove(room);
    }

    public Game getGameByRoom(Room room) {
        return runningGames.get(room);
    }

    private void loadDefinitions() throws IOException {
        String extensionDir = getCurrentFolder();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        File path = new File(extensionDir + "/definitions");

        int missionCount = 0;
        int actorCount = 0;

        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (!files[i].isFile()) {
                continue;
            }

            String fileExtension = "";
            int j = files[i].getName().lastIndexOf('.');
            if (j > 0) {
                fileExtension = files[i].getName().substring(j+1);
            }

            if (!fileExtension.equalsIgnoreCase("xml")) {
                continue;
            }

            try {
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(files[i]);
                Element docRoot = doc.getDocumentElement();

                switch (docRoot.getNodeName()) {
                    case "actor":
                        addActorDefinition(doc);
                        actorCount++;
                        break;
                    case "act":
                        addMission(doc);
                        missionCount++;
                        break;
                    case "missions":
                    case "actors":
                        // unused
                        break;
                    default:
                        trace("weird xml: " + files[i].getName());
                }

            } catch (ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
        }

        trace(actorCount + " actors loaded.");
        trace(missionCount + " missions loaded.");
    }

    private void addActorDefinition(Document doc) {
        // TODO this
    }

    private void addMission(Document doc) {
        Mission newMission = new Mission();

        newMission.name = doc.getElementsByTagName("name").item(0).getTextContent();
        newMission.capacity = Integer.parseInt(doc.getElementsByTagName("capacity").item(0).getTextContent());

        NodeList points = doc.getElementsByTagName("spawn_point");
        newMission.spawnPoints = new Vector3[points.getLength()];

        for (int i = 0; i < points.getLength(); i++){
            NamedNodeMap attrs = points.item(i).getAttributes();
            newMission.spawnPoints[i] = new Vector3 (
                    Double.parseDouble(attrs.getNamedItem("x").getTextContent()),
                    Double.parseDouble(attrs.getNamedItem("y").getTextContent()),
                    Double.parseDouble(attrs.getNamedItem("z").getTextContent())
            );
        }

        if (doc.getElementsByTagName("asset").getLength() == 1) {
            newMission.assetBundle = doc.getElementsByTagName("asset").item(0).getTextContent();
        } else {
            // older xml variation
            newMission.assetBundle = doc.getElementsByTagName("set").item(0).getTextContent();
        }

        if (doc.getElementsByTagName("soundtrack").getLength() == 1) {
            newMission.soundtrack = doc.getElementsByTagName("soundtrack").item(0).getTextContent();
            newMission.bossSoundtrack = doc.getElementsByTagName("boss_soundtrack").item(0).getTextContent();
        }

        // TODO: read/set scripts and scriptData

        missions.put(doc.getElementsByTagName("id").item(0).getTextContent(), newMission);
    }

    public HashMap<String, Element> getActorDefinitions() {
        return actorDefinitions;
    }

    public Mission getMission(String mission) {
        return missions.get(mission);
    }

    public HashMap<String, Mission> getAllMissions() {
        return missions;
    }

    public ISFSObject addTimeStamp(ISFSObject data) {
        Date date = new Date();
        data.putLong("timestamp", date.getTime());
        return data;
    }
}

