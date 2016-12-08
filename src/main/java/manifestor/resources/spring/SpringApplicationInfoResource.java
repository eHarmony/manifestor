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

package manifestor.resources.spring;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import manifestor.resources.ApplicationInfoResource;
import manifestor.util.ManifestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import java.io.InputStream;
import java.util.Map;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * For Jersey 2 applications using spring for DI
 */
@Path("/v1.0")
@Produces({APPLICATION_JSON})
@Component
public class SpringApplicationInfoResource extends ApplicationInfoResource {

    @Context
    private ServletContext servletContext;

    @GET
    @Path("info")
    @Produces({APPLICATION_JSON})
    @ApiOperation(value = "Display application information from the manifest")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No manifest information could be found")})
    public Response getInfo() {
        Optional<Map<String, String>> manifest = getManifest();

        if (manifest.isPresent()) {
            return Response.ok(manifest.get()).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    public Optional<Map<String, String>> getManifest() {
        InputStream manifestStream = servletContext.getResourceAsStream("/META-INF/MANIFEST.MF");
        if (manifestStream == null) {
            return Optional.absent();
        }

        return ManifestUtil.readManifestFromInputStream(manifestStream);
    }
}
