package resources;
import java.util.Hashtable;


public class Height {
	
	private Hashtable<Integer, String> displayHeight = new Hashtable<Integer, String>();
	
	public Height()
	{
		displayHeight.put(60, "5-0");
		displayHeight.put(61, "5-1");
		displayHeight.put(62, "5-2");
		displayHeight.put(63, "5-3");
		displayHeight.put(64, "5-4");
		displayHeight.put(65, "5-5");
		displayHeight.put(66, "5-6");
		displayHeight.put(67, "5-7");
		displayHeight.put(68, "5-8");
		displayHeight.put(69, "5-9");
		displayHeight.put(70, "5-10");
		displayHeight.put(71, "5-11");
		displayHeight.put(72, "6-0");
		displayHeight.put(73, "6-1");
		displayHeight.put(74, "6-2");
		displayHeight.put(75, "6-3");
		displayHeight.put(76, "6-4");
		displayHeight.put(77, "6-5");
		displayHeight.put(78, "6-6");
		displayHeight.put(79, "6-7");
		displayHeight.put(80, "6-8");
		displayHeight.put(81, "6-9");
		displayHeight.put(82, "6-10");
		displayHeight.put(83, "6-11");
		displayHeight.put(84, "7-0");
		displayHeight.put(85, "7-1");
		displayHeight.put(86, "7-2");
		displayHeight.put(87, "7-3");
		displayHeight.put(88, "7-4");
		displayHeight.put(89, "7-5");
		displayHeight.put(90, "7-6");
		displayHeight.put(91, "7-7");
		displayHeight.put(92, "7-8");
		displayHeight.put(93, "7-9");
		displayHeight.put(94, "7-10");
	}
	
	public String getDisplayHeight(int num)
	{
		return displayHeight.get(num);
	}
	
}
