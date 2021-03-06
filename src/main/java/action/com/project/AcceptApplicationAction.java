package action.com.project;

import action.com.Base.BaseAction;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import pojo.businessObject.ProjectBO;
import pojo.valueObject.domain.UserVO;
import tool.BeanFactory;
import tool.JSONHandler;

/**
 * Created by GR on 2017/4/17.
 */
public class AcceptApplicationAction extends BaseAction{

    //jsp
    private Integer applicationId;


    @Override
    public String execute() throws Exception {
        JSONObject jsonObject = BeanFactory.getJSONO();
        try{
            System.out.println(this.getClass());
            ProjectBO projectBO = BeanFactory.getBean("projectBO",ProjectBO.class);
            System.out.println("applicationId"+applicationId);
            projectBO.acceptApplyProjectApplication(applicationId);
            jsonObject.put("result", "success");
            JSONHandler.sendJSON(jsonObject, response);
            System.out.println("获取申请");
            System.out.println(jsonObject);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            JSONHandler.sendJSON(jsonObject, response);
            return "fail";
        }
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
}
