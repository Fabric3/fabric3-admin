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

import junit.framework.TestCase;
import org.easymock.EasyMock;

import org.fabric3.admin.interpreter.communication.DomainConnection;
import org.fabric3.admin.interpreter.impl.InterpreterImpl;

/**
 *
 */
public class UndeployTestCase extends TestCase {

    public void testDeployWithName() throws Exception {
        DomainConnection domainConnection = EasyMock.createMock(DomainConnection.class);
        domainConnection.setUsername("username");
        domainConnection.setPassword("password");
        EasyMock.expect(domainConnection.createConnection(EasyMock.isA(String.class), EasyMock.eq("DELETE"))).andReturn(new MockConnection());
        EasyMock.replay(domainConnection);

        Interpreter interpreter = new InterpreterImpl(domainConnection);

        InputStream in = new ByteArrayInputStream("undeploy foo.jar -u username -p password \n quit".getBytes());
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        interpreter.processInteractive(in, out);

        EasyMock.verify(domainConnection);
    }

    public void testDeployWithNameAndForce() throws Exception {
        DomainConnection domainConnection = EasyMock.createMock(DomainConnection.class);
        domainConnection.setUsername("username");
        domainConnection.setPassword("password");
        EasyMock.expect(domainConnection.createConnection(EasyMock.isA(String.class), EasyMock.eq("DELETE"))).andReturn(new MockConnection());
        EasyMock.replay(domainConnection);

        Interpreter interpreter = new InterpreterImpl(domainConnection);

        InputStream in = new ByteArrayInputStream("undeploy foo.jar -force -u username -p password \n quit".getBytes());
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        interpreter.processInteractive(in, out);

        EasyMock.verify(domainConnection);
    }

    public void testDeployWithNameAndForceNoSecurity() throws Exception {
        DomainConnection domainConnection = EasyMock.createMock(DomainConnection.class);
        EasyMock.expect(domainConnection.createConnection(EasyMock.isA(String.class), EasyMock.eq("DELETE"))).andReturn(new MockConnection());
        EasyMock.replay(domainConnection);

        Interpreter interpreter = new InterpreterImpl(domainConnection);

        InputStream in = new ByteArrayInputStream("undeploy foo.jar -force \n quit".getBytes());
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        interpreter.processInteractive(in, out);

        EasyMock.verify(domainConnection);
    }

}