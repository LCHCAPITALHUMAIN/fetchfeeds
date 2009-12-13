/*
 * Copyright (c) 2009  OCTO Technology www.octo.com
 *
 * This file is part of FetchFeeds.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.octo.fetchfeeds.beans.FeedItem;
import com.google.appengine.api.urlfetch.HTTPRequest
import com.google.appengine.api.urlfetch.HTTPResponse
import com.sun.syndication.io.SyndFeedInput
import com.sun.syndication.feed.synd.SyndFeed;

def email = request.getParameter('email');
def feedSourceKey = KeyFactory.stringToKey(request.getParameter('feedSourceKey'));

def feedSource = datastore.get(feedSourceKey);
def lastUpdate = null;

def feedItems = getNewFeedEntries(feedSource.url, feedSource.lastUpdate);

if (feedItems.size() > 0) {
  def userMap;

  if (email in memcache) {
    userMap = memcache[email];
  } else {
    userMap = new HashMap<String, List<FeedItem>>();
  }

  userMap.put(feedItems[0].getSource(), feedItems);

  memcache[email] = userMap;

  //update lastUpdate
  feedSource.lastUpdate = getLastDate(feedItems);
  feedSource.save();
}

private Date getLastDate(List<FeedItem> feedItems) {
  Date result = feedItems[0].publishedDate;

  feedItems.each {
    if (it.publishedDate.after(result))
      result = it.publishedDate;
  }

  return result;
}


public List<FeedItem> getNewFeedEntries(String url, Date lastFeedDate) {

  HTTPRequest request = new HTTPRequest(new URL(url));
  HTTPResponse response = urlFetch.fetch(request);

  SyndFeedInput input = new SyndFeedInput();
  SyndFeed feed = input.build(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.getContent()), "UTF-8")));

  List<FeedItem> result = new ArrayList<FeedItem>();

  String source = feed.getTitle();

  feed.getEntries().each {
    Date publishedDate = it.getPublishedDate();

    if (publishedDate == null || lastFeedDate == null || publishedDate > lastFeedDate) {
      result.add(new FeedItem(title: it.getTitle(), source: source, link: it.getLink(), publishedDate: publishedDate))
    }
  };

  return result;

}
