package Server;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class MyServer extends Thread {
    int port;

    public void startListen(int port) {
        this.port = port;
        this.start();
    }

    private String GetDate() {

        Date date = new Date();//获取当前的日期
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");//设置日期格式
        return df.format(date);
    }

    public void run() {
        try {
            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = in.readLine();
            float[][] TemData = new float[24][32];
            int x = 0;
            int y = 0;
            Draw pic = new Draw();
            while (!line.equals("bye")) {
                //System.out.println(GetDate());
                // System.out.println(line + " ");
                String[] lines = line.split(",");
                int Index = 1;
                for (y = 0; y < 24; y++)
                    for (x = 0; x < 32; x++) {
                        if (lines[Index].equals("nan") && Index != 1) {
                            if (x != 0) TemData[y][x] = TemData[y][x-1];
                            else TemData[y][x] = TemData[y-1][31];
                            Index++;
                        }
                        else if (lines[Index].equals("nan") && Index == 1) {
                            TemData[y][x] = 0;
                            Index++;
                        }
                        else
                        TemData[y][x] = Float.parseFloat(lines[Index++]);
                    }
                PixelConversion picture = new PixelConversion();
                float[][] Complete_TemData;
                Complete_TemData = TemData;
                picture.run(Complete_TemData,pic);
//                if (line.equals("end"))
//                {
//                    //处理温度数组
//
//
//
//
//                    x = 0;
//                    y = 0;
//                }
//                else if (line.equals("nan"))
//                {
//                    if (x == 0)
//                        TemData[y][x] = 0;
//                    else
//                        TemData[y][x] = TemData[y][x-1];
//                    x++;
//                }
//                else if (x == 32){
//                    y++;
//                    x = 0;
//
//                    x++;
//                }

//                else
//                {
//                    TemData[y][x] = Float.parseFloat(line);
//                    x++;
//                }
                //OutputStream os = socket.getOutputStream();
                //os.write((line+"\r\n").getBytes());
                //System.out.println("x= " + x + " y = " + y);
                line = in.readLine();
               /* try {

                    line = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }
            socket.close();
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
