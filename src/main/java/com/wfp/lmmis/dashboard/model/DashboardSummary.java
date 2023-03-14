/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dashboard.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Philip
 */
@Entity
@Table(name = "dashboard_summary")
public class DashboardSummary implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "tinyint(2) unsigned")
    private Integer id;

    @Column(name = "summary_type", length = 255, nullable = false)
    private String summaryType;  // chart, table

    @Column(name = "summary_key", length = 255, nullable = false)
    private String summaryKey; // PSheader, BSHeader 

    @Column(name = "name_bn", length = 255, nullable = false)
    private String nameBn;

    @Column(name = "name_en", length = 255, nullable = false)
    private String nameEn;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    private DashboardSummary parentSummary;
}
