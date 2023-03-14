/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.service;

import com.wfp.lmmis.dao.ServiceSettingsDao;
import com.wfp.lmmis.enums.ServiceType;
import com.wfp.lmmis.model.ServiceSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServiceSettingsServiceImpl implements ServiceSettingsService
{

    @Autowired
    private ServiceSettingsDao serviceSettingsDao;

    @Override
    public ServiceSettings getServiceSettings(ServiceType serviceType)
    {
        return serviceSettingsDao.getServiceSettings(serviceType);
    }

}
