import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.sql.*;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
		
		//Data Columns
                String title = "";
                String shortDesc = "";
                String longDesc = "";
                String courseLinkz = "";
                String videoLinkz = "";
                String startDate = "";
                int courseLength = 0;
                String courseImage = "";
                String category = "";
                String site = "Future Learn";
                int courseFee = 0;
                String language = "English";
                boolean certificate = false;
                String university = "";
                String timeScraped = "";
                
                
                
		//Arraylist urlXSS contains the URLS that this program will XSS with JSOUP
		ArrayList urlXSS = new ArrayList<String>(); //Array which will store each course URLs 
		urlXSS.add(url1);
		urlXSS.add(url2);
		urlXSS.add(url3);
		urlXSS.add(url4);
		urlXSS.add(url5);
		urlXSS.add(url6);
		urlXSS.add(url7);
		urlXSS.add(url8);
		urlXSS.add(url9);
		urlXSS.add(url10);
		urlXSS.add(url11);
		urlXSS.add(url12);
		urlXSS.add(url13);
		
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
			Document doc = Jsoup.connect(furl).timeout(10000).get(); //get HTML code from the website
			//System.out.println("HTML document: " + doc); // return the HTML code from the website
			
                        //Extract category
                        category = doc.select("span[class*=o-page-header__title__text]").text();
                        System.out.println(category);
                        
			//Find the courseNames
			Elements link = doc.select("div[class*=m-course-run__main");//("a[href*=courses]"); //get links with hyperlinks containing /courses/
			Elements pictures = doc.select("img[class*=js-lazyload]");
                        Elements univer = doc.select("h3");
                        System.out.println("Universities: " + univer.size());
			ArrayList courseNames = new ArrayList<String>(); //holds the course names
			ArrayList courseLinks = new ArrayList<String>(); //holds the course links
			ArrayList shortDescs = new ArrayList <String>();
                        
                        System.out.println(pictures.size());
                        System.out.println(link.size());
                        
			for(int j = 0; j < pictures.size() ; j++)
			{
                                //Extract University
                                university = univer.get(j).select("a").text().replace("'", "\\'");
                                //Extract Course Image
                                courseImage = pictures.get(j).attr("data-src");
                                //System.out.println("Course image is: " + courseImage);
				String courseName = link.get(j).text();
                                //Extract Short Description
                                shortDesc = link.get(j).select("section[class*=media_description]").select("p[class*=introduction]").text();
                                System.out.println(shortDesc);
                                //Sanitize Short Description
                                shortDesc = shortDesc.replace("'", "\\'");
                                if(!shortDescs.contains(shortDesc))
                                {
                                    shortDescs.add(shortDesc);
                                    
                                }
                                
                                else
                                {
                                    System.out.println("YAYSZ");
                                    //i = Integer.MAX_VALUE;
                                   continue;
                                }
				String courseLink = link.get(j).select("header").select("h2[class*=title]").select("a").attr("href");
                                String courseNombre = link.get(j).select("header").select("h2[class*=title]").select("a").attr("title");
                                //courseNames.add(courseNombre);
                                //System.out.println(courseNames.get(j));
                                //courseLinks.add(courseLink);
                                //System.out.println("Course Name: " + courseNames.get(j));
				//System.out.println("Course LinkL " + courseLinks.get(j));
                                String courseP4ge = "http://www.futurelearn.com" + courseLink;
                                Document coursePage = Jsoup.connect(courseP4ge).timeout(10000).get();
                                
                                //Begin Extraction
                                //Extract Course Link
                                courseLinkz = courseP4ge;
                                //Extract Title
                                Elements titlez = coursePage.select("title");
                                title = titlez.get(0).text().replace("'", "\\'");
                                System.out.println(title);
                                /*
                                //Extract University
                                Elements affiliate = coursePage.select("li[class*=run-organisation]").select("a");
                                for(int z = 0; z<affiliate.size(); z++)
                                {
                                    System.out.println(affiliate.get(z).attr("href"));
                                    university = affiliate.get(z).attr("href");
                                }
                                */
                                //Extract Video Link
                                Elements videoLink = coursePage.select("video").select("source");
                                for(int z = 0; z<videoLink.size(); z++)
                                {
                                    videoLinkz = videoLink.get(z).attr("src");
                                    System.out.println(videoLink.get(z).attr("src"));
                                }
                                //Extract Fee, Course Length, Certificate
                                Elements feeLengthCert = coursePage.select("p[class*=run-data]");
                                if(feeLengthCert.get(0).text().substring(0,4).equals("FREE"))
                                {
                                    courseFee = 0;
                                }
                                else
                                {
                                    courseFee = -1; //Don't really see a class that isn't free
                                }
                                courseLength = Integer.parseInt(feeLengthCert.get(1).text().substring(10,11));
                                if(feeLengthCert.size()==4)
                                {
                                    if(feeLengthCert.get(3).text().equalsIgnoreCase("Certificates Available"))
                                    {
                                        certificate = true;
                                    }
                                }
                                else
                                {
                                    certificate = false;
                                }
                                System.out.println(courseFee);
                                System.out.println(courseLength);
                                System.out.println(certificate);
                                
                                //Extract Long Description
                                Elements longDes = coursePage.select("div[class*=run-overview-main]").select("p");
                                for(int z = 0; z<longDes.size(); z++)
                                {
                                    longDesc = longDesc + " " + longDes.get(z).text();
                                    
                                }

				System.out.println();
                                //Extract Start Date
                                startDate = coursePage.select("time[itemprop*=startDate]").attr("dateTime");
                                System.out.println(startDate);
                                Statement statement = connection.createStatement();
                                String query;
                                longDesc = longDesc.replace("'", "\\'");
                                String cert = "";
                                if(certificate)
                                {
                                    cert = "yes";
                                }
                                else
                                {
                                    cert = "no";
                                }
                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                Calendar cal = Calendar.getInstance();
                                timeScraped = dateFormat.format(cal.getTime());
                                timeScraped = timeScraped.replace("/", "-");
                                query = "INSERT into course_data values(null,'"+title+"','"+shortDesc+"','"+longDesc+"','"+courseLinkz+"','"+videoLinkz+"','"+startDate+"','"+courseLength+"','"+courseImage+"','"+category+"','"+site+"','"+courseFee+"','"+language+"','"+cert+"','"+university+ "','" + timeScraped +"')";
                                System.out.println(query);
                                statement.executeUpdate(query);
                                
                                statement.close();
                                
                                longDesc = "";
			}
		} //end loop
        }//end main
}