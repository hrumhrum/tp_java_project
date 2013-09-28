/**
 * Created with IntelliJ IDEA.
 * User: yulia
 * Date: 21.09.13
 * Time: 10:24
 * To change this template use File | Settings | File Templates.
 */

package frontend;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import templater.PageGenerator;

public class Frontend extends HttpServlet {

    private String login = "";
    private AtomicLong freeId = new AtomicLong();
    private Map<String, Long> users = new HashMap<String,Long>();
    private Map<Long, String> passwords = new HashMap<Long, String>();

    //private Map<Long, AccountData> sessionToUser = new HashMap<>();
    //Account data: userId, name...
    private boolean loggedIn = false;
    long userId;


    public static String getTime() {
        Date date = new Date();
        date.getTime();
        DateFormat formatter = new SimpleDateFormat("HH.mm.ss");
        return formatter.format(date);
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        if (request.getPathInfo().equals("/authform")) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            pageVariables.put("userId", " ");
            pageVariables.put("serverMessage", " ");
            pageVariables.put("username", " ");
            pageVariables.put("sessionID", " ");
            pageVariables.put("loggedIn", 0);


        if (loggedIn)
            pageVariables.put("loggedIn", 1);
        response.getWriter().println(PageGenerator.getPage("authform", pageVariables));
        }

        if (request.getPathInfo().equals("/timer") && loggedIn) {
            pageVariables.put("refreshPeriod", "1000");
            pageVariables.put("serverTime", getTime());

            response.getWriter().println(PageGenerator.getPage("timer.tml", pageVariables));
            return;
        }
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        login = request.getParameter("login");
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        Map<String, Object> pageVariables = new HashMap<>();

        if (request.getPathInfo().equals("/authform")) {
            login = request.getParameter("login");
            String pass=request.getParameter("password");
             String serverMessage = " ";

            if (!users.containsKey(login)) {
                long id = freeId.getAndIncrement();
                users.put(login,id);
                passwords.put(id, pass);

                serverMessage = "new User registered";
                loggedIn = true;
                pageVariables.put("loggedIn", 1);
            }


            pageVariables.put("userId"," ");
            pageVariables.put("serverMessage", " ");
            pageVariables.put("username", " ");
            pageVariables.put("sessionID", " ");

            userId = users.get(login);

            if (pass.equals(passwords.get(userId))) {
                pageVariables.put("userId","UserId: " + userId);
                pageVariables.put("username", "Username: " + login);
                HttpSession session = request.getSession();
                pageVariables.put("sessionID", "Session ID: " + session.getId());
                loggedIn = true;

            } else
                serverMessage = "Wrong pair login-password";




            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            pageVariables.put("serverMessage", serverMessage);
            response.getWriter().println(PageGenerator.getPage("authform", pageVariables));
            response.getWriter().println(PageGenerator.getPage("timer", pageVariables));

        }
    }
}
