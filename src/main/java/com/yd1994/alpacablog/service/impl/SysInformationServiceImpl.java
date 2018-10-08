package com.yd1994.alpacablog.service.impl;

import com.yd1994.alpacablog.common.exception.RequestParamErrorException;
import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.common.exception.TableVersionNotFoundException;
import com.yd1994.alpacablog.common.exception.VersionNotFoundException;
import com.yd1994.alpacablog.dto.SysInformation;
import com.yd1994.alpacablog.entity.SysInformationDO;
import com.yd1994.alpacablog.repository.SysInformationRepository;
import com.yd1994.alpacablog.service.SysInformationService;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * 系统信息 service 实现
 *
 * @author yd
 */
@Service
@CacheConfig(cacheNames = "sys_information")
public class SysInformationServiceImpl implements SysInformationService {

    @Autowired
    private SysInformationRepository sysInformationRepository;

    @Cacheable(key = "#id", unless = "#result == null")
    @Override
    public SysInformation get(Long id) {
        SysInformationDO sysInformationDO = this.sysInformationRepository.findFirstByIdAndDelete(id, false);
        if (sysInformationDO == null) {
            throw new ResourceNotFoundException();
        }
        return new SysInformation(sysInformationDO);
    }

    @Cacheable(key = "#name", unless = "#result == null")
    @Override
    public SysInformation get(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new RequestParamErrorException("name不能为空");
        }
        SysInformationDO sysInformationDO = this.sysInformationRepository.findFirstByNameAndDelete(name, false);
        if (sysInformationDO == null) {
            throw new ResourceNotFoundException("资源：" + name + "不存在。");
        }
        return new SysInformation(sysInformationDO);
    }

    @Override
    public void add(SysInformation sysInformation) {
        if (sysInformation == null) {
            throw new RequestParamErrorException();
        }
        if (StringUtils.isEmpty(sysInformation.getName()) || StringUtils.isEmpty(sysInformation.getValue())) {
            throw new RequestParamErrorException("name, value不能为空");
        }
        String name = sysInformation.getName();
        SysInformationDO hasSysInformationDO = this.sysInformationRepository.findFirstByNameAndDelete(name, false);
        if (hasSysInformationDO != null) {
            throw new ResourceNotFoundException("name:" + name + "已经存在。");
        }
        SysInformationDO sysInformationDO = sysInformation.toEntity();
        sysInformationDO.setDelete(false);
        sysInformationDO.setVersion(null);
        Date date = new Date();
        sysInformationDO.setGmtCreated(date);
        sysInformationDO.setGmtModified(date);
        this.sysInformationRepository.saveAndFlush(sysInformationDO);
    }

    @CacheEvict(key = "#id")
    @Override
    public void update(SysInformation sysInformation, Long id) {

        SysInformationDO sysInformationDO = this.sysInformationRepository.findFirstByIdAndDelete(id, false);
        if (sysInformationDO == null) {
            throw new ResourceNotFoundException();
        }
        SysInformationDO targetSysInformationDO = sysInformationDO;
        SysInformationDO sourceSysInformationDO = sysInformation.toEntity();
        this.copyForUpdate(sourceSysInformationDO, targetSysInformationDO);
        this.sysInformationRepository.save(targetSysInformationDO);

    }

    @CacheEvict(key = "#name")
    @Override
    public void update(SysInformation sysInformation, String name) {
        SysInformationDO sysInformationDO = this.sysInformationRepository.findFirstByNameAndDelete(name, false);
        if (sysInformationDO == null) {
            throw new ResourceNotFoundException();
        }
        SysInformationDO targetSysInformationDO = sysInformationDO;
        SysInformationDO sourceSysInformationDO = sysInformation.toEntity();
        this.copyForUpdate(sourceSysInformationDO, targetSysInformationDO);
        this.sysInformationRepository.save(targetSysInformationDO);
    }

    /**
     * 更新， 将原来的entity不为空的值拷贝到目标entity里
     * @param sourceSysInformationDO 原来的entity
     * @param targetSysInformationDO 目标entity
     */
    private void copyForUpdate(SysInformationDO sourceSysInformationDO, SysInformationDO targetSysInformationDO) {
        if (sourceSysInformationDO.getVersion() != null) {
            if (targetSysInformationDO.getVersion() == null) {
                throw new TableVersionNotFoundException("表：SysInformationDO中乐观锁：version为null。");
            }
            if (sourceSysInformationDO.getVersion() != targetSysInformationDO.getVersion()) {
                throw new StaleObjectStateException("SysInformation:" + sourceSysInformationDO.getId() + "已经被修改", sourceSysInformationDO);
            }
            targetSysInformationDO.setVersion(sourceSysInformationDO.getVersion());
        } else {
            // 乐观锁不能为空
            throw new VersionNotFoundException();
        }
        if (!StringUtils.isEmpty(sourceSysInformationDO.getValue())) {
            targetSysInformationDO.setValue(sourceSysInformationDO.getValue());
        }
        if (!StringUtils.isEmpty(sourceSysInformationDO.getType())) {
            targetSysInformationDO.setType(sourceSysInformationDO.getType());
        }
    }

    @CacheEvict(key = "#id")
    @Override
    public void delete(Long id) {
        SysInformationDO sysInformationDO = this.sysInformationRepository.findFirstByIdAndDelete(id, false);
        if (sysInformationDO == null) {
            throw new ResourceNotFoundException();
        }
        sysInformationDO.setDelete(true);
        sysInformationDO.setGmtModified(new Date());
        this.sysInformationRepository.save(sysInformationDO);
    }

    @CacheEvict(key = "#name")
    @Override
    public void delete(String name) {
        SysInformationDO sysInformationDO = this.sysInformationRepository.findFirstByNameAndDelete(name, false);
        if (sysInformationDO == null) {
            throw new ResourceNotFoundException();
        }
        sysInformationDO.setDelete(true);
        sysInformationDO.setGmtModified(new Date());
        this.sysInformationRepository.save(sysInformationDO);
    }
}
