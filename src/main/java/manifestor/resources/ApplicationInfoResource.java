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

package manifestor.resources;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.InputStream;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import manifestor.util.ManifestUtil;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

/**
 * Uses {@link ManifestUtil} to construct application info.
 *
 * Usage example:
 * <code>
 *      final Package currentPackage = getClass().getPackage();
 *      logger.info("Current package implementationTitle: {}", currentPackage.getImplementationTitle());
 *
 *      If you are using Dropwizard, or similar framework.
 *      final ApplicationInfoResource appInfoResource = new ApplicationInfoResource(currentPackage.getImplementationTitle());
 *      environment.jersey().register(appInfoResource);
 *
 *      If your service is using Jersey load the resource by including this inside the WEB-INF/web.xml:
 *      <servlet>
 *          <servlet-name>jersey</servlet-name>
 *          <servlet-class>com.sun.jersey.spi.spring.container.servlet.SpringServlet</servlet-class>
 *          <init-param>
 *              <param-name>com.sun.jersey.config.property.packages</param-name>
 *              <param-value>manifestor</param-value>
 *          </init-param>
 *      </servlet>
 * </code>
 */
@Api(value = "Application Information")
@Path("/v1.0")
@Produces({APPLICATION_JSON})
public class ApplicationInfoResource {

    private final Optional<Map<String, String>> optionalManifest;

    /**
     * Required by Jersey.
     * Setup and load the resource using other constructor from Application context.
     */
    public ApplicationInfoResource() {
        final Map<String, String> emptyMap = Maps.newHashMap();
        this.optionalManifest = Optional.of(emptyMap);
    }

    public ApplicationInfoResource(final String implementationTitle) {
        this.optionalManifest = ManifestUtil.readManifestByImplementationTitle(implementationTitle);
    }

    @GET
    @Path("info")
    @Produces({APPLICATION_JSON})
    @ApiOperation(value = "Display application information from the manifest")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No manifest information could be found")})
    public Response getInfo() {
        if (optionalManifest.isPresent()) {
            return Response.ok(optionalManifest.get()).build();
        }

        return Response.status(Status.NOT_FOUND).build();
    }
}
