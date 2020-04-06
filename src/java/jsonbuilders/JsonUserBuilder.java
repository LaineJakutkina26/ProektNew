
package jsonbuilders;

import entity.User;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;


public class JsonUserBuilder {

    public JsonObject createJsonUserObject(User user) {
        JsonPersonBuilder jsonPersonBuilder = new JsonPersonBuilder();
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", user.getId())
                .add("login", user.getLogin())
                .add("active",user.isActive())
                .add("person", jsonPersonBuilder.createJsonPersonObject(user.getPerson()));
        return job.build();
    }
    
}
