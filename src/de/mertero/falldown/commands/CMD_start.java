package de.mertero.falldown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mertero.falldown.countdown.Countdown;
import de.mertero.falldown.main.Main;
import de.piet.cloud.api.CloudAPI;
import net.twerion.rank.Rank;

public class CMD_start implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			Rank playerRankData = CloudAPI.getPlayerAPI().getRank(p);

			if (cmd.getName().equals("start")) {
				if(args.length == 3){
					Main.getInstance().getLm().registerSpawns(p, args[0], args[1], args[2]);
					return true;
				}
				if (playerRankData.isHigherEqualsLevel(Rank.LAPIS)) {
					if (Countdown.lobby <= 5) {
						p.sendMessage(Main.getInstance().getPrefix() + "Du kannst das Spiel nicht mehr starten!");
					} else {
						if (Main.getInstance().getAlive().size() >= 4) {
							Countdown.lobby = 5;
							p.sendMessage(Main.getInstance().getPrefix() + "Du hast das Spiel getartet!");
						} else {
							p.sendMessage(
									Main.getInstance().getPrefix() + "Es müssen mehr als 3 Spieler in der Runde sein!");
						}
					}
				} else {
					p.sendMessage(Main.getInstance().getCommand("start").getPermissionMessage());
				}
			}
		}

		return false;
	}

}
