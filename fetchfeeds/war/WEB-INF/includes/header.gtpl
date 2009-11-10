<html>
    <head>
    	<meta http-equiv="content-type" content="text/html; charset=utf-8">
        <title>FetchFeeds</title>
        <link rel="icon" href="/images/favicon.ico" type="image/png">
        <link rel="stylesheet" type="text/css" href="/css/main.css"/>
    </head>
    <body>
        <div align="center">
			<img class="logo"src="/images/gaelyk.png">    
            <img class="logo"src="/images/octo-technology.jpg">          
        </div>
        <div align="right">
    		<h2>Welcome ${session.getAttribute('feedUser').email}</h2>
        	<% logout = userService.createLogoutURL("/cleanSession.groovy") %>
			<p><a href="${logout}">Logout</a></p>
		</div>
		<div id="content" align="center">
          