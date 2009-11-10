<% 	if (user) {
		forward "/feedUserPanel.groovy" 
	} else {
		redirect userService.createLoginURL("/") 
	}
%>

