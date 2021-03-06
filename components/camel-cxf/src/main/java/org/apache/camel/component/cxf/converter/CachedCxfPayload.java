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
package org.apache.camel.component.cxf.converter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;

import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import org.apache.camel.Exchange;
import org.apache.camel.StreamCache;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.camel.converter.jaxp.XmlConverter;
import org.apache.camel.converter.stream.CachedOutputStream;
import org.apache.camel.converter.stream.StreamSourceCache;
import org.apache.cxf.staxutils.StaxSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CachedCxfPayload<T> extends CxfPayload<T> implements StreamCache {
    private static final Logger LOG = LoggerFactory.getLogger(CachedCxfPayload.class);
    private final XmlConverter xml;

    public CachedCxfPayload(CxfPayload<T> orig, Exchange exchange, XmlConverter xml) {
        super(orig.getHeaders(), new ArrayList<Source>(orig.getBodySources()), orig.getNsMap());
        ListIterator<Source> li = getBodySources().listIterator();
        this.xml = xml;
        while (li.hasNext()) {
            Source source = li.next();
            XMLStreamReader reader = null;
            // namespace definitions that are on the SOAP envelope can get lost, if this is 
            // not a DOM (there is special coding on the CXFPayload.getBody().get() method for 
            // this, that only works on DOM nodes.
            // We have to do some delegation on the XMLStreamReader for StAXSource and StaxSource
            // that re-injects the missing namespaces into the XMLStreamReader.
            // Replace all other Sources that are not DOMSources with DOMSources.
            if (source instanceof StaxSource) {
                reader = ((StaxSource) source).getXMLStreamReader();
            } else if (source instanceof StAXSource) {
                reader = ((StAXSource) source).getXMLStreamReader();
            }
            if (reader != null) {
                Map<String, String> nsmap = getNsMap();
                if (nsmap != null && !(reader instanceof DelegatingXMLStreamReader)) {
                    source = new StAXSource(new DelegatingXMLStreamReader(reader, nsmap));
                }
                CachedOutputStream cos = new CachedOutputStream(exchange);
                StreamResult sr = new StreamResult(cos);
                try {
                    xml.toResult(source, sr);
                    li.set(new StreamSourceCache(cos.newStreamCache()));
                } catch (TransformerException e) {
                    LOG.error("Transformation failed ", e);
                } catch (IOException e) {
                    LOG.error("Cannot Create StreamSourceCache ", e);
                }
            } else if (!(source instanceof DOMSource)) {
                Document document = exchange.getContext().getTypeConverter().convertTo(Document.class, exchange, source);
                if (document != null) {
                    li.set(new DOMSource(document));
                }
            }
        }
    }

    private CachedCxfPayload(CachedCxfPayload<T> orig, Exchange exchange) throws IOException {
        super(orig.getHeaders(), new ArrayList<Source>(orig.getBodySources()), orig.getNsMap());
        ListIterator<Source> li = getBodySources().listIterator();
        this.xml = orig.xml;
        while (li.hasNext()) {
            Source source = li.next();
            if (source instanceof StreamCache) {
                li.set((Source) (((StreamCache) source)).copy(exchange));
            }
        }
    }

    @Override
    public void reset() {
        for (Source source : getBodySources()) {
            if (source instanceof StreamCache) {
                ((StreamCache) source).reset();
            }
        }
    }

    @Override
    public void writeTo(OutputStream os) throws IOException {
        // no body no write
        if (getBodySources().size() == 0) {
            return;
        }
        Source body = getBodySources().get(0);
        if (body instanceof StreamCache) {
            ((StreamCache) body).writeTo(os);
        } else {
            StreamResult sr = new StreamResult(os);
            try {
                xml.toResult(body, sr);
            } catch (TransformerException e) {
                throw new IOException("Transformation failed", e);
            }
        }
    }

    @Override
    public boolean inMemory() {
        boolean inMemory = true;
        for (Source source : getBodySources()) {
            if (source instanceof StreamCache && !((StreamCache) source).inMemory()) {
                inMemory = false;
            }
        }
        return inMemory;
    }

    @Override
    public long length() {
        return 0;
    }

    @Override
    public StreamCache copy(Exchange exchange) throws IOException {
        return new CachedCxfPayload<T>(this, exchange);
    }
}
