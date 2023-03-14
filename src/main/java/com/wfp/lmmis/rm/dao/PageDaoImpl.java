package com.wfp.lmmis.rm.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.rm.model.Page;
import com.wfp.lmmis.rm.model.RolePage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.hibernate.HibernateException;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PageDaoImpl implements PageDao
{

    @Autowired
    private SessionFactory sessionFactory;

    private Session getCurrentSession()
    {
        return sessionFactory.getCurrentSession();
    }

    /**
     *
     * @param id
     * @return
     */
    @Override
    public Page getPage(Integer id)
    {
        return (Page) sessionFactory.getCurrentSession().get(Page.class, id);
    }

    @Override
    public void Save(Page page)
    {
        this.getCurrentSession().save(page);
    }

    @Override
    public void Edit(Page page)
    {
        Page dBPage = getPage(page.getId());
        page.setCreatedBy(dBPage.getCreatedBy());
        page.setCreationDate(dBPage.getCreationDate());
        this.getCurrentSession().merge(page);
    }

    @Override
    public void Delete(Page page)
    {
        if (page != null)
        {
            this.getCurrentSession().delete(page);
        }
    }

    @Override
    public List<Page> getPageList(Locale locale, Integer schemeId) throws ExceptionWrapper
    {
        List<Page> list;
        try
        {
            if (locale.getLanguage().equals("en"))
            {
                list = sessionFactory.getCurrentSession().createQuery("from Page order by parentPage.nameInEnglish").list();
            }
            else
            {
                list = sessionFactory.getCurrentSession().createQuery("from Page order by parentPage.nameInBangla").list();
            }
            return list;
        }
        catch (Exception e)
        {
            throw new ExceptionWrapper(e.getMessage());
        }

    }

    @Override
    public List<Page> getParentPageList()
    {
        @SuppressWarnings("unchecked")
//        List<Page> list = sessionFactory.getCurrentSession().createQuery("from Page where parentPage.id is null").list();
        List<Page> list = sessionFactory.getCurrentSession().createQuery("from Page").list();
        return list;
    }

    @Override
    public List<Integer> getPageIdListByRole(Integer roleId)
    {
        @SuppressWarnings("unchecked")
        List<Integer> pageIdList = this.sessionFactory.getCurrentSession().createQuery("select rp.page.id from RolePage rp where rp.role.id = " + roleId).list();
        //this.sessionFactory.getCurrentSession().createSQLQuery("select pages_Id from role_page where role_id = " + roleId).list();
        return pageIdList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public HashMap<Page, Map<Page, List<Page>>> getPageListMap(Integer schemeId)
    {
        HashMap<Page, Map<Page, List<Page>>> returnMap = new LinkedHashMap<>();
        try
        {
            List<Page> topLevelPageList = this.sessionFactory.getCurrentSession().createQuery("select p from Page p where p.parentPage is null and p.active=1 order by p.pageOrder").list();
            for (Page topLevelPage : topLevelPageList)
            {
                List<Page> secondLevelPageList = this.sessionFactory.getCurrentSession().createQuery("select p from Page p where p.parentPage.id = " + topLevelPage.getId()+ " and p.active=1 order by p.pageOrder").list();
                Map<Page, List<Page>> pageMap = new LinkedHashMap<>();
                for (Page secondLevelPage : secondLevelPageList)
                {
                    List<Page> childLevelPageList = this.sessionFactory.getCurrentSession().createQuery("select p from Page p where p.parentPage.id = " + secondLevelPage.getId()+ " and p.active=1 order by p.pageOrder").list();
                    List<Page> tchilds = new ArrayList<>();

                    if (!childLevelPageList.isEmpty())
                    {
                        for (Page childLevelPage : childLevelPageList)
                        {
                            tchilds.add(childLevelPage);
                        }
                        pageMap.put(secondLevelPage, tchilds);
                    }
                    else
                    {
                        pageMap.put(secondLevelPage, tchilds);
                    }
                }
                returnMap.put(topLevelPage, pageMap);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return returnMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<Page, List<Page>> getPageListMapByRole(Integer roleId)
    {

        Map<Page, List<Page>> pageMap = new HashMap<Page, List<Page>>();

        try
        {

            List<Object> pageIdList = this.sessionFactory.getCurrentSession().createSQLQuery("select pages_Id from role_page where role_id = " + roleId).list();
            List<Object> parentPageIdList = this.sessionFactory.getCurrentSession()
                    .createSQLQuery("select distinct p.parent_page_id from role_page rp join page p on rp.pages_Id=p.Id where rp.role_id= " + roleId).list();
            for (Object parentId : parentPageIdList)
            {

                Page parentPage = getPage(new Integer(((Byte) parentId).intValue()));
                List<Page> childPageList = this.sessionFactory.getCurrentSession().createQuery("from Page where parentPage.id = :parentPageId")
                        .setParameter("parentPageId", parentPage.getId()).list();
//                System.out.println("childPageList.size() = " + childPageList.size());
                List<Page> pagePerRole = new ArrayList<Page>();
                for (Object childPageId : pageIdList)
                {
                    Integer cpid = new Integer(((Byte) childPageId).intValue());
                    for (Page childPage : childPageList)
                    {
                        if (childPage.getId().equals(cpid))
                        {
//                            System.out.println("childPage.getNameInBangla() = " + childPage.getNameInBangla());
                            pagePerRole.add(childPage);
                        }
                    }
                }
//                System.out.println("pagePerRole.size() = " + pagePerRole.size());
                pageMap.put(parentPage, pagePerRole);

            }
            //getPageListNewMapByRole(roleId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return pageMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<Page, Map<Page, List<Page>>> getPageListNewMapByRole(Integer roleId)
    {
        HashMap<Page, Map<Page, List<Page>>> returnMap = new LinkedHashMap<>();
        Map<Page, List<Page>> pageMap;
        try
        {
            List<Integer> pageListByRole = this.sessionFactory.getCurrentSession().createQuery("select p.id from RolePage rp join rp.page p where rp.role.id=:roleId and p.active=1 order by p.pageOrder").setParameter("roleId", roleId).list();
            List<Page> topLevelPageList = this.sessionFactory.getCurrentSession().createQuery("select p from Page p where p.parentPage is null order by p.pageOrder").list();
            for (Page topLevelPage : topLevelPageList)
            {
                List<Page> secondLevelPageList = this.sessionFactory.getCurrentSession().createQuery("select p from Page p where p.parentPage.id = " + topLevelPage.getId() + " and p.active=1 order by p.pageOrder").list();
                pageMap = new LinkedHashMap<>();
                boolean exflag = false;
                for (Page secondLevelPage : secondLevelPageList)
                {
                    boolean inflag = false;
                    List<Page> childLevelPageList = this.sessionFactory.getCurrentSession().createQuery("select p from Page p where p.parentPage.id = " + secondLevelPage.getId() + " and p.active=1 order by p.pageOrder").list();
                    List<Page> tchilds = new ArrayList<>();

                    if (!childLevelPageList.isEmpty())
                    {
                        for (Page childLevelPage : childLevelPageList)
                        {
                            //if (isPageInRole(roleId, childLevelPage.getId()))
                            if(pageListByRole.contains(childLevelPage.getId()))
                            {
                                tchilds.add(childLevelPage);
                                inflag = true;
                            }
                        }
                        if (inflag)
                        {
                            pageMap.put(secondLevelPage, tchilds);
                            exflag = true;
                        }
                    }
                    else
                    {
//                        if (isPageInRole(roleId, secondLevelPage.getId()))
                        if(pageListByRole.contains(secondLevelPage.getId()))    
                        {
                            pageMap.put(secondLevelPage, tchilds);
                            exflag = true;
                        }
                    }
                }
                if (exflag)
                {
                    returnMap.put(topLevelPage, pageMap);
                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return returnMap;
    }
    private boolean isPageInRole(Integer roleId, Integer pageId)
    {
        try
        {
            RolePage rolePage = (RolePage) this.sessionFactory.getCurrentSession().createQuery("select rp from RolePage rp where rp.role.id = " + roleId + " and rp.page.id=" + pageId).uniqueResult();
            return rolePage != null;
        }
        catch (HibernateException e)
        {
            return false;
        }
    }

    @Override
    public void saveRolePage(Integer roleId, List<Integer> pageIds
    )
    {
        Query q = this.sessionFactory.getCurrentSession().createSQLQuery("delete from role_page where role_id = " + roleId);
        q.executeUpdate();

        for (Integer pageId : pageIds)
        {
            Query query = this.sessionFactory.getCurrentSession().createSQLQuery("INSERT INTO role_page (role_id, pages_id) VALUES (:roleId, :pageId)");
            query.setParameter("roleId", roleId);
            query.setParameter("pageId", pageId);
            query.executeUpdate();
        }
    }

}
