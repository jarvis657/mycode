package origin.spring;

import origin.spring.ioc.test.beans.User;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @Author:lmq
 * @Date: 2020/8/25
 * @Desc:
 **/
public class JerseyController {

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User hello(@PathParam("id") Long id) {
        User user = new User();
        return user;
    }
}
