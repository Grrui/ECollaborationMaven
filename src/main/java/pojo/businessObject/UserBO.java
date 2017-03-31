package pojo.businessObject;

import net.sf.json.JSONObject;
import org.hibernate.SessionFactory;
import pojo.DAO.*;
import pojo.valueObject.DTO.ManagerDTO;
import pojo.valueObject.DTO.StudentDTO;
import pojo.valueObject.DTO.TeacherDTO;
import pojo.valueObject.DTO.UserDTO;
import pojo.valueObject.domain.*;
import tool.BeanFactory;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by GR on 2017/2/26.
 */

public class UserBO {

    @Resource(name = "projectDAO")
    private ProjectDAO projectDAO;

    /**
     * 登录
     * @param logName
     * @param passWord
     * @param session
     * @return student、manager、teacher、fail
     */

    public JSONObject logIn(String logName, String passWord, Map<String , Object> session) throws Exception{
        if(logName==null||logName.equals("")||passWord==null||passWord.equals("")){
            System.out.println("用户名或者密码为空---"+this.getClass()+"logIn()");
            throw new NullPointerException("用户名或者密码为空---"+this.getClass()+"logIn()");
        }else {
            JSONObject jsonObject = BeanFactory.getApplicationContext().getBean("jsonObject", JSONObject.class);
            UserDAO userDAO = BeanFactory.getApplicationContext().getBean("userDAO", UserDAO.class);
            UserDTO userDTO = BeanFactory.getApplicationContext().getBean("userDTO",UserDTO.class);
            UserVO userVO = userDAO.getUserInfo(logName,passWord);
            if(userVO==null){
                //账号密码不正确，获取不到。
                System.out.println("useVO is null---"+this.getClass()+"logIn()");
                jsonObject.put("result", "logInfoWrong");
                jsonObject.put("role","visitor");
                return jsonObject;
            }else{
                jsonObject.put("result", "success");
                session.clear();
                if(userVO.getRole().equals("manager")){
                    ManagerVO managerVO = (ManagerVO)userVO;
                    if(managerVO != null) {
                        ManagerDTO managerDTO = BeanFactory.getApplicationContext().getBean("managerDTO",ManagerDTO.class);
                        managerDTO.clone(managerVO);
                        session.put("managerVO", managerVO);
                        jsonObject.put("managerBean", managerDTO);
                        jsonObject.put("role", "manager");
                        return jsonObject;
                    }else{
                        System.out.println("获取管理员的信息为空---"+this.getClass()+"logIn()");
                        throw new NullPointerException("用户名或者密码为空---"+this.getClass()+"logIn()");
                    }
                }else if(userVO.getRole().equals("teacher")){
                    TeacherVO teacherVO = (TeacherVO)userVO;
                    if(teacherVO != null) {
                        //获取老师的project
                        ArrayList<ProjectVO> teacherProjects = projectDAO.getTeacherProjectVOList(teacherVO);
                        if(teacherProjects.isEmpty()){
                            throw new NullPointerException("老师项目为空---"+this.getClass()+"logIn()");
                        }
                        Set<ProjectVO> projectVOSet = new HashSet<ProjectVO>(teacherProjects);
                        teacherVO.setProjectVOSet(projectVOSet);
                        TeacherDTO teacherDTO = BeanFactory.getApplicationContext().getBean("teacherDTO",TeacherDTO.class);
                        teacherDTO.clone(teacherVO);
                        session.put("teacherVO", teacherVO);
                        jsonObject.put("teacherBean", teacherDTO);
                        jsonObject.put("role", "teacher");
                        return jsonObject;
                    }else{
                        System.out.println("获取老师的信息为空---"+this.getClass()+"logIn()");
                        throw new NullPointerException("用户名或者密码为空---"+this.getClass()+"logIn()");
                    }
                }else if(userVO.getRole().equals("student")){
                    StudentVO studentVO = (StudentVO)userVO;
                    if(studentVO != null) {
                        StudentDTO studentDTO = BeanFactory.getApplicationContext().getBean("studentDTO",StudentDTO.class);
                        studentDTO.clone(studentVO);
                        session.put("studentVO", studentVO);
                        jsonObject.put("studentBean",studentDTO);
                        jsonObject.put("role", "student");
                        return jsonObject;
                    }else{
                        System.out.println("获取学生的信息为空---"+this.getClass()+"logIn()");
                        throw new NullPointerException("获取学生的信息为空---"+this.getClass()+"logIn()");
                    }
                }else{
                    System.out.println("没有这种角色---"+this.getClass()+"logIn()");
                    throw new NullPointerException("没有这种角色---"+this.getClass()+"logIn()");
                }
            }
        }
    }

    /**
     * 登出
     * @param session
     * @return
     */
    public JSONObject logOut(Map<String, Object> session){
        JSONObject jsonObject = BeanFactory.getApplicationContext().getBean("jsonObject",JSONObject.class);
        session.clear();
        if(session.isEmpty()){
            jsonObject.put("result","success");
            return jsonObject;
        }else{
            return null;
        }
    }
}
