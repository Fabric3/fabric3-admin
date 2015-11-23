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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.LinkedList;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JsonMapperConfigurator;

/**
 *
 */
public class DomainConnectionImpl implements DomainConnection {
    private static final String ADDRESS = "http://localhost:8180/management";
    private static final int TIMEOUT = 20000;
    private static final Annotations[] DEFAULT_ANNOTATIONS = {Annotations.JACKSON};
    private static final String KEY_STORE = "javax.net.ssl.keyStore";
    private static final String TRUST_STORE = "javax.net.ssl.trustStore";

    private LinkedList<String> aliases;
    private LinkedList<String> addresses;
    private String username;
    private String password;

    private int connectionTimeout;

    private ObjectMapper mapper;
    private SSLSocketFactory sslFactory;

    public DomainConnectionImpl() {
        JsonMapperConfigurator configurator = new JsonMapperConfigurator(null, DEFAULT_ANNOTATIONS);
        mapper = configurator.getDefaultMapper();
        aliases = new LinkedList<>();
        addresses = new LinkedList<>();
        aliases.add("default");
        addresses.add(ADDRESS);

        try {
            connectionTimeout = Integer.parseInt(System.getProperty("fabric3.timeout", "-1"));
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid connection timeout. Ignoring");
        }
    }

    public void setAddress(String alias, String address) {
        aliases.clear();
        addresses.clear();
        aliases.add(alias);
        addresses.add(address);
    }

    public void pushAddress(String alias, String address) {
        aliases.add(alias);
        addresses.add(address);
    }

    public String popAddress() {
        if (addresses.size() == 1) {
            return null;
        }
        aliases.removeLast();
        addresses.removeLast();
        return aliases.getLast();
    }

    public String getAlias() {
        return aliases.getLast();
    }

    public String getAddress() {
        return addresses.getLast();
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressWarnings({"unchecked"})
    public <T> T parse(Class<?> type, InputStream stream) throws IOException {
        JsonParser jp = mapper.getFactory().createParser(stream);
        return (T) mapper.readValue(jp, type);
    }

    public void serialize(String message, OutputStream stream) throws IOException {
        byte[] bytes = mapper.writeValueAsBytes(message);
        stream.write(bytes);
    }

    public HttpURLConnection createControllerConnection(String path, String verb) throws CommunicationException {
        return createAddressConnection(addresses.getFirst(), path, verb);
    }

    public HttpURLConnection createConnection(String path, String verb) throws CommunicationException {
        return createAddressConnection(addresses.getLast(), path, verb);
    }

    public HttpURLConnection put(String path, URL resource) throws CommunicationException {
        try {
            URL url = createUrl(addresses.getLast() + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connectionTimeout > 0) {
                connection.setConnectTimeout(connectionTimeout);
            }
            connection.setChunkedStreamingMode(4096);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-type", "application/json");
            setBasicAuth(connection);

            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                setSocketFactory(httpsConnection);
            }

            InputStream is = null;
            OutputStream os = null;
            try {
                os = connection.getOutputStream();
                is = resource.openStream();
                copy(is, os);
                os.flush();
            } finally {
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            }

            return connection;
        } catch (IOException e) {
            throw new CommunicationException(e);
        }
    }

    private HttpURLConnection createAddressConnection(String address, String path, String verb) throws CommunicationException {

        try {
            URL url = createUrl(address + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connectionTimeout > 0) {
                connection.setConnectTimeout(connectionTimeout);
            }
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod(verb);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setReadTimeout(TIMEOUT);

            setBasicAuth(connection);

            if (connection instanceof HttpsURLConnection) {
                HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
                setSocketFactory(httpsConnection);
            }

            return connection;
        } catch (IOException e) {
            throw new CommunicationException(e);
        }
    }

    private int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];
        int count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private void setBasicAuth(HttpURLConnection connection) {
        if (username != null) {
            String header = username + ":" + password;
            String encoded = Base64.encode(header.getBytes());
            connection.setRequestProperty("Authorization", "Basic " + encoded);
        }
    }

    private void setSocketFactory(HttpsURLConnection connection) throws CommunicationException {
        try {
            if (sslFactory == null) {
                // initialize the SSL context
                String keyStoreLocation = getKeystoreLocation();
                if (keyStoreLocation == null) {
                    throw new CommunicationException("Keystore not configured. A keystore must be placed in /config when using SSL.");
                }
                System.setProperty(KEY_STORE, keyStoreLocation);
                System.setProperty(TRUST_STORE, keyStoreLocation);
                KeyStore keyStore = KeyStore.getInstance("JKS");
                InputStream stream = new FileInputStream(keyStoreLocation);
                keyStore.load(stream, null);

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(keyStore);
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, tmf.getTrustManagers(), null);
                sslFactory = ctx.getSocketFactory();
            }
            connection.setSSLSocketFactory(sslFactory);
        } catch (NoSuchAlgorithmException | CertificateException | KeyManagementException | KeyStoreException | IOException e) {
            throw new CommunicationException(e);
        }
    }

    private String getKeystoreLocation() {
        File configDir = new File(getInstallDirectory(), "config");
        if (!configDir.exists() || !configDir.isDirectory()) {
            return null;
        }
        for (String file : configDir.list()) {
            if (file.endsWith(".jks") || file.endsWith(".keystore ")) {
                return new File(configDir, file).getAbsolutePath();
            }
        }
        return null;
    }

    /**
     * Gets the installation directory based on the location of a class file. The installation directory is calculated by determining the path of the jar
     * containing the given class file and returning its parent directory.
     *
     * @return directory where Fabric3 runtime is installed.
     * @throws IllegalStateException if the location could not be determined from the location of the class file
     */
    private File getInstallDirectory() throws IllegalStateException {
        // get the name of the Class's bytecode
        String name = getClass().getName();
        int last = name.lastIndexOf('.');
        if (last != -1) {
            name = name.substring(last + 1);
        }
        name = name + ".class";

        // get location of the bytecode - should be a jar: URL
        URL url = getClass().getResource(name);
        if (url == null) {
            throw new IllegalStateException("Unable to get location of bytecode resource " + name);
        }

        String jarLocation = url.toString();
        if (!jarLocation.startsWith("jar:")) {
            throw new IllegalStateException("Must be run from a jar: " + url);
        }

        // extract the location of thr jar from the resource URL
        jarLocation = jarLocation.substring(4, jarLocation.lastIndexOf("!/"));
        if (!jarLocation.startsWith("file:")) {
            throw new IllegalStateException("Must be run from a local filesystem: " + jarLocation);
        }

        File jarFile = new File(URI.create(jarLocation));
        return jarFile.getParentFile().getParentFile();
    }

    private URL createUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new AssertionError(e);
        }
    }

}
