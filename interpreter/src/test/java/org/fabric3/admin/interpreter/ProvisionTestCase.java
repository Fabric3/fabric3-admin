/*
 * Fabric3
 * Copyright (c) 2009-2015 Metaform Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fabric3.admin.interpreter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;

import junit.framework.TestCase;
import org.easymock.EasyMock;

import org.fabric3.admin.interpreter.command.HttpStatus;
import org.fabric3.admin.interpreter.communication.DomainConnection;
import org.fabric3.admin.interpreter.impl.InterpreterImpl;

/**
 *
 */
public class ProvisionTestCase extends TestCase {

    public void testProvisionWithName() throws Exception {
        DomainConnection domainConnection = EasyMock.createMock(DomainConnection.class);
        domainConnection.setUsername("username");
        domainConnection.setPassword("password");
        MockConnection connection = new MockConnection(HttpStatus.CREATED.getCode());
        EasyMock.expect(domainConnection.put(EasyMock.isA(String.class), EasyMock.isA(URL.class))).andReturn(connection);

        EasyMock.expect(domainConnection.createConnection(EasyMock.isA(String.class), EasyMock.eq("POST"))).andReturn(new MockConnection());

        EasyMock.replay(domainConnection);

        Interpreter interpreter = new InterpreterImpl(domainConnection);

        InputStream in = new ByteArrayInputStream("provision foo.jar -u username -p password \n quit".getBytes());
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        interpreter.processInteractive(in, out);

        EasyMock.verify(domainConnection);
    }


}