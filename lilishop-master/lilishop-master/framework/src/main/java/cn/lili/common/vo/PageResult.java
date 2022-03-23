package cn.lili.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PageResult {

    @ApiModelProperty(value = "当前页")
    private long currentPage;

    @ApiModelProperty(value = "总条数")
    private long total;

    @ApiModelProperty(value = "总页数")
    private long pages;
}
