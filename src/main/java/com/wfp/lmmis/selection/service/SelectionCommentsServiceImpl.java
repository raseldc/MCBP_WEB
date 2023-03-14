package com.wfp.lmmis.selection.service;
import com.wfp.lmmis.selection.dao.SelectionCommentsDao;
import com.wfp.lmmis.selection.model.SelectionComments;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SelectionCommentsServiceImpl implements SelectionCommentsService
{
    @Autowired
    SelectionCommentsDao selectionCommentsDao;
    
    @Override
    public void save(SelectionComments selectionComments)
    {
        this.selectionCommentsDao.save(selectionComments);
    }

    @Override
    public List<SelectionComments> getSelectionCommentsList(Map parameters)
    {
        return this.selectionCommentsDao.getSelectionCommentsList(parameters);
    }
    
}