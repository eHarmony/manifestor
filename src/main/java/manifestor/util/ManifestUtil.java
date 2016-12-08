/*
 * Copyright 2016 eHarmony, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package manifestor.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;

/**
 * Utility that reads useful information about the artifact from
 * the manifest file that can be retrieved through {@link #getManifestDetailsByAttribute(String, String)}.
 */
public class ManifestUtil {

    private final static Logger logger = LoggerFactory.getLogger(ManifestUtil.class);

    public final static String ATTR_IMPLEMENTATION_TITLE = "Implementation-Title";

    public final static Optional<Map<String, String>> readManifestByImplementationTitle(final String implTitle) {
        final Optional<Map<String, String>> optionalManifest =
                ManifestUtil.getManifestDetailsByAttribute(ATTR_IMPLEMENTATION_TITLE, implTitle);

        return optionalManifest;
    }

    public final static Optional<Map<String, String>> readManifestFromInputStream(final InputStream inputStream) {
        try {
            final Map<String, String> manifestMap = ManifestUtil.load(inputStream);
            return Optional.of(manifestMap);
        } catch (IOException ioex) {
            logger.error("Failed to fetch manifest details", ioex);
        }

        return Optional.absent();
    }

    /**
     * Example would be to retrieve by Implementation-Title:
     * <code>
     * ManifestUtil.getManifestDetailsByAttribute("Implementation-Title", implTitle);
     * </code>
     *
     * In order not to hard code the impTitle inside a property, you can retrieve it using:
     *
     * <code>
     * final Package currentPackage = getClass().getPackage();
     * logger.info("Current package implementationTitle: {}", currentPackage.getImplementationTitle());
     * </code>
     */
    private static Optional<Map<String, String>> getManifestDetailsByAttribute(final String attributeName,
                                                                              final String attributeValue) {
        try {
            final Collection<InputStream> manifestStreams = ManifestUtil.fetch();

            for (final InputStream stream : manifestStreams) {
                try {
                    final Map<String, String> manifestMap = ManifestUtil.load(stream);
                    final String valueFromMap = manifestMap.get(attributeName);

                    if (valueFromMap != null && valueFromMap.trim().equals(attributeValue)) {
                        return Optional.of(manifestMap);
                    }
                } finally {
                    stream.close();
                }
            }
        } catch (final IOException ioex) {
            logger.error("Failed to fetch manifest details", ioex);
        }

        return Optional.absent();
    }

    private static Collection<InputStream> fetch() throws IOException {
        final Enumeration<URL> resources = Thread.currentThread()
                .getContextClassLoader()
                .getResources("META-INF/MANIFEST.MF");

        final Collection<URI> uris = new LinkedList<URI>();
        while (resources.hasMoreElements()) {
            try {
                uris.add(resources.nextElement().toURI());
            } catch (final URISyntaxException ex) {
                logger.error("Failed to get URI from the resource", ex);
            }
        }

        final Collection<InputStream> streams = new ArrayList<InputStream>(uris.size());
        for (final URI uri : uris) {
            logger.debug("Loaded manifest from {}", uri.toString());

            streams.add(uri.toURL().openStream());
        }

        return streams;
    }

    private static Map<String, String> load(final InputStream stream) throws IOException {
        final Map<String, String> props = new HashMap<String, String>();

        final Manifest manifest = new Manifest(stream);
        final Attributes attrs  = manifest.getMainAttributes();

        for (final Object key : attrs.keySet()) {
            final String value = attrs.getValue(
                Attributes.Name.class.cast(key)
            );

            props.put(key.toString(), value);
        }

        return props;
    }
}
