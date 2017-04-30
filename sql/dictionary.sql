/*
Navicat MySQL Data Transfer

Source Server         : 192.168.188.100-ROOT
Source Server Version : 50172
Source Host           : 192.168.188.100:3306
Source Database       : foxhunter

Target Server Type    : MYSQL
Target Server Version : 50172
File Encoding         : 65001

Date: 2017-01-13 17:20:52
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dictionary
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary` (
  `dictId` varchar(255) NOT NULL,
  `createDate` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `selected` bit(1) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `value` int(11) DEFAULT NULL,
  PRIMARY KEY (`dictId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dictionary
-- ----------------------------
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc56b0000', '2016-12-24 18:28:19', '超级管理员', null, 'managerType', '1');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc5c50001', '2016-12-24 18:28:20', '普通管理员', null, 'managerType', '2');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc6030002', '2016-12-24 18:28:20', '保密', null, 'gender', '0');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc63e0003', '2016-12-24 18:28:20', '男', null, 'gender', '1');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc6760004', '2016-12-24 18:28:20', '女', null, 'gender', '2');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc6a70005', '2016-12-24 18:28:20', '提现中', null, 'billState', '1');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc6ea0006', '2016-12-24 18:28:20', '提现成功', null, 'billState', '2');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc7200007', '2016-12-24 18:28:20', '提现失败', null, 'billState', '3');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc7570008', '2016-12-24 18:28:20', '人民币', null, 'billCurrencyType', '1');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc78b0009', '2016-12-24 18:28:20', '美元', null, 'billCurrencyType', '2');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc7c2000a', '2016-12-24 18:28:20', '英镑', null, 'billCurrencyType', '3');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc7f7000b', '2016-12-24 18:28:20', '正常', null, 'customState', '1');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc82e000c', '2016-12-24 18:28:20', '锁定', null, 'customState', '2');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc86d000d', '2016-12-24 18:28:20', '高危', null, 'blacklistLevel', '3');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc8a5000e', '2016-12-24 18:28:20', '危险', null, 'blacklistLevel', '2');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc8e0000f', '2016-12-24 18:28:20', '一般', null, 'blacklistLevel', '1');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc9200010', '2016-12-24 18:28:20', '待确认', null, 'blacklistOrgType', '1');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc9540011', '2016-12-24 18:28:20', '个人', null, 'blacklistOrgType', '2');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fc9990012', '2016-12-24 18:28:21', '团伙', null, 'blacklistOrgType', '3');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fcabd0017', '2016-12-24 18:28:21', '灰名', null, 'blacklistState', '1');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fcb050018', '2016-12-24 18:28:21', '黑名未验证', null, 'blacklistState', '2');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fcb4c0019', '2016-12-24 18:28:21', '黑名已验证', null, 'blacklistState', '3');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fcbab001a', '2016-12-24 18:28:21', '待审核', null, 'photoCheckState', '1');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fcbf6001b', '2016-12-24 18:28:21', '审核通过', null, 'photoCheckState', '2');
INSERT INTO `dictionary` VALUES ('40283c8c59305fba0159305fcc3e001c', '2016-12-24 18:28:21', '审核未通过', null, 'photoCheckState', '3');
INSERT INTO `dictionary` VALUES ('40283c9e59630587015963058dcc0000', '2017-01-03 14:30:25', '正常', null, 'clientState', '1');
INSERT INTO `dictionary` VALUES ('40283c9e59630587015963058e2a0001', '2017-01-03 14:30:25', '锁定', null, 'clientState', '2');
INSERT INTO `dictionary` VALUES ('40283c9e599065380159906562180000', '2017-01-12 09:58:00', '地推', null, 'blacklistFromType', '100');
INSERT INTO `dictionary` VALUES ('40283c9e599065380159906562660001', '2017-01-12 09:58:00', '地推-商业区', null, 'blacklistFromType', '101');
INSERT INTO `dictionary` VALUES ('40283c9e599065380159906562c40002', '2017-01-12 09:58:00', '地推-休闲区', null, 'blacklistFromType', '102');
INSERT INTO `dictionary` VALUES ('40283c9e599065380159906563020003', '2017-01-12 09:58:00', '地推-交通线', null, 'blacklistFromType', '103');
INSERT INTO `dictionary` VALUES ('40283c9e599065380159906563500004', '2017-01-12 09:58:00', '地推-住宅区', null, 'blacklistFromType', '104');
INSERT INTO `dictionary` VALUES ('40283c9e5990653801599065639e0005', '2017-01-12 09:58:00', '地推-办公区', null, 'blacklistFromType', '105');
INSERT INTO `dictionary` VALUES ('40283c9e5990653801599065640b0006', '2017-01-12 09:58:00', '网络', null, 'blacklistFromType', '200');
INSERT INTO `dictionary` VALUES ('40283c9e599065380159906565c00007', '2017-01-12 09:58:01', '网络-QQ群', null, 'blacklistFromType', '201');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656a520008', '2017-01-12 09:58:02', '网络-微信群', null, 'blacklistFromType', '202');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656b9a0009', '2017-01-12 09:58:02', '网络-QQ空间', null, 'blacklistFromType', '203');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656c16000a', '2017-01-12 09:58:02', '网络-朋友圈', null, 'blacklistFromType', '204');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656c93000b', '2017-01-12 09:58:03', '网络-贴吧', null, 'blacklistFromType', '205');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656d00000c', '2017-01-12 09:58:03', '网络-论坛', null, 'blacklistFromType', '206');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656d5e000d', '2017-01-12 09:58:03', '网络-知道', null, 'blacklistFromType', '207');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656dea000e', '2017-01-12 09:58:03', '网络-广告', null, 'blacklistFromType', '208');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656e48000f', '2017-01-12 09:58:03', '推算', null, 'blacklistFromType', '300');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656ef40010', '2017-01-12 09:58:03', '推算-智能算法', null, 'blacklistFromType', '301');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656f700011', '2017-01-12 09:58:03', '推算-直接关联', null, 'blacklistFromType', '302');
INSERT INTO `dictionary` VALUES ('40283c9e59906538015990656fbe0012', '2017-01-12 09:58:03', '推算-人工推测', null, 'blacklistFromType', '303');
INSERT INTO `dictionary` VALUES ('40283c9e5990653801599065700c0013', '2017-01-12 09:58:04', '举报', null, 'blacklistFromType', '400');
INSERT INTO `dictionary` VALUES ('40283c9e5990653801599065705a0014', '2017-01-12 09:58:04', '举报-广告推送', null, 'blacklistFromType', '401');
INSERT INTO `dictionary` VALUES ('40283c9e599065380159906570a80015', '2017-01-12 09:58:04', '举报-宣传营销', null, 'blacklistFromType', '402');
INSERT INTO `dictionary` VALUES ('40283c9e599065380159906571060016', '2017-01-12 09:58:04', '举报-诈骗诱骗', null, 'blacklistFromType', '403');
INSERT INTO `dictionary` VALUES ('40283c9e599065380159906571540017', '2017-01-12 09:58:04', '举报-信息搔扰', null, 'blacklistFromType', '404');
INSERT INTO `dictionary` VALUES ('40283c9e599073e801599073ef160000', '2017-01-12 10:13:54', '地推-生产区', null, 'blacklistFromType', '106');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b968e0000', '2017-01-13 09:05:24', '小学', null, 'gradute', '1');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b96dc0001', '2017-01-13 09:05:24', '初中', null, 'gradute', '2');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b972a0002', '2017-01-13 09:05:24', '职高', null, 'gradute', '3');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b97680003', '2017-01-13 09:05:24', '中专', null, 'gradute', '4');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b97c60004', '2017-01-13 09:05:24', '高中', null, 'gradute', '5');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b97f40005', '2017-01-13 09:05:24', '大专', null, 'gradute', '6');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b98420006', '2017-01-13 09:05:25', '本科', null, 'gradute', '7');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b98810007', '2017-01-13 09:05:25', '本科学士', null, 'gradute', '8');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b98cf0008', '2017-01-13 09:05:25', '研究生', null, 'gradute', '9');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b990d0009', '2017-01-13 09:05:25', '研究生硕士', null, 'gradute', '10');
INSERT INTO `dictionary` VALUES ('40283c9e59955b610159955b994c000a', '2017-01-13 09:05:25', '研究生博士', null, 'gradute', '11');
