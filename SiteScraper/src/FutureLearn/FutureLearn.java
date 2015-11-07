package FutureLearn;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.sql.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

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
		String url2 = "https://www.futurelearn.com/courses/categories/business-and-management"; 		//FutureLearn: Business & Management 
		String url3 = "https://www.futurelearn.com/courses/categories/creative-arts-and-media"; 		//FutureLearn: Creative Arts & Media
		String url4 = "https://www.futurelearn.com/courses/categories/health-and-psychology"; 			//FutureLearn: Health and Psychology 
		String url5 = "https://www.futurelearn.com/courses/categories/history"; 						//FutureLearn: History 
		String url6 = "https://www.futurelearn.com/courses/categories/languages-and-cultures"; 			//FutureLearn: Languages & Cultures
		String url7 = "https://www.futurelearn.com/courses/categories/law"; 							//FutureLearn: Law
		String url8 = "https://www.futurelearn.com/courses/categories/literature"; 						//FutureLearn: Literature
		String url9 = "https://www.futurelearn.com/courses/categories/nature-and-environment"; 			//FutureLearn: Nature & Environment 
		String url10 = "https://www.futurelearn.com/courses/categories/online-and-digital";				//FutureLearn: Online & Digital 
		String url11 = "https://www.futurelearn.com/courses/categories/politics-and-the-modern-world"; 	//FutureLearn: Politics & the Modern World 
		String url12 = "https://www.futurelearn.com/courses/categories/science-maths-and-technology"; 	//FutureLearn: Science, Maths, & Technology 
		String url13 = "https://www.futurelearn.com/courses/categories/sport-and-leisure";				//FutureLearn: Sport & Leisure
		
		 
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
			Document doc = Jsoup.connect(furl).get(); //get HTML code from website
			
			//Find the link
			Elements link = doc.select("a[href*=courses]"); //get links with hyperlinks containing /courses/
			
			for(int j = 0; j < link.size(); j++)
			{
				System.out.println("Link: " + link.get(j).attr("href"));
			}
			
			/*
			  TEST
			  System.out.println("furl: " + furl);
			  System.out.println("doc: " + doc);
			  System.out.println("link: " + link);
			  for(int i = 0; i < link.size(); i++)
				{
					System.out.println(link.get(i).attr("href"));
				}
			END: TEST
			*/
		}
		}
}