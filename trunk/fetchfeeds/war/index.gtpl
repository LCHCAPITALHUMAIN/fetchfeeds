<% 	if (user) {
		forward "/feedUserPanel.groovy" 
	} else {
		redirect users.createLoginURL("/") 
	}
%>

