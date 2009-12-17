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
import com.google.appengine.api.datastore.Key
import com.google.appengine.api.datastore.KeyFactory
import com.google.appengine.api.datastore.Query
import com.google.appengine.api.labs.taskqueue.TaskOptions

import static com.google.appengine.api.datastore.FetchOptions.Builder.*


def q = new Query("feedUser")

def feedUsers = datastore.prepare(q).asList(withLimit(10000))

feedUsers.each {
	def email = it.email

	q = new Query("feedSource", it.key)
	def feedSources = datastore.prepare(q).asList(withLimit(1000))

	feedSources.each {
		defaultQueue <<	[
		                 url: "/fetchFeeds.groovy",
		                 params: [email: email, feedSourceKey: KeyFactory.keyToString(it.key)]
		                 ]
	}
	
	defaultQueue <<	[ url: "/notify.groovy", params: [email: email] ]
}