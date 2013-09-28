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
import templater.PageGenerator;

public class Frontend extends HttpServlet {

    private String login = "";
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

        if (request.getPathInfo().equals("/index")) {
            pageVariables.put("lastLogin", login);
            response.getWriter().println(PageGenerator.getPage("authform", pageVariables));
        }

        if (request.getPathInfo().equals("/authform")) {
            pageVariables.put("lastLogin", login);
            response.getWriter().println(PageGenerator.getPage("authform", pageVariables));
            return;
        }

        if (request.getPathInfo().equals("/timer")) {
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
            pageVariables.put("lastLogin", login);
            response.getWriter().println(PageGenerator.getPage("authform", pageVariables));
        }
    }
}
