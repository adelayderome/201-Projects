<!-- Adelayde Rome (7675884868) -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="signin.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<title>Friends Profile</title>
		
		<script>
			var CLIENT_ID = "222490153320-7ebr2u3avv592pnlc4ke57f8tjifh4g8";
			var API_KEY = "AIzaSyAmYD-vI414e5ILOqM3_1TOrr3-LioSO_Y";
			
			var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];
			var SCOPES = "https://www.googleapis.com/auth/calendar";
			var imgURL, name, email, following, fEmail;
			
	
		    function handleClientLoad() {
		    	gapi.load('client:auth2', initClient);
		    }
		    
		    function initClient() {
		        gapi.client.init({
		          apiKey: API_KEY,
		          clientId: CLIENT_ID,
		          discoveryDocs: DISCOVERY_DOCS,
		          scope: SCOPES
		        }).then(function () {
	
		        });
		        
		    }
		    
		    function onLoad(){
		    	gapi.load('client:auth2', function(){
			    	var auth = gapi.auth2.getAuthInstance();
			    	auth.then(function(){
						var profile = auth.currentUser.get().getBasicProfile();
						
						name = profile.getName();
						email = profile.getEmail();
						
						fEmail = location.search.split('userid=')[1];
						
						//send friends email, recieve name then another call forimgurl
						//then another for if they are followed by the current user
						//then if following, another to get all of their events 
							//more specifically the html to add the events in because thats easier
						$.ajax({
							url: "friendInfo",
							data: {
								field: "friendEmail",
								friendEmail: fEmail,
								field: "lookingfor",
								lookingfor: "name"
							},
							success: function(result){
								document.getElementById('name').innerHTML = result;
							}
						})
						
						$.ajax({
							url: "friendInfo",
							data: {
								field: "friendEmail",
								friendEmail: fEmail,
								field: "lookingfor",
								lookingfor: "img"
							},
							success: function(result){
								image = document.getElementById('profPic')
								image.src = result;
							}
						})
						
						$.ajax({
							url: "friendInfo",
							data: {
								field: "friendEmail",
								friendEmail: fEmail,
								field: "userEmail",
								userEmail: email,
								field: "lookingfor",
								lookingfor: "followStatus"
							},
							success: function(result){
								followButton(result);
							}
						})
			    	});
		    	});
		    }
		    
			function followButton(F){
				document.getElementById('followButton').innerText = F;
				
				var button = $("button").text()
				if(button == "Follow"){
					following = false;
			    	document.getElementById("events").innerHTML = "<tr><td>Date</td><td>Time</td>"
							+ "<td>Event Summary</td></tr>";
					document.getElementById("notFollowingText").innerHTML = 
						"<table id=\"notFollowing\"><tr><td>Follow " + document.getElementById('name').innerHTML
						+" to view Upcoming Events</td></tr><table>";
				}
				else if(button == "Unfollow"){
					following = true;
					document.getElementById("notFollowingText").innerHTML = "";
					$.ajax({
						url: "friendInfo",
						data: {
							field: "friendEmail",
							friendEmail: fEmail,
							field: "lookingfor",
							lookingfor: "fEvents"
						},
						success: function(result){
							//insert html into table
							document.getElementById("events").innerHTML = "<tr><td>Date</td><td>Time</td>"
								+ "<td>Event Summary</td></tr>" + result;
						}
					})
				}
			}
			
			//change following status and contents of page
			//and add/remove row from the database
			function changeFollow(){
				if(following){
					//change to not following, change page
					followButton("Follow");
					//delete follow from database
					$.ajax({
						url: "friendInfo",
						data: {
							field: "friendEmail",
							friendEmail: fEmail,
							field: "userEmail",
							userEmail: email,
							field: "lookingfor",
							lookingfor: "deleteFollow"
						}
					})
				}
				else{
					//change to following and change page
					followButton("Unfollow");
					//add follow to database
					$.ajax({
						url: "friendInfo",
						data: {
							field: "friendEmail",
							friendEmail: fEmail,
							field: "userEmail",
							userEmail: email,
							field: "lookingfor",
							lookingfor: "addFollow"
						}
					})
				}
			}
			//adding event to google calendar
			function addEvent(Title, sDateTime, eDateTime){
				
				var event = {
						'summary': Title,
						'start': {
							'dateTime': sDateTime,
						    'timeZone': 'America/Los_Angeles'
						},
						'end': {
						    'dateTime': eDateTime, //kept in correct format whole time 
						    'timeZone': 'America/Los_Angeles'
						}
				};
				request = gapi.client.calendar.events.insert({
					'calendarId': 'primary',
					'resource': event
				});
				
				request.execute(function(event) {
					if(event.htmlLink.charAt(0) == 'h'){
						alert("Success! Event has been added!");
						//document.location.href = "home.jsp";
					}
					else {
						alert("Failed to add event.");
						//document.location.href = "home.jsp";
					}
				});
			}
			//add following functionality for events
			//when curruser clicks on an event, directs to this function which:
			//makes an ajax call to the servlet which adds the clicked event to users events
			//add event to user in database AND users google calendar
			
			//follow event called on click from friend events when event row is clicked 
			function followEvent(id, date, time, name, startDT, endDT){
				//use confirm box to confirm user wants to add
				
				if(confirm("Are you sure you want to add this event to your calendar?")){
					//send to info to add to database
					$.ajax({
						url: "friendInfo",
						data: {
							field: "userEmail",
							userEmail: email,
							field: "eid",
							eid: id,
							field: "edate",
							edate: date,
							field: "etime",
							etime: time,
							field: "ename",
							ename: name, 
							field: "startDateTime",
							startDateTime: startDT,
							field: "endDateTime",
							endDateTime: endDT,
							field: "lookingfor",
							lookingfor: "followEvent"
						}
	
					})
					//calls addevent fxn for adding to google calendar
					addEvent(name, startDT, endDT);
				}
				else{
					//do nothing or prompt?
				}
			}
			
			function searching(){
				document.getElementById("searchPage").style.display = "block";
 		    	document.getElementById("main").style.display = "none";
 		    	var input = document.getElementById('searchInput').innerHTML;
 		    	
 		    	$.ajax({
					url: "Searching",
					data: {
						field: "searchInput",
						searchInput: input,
						field: "Email",
						Email: email
					},
					success: function(result){
						//result is the html for the list items for the appropriate users
						//display in the search list html
						document.getElementById("searchUsers").innerHTML = result;
					}
 		    	})
		   	 } 
		</script>
	</head>
	<body onLoad="onLoad()">
	
	    <script async defer src="https://apis.google.com/js/api.js"
      		onload="this.onload=function(){};handleClientLoad()"
      		onreadystatechange="if (this.readyState === 'complete') this.onload()">
   		</script>
	  	<div id="top"> 
	  		<div id="topleft">
				<a href="loggedin.jsp">Sycamore Calendar</a>
			</div>
			<div id="search">
	  			<input id="searchInput" type="text" placeholder="Search Friends"  onclick="searching()" onkeyup="searching()"/> 
	  			<div id="searchPage">
		  			<ul>
		  				<div id ="searchUsers"> </div>
		  			</ul>
	  			</div>
	  		</div>
	  		<div id="topright">
	  			<a href="home.jsp"> Home</a>
	  			<a href="profile.jsp"> Profile </a>
	  		</div>
	  	</div>
	  	<div id="main">
	  		<p> Upcoming Events</p>
	  		
	  		<button type="button" id="followButton" onclick="changeFollow()"> </button>
	  		<img id="profPic" />
	  		<div id="name"></div>
	  		<div id = "eTable">
		  		<table id="events">
		  			<tr>
						<td>Date</td>
						<td>Time</td>
						<td>Event Summary</td>
					</tr>
		  		</table>
		  		<div id="notFollowingText"></div>
			</div>

	    </div>
	    <div id="bottom"></div>
	</body>
</html>