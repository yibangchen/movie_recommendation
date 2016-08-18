import java.io.File;
import java.util.Scanner;

public class UserInterface {
	
	public static final Scanner sc = new Scanner(System.in);
	
	public static void main(String args[]){
		
		String path = getDataFolder(); //"/Users/user/Downloads/ml-latest-small/";
		String dataType = getDelim();
		
		String usage = "***********What functionalities would you like to try?\r\n"
					+  "1. Predict a user's rating for a movie\r\n"
					+  "2. Recommand movies for a user\r\n"
					+  "Please enter the corresponding number: \r\n"
					+  "(enter 'quit' to exit)\r\n";
		
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
		
		while (true) {
			String input;
			System.out.println(usage);
			input = sc.nextLine();
			if (input.equals("1")) {
				int userId = getUserId();
				int itemId = getItemId();
				int size = getNeighborSize();
				
				pr.printPrediction(userId, itemId, size);
			}
			else if (input.equals("2")) {
				int userId = getUserId();
				int itemSize = getRecommendSize();
				int size = getNeighborSize();
				
				pr.printRecommend(userId, itemSize, size);
			}
			else if (input.equals("quit")) break;
			else System.out.println("Invalid input!");
		}
		
		sc.close();
	}
	
	private static String getDelim() {
		String type = "";
		
		while (type.equals("")) {
			System.out.println(	"***********What data file would you like to parse?\r\n"
					+ 		"Type 1 for .dat file"
					+ 		"Type 2 for .csv file");
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
	
	/**
	 * This method asks the user to provide a valid user ID
	 * @return
	 */
	private static int getUserId(){
		int userId = -1;
		
		while (userId < 0) {
			System.out.println("Please enter the ID of user to predict/recommend: ");
			try {
				userId = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				System.out.println("Invalid input. Please retry.");
			}
		}
		return userId;
	}
	
	/**
	 * This method asks the user to provide a valid item ID
	 * @return
	 */
	private static int getItemId(){
		int id = -1;
		
		while (id < 0) {
			System.out.println("Please enter the ID of item to predict: ");
			try {
				id = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				System.out.println("Invalid input. Please retry.");
			}
		}
		return id;
	}
	
	/**
	 * This method asks the user to provide size of neighbors for predicting an item
	 * @return
	 */
	private static int getNeighborSize(){
		int size = -1;
		
		while (size < 0) {
			System.out.println("Please enter the size of neighbors: ");
			try {
				size = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				System.out.println("Invalid input. Please retry.");
			}
		}
		return size;
	}
	
	/**
	 * This method asks the user to provide number of items to recommend
	 * @return
	 */
	private static int getRecommendSize(){
		int size = -1;
		
		while (size < 0) {
			System.out.println("Please enter the number of movies to recommend: ");
			try {
				size = Integer.parseInt(sc.nextLine());
			} catch (Exception e) {
				System.out.println("Invalid input. Please retry.");
			}
		}
		return size;
	}


}
