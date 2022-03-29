package com.redhat.saiello;

import com.redhat.saiello.data.BlogPost;
import org.apache.cayenne.BaseContext;
import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.query.EJBQLQuery;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;
import java.util.stream.Collectors;

@Consumes("application/json")
@Produces("application/json")
@Path("/blog")
public class BlogResource {


    @GET
    @Path("/posts")
    public List getAllBlogPosts(){

        DataContext context = (DataContext) BaseContext.getThreadObjectContext();

        EJBQLQuery query = new EJBQLQuery("select p FROM BlogPost p");

        List<BlogPost> blogPosts = context.performQuery(query);

        return blogPosts.stream()
                .map(obj -> new BlogPostView(obj.getTitle(), obj.getDescription()))
                .collect(Collectors.toList());
    }


    public static class BlogPostView {
        public String title;
        public String description;

        public BlogPostView(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

}
