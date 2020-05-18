/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonbuilders;

import entity.Book;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class JsonBookBuilder {
    public JsonObject cerateJsonBookObject(Book book){
        JsonObjectBuilder job = Json.createObjectBuilder();
         job.add("id",book.getId())
                .add("name", book.getName())
                .add("author", book.getAuthor())
                .add("publishedYear",book.getPublishedYear())
                .add("coverUrl", book.getCoverUrl())
                .add("price",book.getPrice())
                .add("dateAdded",book.getDateAdded().toString())
                .add("active", book.isActive())
                .add("textBook", book.getTextBookFull());
         return job.build();
    }
    public JsonArray createJsonListBooks(List<Book> listBooks){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for(Book book : listBooks){
            jab.add(this.cerateJsonBookObject(book));
        }
        return jab.build();
    }
   
}