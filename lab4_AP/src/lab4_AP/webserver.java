package lab4_AP;

import java.util.*;
import java.io.*;
import java.net.*;

// A web server waits for clients to connect, then starts a separate
// thread to handle the request.

public class webserver {
  private static ServerSocket servSocket;

  public static void main(String[] args) throws IOException {
    servSocket=new ServerSocket(55555);  // Start, listen on port 55558
    while (true) {
      try {
        Socket s=servSocket.accept();  // Wait for a client to connect
        new ClientHandler(s);  // Handle the client in a separate thread
      }
      catch (Exception x) {
        System.out.println(x);
      }
    }
  }
}

// A ClientHandler reads an HTTP request and responds
class ClientHandler extends Thread {
  private Socket socket;  // The accepted socket from the Webserver

  // Start the thread in the constructor
  public ClientHandler(Socket s) {
    socket=s;
    start();
  }

  // Read the HTTP request, respond, and close the connection
  public void run() {
    try {

      // Open connections to the socket
      BufferedReader in=new BufferedReader(new InputStreamReader(
        socket.getInputStream()));
      PrintStream out=new PrintStream(new BufferedOutputStream(
        socket.getOutputStream()));

      // Read filename from first input line "GET /filename.html ..."
      // or if not in this format, treat as a file not found.
      
      String s=in.readLine();
      System.out.println(s);  // Log the request

      // Attempt to serve the file.  Catch FileNotFoundException and
      // return an HTTP error "404 Not Found".  Treat invalid requests
      // the same way.
      String filename="";
      StringTokenizer st=new StringTokenizer(s);
      try {

        // Parse the filename from the GET command
    	  String requesttype = st.nextToken();
        if (st.hasMoreElements() && (requesttype.equalsIgnoreCase("HEAD") || requesttype.equalsIgnoreCase("GET"))
            && st.hasMoreElements()){
          filename=st.nextToken();
        

        // Append trailing "/" with "index.html"
        if (filename.endsWith("/"))
          filename+="index.html";

        // Remove leading / from filename
        while (filename.indexOf("/")==0)
          filename=filename.substring(1);

        // Replace "/" with "\" in path for PC-based servers
        filename=filename.replace('/', File.separator.charAt(0));

        // Check for illegal characters to prevent access to super directories
        if (filename.indexOf("..")>=0 || filename.indexOf(':')>=0
            || filename.indexOf('|')>=0)
          throw new FileNotFoundException();

        // If a directory is requested and the trailing / is missing,
        // send the client an HTTP request to append it.  (This is
        // necessary for relative links to work correctly in the client).
        if (new File(filename).isDirectory()) {
          filename=filename.replace('\\', '/');
          out.print("HTTP/1.0 301 Moved Permanently\r\n"+
            "Location: /"+filename+"/\r\n\r\n");
          out.close();
          return;}
          
         

        // Open the file (may throw FileNotFoundException)
        InputStream f=new FileInputStream(filename);

        // Determine the MIME type and print HTTP header
        String mimeType="text/plain";
        if (filename.endsWith(".html") || filename.endsWith(".htm"))
          mimeType="text/html";
        else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
          mimeType="image/jpeg";
        else if (filename.endsWith(".gif"))
          mimeType="image/gif";
        else if (filename.endsWith(".class"))
          mimeType="application/octet-stream";
        out.print("HTTP/1.0 200 OK\r\n"+
          "Content-type: "+mimeType+"\r\n\r\n");

        // Send file contents to client, then close the connection
        byte[] a=new byte[4096];
        int n;
        while ((n=f.read(a))>0)
          out.write(a, 0, n);
        out.close();
        
        }else{
        	
              	//POST request
        				System.out.println("POST request");

        				do {

        					String line = in.readLine();
        					
        					if (line.indexOf("Content-Type: multipart/form-data") == -1)
        						continue;
        					
        						System.out.println(line);
        						String boundary = line.split("boundary=")[1];
        						while (true) {
        							line = in.readLine();
        							if (line.indexOf("--" + boundary) != -1) {
        								filename = in.readLine().split("filename=")[1].replaceAll("\"", "");                                
        								String [] filelist = filename.split("\\" + "/");
        								filename = filelist[filelist.length - 1];                  
        								System.out.println("Post File - " + filename);
        								break;
        							}              
        						}

        						String fileContentType = in.readLine().split(" ")[1];
        						System.out.println("Filetype = " + fileContentType);

        						in.readLine(); 
        						PrintWriter fileOutStream = new PrintWriter(filename);
        						String last = in.readLine();
        						line = in.readLine();      

        						while (true) {
        							if (line.equals("--" + boundary + "--")) {
        								fileOutStream.print(last);
        								break;
        							}
        							else {
        								fileOutStream.println(last);
        							}
        							last = line;              
        							line = in.readLine();
        						}

        						
        						out.print("HTTP/1.1 200 Not Found\r\n"+
        								"Content-type: text/html\r\n\r\n"+
        								"<html><head></head><body><h1>POST SUCCESS: File Uploaded</h1><br>"+filename+"</body></html>\n");
        						out.close();

        						fileOutStream.close();           
        					                                              
        				}while (in.ready()); //End of do-while  
                
                }
              
        
    }
    
      
      catch (FileNotFoundException x) {
        out.println("HTTP/1.0 404 Not Found\r\n"+
          "Content-type: text/html\r\n\r\n"+
          "<html><head></head><body>HTTP/1.0 404 File Not Found,\n"+filename+" not found</body></html>\n");
        out.close();
      }
    }
    catch (IOException x) {
      System.out.println(x);
    }
  }
}

