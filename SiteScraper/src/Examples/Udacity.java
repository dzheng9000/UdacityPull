package Examples;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.sql.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Udacity
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
		//urls to XSS 
		String url1 = "https://www.udacity.com/courses/all"; //Udacity course catalog
		//String url2 = "https://www.futurelearn.com/courses/categories/business-and-management"; //FutureLearn: Business & Management 
		//String url3 = "https://www.futurelearn.com/courses/categories/creative-arts-and-media"; //FutureLearn: Creative Arts & Media
		//String url4 = "https://www.futurelearn.com/courses/categories/health-and-psychology"; //FutureLearn: Health and Psychology 
		//String url5 = "https://www.futurelearn.com/courses/categories/history"; //FutureLearn: History 
		//String url6 = "https://www.futurelearn.com/courses/categories/languages-and-cultures"; //FutureLearn: Languages & Cultures
		//String url7 = "https://www.futurelearn.com/courses/categories/law"; //FutureLearn: Law
		//String url8 = "https://www.futurelearn.com/courses/categories/literature"; //FutureLearn: Literature
		//String url9 = "https://www.futurelearn.com/courses/categories/nature-and-environment"; //FutureLearn: Nature & Environment 
		//String url10 = "https://www.futurelearn.com/courses/categories/online-and-digital"; //FutureLearn: Online & Digital 
		//String url11 = "https://www.futurelearn.com/courses/categories/politics-and-the-modern-world"; //FutureLearn: Politics & the Modern World 
		//String url12 = "https://www.futurelearn.com/courses/categories/science-maths-and-technology"; //FutureLearn: Science, Maths, & Technology 
		//String url13 = "https://www.futurelearn.com/courses/categories/sport-and-leisure"; //FutureLearn: Sport & Leisure
		//String url14 = "https://www.futurelearn.com/courses/categories/teaching-and-studying"; //FutureLearn: Teaching & Studying
		 
		//arraylist pgcrs contains the URLS that this program will XSS with JSOUP
		ArrayList pgcrs = new ArrayList<String>(); //Array which will store each course URLs 
		pgcrs.add(url1);
		//pgcrs.add(url2);
		//pgcrs.add(url3);
		//pgcrs.add(url4);
		//pgcrs.add(url5);
		//pgcrs.add(url6);
		//pgcrs.add(url7);
		//pgcrs.add(url8);
		//pgcrs.add(url9);
		//pgcrs.add(url10);
		//pgcrs.add(url11);
		//pgcrs.add(url12);
		//pgcrs.add(url13);
		//pgcrs.add(url14);
		
		//REGISTER JDBC Driver 
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		
		//OPEN A CONNECTION 
		System.out.print("Connecting to database...");
		java.sql.Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
		System.out.print("\t\tSuccessful\n"); 
		
		//loop through the arraylist pgcrs
		for(int a=0; a<pgcrs.size();a++)
		{
			String furl = (String) pgcrs.get(a);  //website url
			Document doc = Jsoup.connect(furl).get(); //HTML code from website
			Elements ele = doc.select("div[class*=views-row]"); //find classes pointing to view-row (loosely)
			Elements crspg = doc.select("article[class=course-tile]"); //find articles with class attribute = "course-title"
			Elements link = doc.select("a[href*=/course/]"); //Select links w/ attribute 'a' and value containing "/course/"
			
			for(int i = 0; i < link.size(); i++)
			{
				System.out.println(link.get(i).attr("href"));
			}
			/*
			  TEST
			  System.out.println("furl: " + furl);
			  System.out.println("doc: " + doc);
			  System.out.println("ele: " + ele);
			  System.out.println("crspg: " + crspg);
			  System.out.println("link: " + link);
			  for(int i = 0; i < link.size(); i++)
				{
					System.out.println(link.get(i).attr("href"));
				}
			END: TEST
			*/
			
			for (int j=0; j<link.size();j++)
			{
				Statement statement = connection.createStatement();
			    
				//*********************************************************
				//COURSE URL 
				String crsurl = "https://www.udacity.com" +link.get(j).attr("href"); 
				System.out.println("Course URL: " + crsurl); 
				
				//*********************************************************
				//COURSE NAME
				String CourseName = crsurl.substring(31, crsurl.length()-7);
				CourseName = CourseName.replace("-", " ");
				System.out.println("Course Name: " + CourseName);
				
				//*********************************************************
				//COURSE DESCRIPTION 
				Document courseDoc = null;
				try
				{
					courseDoc = Jsoup.connect(crsurl).get(); //get html code from page
				}
				catch(SocketTimeoutException e)
				{
					//do nothing
				}
				
				
				String SCrsDesrpTemp = courseDoc.select("span[class=\"h-slim course-header-subtitle h2 text-inverse\"]").get(j).text();
				System.out.println("Short Course Description: " + SCrsDesrpTemp);
				
				//*********************************************************
				//COURSE IMAGE 
				String CrsImg;
				if(a==0||a==1)
				{
					CrsImg  = courseDoc.select("meta[property=og:image]").get(j).text();
				}
				else
				{
					CrsImg  = courseDoc.select("meta[property=og:image]").get(j).text(); 
				}
				System.out.println("Course Image: " + CrsImg);
				
				//*********************************************************
				//YOUTUBE
				String youtube = "https://www.youtube.com" +link.get(j).attr("href"); //Get the youtube Url from href tag and add to www.youtube.org to get the full URL to the course; //Youtube link
				System.out.println("youtube: " + youtube);
				
				//Elements crsbodyele = courseDoc.select("section[class=course-detail clearfix]");
				
				//*********************************************************
				//LONG COURSE DESCRIPTION 
				String CrsDes = courseDoc.select("div[class=pretty-format]").get(j).text();; //Course Description Element
				//CrsDes = CrsDes.replace("'", "''");
				//CrsDes = CrsDes.replace(",", "");
				/*if(CrsDes.contains("?"))
				{
					CrsDes = CrsDes.replace("?", "");
				}*/
				CrsDes = "";
				System.out.println("Long Course Description: " + CrsDes);
				
				//*********************************************************
				//DATE
				String Date = courseDoc.select("div[class=startdate]").text(); //CHANGE
				String StrDate = ""; //DEFAULT StrDate
				if(Date.equals("")) //if there is no designated start date
				{
					StrDate = "N/A";
				}
				else
				{
					StrDate = Date.substring(Date.indexOf(":")+1, Date.length()); //Start date after the :
					String datechk = StrDate.substring(0, StrDate.indexOf(" "));
					if(!datechk.matches(".*\\d.*"))
					{
						if(StrDate.contains("n/a"))
						{
							StrDate = "N/A";
						}
						else
						{
							StrDate = "write your own code";
						}
					}
					else
					{
						String date = StrDate.substring(0, StrDate.indexOf(" "));
						String month = StrDate.substring(StrDate.indexOf(" ")+1, StrDate.indexOf(" ")+4);
						String year = StrDate.substring(StrDate.length()-4,StrDate.length());
						StrDate = "write your own code";
					}
				}
				StrDate = "2015-11-03";
				System.out.println("Start Date: " + StrDate);
				
				//*********************************************************
				Element chk = courseDoc.select("div[class=effort last]").first(); //what is this?!
				Element crslenschk = courseDoc.select("div[class*=duration]").first(); //class duration? 
				String crsduration;
				if (crslenschk==null)
				{
					crsduration = "0";
				}
				else if(StrDate.contains("n/a self-paced"))
				{
					crsduration = "0";
				}
				else
				{
					try{
						String crsdurationtmp = courseDoc.select("div[class*=duration]").text();
						int start = crsdurationtmp.indexOf(":")+1;
						int end = crsdurationtmp.indexOf((" "),crsdurationtmp.indexOf(":"));
						crsduration = crsdurationtmp.substring(start, end);
					}
					catch (Exception e)
					{
						crsduration ="0";
						System.out.println("Exception");
					}
				}
				//The following is used to insert scraped data into your database table. Need to uncomment all database related code to run this.
				
				String query = "INSERT INTO course_data(id, title, short_desc, long_desc, course_link, video_link, start_date, course_length, course_image, category, site, course_fee, language, certificate, university, time_scraped) " +
								"VALUES ('" + j +"','" + CourseName + "','" + SCrsDesrpTemp + "','" + CrsDes + "','" + crsurl + "','" + youtube + "','" + StrDate + "','" + crsduration + "','" + CrsImg + "','CS160','Udacity','0','english','yes','sjsu','1000-01-01 00:00:00')";
				System.out.println(query);                	
				statement.executeUpdate(query);// skip writing to database; focus on data printout to a text file instead.
			 }
		}
		connection.close();   
	}
}