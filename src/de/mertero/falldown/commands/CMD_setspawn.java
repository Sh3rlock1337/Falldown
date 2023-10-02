package de.mertero.falldown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mertero.falldown.main.Main;
import de.piet.cloud.api.CloudAPI;
import net.twerion.rank.Rank;


public class CMD_setspawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player p = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("setspawn")){
				if(CloudAPI.getPlayerAPI().getRank(p).equals(Rank.ADMIN)){
					if(args.length >= 2 || args.length == 0){
						p.sendMessage(Main.getInstance().getPrefix() + "§7/setspawn <Nummer>");
						return true;
					}
			
					try {
						int number =  Integer.parseInt(args[0]);
						Main.getInstance().getLm().setSpawn(number, p.getLocation());
						p.sendMessage(Main.getInstance().getPrefix() + "Du hast den Spawn Nummer §6["+number+"] §7gesetzt");
					} catch(NumberFormatException ex) {
						p.sendMessage(Main.getInstance().getPrefix() + "Du musst eine Zahl verwenden");
					}
				}
		
			}
		}
		return false;
	}

}
