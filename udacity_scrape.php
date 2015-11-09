<?php
  $resp = file_get_contents("https://www.udacity.com/public-api/v0/courses");
  $json_response = json_decode($resp, true);
  $servername = "localhost";
  $username = "root";
  $password = "";
  $dbname = "moocs160";
  // Create connection
  $conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
   if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 
  $id;
  $title;
  $short_desc;
  $long_desc;
  $course_link;
  $video_link;
  $start_date;
  $course_length;
  $course_image;
  $category;
  $site;
  $course_fee;
  $language;
  $certificate;
  $university;
  $time_scraped;
  $profName = "";
  $profImg = ""; 
  $largest_id = 1;
  $j = 3;
  $z = 0;
  foreach ($json_response["courses"] as $course) 
  {
    //echo $course["title"], "<br>";
    //echo $course["homepage"], "<br>";
	$id = 0;
	$title = mysql_real_escape_string($course["title"]);

	$short_desc = mysql_real_escape_string($course["short_summary"]);
	$long_desc = mysql_real_escape_string($course["summary"]);
	$course_link = mysql_real_escape_string($course["homepage"]);
	$video_link = $course["teaser_video"];
	//echo key($video_link);
	$video_link = mysql_real_escape_string($video_link["youtube_url"]);
	$start_date = "Default";
	$course_length = $course["expected_duration"];
	$course_image = mysql_real_escape_string($course["image"]);
	$category = mysql_real_escape_string($course["subtitle"]);
	$site = "Udacity";
	$course_fee = "$0";
	$language = "English";
	$certificate = $course["full_course_available"];
	$sql = "SELECT id from course_data";
	$result = mysqli_query($conn, $sql);

	if (mysqli_num_rows($result) > 0) 
	{
		// output data of each row
		while($row = mysqli_fetch_assoc($result)) 
		{
			//echo "id: " . $row["id"]. "<br>";
			$largest_id = $row["id"];
		}
		$largest_id++;
	}
	else 
	{
		echo "0 results";
	}

	if(count($course["affiliates"]) > 0)
	{
		$university = $course["affiliates"][0]["name"];
	}
	else
	{
		$university = "";
	}
	//$time_scraped = date("Y-m-d"). " ". date("h:i:sa");
	date_default_timezone_set("America/Los_Angeles");
	$time_scraped = date("Y-m-d"). " ". date("G:i:s");
	if($course["expected_duration_unit"] == "days")
	{
		$course_length = 1;
	}
	else if($course["expected_duration_unit"] == "months")
	{
		$course_length = $course_length * 4;
	}
	else
	{
		//do nothing. Leave in weeks formation
	}
    $sql = "INSERT INTO course_data (id, title, short_desc, long_desc, course_link, video_link,
	start_date, course_length, course_image, category, site, course_fee, language, certificate, university, time_scraped) 
	VALUES ('$id', '$title', '$short_desc', '$long_desc', '$course_link', '$video_link', '$start_date', '$course_length', 
    '$course_image','$category', '$site', '$course_fee', '$language', '$certificate', '$university', '$time_scraped')";

	if ($conn->query($sql) === TRUE) 
	{

		echo "New record created successfully";
	} 
	else 
	{
		echo  "Error: " . $sql . "<br>" . "<b>" .$conn->error . "</b>";
	}
	
	for($i = 0; $i<1; $i++ )
	{
		//echo $j . "<br>";
		//echo $z . "<br>";
		$profName = $course["instructors"][$i]["name"];
		$profImg = $course["instructors"][$i]["image"];
		//$courseName = mysql_real_escape_string($course["title"]);
		
		
		
		$sql = "INSERT INTO coursedetails VALUES (null,'$profName', '$profImg', '$largest_id')";
		if ($conn->query($sql) === TRUE) 
		{

			echo "New record created successfully";
		} 
		else 
		{
			echo  "Error: " . $sql . "<br>" . "<b>" .$conn->error . "</b>";
		}
		$largest_id++;
	//$z++;
	}
	
  }
  $conn->close();
?>