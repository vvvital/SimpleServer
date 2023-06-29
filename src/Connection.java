import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {

    public void connection() throws IOException {
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(8084)) {
                Socket socket = serverSocket.accept();
                System.out.println("client connected");
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                while (socket.isConnected()) {
                    String header = input.readLine();
                    if (header==null)break;
                    String name = parseHeader(header);
                    System.out.println(header);
                    while (input.ready()) {
                        System.out.println(input.readLine());
                    }
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html; charset=utf-8");
                    output.println();
                    output.println("<p>Привет " + name + "!</p>");
                    output.println("<a href='http://localhost:63342/SimpleServer/my1.html'>link</a>");
                    output.flush();

                }
                System.out.println("client disconnected");
            }
        }
    }

    public String parseHeader(String header) {
        try {
            System.out.println(header);
            String s = header.substring(header.indexOf('?') + 1, header.lastIndexOf(' '));
            String email = s.substring(s.indexOf('=') + 1, s.indexOf('&'));
            String name = s.substring(s.lastIndexOf('=') + 1);
            return name;
        } catch (StringIndexOutOfBoundsException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
