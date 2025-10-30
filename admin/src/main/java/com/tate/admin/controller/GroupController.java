package com.tate.admin.controller;

import com.tate.admin.common.convention.result.Result;
import com.tate.admin.common.convention.result.Results;
import com.tate.admin.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
