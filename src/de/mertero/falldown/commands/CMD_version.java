package de.mertero.falldown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mertero.falldown.main.Main;


public class CMD_version implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			if(cmd.getName().equals("falldown")){
				sender.sendMessage(Main.getInstance().getPrefix() + "Version: " + Main.getInstance().getVersion());
			}
			
		}
		return false;
	}

}
