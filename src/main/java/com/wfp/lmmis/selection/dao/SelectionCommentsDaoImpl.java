package com.wfp.lmmis.selection.dao;

import com.wfp.lmmis.selection.model.SelectionComments;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author rasel
 */
@Repository
public class SelectionCommentsDaoImpl implements SelectionCommentsDao
{

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void save(SelectionComments selectionComments)
    {
        this.sessionFactory.getCurrentSession().save(selectionComments);
    }

    @Override
    public List<SelectionComments> getSelectionCommentsList(Map parameter)
    {
        try
        {
            Integer applicantId = parameter.get("applicantId") != null ? (Integer) parameter.get("applicantId") : null;
            String querySt = "from SelectionComments o where 0=0 ";
            if (applicantId != null && applicantId != 0)
            {
                querySt += " AND o.applicant.id = " + applicantId;
            }
            querySt += " order by o.changedDate desc";
            System.out.println("query is "+ querySt);
            Query mainQuery = sessionFactory.getCurrentSession().createQuery(querySt);
            List<SelectionComments> list = new ArrayList<SelectionComments>();
            list = mainQuery.list();
            System.out.println("SOC" +list.size());
            return list;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }
}
