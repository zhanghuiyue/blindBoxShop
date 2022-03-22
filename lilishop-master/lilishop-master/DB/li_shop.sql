DROP TABLE li_price
CREATE TABLE `li_price` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME(6) DEFAULT NULL COMMENT '创建时间',
  `delete_flag` BIT(1) DEFAULT NULL COMMENT '删除标志 true/false 删除/未删除',
  `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME(6) DEFAULT NULL COMMENT '更新时间',
  `price` DOUBLE(10,2) DEFAULT NULL COMMENT '价格',
  `original_price` DOUBLE(10,2) DEFAULT NULL COMMENT '原价',
  `num` INT DEFAULT NULL COMMENT '数量',
  `blind_box_category` VARCHAR(255) DEFAULT NULL COMMENT '种类id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `li_prize` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME(6) DEFAULT NULL COMMENT '创建时间',
  `delete_flag` BIT(1) DEFAULT NULL COMMENT '删除标志 true/false 删除/未删除',
  `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME(6) DEFAULT NULL COMMENT '更新时间',
  `goods_id` VARCHAR(255) DEFAULT NULL COMMENT '商品ID',
  `mobile` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `substitution_goods_id` VARCHAR(255) DEFAULT NULL COMMENT '置换商品的id',
  `sku_id` VARCHAR(255) DEFAULT NULL COMMENT '商品的skuid',
  `substitution_sku_id` VARCHAR(255) DEFAULT NULL COMMENT '置换商品的skuid',
  `substitution_num` INT DEFAULT NULL COMMENT '置换次数',
  `open_box_flag` CHAR(1) DEFAULT NULL COMMENT '开盒标识,0表示未开盒，1表示已开盒',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE li_goods ADD substitution_flag CHAR(1) COMMENT '1表示置换，0表示奖品';
ALTER TABLE li_goods_sku ADD probability FLOAT(10,2) COMMENT '中奖概率';

CREATE TABLE `li_blind_box_category` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME(6) DEFAULT NULL COMMENT '创建时间',
  `delete_flag` BIT(1) DEFAULT NULL COMMENT '删除标志 true/false 删除/未删除',
  `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME(6) DEFAULT NULL COMMENT '更新时间',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '分类图标',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '分类名称',
  `sort_order` INT DEFAULT NULL COMMENT '排序值',
  `label` VARCHAR(255) DEFAULT NULL COMMENT '盲盒的标签',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE li_blind_box_goods
CREATE TABLE `li_blind_box_goods` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME(6) DEFAULT NULL COMMENT '创建时间',
  `delete_flag` BIT(1) DEFAULT NULL COMMENT '删除标志 true/false 删除/未删除',
  `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME(6) DEFAULT NULL COMMENT '更新时间',
  `auth_message` INT DEFAULT NULL COMMENT '审核信息',
  `goods_name` VARCHAR(255) DEFAULT NULL COMMENT '商品名称',
  `logo` VARCHAR(255)  DEFAULT NULL COMMENT '品牌图标',
  `name` VARCHAR(255)  DEFAULT NULL COMMENT '品牌名称',
  `goods_unit` VARCHAR(20) DEFAULT NULL COMMENT '计量单位',
  `intro` MEDIUMTEXT DEFAULT NULL COMMENT '商品详情',
  `price` DOUBLE(10,2) DEFAULT NULL COMMENT '商品价格',
  `auth_flag` VARCHAR(255) DEFAULT NULL COMMENT '审核状态',
  `small` VARCHAR(255)  DEFAULT NULL COMMENT '小图路径',
  `big` VARCHAR(255)  DEFAULT NULL COMMENT '大图路径',
  `quantity` INT DEFAULT NULL COMMENT '库存',
  `probability` FLOAT(10,2) COMMENT '中奖概率',
  `blind_box_category` VARCHAR(255) DEFAULT NULL COMMENT '种类id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE li_member_coupon ADD coupon_name VARCHAR(255) COMMENT '优惠券名称';
ALTER TABLE li_member_coupon ADD `name` VARCHAR(255) COMMENT '盲盒名称';

DROP TABLE li_blind_box_order
CREATE TABLE `li_blind_box_order` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME(6) DEFAULT NULL COMMENT '创建时间',
  `delete_flag` BIT(1) DEFAULT NULL COMMENT '删除标志 true/false 删除/未删除',
  `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME(6) DEFAULT NULL COMMENT '更新时间',
  `cancel_reason` VARCHAR(255) DEFAULT NULL COMMENT '订单取消原因',
  `complete_time` DATETIME(6) DEFAULT NULL COMMENT '完成时间',
  `consignee_address_id_path` VARCHAR(255) DEFAULT NULL COMMENT '地址id ,分割',
  `consignee_address_path` VARCHAR(255) DEFAULT NULL COMMENT '地址名称 ,分割',
  `consignee_detail` VARCHAR(255) DEFAULT NULL COMMENT '详细地址',
  `consignee_mobile` VARCHAR(255) DEFAULT NULL COMMENT '收件人手机',
  `consignee_name` VARCHAR(255) DEFAULT NULL COMMENT '收件人姓名',
  `deliver_status` VARCHAR(255) DEFAULT NULL COMMENT '货运状态',
  `discount_price` DOUBLE(10,2) DEFAULT NULL COMMENT '优惠的金额',
  `flow_price` VARCHAR(255) DEFAULT NULL COMMENT '总价格',
  `freight_price` DOUBLE(10,2) DEFAULT NULL COMMENT '运费',
  `goods_num` INT DEFAULT NULL COMMENT '商品数量',
  `logistics_code` VARCHAR(255) DEFAULT NULL COMMENT '物流公司CODE',
  `logistics_name` VARCHAR(255) DEFAULT NULL COMMENT '物流公司名称',
  `logistics_no` VARCHAR(255) DEFAULT NULL COMMENT '发货单号',
  `logistics_time` DATETIME(6) DEFAULT NULL COMMENT '送货时间',
  `member_id` VARCHAR(255) DEFAULT NULL COMMENT '会员ID',
  `member_name` VARCHAR(255) DEFAULT NULL COMMENT '用户名',
  `order_status` VARCHAR(255) DEFAULT NULL COMMENT '订单状态',
  `pay_order_no` VARCHAR(255) DEFAULT NULL COMMENT '支付方式返回的交易号',
  `pay_status` VARCHAR(255) DEFAULT NULL COMMENT '付款状态',
  `payment_method` VARCHAR(255) DEFAULT NULL COMMENT '支付方式',
  `payment_time` DATETIME(6) DEFAULT NULL COMMENT '支付方式返回的交易号',
  `qr_code` VARCHAR(255) DEFAULT NULL COMMENT '提货码',
  `delivery_method` VARCHAR(255) DEFAULT NULL COMMENT '配送方式',
  `coupon_id` VARCHAR(255) DEFAULT NULL COMMENT '优惠券id',
  `sn` VARCHAR(255) DEFAULT NULL COMMENT '订单编号',
  `receivable_no` VARCHAR(255) DEFAULT NULL COMMENT '第三方付款流水号',
  `name` VARCHAR(255) COMMENT '盲盒名称',
  `blind_box_category` VARCHAR(255) DEFAULT NULL COMMENT '种类id',
  `extract_status` VARCHAR(1) DEFAULT NULL COMMENT '抽取的状态，0表示未抽取，1表示已抽取',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;





