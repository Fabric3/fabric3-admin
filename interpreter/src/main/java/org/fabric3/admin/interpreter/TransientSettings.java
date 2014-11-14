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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Retains settings in memory.
 */
public class TransientSettings implements Settings {
    private Map<String, DomainConfiguration> domains = new HashMap<>();

    public void addConfiguration(DomainConfiguration configuration) {
        domains.put(configuration.getName(), configuration);
    }

    public DomainConfiguration getDomainConfiguration(String name) {
        return domains.get(name);
    }

    public List<DomainConfiguration> getDomainConfigurations() {
        return new ArrayList<>(domains.values());
    }

    public void load() throws IOException {
        // no-op
    }

    public void save() throws IOException {
        // no-op
    }
}
