package com.vasmatheus.easymixology;

import com.vasmatheus.mokhaiotlhelper.MokhaiotlHelperPlugin;
import com.vasmatheus.seupulchrestrangetile.SepulchreStrangeTileMarkerPlugin;
import com.vasmatheus.trawling.DeepSeaTrawlingPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class EasyMixologyPluginTest {
  public static void main(String[] args) throws Exception {
    ExternalPluginManager.loadBuiltin(
        EasyMixologyPlugin.class,
        SepulchreStrangeTileMarkerPlugin.class,
        MokhaiotlHelperPlugin.class,
        DeepSeaTrawlingPlugin.class);
    RuneLite.main(args);
  }
}
