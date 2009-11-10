<% include '/WEB-INF/includes/header.gtpl';
   import com.google.appengine.api.datastore.KeyFactory;
   import java.text.SimpleDateFormat;
%>

<h1>Your feeds sources</h1>
	<p>
<% if (session.getAttribute('error')) { %>
<div class="error">${session.getAttribute('error')}</div>
<% session.removeAttribute('error')
}

if (session.getAttribute('feedUser')?.feedSources?.size() == 0) { %>
<p><b>No feed source specified, add one using the box below !</b></p>
<% } else { %>
<table class="table">
  <thead class="thead">
  <tr><th>URL</th><th>Last update</th><th></th></tr>
  </thead>


  <% def trClass = "odd";
    def df = new SimpleDateFormat("dd/MM/yyyy");
    session.getAttribute('feedSources').each {
      def lastUpdate = it.lastUpdate ? df.format(it.lastUpdate) : "-"
      def keyString = KeyFactory.keyToString(it.key);
      trClass = (trClass == "even") ? "odd" : "even" %>
  <tr class="${trClass}"><td>
    <script language="JavaScript" src="http://lesindispensables.com/feed2js/feed2js.php?src=${it.url}&amp;chan=title&amp;num=2&amp;targ=ny&amp;utf=y" type="text/javascript"></script></td>

  </td>
    <td align="center">${lastUpdate}</td>
    <td align="center"><a href="/deleteFeedSource.groovy?key=${keyString}">Delete</a></td>
  </tr>
  <% } %>
</table>
<% } %>
</p>

<form method=POST ACTION="/addFeedSource.groovy">
  <label for="url">Feed url :</label>
  <input type="text" size="100" id="url" name="url"/>
  <input type="submit" value="add feed URL">
</form>

<% include '/WEB-INF/includes/footer.gtpl' %>