import com.google.gson.Gson;
import dal.ResortDao;
import java.io.IOException;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ResortServlet")
public class ResortServlet extends HttpServlet {

    private static ResortDao resortDao;

    @Override
    public void init() throws ServletException {
        resortDao = new ResortDao();
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest req,
            HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/plain");
        String urlPath = req.getPathInfo();
//        System.out.println("port" + req.getServerPort());
//        System.out.println("path" + req.getPath);

        // now validate url path and return the response status code
        // (and maybe also some value if input is valid)
        if (urlPath == null) {
            res.setStatus(HttpServletResponse.SC_OK);
            // TODO: should get a lists of resorts
            res.getWriter().write("\nIt works!, should print out a list of resorts");
        }

        if (!isGetUrlValid(urlPath)) {
            res.setStatus(HttpServletResponse.SC_NOT_FOUND);
//            System.out.println("urlPath=" + urlPath);
            res.getWriter().write("Invalid url address");
        } else {
            // do any sophisticated processing with urlParts which contains all the url params
            // TODO: process url params in `urlParts`
            String[] urlParts = urlPath.split("/");
            if (urlParts.length == 7) {
                int resortId = Integer.parseInt(urlParts[1]);
                int seasonId = Integer.parseInt(urlParts[3]);
                int dayId = Integer.parseInt(urlParts[5]);
                int numSkiers = resortDao.getNumSkiersOnResortDay(resortId, dayId);
                res.getWriter().write("Number for skiers in resort " + resortId + " for season "
                        + seasonId + " is: " + numSkiers);
            }
            res.getWriter().write("\nIt works!");
        }
    }

    private boolean isGetUrlValid(String urlPath) {
        // TODO: validate the request url path according to the API spec
        // valid urlPaths:
        //      GET Request:  "" or "/{resortID}/seasons" or /{resortID}/seasons/{seasonID}/days/{dayID}/skiers
        //      POST Request: "/{resortID}/seasons"

        String[] urlParts = urlPath.split("/");
        if (urlParts.length == 7) {
            try {
                int resortId = Integer.parseInt(urlParts[1]);
                int season = Integer.parseInt(urlParts[3]);
                int day = Integer.parseInt(urlParts[5]);

                System.out.printf("resort=%d, season=%d, day=%d", resortId, season, day);
                return urlParts[2].equals("seasons") && urlParts[4].equals("days") && urlParts[6].equals("skiers")
                        && day >= 1 && day <= 366 && season >= 1990 && season <= 2999;
            } catch (NumberFormatException nfe){
                return false;
            }
        } else {
            return isPostUrlValid(urlPath);
        }
    }

    private boolean isPostUrlValid(String urlPath) {
        // TODO: validate the request url path according to the API spec
        // valid urlPaths:
        //      GET Request:  "" or "/{resortID}/seasons"
        //      POST Request: "/{resortID}/seasons"
        // urlParts = [, 1, seasons]
        String[] urlParts = urlPath.split("/");
        return urlParts.length == 3 && urlParts[2].equals("seasons")
                && urlParts[1].matches("\\d+");
    }
}
