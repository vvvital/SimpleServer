import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {

    private BufferedReader input;
    private PrintWriter output;

    public void connection() throws IOException {
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(8084)) {
                Socket socket = serverSocket.accept();
                System.out.println("client connected");
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                output = new PrintWriter(socket.getOutputStream());
                SocketProcessor processor = new SocketProcessor(socket, input, output);
                processor.run();
                System.out.println("client disconnected");
            }
        }
    }

    private class SocketProcessor implements Runnable {
        public SocketProcessor(Socket socket, BufferedReader input, PrintWriter output) {
            this.socket=socket;
            this.input = input;
            this.output = output;
        }

        private BufferedReader input;
        private PrintWriter output;
        private Socket socket;

        @Override
        public void run() {
            try {
                System.out.println("new thread started");
                String header = input.readLine();
                String name = parseHeader(header);
                while (input.ready()) {
                    System.out.println(input.readLine());
                }
                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();
                output.println("<p>Привет " + name + "!</p>");
                output.println("<a href='http://localhost:63342/SimpleServer/my1.html'>link</a>");
                output.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("thread stopped");
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
