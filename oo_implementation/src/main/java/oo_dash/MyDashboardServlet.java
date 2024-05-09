package oo_dash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.servlet.annotation.WebServlet;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.Response;
/**
 * Servlet implementation class DashboardServlet
 */
@WebServlet(name = "DashboardServlet", urlPatterns = "/dashboard")
@Path("/dashboard")
public class MyDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String API_URL = "http://dl1owooappqa01.it.savvis.net:8443/";

    // Method to check authentication
    private boolean authenticateUser(String username, String password) {
        return "dvs".equals(username) && "1nternUser$".equals(password);
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void getDashboard(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (!authenticateUser(username, password)) {
            try {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed. Access denied.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        List<String> contentPacks = new ArrayList<>();
        List<String> flows = new ArrayList<>();

        try {
            // Fetch content packs
            URL contentPacksUrl = new URL(API_URL + "content-packs");
            HttpURLConnection contentPacksConnection = (HttpURLConnection) contentPacksUrl.openConnection();
            contentPacksConnection.setRequestMethod("GET");
            InputStream contentPacksInput = contentPacksConnection.getInputStream();
            ByteArrayOutputStream contentPacksOutput = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = contentPacksInput.read(buffer)) != -1) {
                contentPacksOutput.write(buffer, 0, bytesRead);
            }
            contentPacksOutput.close();
            byte[] contentPacksBytes = contentPacksOutput.toByteArray();
            contentPacksInput.close();

            JsonArray contentPacksJson = JsonParser.parseString(new String(contentPacksBytes)).getAsJsonArray();
            for (JsonElement element : contentPacksJson) {
                JsonObject pack = element.getAsJsonObject();
                String name = pack.get("name").getAsString();
                String version = pack.get("version").getAsString();
                String publisher = pack.get("publisher").getAsString();
                String description = pack.get("description").getAsString();
                contentPacks.add("Name: " + name + ", Version: " + version + ", Publisher: " + publisher + ", Description: " + description);
            }

            // Fetch flows
            URL flowsUrl = new URL(API_URL + "flows");
            HttpURLConnection flowsConnection = (HttpURLConnection) flowsUrl.openConnection();
            flowsConnection.setRequestMethod("GET");
            InputStream flowsInput = flowsConnection.getInputStream();
            ByteArrayOutputStream flowsOutput = new ByteArrayOutputStream();
            while ((bytesRead = flowsInput.read(buffer)) != -1) {
                flowsOutput.write(buffer, 0, bytesRead);
            }
            flowsOutput.close();
            byte[] flowsBytes = flowsOutput.toByteArray();
            flowsInput.close();

            JsonArray flowsJson = JsonParser.parseString(new String(flowsBytes)).getAsJsonArray();
            for (JsonElement element : flowsJson) {
                JsonObject flow = element.getAsJsonObject();
                String name = flow.get("name").getAsString();
                flows.add("Name: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set data as request attributes
        request.setAttribute("contentPacks", contentPacks);
        request.setAttribute("flows", flows);

        // Forward request to JSP page
        try {
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}