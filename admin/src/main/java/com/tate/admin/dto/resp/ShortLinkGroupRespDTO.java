package com.tate.admin.dto.resp;

import lombok.Data;

@Data
public class ShortLinkGroupRespDTO {
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 创建人用户名
     */
    private String username;

    /**
     * 分组排序
     */
    private Integer sortOrder;
}
