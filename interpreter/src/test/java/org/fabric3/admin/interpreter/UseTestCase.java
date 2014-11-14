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
public class UseTestCase extends TestCase {
    private static final String DOMAIN_ADDRESS = "http://localhost:8180/management/domain";

    public void testUse() throws Exception {
        DomainConnection domainConnection = EasyMock.createMock(DomainConnection.class);
        domainConnection.setAddress("MyDomain", DOMAIN_ADDRESS);
        EasyMock.replay(domainConnection);

        Settings settings = new TransientSettings();
        DomainConfiguration configuration = new DomainConfiguration("MyDomain", DOMAIN_ADDRESS, null, null);
        settings.addConfiguration(configuration);
        Interpreter interpreter = new InterpreterImpl(domainConnection, settings);

        InputStream in = new ByteArrayInputStream("use MyDomain \n quit".getBytes());
        PrintStream out = new PrintStream(new ByteArrayOutputStream());
        interpreter.processInteractive(in, out);

        EasyMock.verify(domainConnection);
    }

}