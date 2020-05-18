/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Book;
import entity.Person;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jsonbuilders.JsonBookBuilder;
import jsonbuilders.JsonUserBuilder;
import session.BookFacade;
import session.PersonFacade;
import session.UserFacade;
import util.EncryptPass;

/**
 *
 * @author JVM
 */
@WebServlet(name = "LoginController", urlPatterns = {
    "/createUser",
    "/login",
    "/logout",
    "/listBooks",})
public class LoginController extends HttpServlet {

    @EJB
    private PersonFacade personFacade;
    @EJB
    private UserFacade userFacade;
    @EJB
    private BookFacade bookFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String json = "";
        JsonObjectBuilder job = Json.createObjectBuilder();
        EncryptPass ep = new EncryptPass();
        String path = request.getServletPath();
        switch (path) {
            case "/createUser":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                String firstname = jsonObject.getString("firstname");
                String lastname = jsonObject.getString("lastname");
                String email = jsonObject.getString("email");
                String city = jsonObject.getString("city");
                String street = jsonObject.getString("street");
                String house = jsonObject.getString("house");
                String room = jsonObject.getString("room");
                String money = jsonObject.getString("money");
                String login = jsonObject.getString("login");
                String password = jsonObject.getString("password");

                if (null == firstname || "".equals(firstname)
                        || null == firstname || "".equals(firstname)
                        || null == lastname || "".equals(lastname)
                        || null == email || "".equals(email)
                        || null == money || "".equals(money)
                        || null == city || "".equals(city)
                        || null == street || "".equals(street)
                        || null == house || "".equals(house)
                        || null == room || "".equals(room)
                        || null == login || "".equals(login)
                        || null == password || "".equals(password)) {

                    job.add("actionStatus", "false")
                            .add("user", "null")
                            .add("authStatus", "false")
                            .add("data", "null");
                    try (Writer writer = new StringWriter()) {
                        Json.createWriter(writer).write(job.build());
                        json = writer.toString();
                    }
                    break;
                }

                Person person = null;
                User user = null;
                try {
                    person = new Person(firstname, lastname, email, city, street, house, room, Integer.parseInt(money));
                    personFacade.create(person);
                    String salts = ep.createSalts();
                    password = ep.setEncriptPass(password, salts);
                    user = new User(login, password, salts, true, person);
                    userFacade.create(user);
                } catch (Exception e) {

                    if (person != null && person.getId() != null) {
                        personFacade.remove(person);
                    }
                    if (user != null && user.getId() != null) {
                        userFacade.remove(user);
                    }
                    job.add("actionStatus", "false")
                            .add("user", "null")
                            .add("authStatus", "false")
                            .add("data", "null");
                    try (Writer writer = new StringWriter()) {
                        Json.createWriter(writer).write(job.build());
                        json = writer.toString();
                    }
                    break;
                }

                job.add("actionStatus", "true")
                        .add("user", "null")
                        .add("authStatus", "false")
                        .add("data", "null");
                try (Writer writer = new StringWriter()) {
                    Json.createWriter(writer).write(job.build());
                    json = writer.toString();
                }
                break;
            case "/login":
                jsonReader = Json.createReader(request.getReader());
                jsonObject = jsonReader.readObject();
                login = jsonObject.getString("login");
                password = jsonObject.getString("password");
                if (null == login || "".equals(login)
                        || null == password || "".equals(password)) {

                    job.add("actionStatus", "false")
                            .add("user", "null")
                            .add("authStatus", "false")
                            .add("data", "null");
                    try (Writer writer = new StringWriter()) {
                        Json.createWriter(writer).write(job.build());
                        json = writer.toString();
                    }
                    break;
                }
                user = userFacade.findByLogin(login);
                if (user == null) {
                    job.add("actionStatus", "false")
                            .add("user", "null")
                            .add("authStatus", "false")
                            .add("data", "null");
                    try (Writer writer = new StringWriter()) {
                        Json.createWriter(writer).write(job.build());
                        json = writer.toString();
                    }
                    break;
                }
                password = ep.setEncriptPass(password, user.getSalts());
                if (!password.equals(user.getPassword())) {
                    job.add("actionStatus", "false")
                            .add("user", "null")
                            .add("authStatus", "false")
                            .add("data", "null");
                    try (Writer writer = new StringWriter()) {
                        Json.createWriter(writer).write(job.build());
                        json = writer.toString();
                    }
                    break;
                }
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                JsonUserBuilder jsonUserBuilder = new JsonUserBuilder();
                job.add("actionStatus", "true")
                        .add("user", jsonUserBuilder.createJsonUserObject(user))
                        .add("authStatus", "true")
                        .add("data", "null");
                try (Writer writer = new StringWriter()) {
                    Json.createWriter(writer).write(job.build());
                    json = writer.toString();
                }
                break;
            case "/logout":
                session = request.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                job.add("actionStatus", "true")
                        .add("user", "null")
                        .add("authStatus", "false")
                        .add("data", "null");
                try (Writer writer = new StringWriter()) {
                    Json.createWriter(writer).write(job.build());
                    json = writer.toString();
                }
                break;
            case "/listBooks":
                List<Book> listBooks = bookFacade.findAll();
                JsonBookBuilder jbb = new JsonBookBuilder();
                job.add("actionStatus", "true")
                        .add("user", "null")
                        .add("authStatus", "true")
                        .add("data", jbb.createJsonListBooks(listBooks));
                try (Writer writer = new StringWriter()) {
                    Json.createWriter(writer).write(job.build());
                    json = writer.toString();
                }
                break;
        }

        if (json != null && !"".equals(json)) {
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            }

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
