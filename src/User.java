import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class User {
	
	private int userId;
	private Map<Integer, Float> ratings = null;
	private float averageRating = -1;
	private double magnitude = -1;
		
	public User(int userId) {
		this.userId = userId;
		ratings = new HashMap<Integer, Float>();
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void addRating(int itemId, float rating) {
		ratings.put(itemId, rating);
	}
	
	public boolean isRated(int itemId) {
		return ratings.containsKey(itemId);
	}
	
	public float getRating(int itemId) {
		if (ratings.containsKey(itemId))
			return ratings.get(itemId);
		return 0;
	}
	
	public Set<Integer> getRatedItems() {
		return ratings.keySet();
	}
	
	/**
	 * This method calculates the average rating of a user
	 * @return
	 */
	public float getAverageRating() {
		if (this.averageRating < 0)
			this.setAverageRating();
		
		return averageRating;
	}
	
	/**
	 * This method calculates the average rating for all items of a user
	 */
	private void setAverageRating() {
		if (ratings.isEmpty()) this.averageRating = 0;
		float total = 0;
		
		for (int item : ratings.keySet()) {
			total += ratings.get(item);
		}
		this.averageRating = total/ratings.size();
	}
	
	/**
	 * This method calculates the magnitude of the user's rating vector
	 * @return
	 */
	public double getMagnitude() {
		if (this.magnitude < 0)
			this.setMagnitude();
		
		return magnitude;
	}
	
	/**
	 * This method calculates the magnitude of all ratings of an user
	 */
	private void setMagnitude() {
		float sum = 0;
		for (int item: ratings.keySet()) {
			float val = ratings.get(item);
			sum += val * val;
		}
		
		magnitude = Math.sqrt(sum);
	}
	
	/**
	 * This method prints all ratings that an user has
	 */
	public void printAllRatings() {
		System.out.println("All items: " + ratings.keySet());
		
		for (int item : ratings.keySet()) {
			System.out.println(item + ": " + ratings.get(item));
		}
		System.out.println("Average: " + this.getAverageRating());
	}
}
