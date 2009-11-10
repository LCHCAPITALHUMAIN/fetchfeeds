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

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;


import static com.google.appengine.api.datastore.FetchOptions.Builder.*;


def securedSession = request.getSession(true);
def feedUser = securedSession['feedUser'];

if (!feedUser) {

	Query q = new Query("feedUser");
	q.addFilter("googleId", Query.FilterOperator.EQUAL, user.userId);
	def list = datastoreService.prepare(q).asList(withLimit(1));
	if (!list) {
		feedUser = new Entity("feedUser");
		feedUser.googleId = user.userId;
		feedUser.email = user.email;
		feedUser.save();



		xmppService.sendInvitation(new JID(feedUser.email));

		//TODO: Send an email message to welcome..
	} else {
		feedUser = list[0];
	}
}

q = new Query("feedSource", feedUser.key);
def feedSources = datastoreService.prepare(q).asList(withLimit(1000));
securedSession['feedSources'] = feedSources;
securedSession['feedUser'] = feedUser;


forward "panel.gtpl"

