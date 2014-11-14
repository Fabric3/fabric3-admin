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
package org.fabric3.admin.interpreter.command;

import java.net.URI;
import java.net.URL;

/**
 *
 */
public class CommandHelper {

    private CommandHelper() {
    }

    /**
     * Derives a contribution name from a URL by selecting the path part following the last '/'.
     *
     * @param contribution the contribution URL
     * @return the contribution name
     */
    public static URI parseContributionName(URL contribution) {
        String contributionName;
        String path = contribution.getPath();
        int pos = path.lastIndexOf('/');
        if (pos < 0) {
            contributionName = path;
        } else if (pos == path.length() - 1) {
            String substring = path.substring(0, pos);
            pos = substring.lastIndexOf('/');
            if (pos < 0) {
                contributionName = substring;
            } else {
                contributionName = path.substring(pos + 1, path.length() - 1);
            }
        } else {
            contributionName = path.substring(pos + 1);
        }
        return URI.create(contributionName);
    }

}
