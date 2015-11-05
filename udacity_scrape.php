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
  foreach ($json_response["courses"] as $course) 
  {
    //echo $course["title"], "<br>";
    //echo $course["homepage"], "<br>";
	$id = mysql_real_escape_string($course["key"]);
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
	$site = mysql_real_escape_string($course["homepage"]);
	$course_fee = "$0";
	$language = "English";
	$certificate = $course["full_course_available"];
	$university = "";//$course["affiliates"];
	$time_scraped = "";
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
  }
  $conn->close();
?>