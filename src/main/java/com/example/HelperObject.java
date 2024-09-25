package com.example;

import net.runelite.api.ObjectComposition;

public class HelperObject {
    public int objectId;
    public ObjectComposition composition;

    public HelperObject(int objectId, ObjectComposition composition) {
        this.objectId = objectId;
        this.composition = composition;
    }
}
