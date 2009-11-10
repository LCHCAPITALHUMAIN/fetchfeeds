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

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;


import static com.google.appengine.api.datastore.FetchOptions.Builder.*;

def userEntity = session.getAttribute("feedUser");

String url = params["url"].toLowerCase().trim();


def pattern = ~/^[h|H][t|T][t|T][p|P]:\/\/.*$/
if (!pattern.matcher(url).matches()) {
	session.setAttribute("error", "'${url}' n'est pas une URL http valide");
} else {

	def found = false;
	def feedSources = session['feedSources'];

	feedSources.each{
		if (url == it.url)
			found = true;
	}

	if (!found) {
		def source = new Entity("feedSource", userEntity.key);
		source.url = url;
		source.lastUpdate = null;
		source.save();
		feedSources.add(source);
		session['feedSources'] = feedSources;
	}
}

redirect "/"