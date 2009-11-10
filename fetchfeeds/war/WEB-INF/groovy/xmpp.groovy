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


import com.google.appengine.api.xmpp.MessageBuilder
import com.google.appengine.api.xmpp.Message
import com.google.appengine.api.xmpp.SendResponse;
import com.google.appengine.api.xmpp.JID



String msgBody = 'I\'m a stupid echo bot yet :  "' + message.body + '"';
Message msg = new MessageBuilder().withRecipientJids(new JID(message.from)).withBody(msgBody).build();

if (xmppService.getPresence(message.from).isAvailable()) {
  SendResponse status = xmppService.sendMessage(msg);
}


