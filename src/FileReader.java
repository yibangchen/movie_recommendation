import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * This class parses movies and ratings files and populates corresponding objects
 * @author Yibang Chen
 *
 */
public class FileReader {
	
	private static Set<String> importedFiles;
	private String folderPath;
	
	/**
	 * The constructor
	 * @param path : the local folder to read and parse data files
	 */
	public FileReader(String path){
		importedFiles = new HashSet<String>();
		folderPath = path;
	}
	
	/**
	 * This method checks if certain file is imported
	 * @param filename : name of file, with extension
	 * @return
	 */
	private boolean isFileImported(String filename){
		return importedFiles.contains(folderPath+filename);
	}
	
	/**
	 * This method parses the creates objects for the recommending system
	 * @param filename : the name of Movie file
	 * @param movies : the ItemPool of movies
	 * @return movies : added new movieItem objects
	 */
	public ItemPool parseMovieData(String filename, ItemPool movies, String delim) {
		if (isFileImported(filename)) {
			System.out.println("*****" + filename + " is already imported.");
			return movies;
		}
		
		File inputFile;
		Scanner in = null;	
		int c1 = 0, c2 = 0;
		try{
			inputFile = new File(folderPath+filename);
			in = new Scanner(inputFile);
			
			while (in.hasNextLine()) {
				String line = in.nextLine();
				Item newMovie = this.parserItem(line, delim);
				if (newMovie == null) {
					c1++;
				} else {					
					movies.addItem(newMovie);
					c2++;
				}
			}
		}
		catch (Exception e) {
			System.out.println("*****Cannot read file: " + filename);
			return movies;
		}
		finally {
			in.close();
		}
		
		importedFiles.add(filename);
		System.out.println(filename + " is successfully parsed and stored");
		System.out.println("In total: " + c2 + " items parsed, " + c1 + " lines unable to parse\r\n");
		return movies;
	}
	
	/**
	 * This method parses a line from movie file to an Item object
	 * @param line : source data line
	 * @return the Item object
	 */
	private Item parserItem(String line, String delim){
		Item newMovie = null;		
		String[] tokens = line.split(delim);
		
		if (tokens.length == 3) {
			try {
				int itemId = Integer.parseInt(tokens[0]);
				String rawTitle = tokens[1].trim();
				
				int year = 0;
				String title = "";
				try {
					year = Integer.parseInt(rawTitle.substring(rawTitle.length()-5, rawTitle.length()-1));
					title = rawTitle.substring(0, rawTitle.length()-6).trim();
				} catch (Exception e) {
					title = rawTitle.trim();
				}
				Set<String> categories = new HashSet<String>(Arrays.asList(tokens[2].trim().split("\\|")));
				
				newMovie = new MovieItem(itemId,title,year,categories);
			} catch (Exception e) {
				return null;
			}
		} else {
			tokens = line.split("\"");
			if (tokens.length == 3) {
				try{
					int itemId = Integer.parseInt(tokens[0].substring(0, tokens[0].length()-1));
					
					String rawTitle = tokens[1].trim();
					
					int year = 0;
					String title = "";
					try {
						title = rawTitle.substring(0, rawTitle.length()-6).trim();
						year = Integer.parseInt(rawTitle.substring(rawTitle.length()-5,rawTitle.length()-1));					
					} catch (Exception e) {
						title = rawTitle.trim();
					}					
					
					Set<String> categories = new HashSet<String>(Arrays.asList(tokens[2].trim().substring(1).split("\\|")));
					newMovie = new MovieItem(itemId,title,year,categories);
				} catch (Exception e) {
					System.out.println("Cannot parse item: "+ line);
					return null;
				}
			}
		}
		
		return newMovie;
	}
	
	/**
	 * This method reads and parses the rating data provided by user
	 * @param filename : name of the rating file
	 * @param users : UserPool object of all users
	 * @return users : added new User objects
	 */
	public UserPool parseRatingData(String filename, UserPool users, String delim) {
		if (isFileImported(filename)) {
			System.out.println("*****" + filename + " is already imported.");
			return users;
		}
		
		File inputFile;
		Scanner in = null;		
		int c1 = 0, c2 = 0, c3 = 0; //counter of ratings and users
		ItemPool items = ItemPool.getInstance();
		
		try{
			inputFile = new File(folderPath+filename);
			in = new Scanner(inputFile);
			
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String[] tokens = line.split(delim);
				
				if (tokens.length == 4) {
					try {
						int userId = Integer.parseInt(tokens[0]);
						int itemId = Integer.parseInt(tokens[1]);
						float rating = Float.parseFloat(tokens[2]);
						
						User user = users.getUser(userId);
						c1++;
						if (user == null) {
							c2++;
							user = new User(userId);
						}
												
						user.addRating(itemId, rating);
						users.addUser(user);
						
						items.getItem(itemId).addUser(userId);
						items.updateAverage(rating, itemId, true);
					}
					catch (Exception e) {
						c3++;
					}
				} else {
					c3++;
				}
			}
		}
		catch (Exception e) {
			System.out.println("*****Cannot read file: " + filename);
			System.out.println(e.toString());
			return users;
		}
		finally {
			in.close();
		}
		
		importedFiles.add(filename);
		System.out.println(filename + " is successfully stored: ");
		System.out.println("In total: " + c1 + " ratings of " + c2 + " users parsed, " + c3 + " lines unable to read\r\n");
		return users;
	}
	
	public void readUserData (String filename) {
		// NOT IMPLEMENTED - irrelevant to current functionalities
	}
}
