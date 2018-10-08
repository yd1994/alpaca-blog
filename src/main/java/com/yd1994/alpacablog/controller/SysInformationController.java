package com.yd1994.alpacablog.controller;

import com.yd1994.alpacablog.common.result.ResultFactory;
import com.yd1994.alpacablog.dto.SysInformation;
import com.yd1994.alpacablog.service.SysInformationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/information")
public class SysInformationController {

    @Autowired
    private SysInformationService sysInformationService;

    @GetMapping("/{name}")
    public SysInformation getByKey(@PathVariable("name") String name) {
        return this.sysInformationService.get(name);
    }

    @PutMapping("/{name}")
    public ResultFactory.Info update(@PathVariable("name") String name, SysInformation sysInformation) {
        this.sysInformationService.update(sysInformation, name);
        return ResultFactory.get200Info().message("修改成功。");
    }

}
