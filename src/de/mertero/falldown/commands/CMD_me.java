package de.mertero.falldown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mertero.falldown.main.Main;

public class CMD_me implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			if(cmd.getName().equals("me")){
				if(args.length == 0){
					sender.sendMessage(Main.getInstance().getPrefix() + "Du?");
				} else {
					sender.sendMessage(Main.getInstance().getPrefix() + "Du?");
				}
			}
			
		}
		return false;
	}

}
