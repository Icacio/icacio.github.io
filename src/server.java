import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;

public class server {
	private static String origins = "Access-Control-Allow-Origin: *";
	public static void main(String[] args) {
    	try (ServerSocket serverSocket = new ServerSocket(8080)) {
			System.out.println("Server is listening on port " + 8080);
			while (true) {
				try (Socket clientSocket = serverSocket.accept();
					var out = new PrintWriter(clientSocket.getOutputStream(), true);
                    var in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                    String requestLine = in.readLine();
					if (requestLine != null) {
						System.out.println("Connection from " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
						if (requestLine.startsWith("GET")) 
							if (getMapping(requestLine, out, clientSocket,in))
								continue;
						else if (requestLine != null && requestLine.startsWith("OPTIONS")) {
							out.println("HTTP/1.1 204 No Content");
							out.println(origins);
						    out.println("Access-Control-Allow-Methods: GET, OPTIONS");
							out.println("Access-Control-Allow-Headers: Content-Type");
							out.println();
							continue;
						}
					}
                } catch (IOException e) {
                    System.err.println("Exception caught handling client connection: " + e);
                }
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port 8080: " + e);
        }
    }

	private static boolean getMapping(
	String requestLine,PrintWriter out,
	Socket clientSocket, BufferedReader in) throws IOException {
		String[] parts = requestLine.split(" ");
		String filePath = parts[1];
		filePath = java.net.URLDecoder.decode(filePath, "UTF-8");
		if (filePath.contains("..")) {
			out.println("HTTP/1.1 403 Forbidden");
			out.println("Content-Type: text/plain");
			out.println();
			out.println("Accesso denegado!");
			return true;
		}
		if (filePath.matches(".*\\.json")) {
			filePath = filePath.replaceAll(".json", "");
			File file = new File("." + filePath);
			if (file.exists() && file.isDirectory()) {
				File[] files = file.listFiles();
				String headerLine,host = "";
				while ((headerLine = in.readLine()) != null && !headerLine.isEmpty()) {
					if (headerLine.toLowerCase().startsWith("host:"))
						host = headerLine.substring(5).trim();
				}
				var sb = new StringBuilder();
				sb.append("[");
				for (File f : files) {
					if (f.isFile()) {
						var name = f.getName();
						sb.append("{\"name\": \"" + name + "\",");
						sb.append("\"link\": \"http://"+ host + filePath + name + "\"}");
						if (f != files[files.length - 1]) {
							sb.append(",");
						}
					}
				}
				sb.append("]");
				out.println("HTTP/1.1 200 OK");
				out.println(origins);
				out.println("Content-Type: application/json; charset=UTF-8");
				out.println();
				out.println(sb.toString());
			} else {
				out.println("HTTP/1.1 404 Not Found");
				out.println(origins);
				out.println("Content-Type: text/plain");
				out.println();
				out.println("File non trovato!");
			}
		} else {
			File file = new File("." + filePath);
			
			if (file.exists()) {
				if (file.isDirectory()) {
					File[] files = file.listFiles();
					out.println("HTTP/1.1 200 OK");
					out.println("Content-Type: text/html; charset=UTF-8");
					out.println(origins);
					out.println();
					out.println("<html><body><h1>Directory listing</h1><ul>");
					if (files != null) {
						for (File f : files) {
							String name = f.getName();
								if (f.isDirectory()) {
									name += "/";
								}
								out.println("<li><a href=\"" + filePath + name + "\">" + name + "</a></li>");
						}
					}
					out.println("</ul></body></html>");
				} else {
        			out.println("HTTP/1.1 200 OK");
	        		out.println("Content-Type: text/html");
					out.println(origins);
    	    		out.println("Content-Length: " + file.length());
        			out.println();
        			java.nio.file.Files.copy(file.toPath(), clientSocket.getOutputStream());
				} 
			} else {
        		out.println("HTTP/1.1 404 Not Found");
				out.println(origins);
        		out.println("Content-Type: text/plain");
        		out.println();
        		out.println("File "+ file.getName()+" non trovato!");
    		}
		}
		return false;
	}
}