package de.mertero.falldown.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mertero.falldown.main.Main;
import de.piet.cloud.api.CloudAPI;
import net.twerion.rank.Rank;


public class CMD_setlobby implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player)sender;
			if(cmd.getName().equalsIgnoreCase("setlobby")) {
				if(CloudAPI.getPlayerAPI().getRank(p).equals(Rank.ADMIN)){
					try{
						if(args.length == 0) {
							Main.getInstance().getLm().setlocation("lobby", p.getLocation());
							p.sendMessage(Main.getInstance().getPrefix() + "Du hast die Lobby gesetzt!");
						}
					
	               
					}catch(Exception ex){
						
					}
				}
			}
		}
	
	
	
	return false;
}

}
