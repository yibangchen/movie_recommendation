import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class provides functions for calculating predictions and recommendations
 * It includes methods that involves both user and items
 * @author Yibang Chen
 *
 */
public class Prediction {
	
	private UserPool users;
	private ItemPool items;
	private final int NEIGHBOR_SIZE = 20;
	
	/**
	 * The constructor
	 */
	public Prediction() {
		users = UserPool.getInstance();
		items = ItemPool.getInstance();
	}
	
	
	/**
	 * This method predicts a user's rating to a particular item by providing user specified neighbor size
	 * @param userId
	 * @param itemId
	 * @param numOfNeighbors
	 * @return : 
	 * 		- 0 if item is invalid
	 */
	public double predictRating(int userId, int itemId, int numOfNeighbors){
		
		if (!items.isValidItem(itemId)) return 0;
		
		Map<Integer, Double> neighbors = new HashMap<Integer, Double>(); //similarities		
		for (int id : users.getUsers()){
			User user = users.getUser(id);
			if (user.isRated(itemId)) {
				neighbors.put(id, users.findSimilarity(userId, id));
			}
		}
		
		List<Map.Entry<Integer, Double>> list = sortMapValueDesc(neighbors);	
		if (numOfNeighbors < list.size()) {
			list.subList(numOfNeighbors, list.size()).clear();
		}
		
		return getAverageRating(userId, list, itemId);
	}
	
	/**
	 * This method predicts a user's rating to a particular item by default neighbor size
	 */
	public double predictRating(int userId, int itemId){
		return predictRating(userId, itemId, NEIGHBOR_SIZE);
	}

	/**
	 * This method predicts a user's rating to a particular item by providing user specified neighbor size
	 * This prediction is based on cosine similarity between users
	 * @param userId
	 * @param itemId
	 * @param numOfNeighbors
	 * @return : 
	 * 		- 0 if item is invalid
	 */
	public double predictCosineRating(int userId, int itemId, int numOfNeighbors){
		
		if (!items.isValidItem(itemId)) return 0;
		
		Map<Integer, Double> neighbors = new HashMap<Integer, Double>(); //similarities		
		for (int id : users.getUsers()){
			User user = users.getUser(id);
			if (user.isRated(itemId)) {
				neighbors.put(id, users.findCosineSimilarity(userId, id));
			}
		}
		
		List<Map.Entry<Integer, Double>> list = sortMapValueDesc(neighbors);	
		if (numOfNeighbors < list.size()) {
			list.subList(numOfNeighbors, list.size()).clear();
		}
		
		return getAverageRating(userId, list, itemId);
	}

	/**
	 * This method predicts a user's rating to a particular item by default neighbor size
	 */
	public double predictCosineRating(int userId, int itemId){
		return predictCosineRating(userId, itemId, NEIGHBOR_SIZE);
	}
	
	/**
	 * This method calculates the baseline rating based on 
	 * 	- overall averages of all items
	 * 	- baseline predictor for user
	 * 	- baseline predictor for item
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public double getBaselineRating(int userId, int itemId) {
//		double itemAvg = items.getBaseline(itemId);
		double userAvg = users.getUser(userId).getAverageRating();
//		double overallAvg = items.getAverage();
		
		double userOffsetTotal = 0;
		Set<Integer> ratedUsers = items.getItem(itemId).getRatedUsers();
				
		if (ratedUsers.isEmpty()) {
			return userAvg;
		}
		
		for (int user : ratedUsers) {
			User u = users.getUser(user);
			userOffsetTotal += u.getRating(itemId) - u.getAverageRating();
		}
		
//		System.out.println("UserOffset: " + userOffsetTotal/ratedUsers.size());		
		return userAvg + userOffsetTotal/ratedUsers.size();
	}
	
	/**
	 * This method calculates and prints a user's preference to an item
	 * @param userId
	 * @param itemId
	 * @param numOfNeighbors
	 */
	public void printPrediction(int userId, int itemId, int numOfNeighbors){
//		double score = predictRating(userId, itemId, numOfNeighbors);
		double score = this.getBaselineRating(userId, itemId);
		System.out.println("The system predicts that user " + userId
				+ " rates item "+ itemId + ": " + score + "\r\n");
	}
	
	/**
	 * This method sorts a Map by its values and converts it to a List
	 * @param map : <Integer, Double> pairs
	 * @return List : <Integer, Double> pairs
	 */
	private List<Map.Entry<Integer, Double>> sortMapValueDesc(Map<Integer, Double> map){
		
		List<Map.Entry<Integer, Double>> list = 
				new ArrayList<Map.Entry<Integer, Double>>(map.entrySet());
		
		Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
			public int compare(Map.Entry<Integer, Double> o1,
                                           Map.Entry<Integer, Double> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});
		
		return list;
	}
	
	/**
	 * This method recommends items for a particular user
	 * @param userId : the 
	 * @param numOfItems : number of items to recommend
	 * @param numOfNeighbors : number of neighbors when calculating similarities
	 * @return
	 */
	public List<Integer> recommendItems(int userId, int numOfItems, int numOfNeighbors){
		User user = users.getUser(userId);
		
		Set<Integer> itemIds = new HashSet<Integer>(); 
		itemIds.addAll(items.getItemIds());
		itemIds.removeAll(user.getRatedItems());
		
		Map<Integer, Double> recommends = new HashMap<Integer, Double>();
		for (int itemId : itemIds){
			recommends.put(itemId, this.predictRating(userId, itemId, numOfNeighbors));
		}
		
		List<Map.Entry<Integer, Double>> list = this.sortMapValueDesc(recommends);
		List<Integer> result = new ArrayList<Integer>();
		
		for (int i = 0; i < list.size() && i < numOfItems; i++) {
			result.add(list.get(i).getKey());
		}

		return result;
	}
	
	/**
	 * This method prints the recommended items for the user provided
	 */
	public void printRecommend(int userId, int numOfItems, int numOfNeighbors) {
		List<Integer> result = recommendItems(userId, numOfItems, numOfNeighbors);
		for (int item: result) {
			System.out.println(result.indexOf(item)+1 + ": " + items.getItemTitle(item));
		}
	}
	
	/**
	 * This methods calculates the weighted average rating for an item 
	 * @param userId : the user for which to predict a rating
	 * @param neighbors : <Integer, Double> pairs
	 * @param itemId
	 * @return
	 */
	private double getAverageRating(int userId, List<Map.Entry<Integer, Double>> neighbors, int itemId) {
		double score = 0;
		double sum1 = 0, sum2 = 0;
		
		score += users.getUser(userId).getAverageRating();
		for (Map.Entry<Integer, Double> n : neighbors) {
			User u = users.getUser(n.getKey());
			sum1 += n.getValue() * (u.getRating(itemId) - u.getAverageRating());
			sum2 += Math.abs(n.getValue());
		}
		
		if (sum2 == 0) return score;
		return score + sum1/sum2;
	}

}
