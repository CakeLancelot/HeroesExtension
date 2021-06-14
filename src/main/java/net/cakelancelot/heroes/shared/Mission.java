package net.cakelancelot.heroes.shared;

import org.w3c.dom.Document;

public class Mission {
    public String name;
    public int capacity;
    public Vector3[] spawnPoints;

    public String soundtrack = "FFHeroes_musicLoop_junkyard";
    public String bossSoundtrack = "FF_Heroes_Boss_Battle";
    public String assetBundle;

    String[] scripts;
    Document scriptData;
}
