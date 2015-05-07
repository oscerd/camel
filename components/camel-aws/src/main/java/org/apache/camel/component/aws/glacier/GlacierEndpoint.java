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
package org.apache.camel.component.aws.glacier;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.aws.s3.S3Configuration;
import org.apache.camel.component.aws.s3.S3Consumer;
import org.apache.camel.impl.ScheduledPollEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.util.ObjectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.glacier.AmazonGlacierClient;
import com.amazonaws.services.glacier.model.ListVaultsRequest;
import com.amazonaws.services.glacier.model.ListVaultsResult;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;


/**
 * Defines the <a href="http://camel.apache.org/aws.html">AWS Glacier Endpoint</a>.  
 */
@UriEndpoint(scheme = "aws-glacier", title = "AWS Glacier Storage Service", syntax = "aws-glacier:bucketName", consumerClass = S3Consumer.class, label = "cloud,file")
public class GlacierEndpoint extends ScheduledPollEndpoint {

    private static final Logger LOG = LoggerFactory.getLogger(GlacierEndpoint.class);

    private AmazonGlacierClient glacierClient;
    
    @UriParam
    private GlacierConfiguration configuration;
    @UriParam(defaultValue = "10")
    private int maxMessagesPerPoll = 10;

    public GlacierEndpoint(String uri, Component comp, GlacierConfiguration configuration) {
        super(uri, comp);
        this.configuration = configuration;
    }
    
	@Override
	public Producer createProducer() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Consumer createConsumer(Processor processor) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleton() {
        return true;
	}
	
    public GlacierConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(GlacierConfiguration configuration) {
        this.configuration = configuration;
    }
    
    public void setGlacierClient(AmazonGlacierClient glacierClient) {
        this.glacierClient = glacierClient;
    }
    
    public AmazonGlacierClient getGlacierClient() {
        return glacierClient;
    }
    
    /**
     * Provide the possibility to override this method for an mock implementation
     *
     * @return AmazonGlacierClient
     */
	AmazonGlacierClient createGlacierClient() {
        AWSCredentials credentials = new BasicAWSCredentials(configuration.getAccessKey(), configuration.getSecretKey());
        AmazonGlacierClient client = new AmazonGlacierClient(credentials);
        return client;
    }

    public int getMaxMessagesPerPoll() {
        return maxMessagesPerPoll;
    }

    public void setMaxMessagesPerPoll(int maxMessagesPerPoll) {
        this.maxMessagesPerPoll = maxMessagesPerPoll;
    }
}
