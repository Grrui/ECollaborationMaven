package pojo.businessObject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import pojo.DAO.AccessScoreDAO;
import pojo.valueObject.assist.ProjectAccessTypeVO;
import pojo.valueObject.assist.StudentScoreVO;
import pojo.valueObject.assist.StudentTeamVO;
import pojo.valueObject.assist.TeamProjectVO;
import pojo.valueObject.domain.ProjectVO;
import pojo.valueObject.domain.StudentVO;
import pojo.valueObject.domain.TeamVO;
import tool.BeanFactory;

import java.util.*;

/**
 * Created by geyao on 2017/3/2.
 */
public class AccessScoreBO {

    /**
     * 添加考核条目
     * @param type
     * @param projectVOList
     * @throws Exception
     */
    public void addProjectAccessType(ArrayList<String> type, ArrayList<ProjectVO> projectVOList) throws Exception{
        if (type == null || type.size() == 0 || projectVOList == null || projectVOList.size() == 0){
            throw new NullPointerException("typeList, projectVOList = null");
        }

        AccessScoreDAO accessScoreDAO = BeanFactory.getBean("accessScoreDAO", AccessScoreDAO.class);
        accessScoreDAO.addProjectAccessType(type, projectVOList);
    }

    /**
     * 根据某一个评价条目，获取该条目下,该团队的学生的分数
     * @param typeId
     * @return
     */
    public HashMap getStudentsScore(Integer typeId, Integer teamId) throws Exception{
        if (typeId == null || teamId == null){
            throw new NullPointerException("id == null || teamId == null");
        }

        HashMap<StudentVO, Integer> hashMap = new HashMap();
        SessionFactory sessionFactory = BeanFactory.getSessionFactory();
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {

            //根据typeId获取ProjectAccessType，拿到projectId，到teamVOList到、，到studentVOList
            ProjectAccessTypeVO projectAccessTypeVO = session.get(ProjectAccessTypeVO.class, typeId);
            ProjectVO projectVO = projectAccessTypeVO.getProjectVO();
            List<TeamProjectVO> teamProjectList = session.createCriteria(TeamProjectVO.class)
                    .add(Restrictions.eq("projectVO", projectVO))
                    .list();
            Iterator forTeam = teamProjectList.iterator();

            List<StudentVO> studentVOList = new ArrayList<>();
            List<TeamVO> teamVOList = new ArrayList<>();
            while (forTeam.hasNext()){
                TeamProjectVO teamProjectVO = (TeamProjectVO) forTeam.next();
                teamVOList.add(teamProjectVO.getTeamVO());
            }//teamVOList拿到

            studentVOList = session.createCriteria(StudentTeamVO.class)
                    .add(Restrictions.in("teamVO", teamVOList))
                    .list();//拿到了studentVOList

            List StuScoRecList = session.createCriteria(StudentScoreVO.class)
                    .add(Restrictions.in("studentVO", studentVOList))
                    .list();

            Iterator iterator = studentVOList.iterator();
            while (iterator.hasNext()){
                StudentScoreVO studentScoreVO = (StudentScoreVO) iterator.next();
                hashMap.put(studentScoreVO.getStudentVO(), studentScoreVO.getScore());
            }

            return hashMap;

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
