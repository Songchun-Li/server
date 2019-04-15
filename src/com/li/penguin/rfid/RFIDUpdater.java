package com.li.penguin.rfid;

import com.li.penguin.IpTool;
import com.li.penguin.JsonReader;
import com.li.penguin.display.PenguinBean;
import com.li.penguin.display.PenguinDAOBean;
import com.li.penguin.display.RFIDBean;
import com.li.penguin.display.RFIDDAOBean;
import com.li.penguin.interactive.UserBean;
import com.li.penguin.interactive.UserDAOBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Time;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "RFIDUpdater")
public class RFIDUpdater extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        response.setContentType("text/html; charset = utf-8");
        Writer out = response.getWriter();

        //output the remote IP here
        IpTool ipTool = new IpTool();
        String ip = ipTool.getLocalIp(request);
        System.out.println("Access IP Addr: " + ip);

        if ("newswim".equalsIgnoreCase(action)) {
            RFIDDAOBean rfidDaoBean = new RFIDDAOBean();
            PenguinDAOBean penguinDAOBean = new PenguinDAOBean();

            JSONObject penguinJson = JsonReader.receivePost(request);
            //transform the JSON to a java object
            String pengiuinId = (String) penguinJson.get("penguinid");
            System.out.println("Penguin ID: " + pengiuinId);

            // Create a new record
            RFIDBean newRecord = new RFIDBean();
            newRecord.setId(pengiuinId);
            SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = new java.sql.Date(new java.util.Date().getTime());
            System.out.println(dateFormat.format(nowDate));
            newRecord.setDate(nowDate);

            Time nowTime = new java.sql.Time(new java.util.Date().getTime());;
            newRecord.setTime(nowTime);
            SimpleDateFormat timeFormat =new SimpleDateFormat("hh:mm:ss");
            String strDuration = "00:00:01";
            Time duration = null;
            try {
                duration = new java.sql.Time(timeFormat.parse(strDuration).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            newRecord.setDuration(duration);
            newRecord.setTag("900_228000561490");
            newRecord.setType("HA");

            //update to the database
            int rowAffected = rfidDaoBean.create(newRecord);

            //Send back message to client
            JSONObject resultJSONObject = new JSONObject();

            if (rowAffected == 1){
                resultJSONObject.put("result", "success");
                System.out.println("Upload Success");
            } else {
                resultJSONObject.put("result", "fail");
            }
            out.write(resultJSONObject.toString());
        } else{
            out.write("No action for this");
        }
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);

    }
}
