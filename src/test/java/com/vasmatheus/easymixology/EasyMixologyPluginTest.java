package com.vasmatheus.easymixology;

import com.vasmatheus.mokhaiotlhelper.MokhaiotlHelperPlugin;
import com.vasmatheus.seupulchrestrangetile.SepulchreStrangeTileMarkerPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class EasyMixologyPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EasyMixologyPlugin.class, SepulchreStrangeTileMarkerPlugin.class, MokhaiotlHelperPlugin.class);
		RuneLite.main(args);
	}
}