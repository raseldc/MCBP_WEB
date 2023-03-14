package com.wfp.lmmis.rm.dao;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.rm.model.Page;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author rasel
 */
public interface PageDao {

    public Page getPage(Integer id);

    public void Save(Page page);

    public void Edit(Page page);
    
    public void Delete(Page page);

    public List<Page> getPageList(Locale locale, Integer schemeId) throws ExceptionWrapper;

    /**
     *
     * @return
     */
    public List<Page> getParentPageList();

    /**
     *
     * @param roleId
     * @return
     */
    public List<Integer> getPageIdListByRole(Integer roleId);

    public Map<Page, List<Page>> getPageListMapByRole(Integer id);

    public HashMap<Page, Map<Page, List<Page>>> getPageListMap(Integer schemeId);

    public void saveRolePage(Integer roleId, List<Integer> pageIds);
    
    public HashMap<Page, Map<Page, List<Page>>> getPageListNewMapByRole(Integer roleId);
}
