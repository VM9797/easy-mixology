package com.vasmatheus.easymixology;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(EasyMixologyPlugin.class);
		RuneLite.main(args);
	}
}