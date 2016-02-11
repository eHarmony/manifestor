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

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * Tests {@link manifestor.util.ManifestUtil}.
 */
public class ManifestUtilTest {

    @Test
    public void readManifestByImplementationTitle() {
        // GIVEN
        final String implementationTitle = "Manifestor";

        // WHEN
        final Optional<Map<String, String>> optionalManifest  = ManifestUtil.readManifestByImplementationTitle(implementationTitle);

        // THEN
        assertEquals(true, optionalManifest.isPresent());
    }
}
