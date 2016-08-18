import java.io.File;
import java.util.Scanner;

public class ExperimentInterface {
	
	public static final Scanner sc = new Scanner(System.in);
	
	/**
	 * This is a tester to experiment different recommending algorithms.
	 * The experiment compares variance of different recommending algorithms:
	 * - variance: square_root ( Sum((rating - predicted rating)^2) / total count)
	 * 1. Baseline Model: 
	 * 2. Pearson Model - using simplest weighted average: 
	 * 3. Pearson Model - using cosine similarity
	 * @param args
	 */
	public static void main(String args[]){
		
		String path = getDataFolder(); //"/Users/user/Downloads/ml-latest-small/";
		String dataType = getDelim();
				
		FileReader reader = new FileReader(path);
		UserPool users = UserPool.getInstance();
		ItemPool movies = ItemPool.getInstance();

		if (dataType.equals(".data")) {			
			reader.parseMovieData("movies.dat", movies, "::");
			reader.parseRatingData("ratings_sample.dat", users, "::");
		} else if (dataType.equals(".csv")) {			
			reader.parseMovieData("movies.csv", movies, ",");
			reader.parseRatingData("ratings.csv", users, ",");
		}
				
		Prediction pr = new Prediction();
				
		int counter = 0;
		double baseVariance = 0, pearsonVariance = 0, cosineVariance = 0;
		for (int userId : users.getUsers()){
			User user = users.getUser(userId);
			for (int itemId : user.getRatedItems()) {
				if (counter++ >= 1000) break;

				float score = user.getRating(itemId);
				
				double base = pr.getBaselineRating(userId, itemId);
				baseVariance += (base - score)*(base - score);
				
				double pearson = pr.predictRating(userId, itemId);
				pearsonVariance += (pearson - score)*(pearson - score);
				
				double cosine = pr.predictCosineRating(userId, itemId);
				cosineVariance += (cosine - score)*(cosine - score);
				}
		}
		
		baseVariance = Math.sqrt(baseVariance/counter);
		pearsonVariance = Math.sqrt(pearsonVariance/counter);
		cosineVariance = Math.sqrt(cosineVariance/counter);
		
		System.out.println("Variance of Baseline Model: "+baseVariance
				+	"\r\nVariance of Pearson Model with weighted average: " + pearsonVariance
				+ 	"\r\nVariance of Pearson Model with cosine similarity: " + cosineVariance);

		sc.close();
	}
	
	/**
	 * This method asks the user for a data type of import files
	 * @return
	 */
	private static String getDelim() {
		String type = "";
		
		while (type.equals("")) {
			System.out.println(	"***********What data file would you like to parse?\r\n"
					+ 		"Type 1 for .dat file\r\n"
					+ 		"Type 2 for .csv file\r\n");
			String ans = sc.next();
			if (ans.equals("1"))	type = ".dat";
			else if (ans.equals("2"))	type = ".csv";
		}
		
		return type;
	}
	
	/**
	 * This method asks the user to provide a valid path in which all data files are located
	 * @return the path
	 */
	private static String getDataFolder(){
		String path = "";
		
		while (! new File(path).exists()) {
			System.out.println("Please provide a path: ");
			System.out.println("Example: /Users/user/Downloads/ml-10M100K/");
			path = sc.nextLine();
		}
		return path;
	}
	
}
