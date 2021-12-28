package presenter.http;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class presenter extends HttpServlet {
    protected String getPath(HttpServletRequest req){
        return req.getPathInfo() != null ? req.getPathInfo() : "/";
    }
}
