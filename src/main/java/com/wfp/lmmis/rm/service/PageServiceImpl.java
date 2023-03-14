package com.wfp.lmmis.rm.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.rm.dao.PageDao;
import com.wfp.lmmis.rm.model.Page;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PageServiceImpl implements PageService
{

    @Autowired
    private PageDao pageDao;

    @Override
    public Page getPage(Integer id)
    {
        return this.pageDao.getPage(id);
    }

    @Override
    public void save(Page page)
    {
        this.pageDao.Save(page);
    }

    @Override
    public void edit(Page page)
    {
        this.pageDao.Edit(page);
    }

    /**
     *
     * @param page
     */
    @Override
    public void delete(Page page)
    {
        this.pageDao.Delete(page);
    }

    @Override
    public List<Page> getPageList(Locale locale, Integer schemeId) throws ExceptionWrapper
    {
        return this.pageDao.getPageList(locale, schemeId);
    }

    @Override
    public List<Page> getParentPageList()
    {
        return this.pageDao.getParentPageList();
    }

    @Override
    public List<Integer> getPageIdListByRole(Integer roleId)
    {
        return this.pageDao.getPageIdListByRole(roleId);
    }

    @Override
    public Map<Page, List<Page>> getPageListMapByRole(Integer id)
    {
        return this.pageDao.getPageListMapByRole(id);
    }

    @Override
    public HashMap<Page, Map<Page, List<Page>>> getPageListMap(Integer schemeId)
    {
        return this.pageDao.getPageListMap(schemeId);
    }

    @Override
    public void saveRolePage(Integer roleId, List<Integer> pageIds)
    {
        this.pageDao.saveRolePage(roleId, pageIds);
    }
    
    @Override
    public HashMap<Page, Map<Page, List<Page>>> getPageListNewMapByRole(Integer roleId)
    {
        return this.pageDao.getPageListNewMapByRole(roleId);
    }
}
