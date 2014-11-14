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

import java.io.IOException;
import java.util.List;

/**
 * Encapsulates persistent settings for the admin interpreter.
 */
public interface Settings {

    /**
     * Adds a domain configuration to the collection of configured domains.
     *
     * @param configuration the domain configuration
     */
    void addConfiguration(DomainConfiguration configuration);

    /**
     * Returns the domain configuration.
     *
     * @param name the domain name
     * @return the domain admin address
     */
    DomainConfiguration getDomainConfiguration(String name);

    /**
     * Returns a list of all configured domains.
     *
     * @return the map of domains
     */
    List<DomainConfiguration> getDomainConfigurations();

    /**
     * Loads settings from persistent storage.
     *
     * @throws IOException if there is an error loading the settings
     */
    void load() throws IOException;

    /**
     * Persists settings.
     *
     * @throws IOException if there is an error persisting the settings
     */
    void save() throws IOException;
}
