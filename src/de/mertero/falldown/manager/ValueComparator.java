package de.mertero.falldown.manager;

import java.util.Comparator;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class ValueComparator implements Comparator<Player> {

	HashMap<Player, Integer> map = new HashMap<Player, Integer>();

	public ValueComparator(HashMap<Player, Integer> map) {
		this.map.putAll(map);
	}

	@Override
	public int compare(Player s1, Player s2) {
		if (map.get(s1) > map.get(s2)) {
			return -1;
		} else {
			return 1;
		}
	}
}