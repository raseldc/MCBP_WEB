/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.selection.model;

import com.wfp.lmmis.applicant.model.Applicant;
import com.wfp.lmmis.enums.CommentType;
import com.wfp.lmmis.enums.StageType;
import com.wfp.lmmis.rm.model.User;
import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Philip
 */
@Entity
@Table(name = "selection_comments")
public class SelectionComments implements Serializable
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "applicant_id", columnDefinition = "int unsigned")
    private Applicant applicant;

    @Column(name = "comment_type")
    private CommentType commentType;
    
     @Column(name = "comment")
    private String comment;
     
    @Column(name = "stage_type")
    private StageType stageType;
    
    @ManyToOne
    @JoinColumn(name = "changed_by", columnDefinition = "smallint unsigned")
    private User changedBy;

    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "changed_date")
    private Calendar changedDate;

    public SelectionComments()
    {
    }
    
    public SelectionComments(Integer id, CommentType commentType, String comment, StageType stageType, Applicant applicant,User changedBy, Calendar changedDate)
    {
        this.id = id;
        this.commentType = commentType;
        this.comment = comment;
        this.stageType = stageType;
        this.applicant = applicant;
        this.changedBy = changedBy;
        this.changedDate = changedDate;
    }
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Applicant getApplicant()
    {
        return applicant;
    }

    public void setApplicant(Applicant applicant)
    {
        this.applicant = applicant;
    }

    public CommentType getCommentType()
    {
        return commentType;
    }

    public void setCommentType(CommentType commentType)
    {
        this.commentType = commentType;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public StageType getStageType()
    {
        return stageType;
    }

    public void setStageType(StageType stageType)
    {
        this.stageType = stageType;
    }

    /**
     *
     * @return
     */
    public User getChangedBy()
    {
        return changedBy;
    }

    public void setChangedBy(User changedBy)
    {
        this.changedBy = changedBy;
    }

    public Calendar getChangedDate()
    {
        return changedDate;
    }

    public void setChangedDate(Calendar changedDate)
    {
        this.changedDate = changedDate;
    }
}
