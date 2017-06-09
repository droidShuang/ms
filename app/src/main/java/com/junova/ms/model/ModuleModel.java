package com.junova.ms.model;

import com.junova.ms.bean.Module;

import java.util.List;

/**
 * Created by junova on 2017-03-16.
 */

public class ModuleModel {


    private List<Module> moduleList;

    public List<Module> getModuleList() {
        return moduleList;
    }

    public void setModuleList(List<Module> moduleList) {
        this.moduleList = moduleList;
    }

    public static class ModuleListBean {
    }
}
