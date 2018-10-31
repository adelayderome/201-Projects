<!-- Adelayde Rome (7675884868) -->
<!-- 	//USE AJAX CALL TO SEND INFO TO BACKEND WHEN THIS PAGE IS LOADED -->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<link rel="stylesheet" type="text/css" href="signin.css">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<title>Profile Page</title>

		<script>
		//taken from quickstart link

			var CLIENT_ID = "222490153320-7ebr2u3avv592pnlc4ke57f8tjifh4g8";
			var API_KEY = "AIzaSyAmYD-vI414e5ILOqM3_1TOrr3-LioSO_Y";
			
			var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];
			var SCOPES = "https://www.googleapis.com/auth/calendar";
			var imgURL, name, email;
			
	
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
		        	
		        	listUpcomingEvents();
		        });
		        
		    }
		    
		    function onLoad(){
		    	gapi.load('client:auth2', function(){
			    	var auth = gapi.auth2.getAuthInstance();
			    	auth.then(function(){
						var profile = auth.currentUser.get().getBasicProfile();
						
						imgURL = profile.getImageUrl();
						name = profile.getName();
						email = profile.getEmail();
						
						$.ajax({
							url: "UserDatabase",
							data: {
								field: "username",
								//does this work 
								username: name,
								field: "imageURL",
								imageURL: imgURL,
								field: "useremail",
								useremail: email
							},
							//callback function?
							success: function(result){
								
							}
							
						})
						
						image = document.getElementById('profPic');
						image.src = imgURL;
						
						document.getElementById('name').innerHTML = name;
			    	});
		    	});
		    	
		    }

		    function getDate(when){
		    	var year = when.substring(0, 4);
		    	var month = when.substring(5, 7);
		    	var Month;
		    	if(month == "01") Month = "January";
		    	if(month == "02") Month = "February";
		    	if(month == "03") Month = "March";
		    	if(month == "04") Month = "April";
		    	if(month == "05") Month = "May";
		    	if(month == "06") Month = "June";
		    	if(month == "07") Month = "July";
		    	if(month == "08") Month = "August";
		    	if(month == "09") Month = "September";
		    	if(month == "10") Month = "October";
		    	if(month == "11") Month = "November";
		    	if(month == "12") Month = "December";
		    	var day = when.substring(8, 10);
		    	if(day[0] == "0"){ day = day.substring(1, 2)}
		    	var date = Month + " " + day + ", " + year;
		    	return date;
		    }
		    
		    function getTime(when){
		    	var t = when.substring(11, 16);
		    	var timeS = t.split(':');
		    	var hours = timeS[0];
		    	var minutes = timeS[1];
		    	
		    	var time = "";
		    	var suf = "";
		    	if(hours < 12){
		    		time += hours;
		    		suf = "A.M.";
		    	}
		    	else if(hours > 12){
		    		time += (hours - 12);
		    		suf = "P.M.";
		    	}
		    	time = time + ":" + minutes + " " + suf;
             	if(time[0] == "0"){
            		time = time.substring(1, 11);
              	}
		    	return time;
		    }
		    
		    function displayEvent(sdate, stime, summary, startDT, endDT, eventID){
		    	var table = document.getElementById("events");
		    	var row = table.insertRow(-1); 
		    	var cell1 = row.insertCell(0);
		    	var cell2 = row.insertCell(1);
		    	var cell3 = row.insertCell(2);
		    	cell1.innerHTML = sdate;
		    	cell2.innerHTML = stime;
		    	cell3.innerHTML = summary;

		    	$.ajax({
					url: "UserDatabase",
					data: {
						field: "eventName",
						//does this work 
						eventName: summary,
						field: "eventDate",
						eventDate: sdate,
						field: "eventTime",
						eventTime: stime,
						field: "eventStart",
						eventStart: startDT,
						field: "eventEnd",
						eventEnd: endDT,
						field: "addevent",
						field: "eid",
						eid: eventID,
						addevent: "yes",
						field: "Email",
						Email: email
					}
					
				})
		    }

		    function listUpcomingEvents() {
		        gapi.client.calendar.events.list({
		          'calendarId': 'primary',
		          'timeMin': (new Date()).toISOString(),
		          'showDeleted': false,
		          'singleEvents': true,
		          'maxResults': 100,
		          'orderBy': 'startTime'
		        }).then(function(response) {
		          var events = response.result.items;

		          if (events.length > 0) {
		            for (i = 0; i < events.length; i++) {
		              var event = events[i];
		              var when = event.start.dateTime;
		              var endDT = event.end.dateTime; //just guessing if rthis is correct fxn
		              var eventID = event.id;
		              if (!when) {
		                when = event.start.date;
		              }
		              var date = getDate(when);
		              var time = getTime(when);

		              displayEvent(date, time, event.summary, when, endDT, eventID);
		              
		            }
		          } 

		        });
		      }
			
			
			//function https://www.w3schools.com/howto/howto_js_filter_lists.asp kinda
			//print whole thing in searching, then just modify list items in this function everytime 
			//onkeyup
 		    function searching(){
 		    	document.getElementById("searchPage").style.display = "block";
 		    	document.getElementById("main").style.display = "none";
 		    	var input = document.getElementById('searchInput').value;
 		    	
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
						//console.log(result);
						document.getElementById("searchUsers").innerHTML = result;
					}
 		    	})

		    	//sending user email in url 
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
			</div>

	    </div>
	    <div id="bottom"></div>
	</body>
</html>