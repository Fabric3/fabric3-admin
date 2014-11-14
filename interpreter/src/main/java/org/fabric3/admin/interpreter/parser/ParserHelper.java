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
package org.fabric3.admin.interpreter.parser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.fabric3.admin.interpreter.Command;
import org.fabric3.admin.interpreter.ParseException;

public class ParserHelper {

    private ParserHelper() {
    }

    /**
     * Parses authorization parameters.
     *
     * @param command the command being parsed.
     * @param tokens  the input parameters in token form
     * @param index   the starting parameter index to parse
     * @throws ParseException if there is an error parsing the parameters
     */
    public static void parseAuthorization(Command command, String[] tokens, int index) throws ParseException {
        if ("-u".equals(tokens[index])) {
            command.setUsername(tokens[index + 1]);
        } else if ("-p".equals(tokens[index])) {
            command.setPassword(tokens[index + 1]);
        } else {
            throw new ParseException("Unrecognized parameter: " + tokens[index]);
        }
        if ("-u".equals(tokens[index + 2])) {
            command.setUsername(tokens[index + 3]);
        } else if ("-p".equals(tokens[index + 2])) {
            command.setPassword(tokens[index + 3]);
        } else {
            throw new ParseException("Unrecognized parameter: " + tokens[index + 2]);
        }

    }

    /**
     * Parses a URL input parameter.
     *
     * @param value the value to parse.
     * @return the URL
     * @throws MalformedURLException if the value is an invalid URL
     */
    public static URL parseUrl(String value) throws MalformedURLException {
        if (!value.contains(":/")) {
            // assume it is a file
            return new File(value).toURI().toURL();
        } else {
            return new URL(value);
        }
    }
}
