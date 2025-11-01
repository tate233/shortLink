package com.tate.admin.dto.req;

import lombok.Data;

@Data
public class ShortLinkGroupSortReqDTO {
    private String gid;

    private Integer sortOrder; //排序字段

}
