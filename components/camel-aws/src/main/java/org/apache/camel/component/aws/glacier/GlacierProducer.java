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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.cloudfront.model.InvalidArgumentException;
import com.amazonaws.services.glacier.TreeHashGenerator;
import com.amazonaws.services.glacier.model.UploadArchiveRequest;
import com.amazonaws.services.glacier.model.UploadArchiveResult;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.StorageClass;
import com.amazonaws.services.s3.model.UploadPartRequest;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.WrappedFile;
import org.apache.camel.component.aws.s3.S3Constants;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.CastUtils;
import org.apache.camel.util.FileUtil;
import org.apache.camel.util.IOHelper;
import org.apache.camel.util.URISupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Producer which sends messages to the Amazon Web Service Simple Storage Service <a
 * href="http://aws.amazon.com/s3/">AWS S3</a>
 */
public class GlacierProducer extends DefaultProducer {

    private static final Logger LOG = LoggerFactory.getLogger(GlacierProducer.class);

    public GlacierProducer(final Endpoint endpoint) {
        super(endpoint);
    }


    @Override
    public void process(final Exchange exchange) throws Exception {
        if (getConfiguration().isMultiPartUpload()) {
            
        } else {
            processSingleOp(exchange);
        }
    }
    
    public void processSingleOp(final Exchange exchange) throws Exception {

    	 File file = new File(getConfiguration().getArchiveName());
    	 InputStream is = new FileInputStream(file);
    	 byte[] body = new byte[(int) file.length()];
    	 is.read(body);
    	 // Send request.
    	 UploadArchiveRequest request = new UploadArchiveRequest()
    	           .withVaultName(getConfiguration().getVaultName())
    	           .withChecksum(TreeHashGenerator.calculateTreeHash(new File(getConfiguration().getArchiveName())))
    	           .withBody(new ByteArrayInputStream(body))
    	           .withContentLength((long)body.length);
    	UploadArchiveResult uploadArchiveResult = getEndpoint().getGlacierClient().uploadArchive(request);
    	System.out.println("ArchiveID: " + uploadArchiveResult.getArchiveId());
    }

    protected GlacierConfiguration getConfiguration() {
        return getEndpoint().getConfiguration();
    }

    @Override
    public String toString() {
        return "GlacierProducer[" + URISupport.sanitizeUri(getEndpoint().getEndpointUri()) + "]";
    }

    @Override
    public GlacierEndpoint getEndpoint() {
        return (GlacierEndpoint) super.getEndpoint();
    }

}
