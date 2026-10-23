import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WebServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/students", new StudentApiHandler());
        server.setExecutor(null);
        System.out.println("Java API Server running at http://localhost:" + port + "/api/students");
        server.start();
    }

    static class StudentApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

            String method = exchange.getRequestMethod();

            if ("OPTIONS".equalsIgnoreCase(method)) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            StudentDAO dao = new StudentDAO();

            try {
                if ("GET".equalsIgnoreCase(method)) {
                    List<Student> list = dao.getAllStudents();
                    StringBuilder json = new StringBuilder("[");
                    for (int i = 0; i < list.size(); i++) {
                        Student s = list.get(i);
                        json.append(String.format(
                            "{\"id\":%d,\"name\":\"%s\",\"email\":\"%s\",\"course\":\"%s\",\"fee\":%.2f}",
                            s.getId(), s.getName(), s.getEmail(), s.getCourse(), s.getFee()
                        ));
                        if (i < list.size() - 1) json.append(",");
                    }
                    json.append("]");

                    byte[] responseBytes = json.toString().getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(responseBytes);
                    }

                } else if ("POST".equalsIgnoreCase(method)) {
                    InputStream is = exchange.getRequestBody();
                    String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    System.out.println(">>> Received POST body: " + body);

                    String name = parseField(body, "name");
                    String email = parseField(body, "email");
                    String course = parseField(body, "course");
                    String feeStr = parseField(body, "fee");
                    double fee = feeStr.isEmpty() ? 0.0 : Double.parseDouble(feeStr);

                    Student student = new Student(name, email, course, fee);
                    dao.addStudent(student);
                    System.out.println(">>> Student successfully saved to database!");

                    byte[] responseBytes = "{\"status\":\"success\"}".getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, responseBytes.length);
                    try (OutputStream os = exchange.getResponseBody()) {
                        os.write(responseBytes);
                    }
                }
            } catch (Exception ex) {
                System.err.println(">>> Error occurred while handling request:");
                ex.printStackTrace();
                byte[] err = ("{\"error\":\"" + ex.getMessage() + "\"}").getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(500, err.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(err);
                }
            }
        }

        private String parseField(String json, String key) {
            String search = "\"" + key + "\":";
            int idx = json.indexOf(search);
            if (idx == -1) return "";
            int start = idx + search.length();
            while (start < json.length() && (json.charAt(start) == ' ' || json.charAt(start) == '"')) {
                start++;
            }
            int end = start;
            while (end < json.length() && json.charAt(end) != '"' && json.charAt(end) != ',' && json.charAt(end) != '}') {
                end++;
            }
            return json.substring(start, end).trim();
        }
    }
}