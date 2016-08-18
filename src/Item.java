import java.util.Set;

/**
 * This interface encapsulates necessary methods for the ItemPool class
 * @author Yibang Chen
 *
 */
public interface Item {
		
	public int getItemId();

	public void setItemId(int itemId);

	public String getTitle();

	public void setTitle(String title);

	public Set<String> getFeatures();

	public void setCategories(Set<String> categories);
	
	public void updateAverage(double rating, boolean isNew);

	public double getBaseline();

	public Set<Integer> getRatedUsers();

	public void addUser(int userId);
}
