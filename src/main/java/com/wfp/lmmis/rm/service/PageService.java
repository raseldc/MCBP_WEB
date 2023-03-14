package com.wfp.lmmis.rm.service;

import com.wfp.lmmis.exception.ExceptionWrapper;
import com.wfp.lmmis.rm.model.Page;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface PageService {

    public Page getPage(Integer id);

    public void save(Page page);

    public void edit(Page page);
    
    public void delete(Page page);

    public List<Page> getPageList(Locale locale, Integer schemeId) throws ExceptionWrapper;

    public List<Page> getParentPageList();

    public List<Integer> getPageIdListByRole(Integer roleId);

    public Map<Page, List<Page>> getPageListMapByRole(Integer id);

    /**
     *
     * @param schemeId
     * @return
     */
    public HashMap<Page, Map<Page, List<Page>>> getPageListMap(Integer schemeId);

    /**
     *
     * @param roleId
     * @param pageIds
     */
    public void saveRolePage(Integer roleId, List<Integer> pageIds);
    
    /**
     *
     * @param roleId
     * @return
     */
    public HashMap<Page, Map<Page, List<Page>>> getPageListNewMapByRole(Integer roleId);
    
}
