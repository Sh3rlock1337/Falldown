package de.mertero.falldown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mertero.falldown.main.Main;

public class CMD_blocken implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			if(cmd.getName().equals("?")){
				if(args.length == 0){
					sender.sendMessage(Main.getInstance().getPrefix() + "Das Buch in der Lobby lesen!");
				} else {
					sender.sendMessage(Main.getInstance().getPrefix() + "Das Buch in der Lobby lesen!");
				}
			}
			
		}
		return false;
	}

}
