<!-- Adelayde Rome (7675884868)-->
<!-- Logged in home page; directed to through sycamore calendar link in top left -->

<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="signin.css" />
		<meta charset="UTF-8">
		<meta name="google-signin-scope" content="profile email">
    	<meta name="google-signin-client_id" content="222490153320-7ebr2u3avv592pnlc4ke57f8tjifh4g8.apps.googleusercontent.com">
    	<script src="https://apis.google.com/js/platform.js" async defer></script>
    	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
		<title>home</title>
		<script>

			var CLIENT_ID = "222490153320-7ebr2u3avv592pnlc4ke57f8tjifh4g8";
			var API_KEY = "AIzaSyAmYD-vI414e5ILOqM3_1TOrr3-LioSO_Y";
			
			var DISCOVERY_DOCS = ["https://www.googleapis.com/discovery/v1/apis/calendar/v3/rest"];
			var SCOPES = "https://www.googleapis.com/auth/calendar";
			var email;
			
			//taken from https://stackoverflow.com/questions/12909332/how-to-logout-of-an-application-where-i-used-oauth2-to-login-with-google
			var logout = function() {
			    document.location.href = "https://www.google.com/accounts/Logout?continue=https://appengine.google.com/_ah/logout?continue=http://localhost:8080/CSCI201_Assignment3/signin.jsp";
			}
			
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
						email = profile.getEmail();
			    	});
		    	});
		    }
		    
			function searching(){
				//need to access current user email to not add that one to search
				document.getElementById("searchPage").style.display = "block";
 		    	document.getElementById("main").style.display = "none";
 		    	//displaying current user
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
	<body>
	  	<div id="top"> 
	  		<div id="topright">
	  			<a href="home.jsp"> Home</a>
	  			<a href="profile.jsp"> Profile </a>
	  		</div>
	  		<div id="search">
	  			<input id="searchInput" type="text" placeholder="Search Friends"  onclick="searching()" onkeyup="searching()"/> 
	  			<div id="searchPage">
		  			<ul>
		  				<div id ="searchUsers"> </div>
		  			</ul>
	  			</div>
	  		</div>
	  	</div>
	  	<div id="main">
	  		
	  		
	  		<h1><img id="image" src="leaf.png" />Sycamore Calendar</h1>
	  		
	  		<!-- need to sign out user (and then direct to sign in pg) when click this button!! -->
	  		<button id="button" class="g-signin2" onclick="logout()"></button>
		    
	    </div>
	    <div id="bottom"></div>
	</body>
</html>