<!-- Adelayde Rome (7675884868) -->

<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="home.css" />
		<meta charset="UTF-8">
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<title>Home</title>
		<script >
			var CLIENT_ID = "222490153320-7ebr2u3avv592pnlc4ke57f8tjifh4g8";
			var API_KEY = "AIzaSyAmYD-vI414e5ILOqM3_1TOrr3-LioSO_Y";
			
			var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];
			var SCOPES = "https://www.googleapis.com/auth/calendar";
			var email; 
			
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
		    
			var Title ="", sDateTime="", eDateTime="";
			var request;
			
		    function onLoad(){
		    	gapi.load('client:auth2', function(){
			    	var auth = gapi.auth2.getAuthInstance();
			    	auth.then(function(){
						var profile = auth.currentUser.get().getBasicProfile();
						
						var imgURL = profile.getImageUrl();
						var name = profile.getName();
						email = profile.getEmail();
						
						image = document.getElementById('profPic');
						image.src = imgURL;
						
						document.getElementById('name').innerHTML = name;
						
						//go through ppl user is following and diplay below the add event form
						//send user email to following servlet and get back html for list of users theyre following 
						$.ajax({
							url: "Following",
							data: {
								field: "userEmail",
								userEmail: email,
							},
							success: function(result){
								//get html and add to followinglist div 
								document.getElementById('followingList').innerHTML = result;
							}
						})
				    });
		    	});
		    }
		    
		    
		    
			function convertDateTime(date, time){
				//date and time value as they should be in format
				var dateAndTime = date + "T" + time + ":00-07:00";
				return dateAndTime;
			}
	 		//'2015-05-28T09:00:00-07:00' date time format
			//date value: YYYY-MM-DD
			//time val: hh:mm military
			function addEvent(){
				Title = document.getElementById('title').value;
				var sdate = document.getElementById('sDate').value;
				var stime = document.getElementById('sTime').value;
				sDateTime = convertDateTime(sdate, stime);
				var edate = document.getElementById('eDate').value;
				var etime = document.getElementById('eTime').value;
				eDateTime = convertDateTime(edate, etime);
				
				
				var event = {
						'summary': Title,
						'start': {
							'dateTime': sDateTime,
						    'timeZone': 'America/Los_Angeles'
						},
						'end': {
						    'dateTime': eDateTime,
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
						document.location.href = "home.jsp";
					}
					else {
						alert("Failed to add event.");
						document.location.href = "home.jsp";
					}
				});
			}
		
		function searching(){ //create overlay here?? https://www.w3schools.com/howto/howto_css_overlay.asp
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
	  		<p> Home</p>
	  		<div id ="outline"> 
				<img id="profPic" />
		  		<div id="name"></div>
			    <div id="block">
			    	<form name="eventForm" target="hiddenFrame">
			    		<input type="text" id="title" name="title" value="Event Title" onfocus="(this.value='')" required/>
			    		<div>
			    			<input type="text" id="sDate" name="sDate" placeholder="Start Date" onfocus="(this.type='date')" 
			    				required/>
			    			<input type="text" id="eDate" name="eDate" placeholder="End Date" onfocus="(this.type='date')"
			    				required/>
			    		</div>
			    		<div>
			    			<input type="text" id="sTime" name="sTime" placeholder="Start Time" onfocus="(this.type='time')"
			    				required/>
			    			<input type="text" id="eTime" name="eTime" placeholder="End Time" onfocus="(this.type='time')"
			    				required/>
			    		</div>
			    		<input type="submit" name="submit" value="Add Event" onclick="addEvent()"/>
			    	</form>
			    </div>
			    <div id="followingList"></div>
		    </div>
		    <iframe name="hiddenFrame" width="0" height="0" style="display: none;"></iframe>
	    </div>
	    <div id="bottom"></div>
	</body>
</html>