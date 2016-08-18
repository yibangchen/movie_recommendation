import java.util.Set;
import java.util.Map;
import java.util.HashMap;

/**
 * This class is a collection of all items from the data
 * It includes methods that ONLY involves items
 * @author Yibang Chen
 *
 */
public class ItemPool {
	
	private static ItemPool instance = null;
	private Map<Integer, Item> items;
	
	private double overallAverage = 0;
	private int ratingCount = 0;
	
	private ItemPool() {
		items = new HashMap<Integer, Item>();
	}
	
	/**
	 * This method ensures singleton design
	 * @return the ItemPool instance
	 */
	public static ItemPool getInstance() {
		if (instance == null) instance = new ItemPool();
		return instance;
	}
	
	public void addItem(Item newItem) {
		items.put(newItem.getItemId(), newItem);
	}
	
	public Item getItem(int itemId) {
		return items.get(itemId);
	}
	
	public String getItemTitle(int itemId) {
		return items.get(itemId).getTitle();
	}
	
	public Set<Integer> getItemIds(){
		return items.keySet();
	}
	
	/**
	 * This method updates the overall average of all imported ratings.
	 * 	- It also updates the average score received for an items
	 * @param rating : the rating score
	 * @param itemId : the item id of the rating
	 * @param isNew : true if the item is a new item; false if update
	 */
	public void updateAverage(double rating, int itemId, boolean isNew) {
		if (isNew) ratingCount++;
		overallAverage += (rating - overallAverage)/ ratingCount;
		
		items.get(itemId).updateAverage(rating, isNew);
	}
	
	public double getAverage() {
		return this.overallAverage;
	}
	
	/**
	 * This method gets the baseline predictor for an item
	 * @param itemId
	 * @return
	 */
	public double getBaseline(int itemId) {
		return items.get(itemId).getBaseline();
	}
	
	/**
	 * This method checks if a certain item (by ID) is imported
	 * @param itemId
	 * @return
	 */
	public boolean isValidItem(int itemId) {
		return items.containsKey(itemId);
	}
}
