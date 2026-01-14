package io.github.jkaano.toomanykeybinds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

	public static final String MOD_ID = "toomanykeybinds";
	public static final String MOD_NAME = "Too Many Keybinds";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	// file paths
	public static final String DIRECTORY = "./config/toomanykeybinds";
	public static final String KEYMAPCONFIGDIRECTORY = DIRECTORY + "/keymap_config.json";
	public static final String ALLKEYBINDS = DIRECTORY + "/key_maps_list.json";

}