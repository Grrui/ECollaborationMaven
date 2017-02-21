import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pojo.valueObject.assist.MessageReceiverVO;
import pojo.valueObject.assist.StudentTeamVO;
import pojo.valueObject.assist.TeamProjectAccessVO;
import pojo.valueObject.assist.TeamProjectVO;
import pojo.valueObject.domain.*;

import java.util.List;

/**
 * Created by geyao on 2017/2/19.
 */

public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("ApplicationContext.xml");
        SessionFactory sf = context.getBean("sessionFactory", SessionFactory.class);
        System.out.println("SesssionFactory是   " + sf);
        Session session = sf.openSession();
        Transaction transaction = session.beginTransaction();

        //从team_project_access的关系表拿取数据1，打印
        TeamProjectAccessVO teamProjectAccessVO = session.get(TeamProjectAccessVO.class, 1);
        System.out.println(teamProjectAccessVO);
        //通过测试
        transaction.commit();
        session.close();
        sf.close();
    }
}
