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
import java.util.Calendar;
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

@WebServlet(name = "UserController", urlPatterns = {
    "/createBook",
    "/buyBook",
    "/readBook",})
public class UserController extends HttpServlet {

    @EJB
    private BookFacade bookFacade;
    @EJB
    private PersonFacade personFacade;

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

        HttpSession session = request.getSession(false);
        if (session == null) {
            job.add("actionStatus", "false")
                    .add("user", "null")
                    .add("authStatus", "false")
                    .add("data", "null");
            try (Writer writer = new StringWriter()) {
                Json.createWriter(writer).write(job.build());
                json = writer.toString();
            }
            if (json != null && !"".equals(json)) {
                try (PrintWriter out = response.getWriter()) {
                    out.println(json);
                }
            }
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            job.add("actionStatus", "false")
                    .add("user", "null")
                    .add("authStatus", "false")
                    .add("data", "null");
            try (Writer writer = new StringWriter()) {
                Json.createWriter(writer).write(job.build());
                json = writer.toString();
            }
            if (json != null && !"".equals(json)) {
                try (PrintWriter out = response.getWriter()) {
                    out.println(json);
                }
            }
        }

        JsonUserBuilder jsonUserBuilder = new JsonUserBuilder();
        String path = request.getServletPath();
        switch (path) {
            case "/createBook":
                JsonReader jsonReader = Json.createReader(request.getReader());
                JsonObject jsonObject = jsonReader.readObject();
                String name = jsonObject.getString("name");
                String author = jsonObject.getString("author");
                String publishedYear = jsonObject.getString("publishedYear");
                String coverUrl = jsonObject.getString("coverUrl");
                String price = jsonObject.getString("price");
                String textBook = jsonObject.getString("textBook");
                if (null == name || "".equals(name)
                        || null == author || "".equals(author)
                        || null == publishedYear || "".equals(publishedYear)
                        || null == coverUrl || "".equals(coverUrl)
                        || null == price || "".equals(price)
                        || null == textBook || "".equals(textBook)) {

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

                Book book = new Book(name,
                        author,
                        publishedYear,
                        Integer.parseInt(price),
                        Calendar.getInstance().getTime(),
                        true,
                        textBook.getBytes(),
                        coverUrl
                );
                bookFacade.create(book);

                job.add("actionStatus", "true")
                        .add("user", jsonUserBuilder.createJsonUserObject(user))
                        .add("authStatus", "true")
                        .add("data", "null");
                try (Writer writer = new StringWriter()) {
                    Json.createWriter(writer).write(job.build());
                    json = writer.toString();
                }
                break;

            case "/readBook":
                String bookId = request.getParameter("bookId");
                book = bookFacade.find(Long.parseLong(bookId));
                JsonBookBuilder jsonBookBuilder = new JsonBookBuilder();
                StringBuffer textBookLimit = new StringBuffer();
                textBookLimit.append(book.getTextBookLimit(20000));
                job.add("actionStatus", "true")
                        .add("user", "null")
                        .add("authStatus", "true")
                        .add("data", textBookLimit.toString());
                try (Writer writer = new StringWriter()) {
                    Json.createWriter(writer).write(job.build());
                    json = writer.toString();
                }
                break;
            case "/buyBook":
                bookId = request.getParameter("bookId");
                book = bookFacade.find(Long.parseLong(bookId));
                Person person = personFacade.find(user.getPerson().getId());
                if (person.getMoney() - book.getPrice() < 0) {
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

                person.setMoney(person.getMoney() - book.getPrice());
                personFacade.edit(person);
                session.setAttribute("user", user);
                jsonBookBuilder = new JsonBookBuilder();
                StringBuilder sbTextBookFull = new StringBuilder();
                sbTextBookFull.append(book.getTextBookFull());
                job.add("actionStatus", "true")
                        .add("user", jsonUserBuilder.createJsonUserObject(user))
                        .add("authStatus", "true")
                        .add("data", sbTextBookFull.toString());
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
