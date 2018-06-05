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

import org.apache.camel.Consumer;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.google.mail.GoogleMailClientFactory;
import org.apache.camel.impl.ScheduledPollEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.support.SynchronizationAdapter;
import org.apache.camel.util.IOHelper;

import com.google.api.services.gmail.Gmail;

/**
 * The google-mail component provides access to Google Mail.
 */
@UriEndpoint(firstVersion = "2.22.0", scheme = "google-mail-stream", title = "Google Mail Stream", syntax = "google-mail-stream:", consumerClass = GoogleMailStreamConsumer.class, label = "api,cloud,mail")
public class GoogleMailStreamEndpoint extends ScheduledPollEndpoint {

	@UriParam
	private GoogleMailStreamConfiguration configuration;

	public GoogleMailStreamEndpoint(String uri, GoogleMailStreamComponent component,
			GoogleMailStreamConfiguration endpointConfiguration) {
		super(uri, component);
		this.configuration = endpointConfiguration;
	}

	@Override
	public Producer createProducer() throws Exception {
		throw new IllegalArgumentException("The camel google mail stream component doesn't support producer");
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		final GoogleMailStreamConsumer consumer = new GoogleMailStreamConsumer(this, processor);
		// also set consumer.* properties
		configureConsumer(consumer);
		return consumer;
	}

	public Gmail getClient() {
		return ((GoogleMailStreamComponent) getComponent()).getClient(configuration);
	}

	public GoogleMailClientFactory getClientFactory() {
		return ((GoogleMailStreamComponent) getComponent()).getClientFactory();
	}

	public void setClientFactory(GoogleMailClientFactory clientFactory) {
		((GoogleMailStreamComponent) getComponent()).setClientFactory(clientFactory);
	}

	public GoogleMailStreamConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
	
    public Exchange createExchange(ExchangePattern pattern, int size) {

        Exchange exchange = super.createExchange();
        Message message = exchange.getIn();
        message.setBody(size);

        return exchange;
    }
}
