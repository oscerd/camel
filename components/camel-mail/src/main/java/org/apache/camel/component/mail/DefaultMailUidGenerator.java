/*
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
package org.apache.camel.component.mail;

import java.util.Enumeration;
import java.util.UUID;

import jakarta.mail.Folder;
import jakarta.mail.Header;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.UIDFolder;

import org.apache.camel.util.ObjectHelper;
import org.eclipse.angus.mail.pop3.POP3Folder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMailUidGenerator implements MailUidGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMailUidGenerator.class);

    @Override
    public String generateUuid(MailEndpoint mailEndpoint, Message message) {
        // Prefer the identifier the server assigns. Message-ID is chosen by the sending client and is not
        // required to be unique in practice, so two messages can share one - and this uuid keys idempotent
        // consumer state and, for POP3, selects which message the commit deletes.
        String answer = generateProtocolUid(message);
        if (answer == null) {
            answer = generateMessageIdHeader(message);
        }
        if (answer == null) {
            answer = generateMessageHash(message);
        }
        // fallback and use message number
        if (answer == null || ObjectHelper.isEmpty(answer)) {
            answer = Integer.toString(message.getMessageNumber());
        }
        return answer;
    }

    /**
     * Returns the identifier the mail server assigns to the message - the POP3 UIDL or the IMAP folder UID - or null
     * when the folder does not provide one. Unlike Message-ID this is not under the sender's control and is stable
     * across polls.
     */
    private String generateProtocolUid(Message message) {
        Folder folder = message.getFolder();
        if (folder == null) {
            return null;
        }
        try {
            if (folder instanceof POP3Folder pop3Folder) {
                String uid = pop3Folder.getUID(message);
                // prefixed so a server-assigned uid can never collide with a sender-supplied Message-ID
                return ObjectHelper.isNotEmpty(uid) ? "pop3-" + uid : null;
            }
            if (folder instanceof UIDFolder uidFolder) {
                return "uid-" + uidFolder.getUID(message);
            }
        } catch (MessagingException e) {
            LOG.debug("Cannot read the server assigned uid from the folder. Falling back to the message headers.", e);
        }
        return null;
    }

    private String generateMessageIdHeader(Message message) {
        LOG.trace("generateMessageIdHeader for msg: {}", message);

        // there should be a Message-ID header with the UID
        try {
            String[] values = message.getHeader("Message-ID");
            if (values != null && values.length > 0) {
                String uid = values[0];
                LOG.trace("Message-ID header found: {}", uid);
                return uid;
            }
        } catch (MessagingException e) {
            LOG.warn("Cannot read headers from mail message. This exception will be ignored.", e);
        }

        return null;
    }

    public String generateMessageHash(Message message) {
        LOG.trace("generateMessageHash for msg: {}", message);

        String uid = null;

        // create an UID based on message headers on the message, that ought to be unique
        StringBuilder buffer = new StringBuilder();
        try {
            Enumeration<?> it = message.getAllHeaders();
            while (it.hasMoreElements()) {
                Header header = (Header) it.nextElement();
                buffer.append(header.getName()).append("=").append(header.getValue()).append("\n");
            }
            if (buffer.length() > 0) {
                LOG.trace("Generating UID from the following:\n {}", buffer);
                uid = UUID.nameUUIDFromBytes(buffer.toString().getBytes()).toString();
            }
        } catch (MessagingException e) {
            LOG.warn("Cannot read headers from mail message. This exception will be ignored.", e);
        }

        return uid;
    }
}
