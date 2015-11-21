package ga.codesplash.scrumplex.sprummlbot.stuff;

import java.util.HashMap;
import java.util.Map;

import ga.codesplash.scrumplex.sprummlbot.Vars;

public class ServerOptimization {

	/**
	 * Sets up all needed permissions for the bot.
	 */
	public static void permissions() {
		Map<String, Integer> perms = new HashMap<>();
		perms.put("i_client_move_power", 2000);
		perms.put("i_client_kick_from_server_power", 2000);
		perms.put("i_client_ban_power", 2000);
		perms.put("i_client_ban_max_bantime", -1);
		perms.put("b_client_ban_create", 1);
		perms.put("b_client_ban_list", 1);
		perms.put("i_channel_permission_modify_power", 2000);
		perms.put("i_group_member_add_power", 2000);
		perms.put("i_group_member_remove_power", 2000);
		perms.put("i_group_modify_power", 2000);
		for (String str : perms.keySet()) {
			Vars.API.addServerGroupPermission(2, str, perms.get(str), false, false);
		}
	}

}
