package pojo.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import pojo.valueObject.domain.ManagerVO;
import tool.BeanFactory;

/**
 * Created by GR on 2017/2/26.
 */
public class ManagerDAO {

    /**
     * 根据id获取管理员信息
     * @param id
     * @return
     */
    public ManagerVO getManagerInfo(Integer id) throws Exception{
        if(id == null){
            return null;
        }else {
            ApplicationContext context = BeanFactory.getApplicationContext();
            SessionFactory sf = BeanFactory.getSessionFactory();
            Session session = sf.getCurrentSession();
            try{
                ManagerVO managerVO = session.get(ManagerVO.class, id);
                return managerVO;
            }catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}
