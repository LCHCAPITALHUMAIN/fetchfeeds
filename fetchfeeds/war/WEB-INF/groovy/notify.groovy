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
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.octo.fetchfeeds.beans.FeedItem;

def email = request.getParameter('email');

def userMap = memcacheService.get(email);

//cas où il n'y a pas de nouveaux feeds
if (userMap == null || userMap.isEmpty()) {
  //Do nothing...
  //notifyXmpp(email, "No new feed found");
} else {
  	sendMail(email, userMap)
	notifyXmpp(email, "New feed fetched and sent by email.")
}

memcacheService.delete(email);

private void notifyXmpp(String to, String chat) {
	JID jid = new JID(to);
	if (xmppService.getPresence(jid).isAvailable()) {
		com.google.appengine.api.xmpp.Message msg = new MessageBuilder().withRecipientJids(jid).withBody(chat).build();
		xmppService.sendMessage(msg);
	}
}

private void sendMail(String to, Map<String, List<FeedItem>> userMap) throws IOException {
	Message message = new Message();
	message.setTo(to);
     //TODO : use some developper account of your google app here
	message.setSender("fetchfeedsdemo@gmail.com");
	message.setSubject("You have new feeds !");

	StringBuilder sb = new StringBuilder();

	def df = new SimpleDateFormat("dd/MM/yyyy");

	userMap.entrySet().each {
		sb.append("<p><h3>" + it.getKey() + "<i>  (" + it.getValue().size() + ")</i></h3>");
		for (FeedItem item : it.getValue()) {
			sb.append("<a href=\"" + item.getLink() + "\">" + item.getTitle() + "</a>");
			if (item.getPublishedDate() != null)
				sb.append("<i> - " + df.format(item.getPublishedDate()) + "</i>");
			sb.append("<br />");
		}
		sb.append("</p>");
	}
	sb.append("<p><i>Sent by \"<b><a href='http://fetchfeeds.appspot.com'>Fetch-Feeds</a></b>\"</i></p>");
	message.setHtmlBody(sb.toString());
	mailService.send(message);
}