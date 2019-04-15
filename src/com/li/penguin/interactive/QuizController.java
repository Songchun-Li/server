package com.li.penguin.interactive;

import com.google.gson.Gson;
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
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(name = "QuizController")
public class QuizController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        String action = request.getParameter("action");
        response.setContentType("text/html; charset = utf-8");

        //output the remote IP here
        System.out.println();
        IpTool ipTool = new IpTool();
        String ip = ipTool.getLocalIp(request);
        System.out.println("Access IP Addr: " + ip);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Curent Time: " + df.format(new Date()));
        PrintWriter out = response.getWriter();

        if ("syncquiz".equals(action)){
            //get quiz list from DB
            QuizDAOBean quizDAOBean = new QuizDAOBean();
            List<QuizBean> list = quizDAOBean.findAll();
            // generate json array
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i<list.size(); i++) {
                QuizBean quizBean = list.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("question", quizBean.getQuestion());
                jsonObject.put("option", quizBean.getOption());
                jsonObject.put("answer", quizBean.getAnswer());
                jsonObject.put("image", quizBean.getAppendix());
                jsonArray.add(jsonObject);
            }
            System.out.println("Ouput quiz Success!");
            out.write(jsonArray.toString());

/*            String json = gson.toJson(list);
            System.out.println(json);
            out.write(json);*/


        } else if ("newscore".equals(action)){
            // new score is generated after the getting quiz
            JSONObject newScoreJSON = JsonReader.receivePost(request);
            System.out.println(newScoreJSON);

            //transform the JSON to a java object
            UserBean newUser = (UserBean)JSONObject.toBean(newScoreJSON,UserBean.class);
            System.out.println("date of score: "+ newUser.getDate());
            //update to the database
            UserDAOBean userDAOBean = new UserDAOBean();
            int rowAffected = userDAOBean.create(newUser);

            //Send back message to client
            JSONObject resultJSONObject = new JSONObject();
            if (rowAffected == 1){
                resultJSONObject.put("result", "success");
                System.out.println("Upload Success");
            } else {
                resultJSONObject.put("result", "fail");

            }
            out.write(resultJSONObject.toString());


        } else if ("getboard".equals(action)){
            // Work to do: give back only the top 10
            // all the leader board data will be return
            UserDAOBean userDAOBean = new UserDAOBean();
            List<UserBean> list = userDAOBean.findAll();

            // generate json array
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i<list.size(); i++) {
                UserBean userBean = list.get(i);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", userBean.getName());
                jsonObject.put("score", userBean.getScore());
                if (userBean.getDate()==null){
                    jsonObject.put("date", "01/01/1999");
                } else{
                    jsonObject.put("date", userBean.getDate());
                }
                jsonArray.add(jsonObject);
            }
            System.out.println("Ouput leader board Success!");
            out.write(jsonArray.toString());

/*            String json = gson.toJson(list);
            System.out.println(json);
            out.write(json);*/

        } else if ("searchscore".equals(action)) {
            UserDAOBean userDAOBean = new UserDAOBean();
            JSONObject userNameJson = JsonReader.receivePost(request);
            String userName = (String) userNameJson.get("name");
            System.out.println(userName);

            List<UserBean> list = userDAOBean.findByName(userName);
            JSONArray jsonArray = new JSONArray();
            for(int i = 0; i<list.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                UserBean userBean = list.get(i);
                jsonObject.put("name", userBean.getName());
                jsonObject.put("score", userBean.getScore());
                jsonObject.put("date", userBean.getDate());
                jsonArray.add(jsonObject);
            }
            System.out.println("Respond to search successfully!");
            if (jsonArray.size()==0){
                System.out.println("No corresponding record in DB");
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", "null");
                jsonObject.put("score", "0");
                jsonArray.add(jsonObject);
            } else{
                System.out.println("Record(s) is(are) founded");
                System.out.println(jsonArray.toString());
            }
            out.write(jsonArray.toString());
        } else if ("getimage".equals(action)) {


        }

        else{
            out.write("No action for this");
        }
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);


    }
}
