package com.tate.admin.controller;

import com.tate.admin.common.convention.result.Result;
import com.tate.admin.common.convention.result.Results;
import com.tate.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.tate.admin.dto.resp.ShortLinkGroupRespDTO;
import com.tate.admin.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/short-link/v1/group")  //v1是指版本号
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PostMapping
    public Result<Void> save(@RequestParam ("groupName")String groupName){
        groupService.saveGroup(groupName);
        return Results.success();
    }

    /**
     * 查询
     */
    @GetMapping
    public Result<List<ShortLinkGroupRespDTO>> listGroup(){
        return Results.success(groupService.listGroup());
    }

    @PutMapping
    public Result<Void> updateGroup(@RequestBody ShortLinkGroupUpdateReqDTO requestParam){
        groupService.updateGroup(requestParam);
        return Results.success();
    }
}
