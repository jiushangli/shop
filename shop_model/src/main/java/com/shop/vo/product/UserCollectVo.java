package com.shop.vo.product;

import com.shop.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 创建者： 积云全哥
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCollectVo extends BaseEntity {
    private Long skuId;
    private String skuName;
    private String thumbImg ;
    private BigDecimal salePrice;
}
