package org.jakub1221.herobrineai.commands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jakub1221.herobrineai.HerobrineAI;
import org.jakub1221.herobrineai.AI.AICore;
import org.jakub1221.herobrineai.AI.Core;

public class CmdExecutor implements CommandExecutor {

	private HerobrineAI P_Core;
	private Logger log;

	public CmdExecutor(final HerobrineAI i) {
		super();
		P_Core = null;
		log = null;
		P_Core = i;
		log = HerobrineAI.log;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			if (args.length > 0) {
				if (args[0].equalsIgnoreCase("attack")) {
					if (args.length > 1) {
						if (player.hasPermission("hb-ai.attack")) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
									if (P_Core.getSupport().checkAttack(Bukkit.getServer().getPlayer(args[1]).getLocation())) {
										if (P_Core.canAttackPlayer(Bukkit.getServer().getPlayer(args[1]), player)) {
											if (!AICore.isTarget) {
												P_Core.getAICore().setAttackTarget(Bukkit.getServer().getPlayer(args[1]));
												player.sendMessage(ChatColor.RED + "[HerobrineAI] Herobrine is now attacking the " + args[1] + "!");
											} else {
												player.sendMessage(ChatColor.RED + "[HerobrineAI] Herobrine already has target! Use " + ChatColor.GREEN + "/hb-ai cancel" + ChatColor.RED
														+ " to cancel actual target");
											}
										}
									} else {
										player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is in secure area.");
									}
								} else {
									player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
								}
							} else {
								player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GREEN + "/hb-ai attack <player name>");
					}
				} else if (args[0].equalsIgnoreCase("pyramid")) {
					if (args.length > 1) {
						if (player.hasPermission("hb-ai.pyramid")) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
									if (P_Core.getSupport().checkBuild(Bukkit.getServer().getPlayer(args[1]).getLocation())) {
										final Object[] data = { Bukkit.getServer().getPlayer(args[1]) };
										if (P_Core.getAICore().getCore(Core.CoreType.PYRAMID).runCore(data).getResult()) {
											player.sendMessage(ChatColor.RED + "[HerobrineAI] Creating pyramind near " + args[1] + "!");
										} else {
											player.sendMessage(ChatColor.RED + "[HerobrineAI] Cannot find good place for pyramid!");
										}
									} else {
										player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is in secure area.");
									}
								} else {
									player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
								}
							} else {
								player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GREEN + "/hb-ai pyramid <player name>");
					}
				} else if (args[0].equalsIgnoreCase("bury")) {
					if (args.length > 1) {
						if (player.hasPermission("hb-ai.bury")) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
									if (P_Core.getSupport().checkBuild(Bukkit.getServer().getPlayer(args[1]).getLocation())) {
										final Object[] data = { Bukkit.getServer().getPlayer(args[1]) };
										if (P_Core.getAICore().getCore(Core.CoreType.BURY_PLAYER).runCore(data).getResult()) {
											player.sendMessage(ChatColor.RED + "[HerobrineAI] Buried " + args[1] + "!");
										} else {
											player.sendMessage(ChatColor.RED + "[HerobrineAI] Cannot find good place!");
										}
									} else {
										player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is in secure area.");
									}
								} else {
									player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
								}
							} else {
								player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GREEN + "/hb-ai bury <player name>");
					}
				} else if (args[0].equalsIgnoreCase("cave")) {
					if (args.length > 1) {
						if (player.hasPermission("hb-ai.cave")) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								final Object[] data = { Bukkit.getServer().getPlayer(args[1]).getLocation(), true };
								player.sendMessage(ChatColor.RED + "[HerobrineAI] " + P_Core.getAICore().getCore(Core.CoreType.BUILD_STUFF).runCore(data).getResultString());
							} else {
								player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GREEN + "/hb-ai cave <player name>");
					}
				} else if (args[0].equalsIgnoreCase("burn")) {
					if (args.length > 1) {
						if (player.hasPermission("hb-ai.burn")) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								final Object[] data = { Bukkit.getServer().getPlayer(args[1]) };
								player.sendMessage(ChatColor.RED + "[HerobrineAI] " + P_Core.getAICore().getCore(Core.CoreType.BURN).runCore(data).getResultString());
							} else {
								player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GREEN + "/hb-ai burn <player name>");
					}
				} else if (args[0].equalsIgnoreCase("curse")) {
					if (args.length > 1) {
						if (player.hasPermission("hb-ai.curse")) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								final Object[] data = { Bukkit.getServer().getPlayer(args[1]) };
								player.sendMessage(ChatColor.RED + "[HerobrineAI] " + P_Core.getAICore().getCore(Core.CoreType.CURSE).runCore(data).getResultString());
							} else {
								player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GREEN + "/hb-ai curse <player name>");
					}
				} else if (args[0].equalsIgnoreCase("heads")) {
					if (args.length > 1) {
						if (player.hasPermission("hb-ai.heads")) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								final Object[] data = { args[1] };
								player.sendMessage(ChatColor.RED + "[HerobrineAI] " + P_Core.getAICore().getCore(Core.CoreType.HEADS).runCore(data).getResultString());
							} else {
								player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GREEN + "/hb-ai heads <player name>");
					}
				} else if (args[0].equalsIgnoreCase("graveyard")) {
					if (args.length > 1) {
						if (player.hasPermission("hb-ai.graveyard")) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
									if (!AICore.isTarget) {
										P_Core.getAICore().graveyardTeleport(Bukkit.getServer().getPlayer(args[1]));
										player.sendMessage(ChatColor.RED + "[HerobrineAI] " + args[1] + " is now in the Graveyard world!");
									} else {
										player.sendMessage(ChatColor.RED + "[HerobrineAI] Herobrine already has target! Use " + ChatColor.GREEN + "/hb-ai cancel" + ChatColor.RED
												+ " to cancel actual target");
									}
								} else {
									player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
								}
							} else {
								player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GREEN + "/hb-ai graveyard <player name>");
					}
				} else if (args[0].equalsIgnoreCase("haunt")) {
					if (args.length > 1) {
						if (player.hasPermission("hb-ai.haunt")) {
							if (Bukkit.getServer().getPlayer(args[1]) != null) {
								if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
									if (P_Core.getSupport().checkHaunt(Bukkit.getServer().getPlayer(args[1]).getLocation())) {
										if (P_Core.canAttackPlayer(Bukkit.getServer().getPlayer(args[1]), player)) {
											if (!AICore.isTarget) {
												P_Core.getAICore().setHauntTarget(Bukkit.getServer().getPlayer(args[1]));
												player.sendMessage(ChatColor.RED + "[HerobrineAI] Herobrine now haunts the " + args[1] + "!");
											} else {
												player.sendMessage(ChatColor.RED + "[HerobrineAI] Herobrine already has target! Use " + ChatColor.GREEN + "/hb-ai cancel" + ChatColor.RED
														+ " to cancel actual target.");
											}
										}
									} else {
										player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is in secure area.");
									}
								} else {
									player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
								}
							} else {
								player.sendMessage(ChatColor.RED + "[HerobrineAI] Player is offline.");
							}
						} else {
							player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
						}
					} else {
						player.sendMessage(ChatColor.RED + "Usage: " + ChatColor.GREEN + "/hb-ai haunt <player name>");
					}
				} else if (args[0].equalsIgnoreCase("cancel")) {
					if (player.hasPermission("hb-ai.cancel")) {
						P_Core.getAICore().cancelTarget(Core.CoreType.ANY);
						player.sendMessage(ChatColor.RED + "[HerobrineAI] Target cancelled!");
					} else {
						player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
					}
				} else if (args[0].equalsIgnoreCase("reload")) {
					if (player.hasPermission("hb-ai.reload")) {
						P_Core.getConfigDB().reload();
						player.sendMessage(ChatColor.RED + "[HerobrineAI] Config reloaded!");
					} else {
						player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
					}
				} else if (args[0].equalsIgnoreCase("help")) {
					if (player.hasPermission("hb-ai.help")) {
						player.sendMessage(ChatColor.RED + "[HerobrineAI] Command list");
						player.sendMessage(ChatColor.GREEN + "/hb-ai help - shows all commands");
						player.sendMessage(ChatColor.GREEN + "/hb-ai attack <player name> - herobrine attacks the player");
						player.sendMessage(ChatColor.GREEN + "/hb-ai haunt <player name> - herobrine haunts the player");
						player.sendMessage(ChatColor.GREEN + "/hb-ai cancel - cancel herobrine?s actual target");
						player.sendMessage(ChatColor.GREEN + "/hb-ai reload - reload config");
						player.sendMessage(ChatColor.GREEN + "/hb-ai position - gets actual position of Herobrine");
						player.sendMessage(ChatColor.GREEN + "/hb-ai pyramid <player name> - build pyramid near the player");
						player.sendMessage(ChatColor.GREEN + "/hb-ai bury <player name> - bury player");
						player.sendMessage(ChatColor.GREEN + "/hb-ai graveyard <player name> - teleport player to the Graveyard world");
						player.sendMessage(ChatColor.GREEN + "/hb-ai temple <player name> - build temple near the player");
						player.sendMessage(ChatColor.GREEN + "/hb-ai heads <player name> - place heads near the player");
						player.sendMessage(ChatColor.GREEN + "/hb-ai cave <player name> - create cave near the player");
						player.sendMessage(ChatColor.GREEN + "/hb-ai burn <player name> - burn player");
						player.sendMessage(ChatColor.GREEN + "/hb-ai curse <player name> - curse player");
						player.sendMessage(ChatColor.GREEN + "/hb-ai allworlds - add all worlds to config");
					} else {
						player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
					}
				} else if (args[0].equalsIgnoreCase("allworlds")) {
					if (player.hasPermission("hb-ai.allworlds")) {
						HerobrineAI.getPluginCore().getConfigDB().addAllWorlds();
						player.sendMessage(ChatColor.GREEN + "[HerobrineAI] All worlds have been added to config.");
						player.sendMessage(ChatColor.YELLOW + "[HerobrineAI] Note: Worlds with blank spaces can cause problems!");
					} else {
						player.sendMessage(ChatColor.RED + "You don?t have permissions to do that.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Usage: /hb-ai help");
				}
			} else {
				player.sendMessage(ChatColor.RED + "Usage: /hb-ai help");
			}
			return true;
		}
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("attack")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
							if (P_Core.getSupport().checkAttack(Bukkit.getServer().getPlayer(args[1]).getLocation())) {
								if (P_Core.canAttackPlayerConsole(Bukkit.getServer().getPlayer(args[1]))) {
									if (!AICore.isTarget) {
										P_Core.getAICore().setAttackTarget(Bukkit.getServer().getPlayer(args[1]));
										log.info("[HerobrineAI] Herobrine is now attacking the " + args[1] + "!");
									} else {
										log.info("[HerobrineAI] Herobrine already has target! Use /hb-ai cancel to cancel actual target");
									}
								}
							} else {
								log.info("[HerobrineAI] Player is in secured area.");
							}
						} else {
							log.info("[HerobrineAI] Player is offline.");
						}
					} else {
						log.info("[HerobrineAI] Player is offline.");
					}
				} else {
					log.info("Usage: /hb-ai attack <player name>");
				}
			} else if (args[0].equalsIgnoreCase("pyramid")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
							if (P_Core.getSupport().checkBuild(Bukkit.getServer().getPlayer(args[1]).getLocation())) {
								final Object[] data2 = { Bukkit.getServer().getPlayer(args[1]) };
								if (P_Core.getAICore().getCore(Core.CoreType.PYRAMID).runCore(data2).getResult()) {
									log.info("[HerobrineAI] Creating pyramind near " + args[1] + "!");
								} else {
									log.info("[HerobrineAI] Cannot find good place for pyramid!");
								}
							} else {
								log.info("[HerobrineAI] Player is in secure area.");
							}
						} else {
							log.info("[HerobrineAI] Player is offline.");
						}
					} else {
						log.info("[HerobrineAI] Player is offline.");
					}
				} else {
					log.info("Usage: " + ChatColor.GREEN + "/hb-ai pyramid <player name>");
				}
			} else if (args[0].equalsIgnoreCase("bury")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
							if (P_Core.getSupport().checkBuild(Bukkit.getServer().getPlayer(args[1]).getLocation())) {
								final Object[] data2 = { Bukkit.getServer().getPlayer(args[1]) };
								if (P_Core.getAICore().getCore(Core.CoreType.BURY_PLAYER).runCore(data2).getResult()) {
									log.info("[HerobrineAI] Buried " + args[1] + "!");
								} else {
									log.info("[HerobrineAI] Cannot find good place!");
								}
							} else {
								log.info("[HerobrineAI] Player is in secure area.");
							}
						} else {
							log.info("[HerobrineAI] Player is offline.");
						}
					} else {
						log.info("[HerobrineAI] Player is offline.");
					}
				} else {
					log.info("Usage: /hb-ai bury <player name>");
				}
			} else if (args[0].equalsIgnoreCase("cave")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						final Object[] data2 = { Bukkit.getServer().getPlayer(args[1]).getLocation(), true };
						log.info(ChatColor.RED + "[HerobrineAI] " + P_Core.getAICore().getCore(Core.CoreType.BUILD_STUFF).runCore(data2).getResultString());
					} else {
						log.info(ChatColor.RED + "[HerobrineAI] Player is offline.");
					}
				} else {
					log.info("Usage: /hb-ai cave <player name>");
				}
			} else if (args[0].equalsIgnoreCase("burn")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						final Object[] data2 = { Bukkit.getServer().getPlayer(args[1]) };
						log.info("[HerobrineAI] " + P_Core.getAICore().getCore(Core.CoreType.BURN).runCore(data2).getResultString());
					} else {
						log.info("[HerobrineAI] Player is offline.");
					}
				} else {
					log.info("Usage: /hb-ai burn <player name>");
				}
			} else if (args[0].equalsIgnoreCase("curse")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						final Object[] data2 = { Bukkit.getServer().getPlayer(args[1]) };
						log.info("[HerobrineAI] " + P_Core.getAICore().getCore(Core.CoreType.CURSE).runCore(data2).getResultString());
					} else {
						log.info("[HerobrineAI] Player is offline.");
					}
				} else {
					log.info("Usage: /hb-ai curse <player name>");
				}
			} else if (args[0].equalsIgnoreCase("heads")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						final Object[] data2 = { args[1] };
						log.info("[HerobrineAI] " + P_Core.getAICore().getCore(Core.CoreType.HEADS).runCore(data2).getResultString());
					} else {
						log.info("[HerobrineAI] Player is offline.");
					}
				} else {
					log.info("Usage: " + ChatColor.GREEN + "/hb-ai heads <player name>");
				}
			} else if (args[0].equalsIgnoreCase("graveyard")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
							if (!AICore.isTarget) {
								P_Core.getAICore().graveyardTeleport(Bukkit.getServer().getPlayer(args[1]));
								log.info("[HerobrineAI] " + args[1] + " is now in the Graveyard world!");
							} else {
								log.info("[HerobrineAI] Herobrine already has target! Use " + ChatColor.GREEN + "/hb-ai cancel" + ChatColor.RED + " to cancel actual target");
							}
						} else {
							log.info("[HerobrineAI] Player is offline.");
						}
					} else {
						log.info("[HerobrineAI] Player is offline.");
					}
				} else {
					log.info("Usage: /hb-ai graveyard <player name>");
				}
			} else if (args[0].equalsIgnoreCase("haunt")) {
				if (args.length > 1) {
					if (Bukkit.getServer().getPlayer(args[1]) != null) {
						if (Bukkit.getServer().getPlayer(args[1]).isOnline()) {
							if (P_Core.getSupport().checkHaunt(Bukkit.getServer().getPlayer(args[1]).getLocation())) {
								if (P_Core.canAttackPlayerConsole(Bukkit.getServer().getPlayer(args[1]))) {
									if (!AICore.isTarget) {
										P_Core.getAICore().setHauntTarget(Bukkit.getServer().getPlayer(args[1]));
										log.info("[HerobrineAI] Herobrine now haunts the " + args[1] + "!");
									} else {
										log.info("[HerobrineAI] Herobrine already has target! Use /hb-ai cancel to cancel actual target.");
									}
								}
							} else {
								log.info("[HerobrineAI] Player is in secure area.");
							}
						} else {
							log.info("[HerobrineAI] Player is offline.");
						}
					} else {
						log.info("[HerobrineAI] Player is offline.");
					}
				} else {
					log.info("Usage: /hb-ai haunt <player name>");
				}
			} else if (args[0].equalsIgnoreCase("cancel")) {
				P_Core.getAICore().cancelTarget(Core.CoreType.ANY);
				log.info(ChatColor.RED + "[HerobrineAI] Target cancelled!");
			} else if (args[0].equalsIgnoreCase("reload")) {
				P_Core.getConfigDB().reload();
				log.info("[HerobrineAI] Config reloaded!");
			} else if (args[0].equalsIgnoreCase("help")) {
				log.info("[HerobrineAI] Command list");
				log.info("/hb-ai help - shows all commands");
				log.info("/hb-ai attack <player name> - herobrine attacks the player");
				log.info("/hb-ai haunt <player name> - herobrine haunts the player");
				log.info("/hb-ai cancel - cancel herobrine?s actual target");
				log.info("/hb-ai reload - reload config");
				log.info("/hb-ai position - gets actual position of Herobrine");
				log.info("/hb-ai pyramid <player name> - build pyramid near the player");
				log.info("/hb-ai bury <player name> - bury player");
				log.info("/hb-ai graveyard <player name> - teleport player to the Graveyard world");
				log.info("/hb-ai heads <player name> - place heads near the player");
				log.info("/hb-ai cave <player name> - create cave near the player");
				log.info("/hb-ai burn <player name> - burn player");
				log.info("/hb-ai curse <player name> - curse player");
				log.info("/hb-ai allworlds - add all worlds to config");
			} if (args[0].equalsIgnoreCase("allworlds")) {
				HerobrineAI.getPluginCore().getConfigDB().addAllWorlds();
				log.info("[HerobrineAI] All worlds have been added to config.");
				log.info("[HerobrineAI] Note: Worlds with blank spaces can cause problems!");
			} else {
				log.info("Usage: /hb-ai help");
			}
		} else {
			log.info("Usage: /hb-ai help");
		}
		return true;
	}

}