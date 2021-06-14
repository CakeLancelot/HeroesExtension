package net.cakelancelot.heroes.shared;

import org.w3c.dom.Document;

public class Mission {
    public String name;
    public int capacity;
    public Vector3[] spawnPoints;

    public String soundtrack;
    public String bossSoundtrack;
    public String assetBundle;

    String[] scripts;
    Document scriptData;
}
