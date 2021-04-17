package com.majesty373.springgarden;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SpringGardenPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(SpringGardenPlugin.class);
		RuneLite.main(args);
	}
}