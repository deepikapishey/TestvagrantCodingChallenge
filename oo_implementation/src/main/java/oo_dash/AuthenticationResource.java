package oo_dash;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
public class AuthenticationResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticate(UserCredentials credentials) {
        if ("dvs".equals(credentials.getUsername()) && "1nternUser$".equals(credentials.getPassword())) {
            return Response.ok("Authentication successful").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Authentication failed").build();
        }
    }
}
