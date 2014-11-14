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
package org.fabric3.admin.interpreter.communication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The interface for performing domain administrative operations.
 */
public interface DomainConnection {

    /**
     * Sets the base admin address, clearing previous addresses on the stack.
     *
     * @param alias   the alias for the address
     * @param address the domain admin address
     */
    void setAddress(String alias, String address);

    /**
     * Push the address on the admin address stack and use it for management operations.
     *
     * @param alias   the alias for the address
     * @param address the address
     */
    void pushAddress(String alias, String address);

    /**
     * Remove that current admin address from the stack.
     *
     * @return the new current domain address
     */
    String popAddress();

    /**
     * Returns the current name (alias) of the runtime this connection is associated with.
     *
     * @return the current runtime name
     */
    String getAlias();

    /**
     * Returns the current address of the runtime this connection is associated with.
     *
     * @return the current runtime address
     */
    String getAddress();

    /**
     * Sets the username to authenticate with.
     *
     * @param username a valid domain admin username
     */
    void setUsername(String username);

    /**
     * Sets the password to authenticate with.
     *
     * @param password a valid domain admin password
     */
    void setPassword(String password);

    /**
     * Creates an HTTP(S) connection to a domain resource.
     *
     * @param path the relative resource path
     * @param verb the HTTP verb
     * @return the connection
     * @throws CommunicationException if there is a non-recoverable connection error
     */
    HttpURLConnection createControllerConnection(String path, String verb) throws CommunicationException;

    /**
     * Creates an HTTP(S) connection to a domain resource.
     *
     * @param path the relative resource path
     * @param verb the HTTP verb
     * @return the connection
     * @throws CommunicationException if there is a non-recoverable connection error
     */
    HttpURLConnection createConnection(String path, String verb) throws CommunicationException;

    /**
     * PUTs a resource.
     *
     * @param path     the relative resource path
     * @param resource the resource to PUT
     * @return the connection
     * @throws CommunicationException if there is a non-recoverable connection error
     */
    HttpURLConnection put(String path, URL resource) throws CommunicationException;

    /**
     * Parses a response stream.
     *
     * @param type   the class of the expected type
     * @param stream the response stream
     * @param <T>    the type
     * @return the parsed instance
     * @throws IOException if there is a parsing error
     */
    <T> T parse(Class<?> type, InputStream stream) throws IOException;

    void serialize(String message, OutputStream stream) throws IOException;


}
