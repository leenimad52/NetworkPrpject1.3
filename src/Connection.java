import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Connection extends Thread {

    private BufferedReader input;
    private DataOutputStream output;
    private Socket socket;

    public Connection(Socket socket) throws IOException {
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            System.out.println("Connected IPAddress: #" + socket.getInetAddress() + " PORT NUMBER: " + socket.getPort());
            String s;
            int x = -1;
            while ((s = input.readLine()) != null) {
                if (s.contains("GET / HTTP/1.1")) {
                    x = 0;
                } else if (s.contains(".css")) {
                    x = 1;
                } else if (s.contains(".js")) {
                    x = 2;
                } else if (s.contains(".png")) {
                    x = 3;
                } else if (s.contains(".jpg")) {
                    x = 4;
                } else if (s.contains("/gl")) {
                    x = 5;
                } else if (s.contains("/ghp")) {
                    x = 6;
                } else if (s.contains("/bzu")) {
                    x = 7;
                } else if (s.contains(".html")) {
                    x = 8;
                }

                System.out.println(s);
                if (s.isEmpty()) {
                    break;
                }
            }
            System.out.println("X : " + x);

            switch (x) {
                case 0:
                    ReadHTMLFILE("D:\\NetWork-Project\\NewWork_Master\\Master\\index.html");
                    break;
                case 1:
                    ReadCssFile("D:\\NetWork-Project\\NewWork_Master\\Master\\style2.css");
                    break;
                case 2:
                    ReadJSFILE("D:\\NetWork-Project\\NewWork_Master\\Master\\js.html");
                    break;
                case 3:
                    readPngFile("D:\\NetWork-Project\\NewWork_Master\\Master\\img2.png");
                    break;
                case 4:
                    readJpegFile("D:\\NetWork-Project\\NewWork_Master\\Master\\img1.jpg");
                    break;
                case 5:
                    redirectPage("www.google.com");
                    break;
                case 6:
                    redirectPage("www.github.com");
                    break;
                case 7:
                    redirectPage("www.birzeit.edu");
                    break;
                case 8:
                    this.ReadHTMLFILE("D:\\NetWork-Project\\NewWork_Master\\Master\\pageHtml.html");
                default:
                    ErrorPage();
            }

            System.out.println("Response sent");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ErrorPage() throws IOException {
        String s = String.format("<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "<title>Error</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "       <h1 style=\"color: red;\"> The file is not found</h1>\n" +
                "       <h3>IP Address : %s And Port Number : %d </h3>\n" +
                "    </body>\n" +
                "</html>", socket.getLocalAddress(), socket.getPort());

        String response = "HTTP/1.1 404 \r\n";
        response += "Content-Type: text/html \r\n";
        response += "Connection: close \r\n";
        response += "Content-Length: " + s.length() + " \r\n";
        response += "\r\n";
        output.write((response + s).getBytes());
        output.flush();
    }

    private void readPngFile(String filePath) throws IOException {
        readFile(filePath, "image/png");
    }

    private void readJpegFile(String filePath) throws IOException {
        readFile(filePath, "image/jpeg");
    }

    private void ReadCssFile(String filePath) {
        readFile(filePath, "text/css");
    }

    private void ReadHTMLFILE(String filePath) {
        readFile(filePath, "text/html");
    }

    private void ReadJSFILE(String filePath) {
        readFile(filePath, "text/html");
    }

    private void readFile(String filePath, String contentType) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                ErrorPage();
                return;
            }

            InputStream inputStream = new FileInputStream(file);
            byte[] bytes = inputStream.readAllBytes();
            String response = "HTTP/1.1 200 \r\n";
            response += "Content-Type: " + contentType + " \r\n";
            response += "Connection: close \r\n";
            response += "Content-Length: " + bytes.length + " \r\n";
            response += "\r\n";
            output.write(response.getBytes());
            output.write(bytes);
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void redirectPage(String website) throws IOException {
        output.write("HTTP/1.1 302 Found\r\n".getBytes());
        output.write("Date: Tue, 11 Jan 2011 13:09:20 GMT\r\n".getBytes());
        output.write("Content-type: text/plain\r\n".getBytes());
        output.write("Server: vinit\r\n".getBytes());
        output.write(("Location: http://" + website + "\r\n").getBytes());
        output.write("Connection: Close".getBytes());
        output.write("\r\n\r\n".getBytes());
        output.flush();
    }
}
