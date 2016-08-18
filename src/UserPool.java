import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * This singleton class is a collection of users
 * It has methods that involves ONLY users
 * @author Yibang Chen
 *
 */
public class UserPool {
	
	private static UserPool instance = null;
	private Map<Integer, User> users;
	
	private UserPool() {
		users = new HashMap<Integer, User>();
	}
	
	/**
	 * This method ensures singleton design
	 * @return
	 */
	public static UserPool getInstance() {
		if (instance == null) instance = new UserPool();
		return instance;
	}
	
	public Set<Integer> getUsers() {
		return users.keySet();
	}
	
	public void addUser(User newUser){
		users.put(newUser.getUserId(), newUser);
	}
	
	public boolean isUserAdded(int userId){
		return users.containsKey(userId);
	}
	
	public User getUser(int userId){
		return users.get(userId);
	}
	
	/**
	 * This method finds the similarity score between 2 users
	 * @param userId1
	 * @param userId2
	 * @return
	 */
	public double findSimilarity(int userId1, int userId2){
		
		double sum12 = 0;
		double sum1 = 0;
		double sum2 = 0;
		
		Set<Integer> commonItems = this.getCommonItems(userId1, userId2);
		if (commonItems == null) return 0;
		if (commonItems.isEmpty()) return 0;
		
		User user1 = this.getUser(userId1);
		User user2 = this.getUser(userId2);
		double avg1 = user1.getAverageRating();
		double avg2 = user2.getAverageRating();
		
		for (int item : commonItems){
			float rate1 = user1.getRating(item);
			float rate2 = user2.getRating(item);
			sum12 += (rate1 - avg1) * (rate2 - avg2);
			sum1 += (rate1 - avg1) * (rate1 - avg1);
			sum2 += (rate2 - avg2) * (rate2 - avg2);
		}
		
		if (sum1 * sum2 == 0) return 0;
		return sum12/(Math.sqrt(sum1) * Math.sqrt(sum2));
	}
	
	/**
	 * This method finds the similarity score between 2 users using cosine similarity
	 * @param userId1
	 * @param userId2
	 * @return
	 */
	public double findCosineSimilarity(int userId1, int userId2) {

		User user1 = this.getUser(userId1);
		User user2 = this.getUser(userId2);
		
		double magnitude1 = user1.getMagnitude();
		double magnitude2 = user2.getMagnitude();
		double dotProduct = 0;
		
		if (magnitude1 == 0 || magnitude2 == 0) return 0;
		
		Set<Integer> commonItems = this.getCommonItems(userId1, userId2);
		if (commonItems == null) return 0;
		if (commonItems.isEmpty()) return 0;
		
		for (int item: commonItems) {
			dotProduct += user1.getRating(item) * user2.getRating(item);
		}
		
		return dotProduct/(magnitude1 * magnitude2);
	}
		
	/**
	 * This method finds the common items rated by 2 users
	 * @param userId1
	 * @param userId2
	 * @return Set of common items
	 */
	private Set<Integer> getCommonItems(int userId1, int userId2){
		Set<Integer> commonItems = new HashSet<Integer>();
		
		try {
			commonItems.addAll(this.getUser(userId1).getRatedItems());
			commonItems.retainAll(this.getUser(userId2).getRatedItems());
		} catch (Exception e) {
			System.out.println("Error at getCommonItems method in UserPool class: ");
			System.out.println(e.toString());
		}
		
		return commonItems;
	}
	
	public List<Integer> findSimilarUsers(int userId, int numOfNeighbors, double similarityThreadshold){
		//NOT IMPLEMENTED
		return null;
	}
	
	public List<Integer> findSimilarUsers(int userId, int numOfNeighbors){
		//NOT IMPLEMENTED
		return null;
	}
	
	public List<Integer> findSimilarUsers(int userId, double similarityThreadshold){
		//NOT IMPLEMENTED
		return null;
	}
	
	public List<Integer> findSimilarUsers(int userId){
		//NOT IMPLEMENTED
		return null;
	}
	
}
