package resources;
import java.util.ArrayList;


public class TeamList {
	
	private ArrayList<String> teams = new ArrayList<String>();
	
	public TeamList()
	{	
		teams.add("Atlanta Hawks");
		teams.add("Boston Celtics");
		teams.add("Brooklyn Nets");
		teams.add("Charlotte Bobcats");
		teams.add("Chicago Bulls");
		teams.add("Cleveland Cavaliers");
		teams.add("Dallas Mavericks");
		teams.add("Denver Nuggets");
		teams.add("Detroit Pistons");
		teams.add("Golden State Warriors");
		teams.add("Houston Rockets");
		teams.add("Indiana Pacers");
		teams.add("Los Angeles Clippers");
		teams.add("Los Angeles Lakers");
		teams.add("Memphis Grizzlies");
		teams.add("Miami Heat");
		teams.add("Milwaukee Bucks");
		teams.add("Minnesota Timberwolves");
		teams.add("New Orleans Hornets");
		teams.add("New York Knicks");
		teams.add("Oklahoma City Thunder");
		teams.add("Orlando Magic");
		teams.add("Philadelphia 76ers");
		teams.add("Phoenix Suns");
		teams.add("Portland Trailblazers");
		teams.add("Sacramento Kings");
		teams.add("San Antonio Spurs");
		teams.add("Toronto Raptors");
		teams.add("Utah Jazz");
		teams.add("Washington Wizards");
	}
	
	public boolean match(String str)
	{
		if (teams.contains(str))
			return true;
		else 
			return false;
	}
	
	public ArrayList<String> getTeams()
	{
		return teams;
	}
}
