/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.dao;

import com.wfp.lmmis.enums.ServiceType;
import com.wfp.lmmis.model.ServiceSettings;

/**
 *
 * @author Philip
 */
public interface ServiceSettingsDao
{

    public ServiceSettings getServiceSettings(ServiceType serviceType);
}
