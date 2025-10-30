package com.tate.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tate.admin.dao.entity.GroupDO;

public interface GroupService extends IService<GroupDO> {
    void saveGroup(String groupName);
}
