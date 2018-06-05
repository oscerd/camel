/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.mail.stream;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledBatchPollingConsumer;
import org.apache.camel.util.CastUtils;
import org.apache.camel.util.ObjectHelper;

import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.ListLabelsResponse;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.ModifyMessageRequest;

/**
 * The GoogleMail consumer.
 */
public class GoogleMailStreamConsumer extends ScheduledBatchPollingConsumer {

	public GoogleMailStreamConsumer(Endpoint endpoint, Processor processor) {
		super(endpoint, processor);
	}

	protected GoogleMailStreamConfiguration getConfiguration() {
		return getEndpoint().getConfiguration();
	}

	protected Gmail getClient() {
		return getEndpoint().getClient();
	}

	@Override
	public GoogleMailStreamEndpoint getEndpoint() {
		return (GoogleMailStreamEndpoint) super.getEndpoint();
	}

	@Override
	protected int poll() throws Exception {
		ListMessagesResponse c = getClient().users().messages().list("me").setQ("is:unread").execute();
		Message mess = getClient().users().messages().get("me", c.getMessages().get(0).getId()).setFormat("FULL").execute();
        byte[] bodyBytes = Base64.decodeBase64(mess.getPayload().getParts().get(0).getBody().getData().trim().toString()); // get body
        String body = new String(bodyBytes, "UTF-8");
        List<String> labels = new ArrayList<String>();
        String unreadId = null;
        ListLabelsResponse listResponse = getClient().users().labels().list("me").execute();
        for (Label label : listResponse.getLabels()) {
            Label countLabel =  getClient().users().labels().get("me", label.getId()).execute();
            if (countLabel.getName().equalsIgnoreCase("UNREAD"))
                unreadId = countLabel.getId();
        }
        List<String> remove = new ArrayList<String>();
        remove.add(unreadId);
        ModifyMessageRequest mods = new ModifyMessageRequest().setRemoveLabelIds(remove);
        Message mod = getClient().users().messages().modify("me", c.getMessages().get(0).getId(), mods).execute();
		System.err.println(mod.getLabelIds().toString());
        Exchange exchange = ((GoogleMailStreamEndpoint)getEndpoint()).createExchange(getEndpoint().getExchangePattern(), c.getMessages().size());
		Queue<Exchange> answer = new LinkedList<>();
		answer.add(exchange);
		return processBatch(CastUtils.cast(answer));
	}

	@Override
	public int processBatch(Queue<Object> exchanges) throws Exception {
        int total = exchanges.size();

        for (int index = 0; index < total && isBatchAllowed(); index++) {
            // only loop if we are started (allowed to run)
            final Exchange exchange = ObjectHelper.cast(Exchange.class, exchanges.poll());
            // add current index and total as properties
            exchange.setProperty(Exchange.BATCH_INDEX, index);
            exchange.setProperty(Exchange.BATCH_SIZE, total);
            exchange.setProperty(Exchange.BATCH_COMPLETE, index == total - 1);

            // update pending number of exchanges
            pendingExchanges = total - index - 1;

            getAsyncProcessor().process(exchange, new AsyncCallback() {
                @Override
                public void done(boolean doneSync) {
                    System.err.println("Processing exchange done");
                }
            });
        }

        return total;
	}

}
