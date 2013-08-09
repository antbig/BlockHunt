package nl.Steffion.BlockHunt;

import java.util.ArrayList;
import nl.Steffion.BlockHunt.Arena.ArenaState;
import nl.Steffion.BlockHunt.Managers.ConfigC;
import nl.Steffion.BlockHunt.Managers.MessageM;
import nl.Steffion.BlockHunt.Serializables.LocationSerializable;

import org.bukkit.Material;
import org.bukkit.block.Sign;

public class SignsHandler {

	public static void createSign(String[] lines, LocationSerializable location) {
		if (lines[1] != null) {
			if (lines[1].equalsIgnoreCase("leave")) {
				boolean saved = false;
				int number = 1;
				while (!saved) {
					if (W.signs.getFile().get("leave_" + number) == null) {
						W.signs.getFile().set("leave_" + number + ".arenaName",
								"leave");
						W.signs.getFile().set("leave_" + number + ".location",
								location);
						W.signs.save();

						saved = true;
					} else {
						number = number + 1;
					}
				}
			} else {
				for (Arena arena : W.arenaList) {
					if (lines[1].equalsIgnoreCase(arena.arenaName)) {
						boolean saved = false;
						int number = 1;
						while (!saved) {
							if (W.signs.getFile().get(
									arena.arenaName + "_" + number) == null) {
								W.signs.getFile().set(
										arena.arenaName + "_" + number
												+ ".arenaName", lines[1]);
								W.signs.getFile().set(
										arena.arenaName + "_" + number
												+ ".location", location);
								W.signs.save();

								saved = true;
							} else {
								number = number + 1;
							}
						}
					}
				}
			}
		}
	}

	public static void removeSign(LocationSerializable location) {
		for (String sign : W.signs.getFile().getKeys(false)) {
			LocationSerializable loc = new LocationSerializable(
					(LocationSerializable) W.signs.getFile().get(
							sign + ".location"));
			if (loc.equals(location)) {
				W.signs.getFile().set(sign, null);
				W.signs.save();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void updateSigns() {
		W.signs.load();
		for (String sign : W.signs.getFile().getKeys(false)) {
			LocationSerializable loc = new LocationSerializable(
					(LocationSerializable) W.signs.getFile().get(
							sign + ".location"));
			if (loc.getBlock().getType().equals(Material.SIGN_POST)
					|| loc.getBlock().getType().equals(Material.WALL_SIGN)) {
				Sign signblock = (Sign) loc.getBlock().getState();
				String[] lines = signblock.getLines();
				if (sign.contains("leave")) {
					ArrayList<String> signLines = (ArrayList<String>) W.config
							.getFile()
							.getList(ConfigC.sign_LEAVE.getLocation());
					int linecount = 0;
					for (String line : signLines) {
						if (linecount <= 3) {
							signblock.setLine(linecount,
									MessageM.replaceAll(line));
						}

						linecount = linecount + 1;
					}
					signblock.update();
				} else {

					for (Arena arena : W.arenaList) {
						if (lines[1].contains(arena.arenaName)) {
							if (arena.gameState.equals(ArenaState.WAITING)) {
								ArrayList<String> signLines = (ArrayList<String>) W.config
										.getFile().getList(
												ConfigC.sign_WAITING
														.getLocation());
								int linecount = 0;
								for (String line : signLines) {
									if (linecount <= 3) {
										signblock
												.setLine(
														linecount,
														MessageM.replaceAll(
																line,
																"arenaname-"
																		+ arena.arenaName,
																"players-"
																		+ arena.playersInArena
																				.size(),
																"maxplayers-"
																		+ arena.maxPlayers,
																"timeleft-"
																		+ arena.timer));
									}

									linecount = linecount + 1;
								}
								signblock.update();
							} else if (arena.gameState
									.equals(ArenaState.STARTING)) {
								ArrayList<String> signLines = (ArrayList<String>) W.config
										.getFile().getList(
												ConfigC.sign_STARTING
														.getLocation());
								int linecount = 0;
								for (String line : signLines) {
									if (linecount <= 3) {
										signblock
												.setLine(
														linecount,
														MessageM.replaceAll(
																line,
																"arenaname-"
																		+ arena.arenaName,
																"players-"
																		+ arena.playersInArena
																				.size(),
																"maxplayers-"
																		+ arena.maxPlayers,
																"timeleft-"
																		+ arena.timer));
									}

									linecount = linecount + 1;
								}
								signblock.update();
							} else if (arena.gameState
									.equals(ArenaState.INGAME)) {
								ArrayList<String> signLines = (ArrayList<String>) W.config
										.getFile().getList(
												ConfigC.sign_INGAME
														.getLocation());
								int linecount = 0;
								for (String line : signLines) {
									if (linecount <= 3) {
										signblock
												.setLine(
														linecount,
														MessageM.replaceAll(
																line,
																"arenaname-"
																		+ arena.arenaName,
																"players-"
																		+ arena.playersInArena
																				.size(),
																"maxplayers-"
																		+ arena.maxPlayers,
																"timeleft-"
																		+ arena.timer));
									}

									linecount = linecount + 1;
								}
								signblock.update();
							}
						}
					}
				}
			} else {
				removeSign(loc);
			}
		}
	}
}
