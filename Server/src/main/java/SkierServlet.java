import com.google.gson.Gson;
import io.swagger.client.model.LiftRide;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SkierServlet extends javax.servlet.http.HttpServlet {

//  private Gson gson = new Gson();

  protected void doPost(javax.servlet.http.HttpServletRequest request,
      javax.servlet.http.HttpServletResponse response)
      throws javax.servlet.ServletException, IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("Missing Paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isPostUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Invalid URL Address");
    } else {
      response.setStatus(HttpServletResponse.SC_OK);
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = request.getReader().readLine()) != null) {
        sb.append(line);
      }
//      LiftRide liftRide = gson.fromJson(line.toString(), LiftRide.class);
//      response.getWriter().write(gson.toJson(liftRide));
      response.getWriter().write(sb.toString());
    }
  }

  protected void doGet(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/plain");
    String urlPath = request.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      response.getWriter().write("Missing Paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)

    if (!isGetUrlValid(urlParts)) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      response.getWriter().write("Invalid URL Address");
    } else {
      response.setStatus(HttpServletResponse.SC_OK);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      response.getWriter().write("It works!");
    }
  }

  private boolean isPostUrlValid(String[] urlParts) {
    // TODO: validate the request post url path according to the API spec
    // valid post url pattern: /{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    if (urlParts.length != 8) {
      return false;
    }
    try {
      int resortId = Integer.parseInt(urlParts[1]);
      int season = Integer.parseInt(urlParts[3]);
      int day = Integer.parseInt(urlParts[5]);
      int skierId = Integer.parseInt(urlParts[7]);
      return urlParts[2].equals("seasons") && urlParts[4].equals("days") && urlParts[6].equals("skiers")
          && day >= 1 && day <= 366 && season >= 1990 && season <= 2999;
    } catch (NumberFormatException nfe){
      return false;
    }
  }

  private boolean isGetUrlValid(String[] urlParts) {
    // TODO: validate the request get url path according to the API spec
    // valid post url pattern: 1. /{resortID}/seasons/{seasonID}/days/{dayID}/skiers/{skierID}
    //                         2. /{skierID}/vertical
    if (urlParts.length == 8) {
      return isPostUrlValid(urlParts);
    } else if (urlParts.length == 3) {
      try {
        Integer.parseInt(urlParts[1]);
        return urlParts[2].equals("vertical");
      } catch (NumberFormatException nfe) {
        return false;
      }
    } else {
      return false;
    }
  }
}
