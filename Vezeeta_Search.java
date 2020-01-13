package test_search;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Vezeeta_Search {
	private static String SEARCH_INPUT_BAR ="q";
	//private static String SEARCH_RESULT_BLOCK ="srg";
	private static String SEARCH_RESULT_HITS ="rc";
	private static String RESULT_TITLE ="h3.LC20lb";
	private static String RESULT_SECTION="span.st";
	private static String FEATURED_SECTION="ILfuVd";
	
	private static ChromeDriver driver = null;
	
	public static void main(String[] args) {
		
		
		System.setProperty("webdriver.chrome.driver", "/home/testing/chromedriver_linux64/chromedriver");
		driver = new ChromeDriver();
		
		loadWebsite("https://www.google.com");
		performGoogleSearch("programming");
		ArrayList<SearchBlock> results1 = parsSearchResults();
		goToNextPage();
		ArrayList<SearchBlock> results2 = parsSearchResults();
		
		ArrayList<SearchBlock> combinedResults = new ArrayList();
		combinedResults.addAll(results1);
		combinedResults.addAll(results2);
		
		printResults(combinedResults);
	}
	
	
	//Sleeps for given number of seconds
	private static void sleep(long seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
			}
	}
	
	//Loads given website
	private static void loadWebsite(String site) {
		driver.get(site);
		sleep(1);
	}
	
	
	//Performs google search for given query
	private static void performGoogleSearch(String query) {
		WebElement searchInput =driver.findElementByName(SEARCH_INPUT_BAR);
		searchInput.sendKeys(query);
		searchInput.sendKeys(Keys.RETURN);
		sleep(1);
	}
	
	
	/*Parse search results and return a list of title/sections.
	 * Also finds featured section if it exists
	 */
	private static ArrayList<SearchBlock> parsSearchResults(){
		ArrayList<SearchBlock> results =new ArrayList<>();
		
		String featuredSectionText ="";
		try {
			WebElement featuredSection = driver.findElementByClassName(FEATURED_SECTION);
			featuredSectionText = featuredSection.getText();
		} catch (NoSuchElementException ignored) {}
		
		List<WebElement> hits = driver.findElements(By.className(SEARCH_RESULT_HITS));
		
		for(WebElement hit: hits) {
			String title = hit.findElement(By.cssSelector(RESULT_TITLE)).getText();
			String section = hit.findElement(By.cssSelector(RESULT_SECTION)).getText();
			if (!featuredSectionText.equals("")) {
				results.add(new SearchBlock(title, featuredSectionText));
				featuredSectionText ="";
			} else if (!title.equals("")) {
				results.add(new SearchBlock(title, section));
				}
				
		}
		return results;	
	}
		
		
    //Goes to next page
	private static void goToNextPage() {
		int nextPageBtnIndex = driver.findElementsByClassName("pn").size() - 1;
        WebElement nextPageButton = driver.findElementsByClassName("pn")
                .get(nextPageBtnIndex);
        nextPageButton.click();
        sleep(2);
	}
	
	
	//Prints google search results by title/section
	private static void printResults(ArrayList<SearchBlock> searchResults) {
		for(SearchBlock block: searchResults) {
			System.out.println("Title:" + block.title);
			System.out.println("Section:" + block.section);
			System.out.println();
		}
	}
	
	//Class to hold title/section
	private static class SearchBlock{
		String title;
		String section;
		
		SearchBlock(String title, String section){
			this.title = title;
			this.section = section;
			}
	}
		
}
		
		