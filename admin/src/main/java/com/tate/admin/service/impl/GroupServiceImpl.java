package com.tate.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tate.admin.dao.entity.GroupDO;
import com.tate.admin.dao.mapper.GroupMapper;
import com.tate.admin.service.GroupService;
import com.tate.admin.toolkit.RandomGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    /**
     * 新增短链接分组
     * @param groupName
     */
    @Override
    public void saveGroup(String groupName) {
        String gid;
        while(true){
            gid = RandomGenerator.generateRandom();
            if(!hasGid(gid)){
                break;
            }
        }
        GroupDO groupDO = GroupDO.builder()
                .gid(RandomGenerator.generateRandom())
                .name(groupName)
                .build();
        baseMapper.insert(groupDO);
    }
    private boolean hasGid(String gid){
        LambdaQueryWrapper<GroupDO> lambdaQueryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                //TODO 设置用户名
                .eq(GroupDO::getUsername,null);
        GroupDO hasGroupFlag = baseMapper.selectOne(lambdaQueryWrapper);
        return hasGroupFlag != null;
    }
}
