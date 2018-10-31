<!-- Adelayde Rome (7675884868) -->
<!-- Sign in page -->
<!DOCTYPE html>
<html lang="en">
  <head>
  	<title>Sign In</title>
  	<link rel="stylesheet" type="text/css" href="signin.css" />
    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id" content="222490153320-7ebr2u3avv592pnlc4ke57f8tjifh4g8.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
  </head>
  <body>
  	<div id="top"> </div>
  	<div id="main">
  		
  		<h1><img id="image" src="leaf.png" />Sycamore Calendar</h1>
  		
  	
  		
  		<div class="g-signin2" data-onsuccess="onSignIn"></div>
	    
	    <script>
			
	      	function onSignIn(googleUser) {
	        	// Useful data for your client-side scripts:
	        	if(googleUser.hasGrantedScopes('https://www.googleapis.com/auth/calendar')){
    				document.location.href = "profile.jsp";
				}
	        	else{
	        		googleUser.grant({'scope':'https://www.googleapis.com/auth/calendar'}).then(function(){document.location.href = "profile.jsp";},function(){document.location.reload();});
				}
	      };
	      
    	</script>
    </div>
    <div id="bottom"></div>
  </body>
</html>