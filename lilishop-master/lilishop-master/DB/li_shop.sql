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
  `name` VARCHAR(255) DEFAULT NULL COMMENT '价格名称',
  `blind_box_category` VARCHAR(255) DEFAULT NULL COMMENT '种类id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE li_prize

CREATE TABLE `li_prize` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME(6) DEFAULT NULL COMMENT '创建时间',
  `delete_flag` BIT(1) DEFAULT NULL COMMENT '删除标志 true/false 删除/未删除',
  `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME(6) DEFAULT NULL COMMENT '更新时间',
  `goods_id` VARCHAR(255) DEFAULT NULL COMMENT '商品ID',
  `member_id` VARCHAR(255) DEFAULT NULL COMMENT '会员ID',
  `substitution_flag` char(1) DEFAULT NULL COMMENT '置换标识，0表示未置换，1表示已置换',
  `substitution_num` INT DEFAULT NULL COMMENT '置换次数',
  `image` VARCHAR(255) DEFAULT NULL COMMENT '分类图标',
	`blind_box_category` VARCHAR(255) DEFAULT NULL COMMENT '种类id',
  `name` VARCHAR(20) DEFAULT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

drop table li_blind_box_category
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
  `price` DOUBLE(10,2) DEFAULT NULL COMMENT '价格',
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
  `brand_intro` VARCHAR(255)  DEFAULT NULL COMMENT '品牌介绍',
  `name` VARCHAR(255)  DEFAULT NULL COMMENT '品牌名称',
  `goods_unit` VARCHAR(20) DEFAULT NULL COMMENT '计量单位',
  `intro` MEDIUMTEXT DEFAULT NULL COMMENT '商品详情',
  `price` DOUBLE(10,2) DEFAULT NULL COMMENT '商品价格',
  `auth_flag` VARCHAR(255) DEFAULT NULL COMMENT '审核状态',
  `small` VARCHAR(255)  DEFAULT NULL COMMENT '小图路径',
  `big` VARCHAR(255)  DEFAULT NULL COMMENT '大图路径',
  `quantity` INT DEFAULT NULL COMMENT '库存',
  `specs` VARCHAR(255)  DEFAULT NULL COMMENT '规格信息',
  `probability`DOUBLE(10,2) DEFAULT NULL COMMENT '中奖概率',
  `blind_box_category` VARCHAR(255) DEFAULT NULL COMMENT '种类id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE li_member_coupon ADD coupon_name VARCHAR(255)DEFAULT NULL COMMENT '优惠券名称';
ALTER TABLE li_member_coupon ADD `name` VARCHAR(255) DEFAULT NULL COMMENT '盲盒名称';

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
  `discount_price` DOUBLE(10,2) DEFAULT NULL COMMENT '优惠的金额',
  `flow_price` VARCHAR(255) DEFAULT NULL COMMENT '总价格',
  `goods_price` DOUBLE(10,2) DEFAULT NULL COMMENT '商品价格',
  `goods_num` INT DEFAULT NULL COMMENT '商品数量',
  `member_id` VARCHAR(255) DEFAULT NULL COMMENT '会员ID',
  `member_name` VARCHAR(255) DEFAULT NULL COMMENT '用户名',
  `order_status` VARCHAR(255) DEFAULT NULL COMMENT '订单状态',
  `pay_order_no` VARCHAR(255) DEFAULT NULL COMMENT '支付方式返回的交易号',
  `pay_status` VARCHAR(255) DEFAULT NULL COMMENT '付款状态',
  `payment_method` VARCHAR(255) DEFAULT NULL COMMENT '支付方式',
  `payment_time` DATETIME(6) DEFAULT NULL COMMENT '支付时间',
  `coupon_id` VARCHAR(255) DEFAULT NULL COMMENT '优惠券id',
  `sn` VARCHAR(255) DEFAULT NULL COMMENT '订单编号',
  `receivable_no` VARCHAR(255) DEFAULT NULL COMMENT '第三方付款流水号',
  `name` VARCHAR(255) COMMENT '盲盒名称',
  `blind_box_category` VARCHAR(255) DEFAULT NULL COMMENT '种类id',
  `buy_way` VARCHAR(255) COMMENT '购买方式',
  `extract_status` VARCHAR(1) DEFAULT NULL COMMENT '抽取的状态，0表示未抽取，1表示已抽取',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

ALTER TABLE li_order ADD substitution_flag CHAR(1) DEFAULT NULL COMMENT '1表示置换，0表示兑换';
ALTER TABLE li_order ADD  goods_id_path VARCHAR(255) DEFAULT NULL  COMMENT '置换商品编号用逗号隔开';
ALTER TABLE li_order ADD  buy_goods_id VARCHAR(255) DEFAULT NULL  COMMENT '购买商品编号';

drop table li_substitution_order

CREATE TABLE `li_substitution_order` (
  `id` BIGINT NOT NULL COMMENT 'ID',
  `create_by` VARCHAR(255) DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME(6) DEFAULT NULL COMMENT '创建时间',
  `delete_flag` BIT(1) DEFAULT NULL COMMENT '删除标志 true/false 删除/未删除',
  `update_by` VARCHAR(255) DEFAULT NULL COMMENT '更新者',
  `update_time` DATETIME(6) DEFAULT NULL COMMENT '更新时间',
  `cancel_reason` VARCHAR(255) DEFAULT NULL COMMENT '订单取消原因',
  `complete_time` DATETIME(6) DEFAULT NULL COMMENT '完成时间',
  `pay_amount` DOUBLE(10,2) DEFAULT NULL COMMENT '付款金额',
  `goods_num` INT DEFAULT NULL COMMENT '商品数量',
  `member_id` VARCHAR(255) DEFAULT NULL COMMENT '会员ID',
  `member_name` VARCHAR(255) DEFAULT NULL COMMENT '用户名',
  `order_status` VARCHAR(255) DEFAULT NULL COMMENT '订单状态',
  `pay_order_no` VARCHAR(255) DEFAULT NULL COMMENT '支付方式返回的交易号',
  `pay_status` VARCHAR(255) DEFAULT NULL COMMENT '付款状态',
  `payment_method` VARCHAR(255) DEFAULT NULL COMMENT '支付方式',
  `payment_time` DATETIME(6) DEFAULT NULL COMMENT '支付时间',
  `sn` VARCHAR(255) DEFAULT NULL COMMENT '订单编号',
  `receivable_no` VARCHAR(255) DEFAULT NULL COMMENT '第三方付款流水号',
  `goods_id_path` VARCHAR(255) DEFAULT NULL  COMMENT '置换商品编号用逗号隔开',
	`buy_goods_id` VARCHAR(255) DEFAULT NULL  COMMENT '购买商品编号',
	`sku_id` VARCHAR(255) DEFAULT NULL  COMMENT '购买商品skuid',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

alter table `li_goods` add column sinewy_beans INT DEFAULT NULL COMMENT '元气豆数'
alter table `li_goods_sku` add column sinewy_beans INT DEFAULT NULL COMMENT '元气豆数'
alter table `li_coupon` add column goods_type char(1) DEFAULT NULL COMMENT '商品类型，0普通商品，1盲盒商品';
alter table `li_coupon` add column blind_box_category VARCHAR(255) DEFAULT NULL COMMENT '盲盒的种类编号';
alter table `li_coupon` add column name VARCHAR(255) DEFAULT NULL COMMENT '盲盒名称';
alter table `li_coupon_activity` add column goods_type char(1) DEFAULT NULL COMMENT '商品类型，0普通商品，1盲盒商品';
alter table `li_member_coupon` add column goods_type char(1) DEFAULT NULL COMMENT '商品类型，0普通商品，1盲盒商品';