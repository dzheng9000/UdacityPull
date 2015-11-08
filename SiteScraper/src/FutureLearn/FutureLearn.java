package FutureLearn;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.sql.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * FutureLearn.java scrapes the FutureLearn website using JSoup library
 * Extracted data will be inserted to the moocs database
 * 
 * VARIABLES
 * url1 = the url to add into urlXSS
 * urlXSS = the arraylist that holds the urls to scrape
 * courseNames = holds the courseNames
 * courseLinks = holds the courseLinks
 * 
 * @author alvin ko 
 * @author dave tseng
 *
 */
public class FutureLearn
{
	 //JDBC DRIVER NAME AND DATABASE URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/moocs160";
	
	//DATABASE CREDENTIALS
	static final String USER = "root";
	static final String PASS = "";

	/**
	 * @param args
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException 
	{
		//URLS
		String url1 = "https://www.futurelearn.com/courses/categories/teaching-and-studying"; 			//FutureLearn: Teaching & Studying
//		String url2 = "https://www.futurelearn.com/courses/categories/business-and-management"; 		//FutureLearn: Business & Management 
//		String url3 = "https://www.futurelearn.com/courses/categories/creative-arts-and-media"; 		//FutureLearn: Creative Arts & Media
//		String url4 = "https://www.futurelearn.com/courses/categories/health-and-psychology"; 			//FutureLearn: Health and Psychology 
//		String url5 = "https://www.futurelearn.com/courses/categories/history"; 						//FutureLearn: History 
//		String url6 = "https://www.futurelearn.com/courses/categories/languages-and-cultures"; 			//FutureLearn: Languages & Cultures
//		String url7 = "https://www.futurelearn.com/courses/categories/law"; 							//FutureLearn: Law
//		String url8 = "https://www.futurelearn.com/courses/categories/literature"; 						//FutureLearn: Literature
//		String url9 = "https://www.futurelearn.com/courses/categories/nature-and-environment"; 			//FutureLearn: Nature & Environment 
//		String url10 = "https://www.futurelearn.com/courses/categories/online-and-digital";				//FutureLearn: Online & Digital 
//		String url11 = "https://www.futurelearn.com/courses/categories/politics-and-the-modern-world"; 	//FutureLearn: Politics & the Modern World 
//		String url12 = "https://www.futurelearn.com/courses/categories/science-maths-and-technology"; 	//FutureLearn: Science, Maths, & Technology 
//		String url13 = "https://www.futurelearn.com/courses/categories/sport-and-leisure";				//FutureLearn: Sport & Leisure
		
		 
		//Arraylist urlXSS contains the URLS that this program will XSS with JSOUP
		ArrayList urlXSS = new ArrayList<String>(); //Array which will store each course URLs 
		urlXSS.add(url1);
//		urlXSS.add(url2);
//		urlXSS.add(url3);
//		urlXSS.add(url4);
//		urlXSS.add(url5);
//		urlXSS.add(url6);
//		urlXSS.add(url7);
//		urlXSS.add(url8);
//		urlXSS.add(url9);
//		urlXSS.add(url10);
//		urlXSS.add(url11);
//		urlXSS.add(url12);
//		urlXSS.add(url13);
		
		//REGISTER JDBC Driver 
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		//OPEN A CONNECTION 
		System.out.print("Connecting to database...");
		java.sql.Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
		System.out.print("\t\tSuccessful\n\n"); 
		
		//Loop through arraylist urlXSS
		for(int i=0; i<urlXSS.size();i++)
		{
			String furl = (String) urlXSS.get(i);  //get website url
			System.out.println("URL: " + furl); //return the website url
			Document doc = Jsoup.connect(furl).get(); //get HTML code from the urlXSS.get(i)
			//System.out.println("HTML document: " + doc); // return the HTML code from the website
			
			//***************************************************************************
			//ELEMENTS
			Elements link = doc.select("a[href*=courses]"); //get links with hyperlinks containing /courses/
			Elements short_desc = doc.select("p[class=introduction]"); //get short descriptions of each course
			
			//***************************************************************************
			//Arraylist for Course Information 
			ArrayList courseNames = new ArrayList<String>(); //holds the course names
			ArrayList courseLinks = new ArrayList<String>(); //holds the course links
			ArrayList courseShort_desc = new ArrayList<String>();
			ArrayList courseLong_desc = new ArrayList<String>();
			
			//System.out.println(link.size());
			for(int j = 0; j < link.size(); j++) //NOTE: LINKS SIZE IS 61 (AKO 11/7/15 3:40pm)
			{
				//***************************************************************************
				//ADDING COURSE NAMES + COURSE LINKS
				String courseName = link.get(j).text(); //course name 
				String courseLink = link.get(j).attr("href"); //course link
				
				if(courseName.equals("") || courseName.equals("More") || courseName.equals("Courses") || courseName.equals("View all categories"))
				{
					//don't add to courseNames
				}
				else //add course name and course link that do NOT contain "", "More", "Courses", "View all categories"
				{
					courseLink = "https://www.futurelearn.com" + courseLink; //update courseLink url to include "https://www.futurelearn.com"
					courseNames.add(courseName); 
					courseLinks.add(courseLink); 
					
				}
			}
	
			//***************************************************************************
			//ADD SHORT DESCRIPTIONS 
			int numOfCourses = courseNames.size();
			for(int j = 0; j < numOfCourses; j++)
			{
				String shortDescription = short_desc.get(j).text(); //get course short description text
				courseShort_desc.add(shortDescription); //add course short description text to courseShort_desc
			}
			
			//***************************************************************************
			//LONG DESCRIPTION 
			for(int j = 0; j < courseLinks.size(); j++)
			{
				String courseURL = (String) courseLinks.get(j);
				//System.out.println("Course URL: " + courseURL); //return the website url
				Document courseDoc = Jsoup.connect(furl).get(); //get HTML code from the courseLinks.get(j)
				//System.out.println("HTML document: " + doc); // return the HTML code from the website
				
				//stuck here
				Elements long_desc = doc.select("div"); //get long descriptions of each course
				String longDescription = long_desc.text();
				//courseLong_desc.add(longDescription);
				System.out.println(longDescription);
			}
			
			//Document doc = Jsoup.connect(furl).get()

//			for(int a = 0; a < courseNames.size(); a++)
//			{
//				System.out.println("Course Name: " + courseNames.get(a));
//				System.out.println("Course Link: " + courseLinks.get(a));
//				System.out.println("Course Short Description: " + courseShort_desc.get(a));
//				System.out.println("Course Long Description: " + courseLong_desc.get(a));
//				System.out.println();
//			}
			
		} //end loop
		}//end main
}