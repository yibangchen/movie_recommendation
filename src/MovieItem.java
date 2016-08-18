import java.util.Set;
import java.util.HashSet;

/**
 * This method is for the current Movies data set to store movies.dat
 * @author Yibang Chen
 *
 */
public class MovieItem implements Item{
	
	private int itemId;
	private String title;
	private int year;
	private Set<String> categories;
	
	private double averageRating = 0;
	private int ratingCount = 0;
	
	private Set<Integer> users;
	
	/**
	 * The constructor
	 * @param id : movieId in dataset
	 * @param title
	 * @param year
	 * @param categories
	 */
	public MovieItem (int id, String title, int year, Set<String> categories) {
		setCategories(new HashSet<String>());
		this.setItemId(id);
		this.setTitle(title);
		this.setYear(year);
		this.setCategories(categories);
		
		users = new HashSet<Integer>();
	}
	
	@Override
	public Set<Integer> getRatedUsers() {
		return users;
	}
	
	@Override
	public void addUser(int userId) {
		users.add(userId);
	}

	@Override
	public int getItemId() {
		return itemId;
	}

	@Override
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Set<String> getCategories() {
		return categories;
	}
	
	@Override
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	
	@Override
	public void updateAverage(double rating, boolean isNew) {
		if (isNew) ratingCount++;
		averageRating = (rating - averageRating)/ ratingCount;
	}
	
	@Override
	public double getBaseline(){
		return this.averageRating;
	}
	
	/**
	 * This method is current not implemented because it is irrelevant
	 */
	@Override
	public Set<String> getFeatures() {
		// TODO Auto-generated method stub
		return null;
	}
}
