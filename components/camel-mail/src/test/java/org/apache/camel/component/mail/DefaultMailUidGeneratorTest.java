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

import jakarta.mail.Message;

import org.eclipse.angus.mail.pop3.POP3Folder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

/**
 * The uuid keys idempotent consumer state and, for POP3, selects which message the commit deletes. Message-ID is chosen
 * by the sending client and is not required to be unique in practice, so it must not be the sole source.
 */
class DefaultMailUidGeneratorTest {

    private static final String SHARED_MESSAGE_ID = "<duplicate@sender.example>";

    private final DefaultMailUidGenerator generator = new DefaultMailUidGenerator();

    @Test
    void messagesSharingAMessageIdGetDistinctUids() throws Exception {
        POP3Folder folder = Mockito.mock(POP3Folder.class);

        Message first = pop3Message(folder, SHARED_MESSAGE_ID);
        Message second = pop3Message(folder, SHARED_MESSAGE_ID);
        when(folder.getUID(first)).thenReturn("0001");
        when(folder.getUID(second)).thenReturn("0002");

        assertNotEquals(generator.generateUuid(null, first), generator.generateUuid(null, second),
                "two messages the server distinguishes must not collapse to one uuid");
    }

    @Test
    void theServerAssignedUidIsStableAcrossCalls() throws Exception {
        POP3Folder folder = Mockito.mock(POP3Folder.class);
        Message message = pop3Message(folder, SHARED_MESSAGE_ID);
        when(folder.getUID(message)).thenReturn("0001");

        // the POP3 commit path re-resolves the uuid when it rescans the folder, so it has to be repeatable
        assertEquals(generator.generateUuid(null, message), generator.generateUuid(null, message));
    }

    @Test
    void fallsBackToTheMessageIdWhenTheFolderAssignsNoUid() throws Exception {
        Message message = Mockito.mock(Message.class);
        when(message.getFolder()).thenReturn(null);
        when(message.getHeader("Message-ID")).thenReturn(new String[] { SHARED_MESSAGE_ID });

        assertEquals(SHARED_MESSAGE_ID, generator.generateUuid(null, message));
    }

    private static Message pop3Message(POP3Folder folder, String messageId) throws Exception {
        Message message = Mockito.mock(Message.class);
        when(message.getFolder()).thenReturn(folder);
        when(message.getHeader("Message-ID")).thenReturn(new String[] { messageId });
        return message;
    }
}
