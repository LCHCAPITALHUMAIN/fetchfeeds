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

import com.google.appengine.api.datastore.KeyFactory

def key = KeyFactory.stringToKey(request.getParameter('key'))

datastore.delete(key)

def feedSources = session['feedSources']

def remove = false
def i = 0;
while (!remove && i < feedSources.size()) {
	def temp = feedSources[i]

	if (temp.key == key) {
		feedSources.remove(i)
		remove = true
		session['feedSources'] = feedSources
	}

	i++
}

redirect "/"