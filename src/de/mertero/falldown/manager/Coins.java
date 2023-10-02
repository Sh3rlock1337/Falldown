package de.mertero.falldown.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

public enum Coins {
	
	  KILL(3), WINNER(20), WINNER2(15), WINNER3(10);
	  
	  private int coins;
	  
	  public int getCoins(){
	    return this.coins;
	  }
	  
	  private Coins(int coins) {
	    String day = new SimpleDateFormat("dd.MM").format(new Date());
	    if((day.equalsIgnoreCase("04.12")) || (day.equalsIgnoreCase("09.12")) || (day.equalsIgnoreCase("30.11")) || (day.equalsIgnoreCase("21.06")
	    	|| (day.equalsIgnoreCase("17.06")) || (day.equalsIgnoreCase("10.01")) || (day.equalsIgnoreCase("31.07")))) {
	      this.coins = (coins * 2);
	    } else {
	      this.coins = coins;
	    }
	  }

}
