/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonbuilders;

import entity.User;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

/**
 *
 * @author Irina
 */
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
