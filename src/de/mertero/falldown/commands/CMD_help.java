package de.mertero.falldown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mertero.falldown.main.Main;

public class CMD_help implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			if(cmd.getName().equals("help")){
				if(args.length == 0){
					sender.sendMessage(Main.getInstance().getPrefix() + "Das Buch in der Lobby lesen oder");
					sender.sendMessage(Main.getInstance().getPrefix() + "https://www.twerion.net/index.php?threads/falldown-release.83076/#post-358331");
				} else {
					sender.sendMessage(Main.getInstance().getPrefix() + "Das Buch in der Lobby lesen!");
				}
			}
			
		}
		return false;
	}

}
