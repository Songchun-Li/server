package com.li.penguin.display;

import com.li.penguin.IpTool;
import com.li.penguin.JsonReader;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

@WebServlet(name = "DispController")
public class DispController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        response.setContentType("text/html; charset = utf-8");
        Writer out = response.getWriter();

        //output the remote IP here
        IpTool ipTool = new IpTool();
        String ip = ipTool.getLocalIp(request);
        System.out.println("Access IP Addr: " + ip);

        if ("syncpenguin".equals(action)){
            PenguinDAOBean penguinDAOBean = new PenguinDAOBean();
            List<PenguinBean> list = penguinDAOBean.findAll();
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i<list.size(); i++) {
                PenguinBean penguinBean = list.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("species",penguinBean.getSpecies());
                jsonObject.put("name", penguinBean.getName());
                jsonObject.put("hatch_year", penguinBean.getHatch_year());
                jsonObject.put("gender", penguinBean.getGender());
                jsonObject.put("band_color1", penguinBean.getBand_color1());
                jsonObject.put("band_color2", penguinBean.getBand_color2());
                jsonObject.put("fun_fact", penguinBean.getFun_fact());
                jsonObject.put("personality", penguinBean.getPersonality());
                jsonArray.add(jsonObject);
            }

            JSONObject finaljsonObject = new JSONObject();
            finaljsonObject.put("penguinlist",jsonArray.toString());
            System.out.println(finaljsonObject.toString());
            out.write(finaljsonObject.toString());


        } else if ("searchpenguin".equalsIgnoreCase(action)) {
            PenguinDAOBean penguinDAOBean = new PenguinDAOBean();
            JSONObject userNameJson = JsonReader.receivePost(request);
            String penguinName = (String) userNameJson.get("name");
            System.out.println(penguinName);
            PenguinBean penguin = penguinDAOBean.searchPenguinByName(penguinName);
            JSONObject jsonObject = new JSONObject();
            if (penguin.getName() == null)
            {
                System.out.println("No penguin is found");
            } else {
                jsonObject.put("species", penguin.getSpecies());
                jsonObject.put("name", penguin.getName());
                jsonObject.put("hatch_year", penguin.getHatch_year());
                jsonObject.put("gender", penguin.getGender());
                jsonObject.put("band_color1", penguin.getBand_color1());
                jsonObject.put("band_color2", penguin.getBand_color2());
                jsonObject.put("fun_fact", penguin.getFun_fact());
                jsonObject.put("personality", penguin.getPersonality());
                jsonObject.put("imageurl", penguin.getAppendix());
            }
            out.write(jsonObject.toString());
            System.out.println(jsonObject.toString());


        } else if ("getnames".equalsIgnoreCase(action)){
            PenguinDAOBean penguinDAOBean = new PenguinDAOBean();
            List<PenguinBean> list = penguinDAOBean.getNames();
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i<list.size(); i++) {
                PenguinBean penguinBean = list.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", penguinBean.getName());
                jsonArray.add(jsonObject);
            }
            JSONObject finaljsonObject = new JSONObject();
            finaljsonObject.put("penguinlist",jsonArray.toString());
            System.out.println(finaljsonObject.toString());
            out.write(finaljsonObject.toString());

        } else if ("updaterfid".equalsIgnoreCase(action)){
            // Upadte RFID data to the database

            // RFID data format:

            // also output to the app
            RFIDDAOBean rfiddaoBean = new RFIDDAOBean();
            //List<RFIDBean> rfidList = rfiddaoBean.getUpdate();

            //Get time record
            RFIDBean timeRec = rfiddaoBean.getRec();
            //Use the record to get the penguinID
            List<RFIDBean> waitlist = rfiddaoBean.getNew(timeRec);
            //Update time record
            RFIDBean newTimeRec = new RFIDBean();
            SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd");
            Date nowDate = new java.sql.Date(new java.util.Date().getTime());
            System.out.println(dateFormat.format(nowDate));
            newTimeRec.setDate(nowDate);
            Time nowTime = new java.sql.Time(new java.util.Date().getTime());;
            newTimeRec.setTime(nowTime);
            rfiddaoBean.updateRec(newTimeRec);
            JSONObject jsonObject = new JSONObject();
            if (waitlist.size() > 0){
                String idToPop = waitlist.get(waitlist.size()-1).getId();

                //Get the penguin with ID
                PenguinBean penguinBean = new PenguinBean();
                PenguinDAOBean penguinDAOBean = new PenguinDAOBean();
                PenguinBean penguin = penguinDAOBean.getNamebyID(idToPop);

                if (penguin.getName() == null)
                {
                    System.out.println("No penguin is found");
                } else {
                    jsonObject.put("penguinid", penguin.getPenguinId());
                    jsonObject.put("name", penguin.getName());
                }


            } else{
                jsonObject.put("penguinid", "no");
                jsonObject.put("name", "no");
            }

            out.write(jsonObject.toString());

            /*
            RFIDBean rfidRecord = rfiddaoBean.getUpdate();
            String toShow = rfidRecord.getId();
            */


        } else{
            out.write("No action for this");
        }
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }
}
