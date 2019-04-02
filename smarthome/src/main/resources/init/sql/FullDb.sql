/*
SQLyog Ultimate v11.3 (64 bit)
MySQL - 5.6.10-log : Database - spms
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`spms` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `spms`;

/*Table structure for table `act_evt_log` */

DROP TABLE IF EXISTS `act_evt_log`;

CREATE TABLE `act_evt_log` (
  `LOG_NR_` bigint(20) NOT NULL AUTO_INCREMENT,
  `TYPE_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TIME_STAMP_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DATA_` longblob,
  `LOCK_OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  `IS_PROCESSED_` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`LOG_NR_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_evt_log` */

/*Table structure for table `act_ge_bytearray` */

DROP TABLE IF EXISTS `act_ge_bytearray`;

CREATE TABLE `act_ge_bytearray` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BYTES_` longblob,
  `GENERATED_` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_FK_BYTEARR_DEPL` (`DEPLOYMENT_ID_`),
  CONSTRAINT `ACT_FK_BYTEARR_DEPL` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ge_bytearray` */

insert  into `act_ge_bytearray`(`ID_`,`REV_`,`NAME_`,`DEPLOYMENT_ID_`,`BYTES_`,`GENERATED_`) values ('13aa8022-54dc-11e9-8d3a-64006a862ca5',1,'D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\jszc.bpmn','13aa5911-54dc-11e9-8d3a-64006a862ca5','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.activiti.org/test\">\r\n  <process id=\"jszc\" name=\"ÊäÄÊúØÊîØÊåÅ\" isExecutable=\"true\">\r\n    <startEvent id=\"startevent1\" name=\"Start\"></startEvent>\r\n    <userTask id=\"usertask1\" name=\"Â∑•ÂçïÁîüÊàê\"></userTask>\r\n    <sequenceFlow id=\"flow1\" name=\"Êèê‰∫§\" sourceRef=\"startevent1\" targetRef=\"usertask1\"></sequenceFlow>\r\n    <userTask id=\"usertask2\" name=\"‰∏äÈó®Áª¥‰øÆ\"></userTask>\r\n    <sequenceFlow id=\"flow2\" name=\"Êèê‰∫§\" sourceRef=\"usertask1\" targetRef=\"usertask2\"></sequenceFlow>\r\n    <userTask id=\"usertask3\" name=\"ÁîµËØùÂõûËÆø\"></userTask>\r\n    <sequenceFlow id=\"flow3\" name=\"Êèê‰∫§\" sourceRef=\"usertask2\" targetRef=\"usertask3\"></sequenceFlow>\r\n    <endEvent id=\"endevent1\" name=\"End\"></endEvent>\r\n    <sequenceFlow id=\"flow4\" name=\"Êèê‰∫§\" sourceRef=\"usertask3\" targetRef=\"endevent1\"></sequenceFlow>\r\n  </process>\r\n  <bpmndi:BPMNDiagram id=\"BPMNDiagram_jszc\">\r\n    <bpmndi:BPMNPlane bpmnElement=\"jszc\" id=\"BPMNPlane_jszc\">\r\n      <bpmndi:BPMNShape bpmnElement=\"startevent1\" id=\"BPMNShape_startevent1\">\r\n        <omgdc:Bounds height=\"35.0\" width=\"35.0\" x=\"110.0\" y=\"200.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask1\" id=\"BPMNShape_usertask1\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"190.0\" y=\"190.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask2\" id=\"BPMNShape_usertask2\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"340.0\" y=\"190.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask3\" id=\"BPMNShape_usertask3\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"490.0\" y=\"190.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"endevent1\" id=\"BPMNShape_endevent1\">\r\n        <omgdc:Bounds height=\"35.0\" width=\"35.0\" x=\"640.0\" y=\"200.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow1\" id=\"BPMNEdge_flow1\">\r\n        <omgdi:waypoint x=\"145.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"190.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"155.0\" y=\"217.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow2\" id=\"BPMNEdge_flow2\">\r\n        <omgdi:waypoint x=\"295.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"340.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"305.0\" y=\"217.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow3\" id=\"BPMNEdge_flow3\">\r\n        <omgdi:waypoint x=\"445.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"490.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"455.0\" y=\"217.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow4\" id=\"BPMNEdge_flow4\">\r\n        <omgdi:waypoint x=\"595.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"640.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"605.0\" y=\"217.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n    </bpmndi:BPMNPlane>\r\n  </bpmndi:BPMNDiagram>\r\n</definitions>',0),('13aa8023-54dc-11e9-8d3a-64006a862ca5',1,'D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\unsubscribe.bpmn','13aa5911-54dc-11e9-8d3a-64006a862ca5','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.activiti.org/test\">\r\n  <process id=\"unsubscribe\" name=\"‰∏öÂä°ÈÄÄËÆ¢\" isExecutable=\"true\">\r\n    <startEvent id=\"startevent1\" name=\"Start\"></startEvent>\r\n    <userTask id=\"usertask1\" name=\"ÂàõÂª∫Â∑•Âçï\"></userTask>\r\n    <sequenceFlow id=\"flow1\" name=\"ÂêØÂä®\" sourceRef=\"startevent1\" targetRef=\"usertask1\"></sequenceFlow>\r\n    <userTask id=\"usertask2\" name=\"Ê¨†Ë¥πÁ°ÆËÆ§\"></userTask>\r\n    <sequenceFlow id=\"flow2\" name=\"Êèê‰∫§\" sourceRef=\"usertask1\" targetRef=\"usertask2\"></sequenceFlow>\r\n    <userTask id=\"usertask3\" name=\"ÂõûÊî∂ËÆæÂ§á\"></userTask>\r\n    <sequenceFlow id=\"flow3\" name=\"Á°ÆÂÆö\" sourceRef=\"usertask2\" targetRef=\"usertask3\"></sequenceFlow>\r\n    <userTask id=\"usertask4\" name=\"ÁîµËØùÂõûËÆø\"></userTask>\r\n    <sequenceFlow id=\"flow4\" name=\"Êèê‰∫§\" sourceRef=\"usertask3\" targetRef=\"usertask4\"></sequenceFlow>\r\n    <endEvent id=\"endevent1\" name=\"End\"></endEvent>\r\n    <sequenceFlow id=\"flow5\" name=\"ÁªìÊùü\" sourceRef=\"usertask4\" targetRef=\"endevent1\"></sequenceFlow>\r\n  </process>\r\n  <bpmndi:BPMNDiagram id=\"BPMNDiagram_unsubscribe\">\r\n    <bpmndi:BPMNPlane bpmnElement=\"unsubscribe\" id=\"BPMNPlane_unsubscribe\">\r\n      <bpmndi:BPMNShape bpmnElement=\"startevent1\" id=\"BPMNShape_startevent1\">\r\n        <omgdc:Bounds height=\"35.0\" width=\"35.0\" x=\"180.0\" y=\"200.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask1\" id=\"BPMNShape_usertask1\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"260.0\" y=\"190.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask2\" id=\"BPMNShape_usertask2\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"410.0\" y=\"190.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask3\" id=\"BPMNShape_usertask3\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"560.0\" y=\"190.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask4\" id=\"BPMNShape_usertask4\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"710.0\" y=\"190.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"endevent1\" id=\"BPMNShape_endevent1\">\r\n        <omgdc:Bounds height=\"35.0\" width=\"35.0\" x=\"860.0\" y=\"200.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow1\" id=\"BPMNEdge_flow1\">\r\n        <omgdi:waypoint x=\"215.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"260.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"24.0\" x=\"225.0\" y=\"217.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow2\" id=\"BPMNEdge_flow2\">\r\n        <omgdi:waypoint x=\"365.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"410.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"24.0\" x=\"375.0\" y=\"217.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow3\" id=\"BPMNEdge_flow3\">\r\n        <omgdi:waypoint x=\"515.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"560.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"24.0\" x=\"525.0\" y=\"217.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow4\" id=\"BPMNEdge_flow4\">\r\n        <omgdi:waypoint x=\"665.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"710.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"24.0\" x=\"675.0\" y=\"217.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow5\" id=\"BPMNEdge_flow5\">\r\n        <omgdi:waypoint x=\"815.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"860.0\" y=\"217.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"24.0\" x=\"825.0\" y=\"217.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n    </bpmndi:BPMNPlane>\r\n  </bpmndi:BPMNDiagram>\r\n</definitions>',0),('13aa8024-54dc-11e9-8d3a-64006a862ca5',1,'D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\OpenService.bpmn','13aa5911-54dc-11e9-8d3a-64006a862ca5','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" xmlns:tns=\"http://www.activiti.org/test\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.activiti.org/test\" id=\"m1422945917652\" name=\"\">\r\n  <process id=\"openService\" name=\"‰∏öÂä°ÂºÄÈÄö\" isExecutable=\"true\" isClosed=\"false\" processType=\"None\">\r\n    <documentation>‰∏öÂä°ÂºÄÈÄö</documentation>\r\n    <startEvent id=\"startevent1\" name=\"Start\"></startEvent>\r\n    <userTask id=\"usertask1\" name=\"Â∑•ÂçïÁîüÊàê\"></userTask>\r\n    <sequenceFlow id=\"flow1\" name=\"Êèê‰∫§\" sourceRef=\"startevent1\" targetRef=\"usertask1\"></sequenceFlow>\r\n    <userTask id=\"usertask2\" name=\"ÂàõÂª∫ËÆ¢Êà∑\"></userTask>\r\n    <sequenceFlow id=\"flow2\" name=\"Êèê‰∫§\" sourceRef=\"usertask1\" targetRef=\"usertask2\"></sequenceFlow>\r\n    <userTask id=\"usertask3\" name=\"ËÆ¢Ë¥≠ÂïÜÂìÅ\"></userTask>\r\n    <sequenceFlow id=\"flow3\" name=\"Êèê‰∫§\" sourceRef=\"usertask2\" targetRef=\"usertask3\"></sequenceFlow>\r\n    <userTask id=\"usertask4\" name=\"ÂÆâË£ÖÊøÄÊ¥ª\"></userTask>\r\n    <sequenceFlow id=\"flow4\" name=\"Êèê‰∫§\" sourceRef=\"usertask3\" targetRef=\"usertask4\"></sequenceFlow>\r\n    <userTask id=\"usertask5\" name=\"ÁîµËØùÂõûËÆø\"></userTask>\r\n    <sequenceFlow id=\"flow5\" name=\"ÊøÄÊ¥ª\" sourceRef=\"usertask4\" targetRef=\"usertask5\"></sequenceFlow>\r\n    <endEvent id=\"endevent1\" name=\"End\"></endEvent>\r\n    <sequenceFlow id=\"flow6\" name=\"ÂÆåÊàê\" sourceRef=\"usertask5\" targetRef=\"endevent1\"></sequenceFlow>\r\n  </process>\r\n  <bpmndi:BPMNDiagram id=\"BPMNDiagram_openService\">\r\n    <bpmndi:BPMNPlane bpmnElement=\"openService\" id=\"BPMNPlane_openService\">\r\n      <bpmndi:BPMNShape bpmnElement=\"startevent1\" id=\"BPMNShape_startevent1\">\r\n        <omgdc:Bounds height=\"35.0\" width=\"35.0\" x=\"80.0\" y=\"170.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask1\" id=\"BPMNShape_usertask1\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"170.0\" y=\"160.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask2\" id=\"BPMNShape_usertask2\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"380.0\" y=\"160.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask3\" id=\"BPMNShape_usertask3\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"560.0\" y=\"160.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask4\" id=\"BPMNShape_usertask4\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"732.0\" y=\"160.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask5\" id=\"BPMNShape_usertask5\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"880.0\" y=\"160.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"endevent1\" id=\"BPMNShape_endevent1\">\r\n        <omgdc:Bounds height=\"35.0\" width=\"35.0\" x=\"1040.0\" y=\"170.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow1\" id=\"BPMNEdge_flow1\">\r\n        <omgdi:waypoint x=\"115.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"170.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"125.0\" y=\"187.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow2\" id=\"BPMNEdge_flow2\">\r\n        <omgdi:waypoint x=\"275.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"380.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"285.0\" y=\"187.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow3\" id=\"BPMNEdge_flow3\">\r\n        <omgdi:waypoint x=\"485.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"560.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"495.0\" y=\"187.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow4\" id=\"BPMNEdge_flow4\">\r\n        <omgdi:waypoint x=\"665.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"732.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"675.0\" y=\"187.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow5\" id=\"BPMNEdge_flow5\">\r\n        <omgdi:waypoint x=\"837.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"880.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"847.0\" y=\"187.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow6\" id=\"BPMNEdge_flow6\">\r\n        <omgdi:waypoint x=\"985.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"1040.0\" y=\"187.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"999.0\" y=\"187.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n    </bpmndi:BPMNPlane>\r\n  </bpmndi:BPMNDiagram>\r\n</definitions>',0),('13aa8025-54dc-11e9-8d3a-64006a862ca5',1,'D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\OpenService.png','13aa5911-54dc-11e9-8d3a-64006a862ca5','âPNG\r\n\Z\n\0\0\0\rIHDR\0\0Q\0\0ç\0\0\0Vøy\0\0RIDATxúÌ›ol˜ôp\"◊–¿*zmåæ1}aik¢wÄÙ™b\n4bÅ6&–¿f˚B!ƒp8«(ä—¬v—≥*ç+C◊^\\¢ÖØ,2úÌ™;U[ŸßãmYé£Ñq¨3}≤sJd%t-Ÿ¥˛P”◊∑^ÔÓg…ùùùg?j5˚oûÔ˛væ\"Eé%\0\0\0qçU˝\0\0\0\0J§Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:\0\0ôŒ\0\0D¶Û\0\0\0ëÈ<\0\0@d:≥æææ¥¥4777===1116™“ÁûÅŸŸŸ≈≈≈µµµ™«ú‘5H]\'ŸhÂl»@C3`v\ruú]H:9vÏX˙ Lóß«{ÏÈgûyÌÏŸ´◊ÆçÊñ>˜Ù=ztˇ˛˝È1I◊©™áñ‘I]Ÿê\r®oÃÆæ≥ãJÁ·¶µµµ˘˘˘}˚ˆù{Û‹’kWm≠€Ö∑/§ÎTzpŒü?_ı†Bë:©À\"≤!ıÕÄŸ’wv±È<‹\\û¶¶¶æıËÔ^Ωv≈ñµ=Ò‰ªwÔ^]]≠z\\AHù‘eë\rŸêÅ˙f¿ÏÍ;ªtí˘˘˘ÕÂÈ[˛ˆÙ3ﬂﬁª˜ÔV=Æ §NÍ≤»Ül»@}3`vıù]x:œ®;vÏÿæ}üøv}›Vd˚˙o˛ÛÉ´Í°’û‘I]Ÿê\r®oÃÆæ≥:œH[__üòòxÛ≠’k◊ﬂ∑Ÿ.]˛≈w¸Ìïïï™GWcR\'uYdC6d†æ0ª˙ŒnDË<#mqqq~~˛⁄ı˜ö€Áf˛a¡üΩÿz≠ë⁄¸◊UèÆ∆§NÍ≤»Ül»@}3`vıù›à–yF⁄ÏÏÏ—«ˇ˚GÀ”Á˜˝Yíº±ë¸¯ZÚÉ+…ÀWí.]?¸¯w˛≠wæ{5˘„ı‰ªÎ…â˜íoê¸áØ‹s‡´ï/ïl/û:955UıËjLÍ§.ãl»Ü‘7fWﬂŸçùg§MNNû9Û“µÎó€ÿÿÿ•$π|#yÁzÚÛ´…{IÚœ˛≈cü¯K{>?˚÷µ‰‹ï‰‹…ÍzÚ⁄ı‰˜ˇËÂÕòπ<Ç€•À«««´]çIù‘eë\rŸêÅ˙f¿ÏÍ;ª°Ût±æææºº¸‡ÉŒŒŒNˇπôôôÙí•••HøCw◊Æ]o_|Î⁄ıKç-]tﬁ€HÆl‹¸´Î7n~¸Éß˛Á_ˇÛ_>ÙçÎIÚÓı‰ùk…œØ%ÈÛˇˆÛﬂ€\\°.çÊñ>˜jWkR\'uYdC6d†æ0ª˙ŒÆ_rŒü+ˇïDqt_§£JgìvÓtZÈÑé}¸Ègæ”ÿûxÚ©˚Ô`nn.}UÔŸ≥\'∆Ø—›\\eﬁmnÈ/_O.æü\\›¯pá¥‡˝˚ﬂ˛ùﬂ˙wø}Ó\'7ìzÌFrusÂzÓ≈ó€Æ;R[§j§NÍ≤»Ül»@}3`vıù›Œ•ÁœÈôÛñˇqkjjÍ»ë#i5™‰AF8–}q˙ÙÈ¥å¶KªÕ’k◊Û∑œ>∑ˇó“…?~ºÍæ#õ´Ãˇkn7W®ç‰¬{…O/%◊í‰Íï+ì∑ﬂ>6ˆKcc„_¸\'˚[Ø¯¬©ó⁄Æ;R[å™*R\'uYdC6d†æ0ª˙Œn\'“3·={ˆl˝s*ZLLL§iµﬁ∫_:îòßü˘Œñmßu{·≈S{˜Ó=p‡@UÖuÁ6Wôwö[„+—ﬂO~ÚnÚ≥íŒ¸…g>Ûô_˙ÒS∑¸Ú‹?˝ÿ\nı¸ãß⁄Æ;R[›W®jIù‘eë\rŸêÅ˙f¿ÏÍ;ªÌIœ~ÁÁÁ;øíÛıØˇfÎ˜I•€˝˜?∞oﬂæ∂=ggg¸øEÍz†˚%ÿ‹‹‹¬¬BOmßu˚Ê#èLOOW˛Mä€≥π ¨5∑ÙèÔﬂH~æ~Û_e.|ê<Û¸ ﬂ∫„W˘Ø˛µ[v˝Âﬂ˘/øˇAí\\∫íºwÂÊOú|æÌ∫#µ’wÖ\ZR\'uYdC6d†æ0ª˙Œn“Ûﬁ∂/Ô‹{Ô◊^;{6ÁÑ˘¬€”sÊ[oΩµı>ßOüÿcÆÂÅÓó¥§[ZZ⁄v·i~´[z;´´´U?°û•Åª~˝Õ≠Ò¬K©wÆ%ø∏ö¸‰›kœΩ≤z«ﬂ˘µœ˝˝≥+…O◊ìü≠\'Ø$È\Zu‚ªœ∑]w§∂öÆPCBÍ§.ãl»Ü‘7fWﬂŸı*=„MÎJ≥∫ÃÃÃ‰∑ù∂Êsˇ˝4Ø;>>~Ú‰…¡<Ï˙Ë>öõõ€y·i~ü[Z{j˜Mnm´ÃÙÁˆ~Ôá?~·ïïìßWû=µÚÏKØ>˜Ω≥ˇÎª?¯£W^ˇ?ß^˝øß~ÙÏK?:˘Ú´Ø¸¯O˘èèﬁu˜]ïØV®:í:©À\"≤!ıÕÄŸ’wv=i|¡†YZ:xpßÕè=ˆ{i€i~µg0ﬂ-U≥›Gá⁄…∑¥unGè>>;;[ı”ÍÕÊ\nıÛ÷Ì◊˝´c¸„/~±Ìä#µ’nÖ\Z*R\'uYdC6d†æ0ª˙ŒÆ\'sssÕŸ•ßæ;˘j¡Æ]ª\Z∑3===ÄG^≥›/ßOüûööÍc·il˜‹Û’#GéT˝‰z∞πB]¥ı∫’nÖ\Z*R\'uYdC6d†æ0ª˙ŒÆ∏ÂÂÂf·ŸﬁWxZ∑\'û|™yká.˚¡◊È@˜QZx“~Ÿ˜ŒsÓÕ∑nªm¢Fﬂ·vsÖ⁄x€÷ÎVØjÿHù‘eë\rŸêÅ˙f¿ÏÍ;ª‚“ÛÁFEôôôÈÀôsÛˇˆ‹zÎ≠eü?◊È@˜K„\'ıΩ4∂{Ô˝⁄\0™jølÆPlΩnıZ°Üç‘I]Ÿê\r®oÃÆæ≥+hii©—O∆««œΩ˘V_NõﬂΩtπ˘ì‹ÓªÔæRmtMOO˜˙´xz˙RO:º™übQõ+‘œlΩn5Z°Üê‘I]Ÿê\r®oÃÆæ≥+(=nîì{Ô˝ZœúøıË£çõ›µkW©èø6∫_÷÷÷“cZR·ilªwÔÿœ›€°ÕÍß∂^∑\Z≠PCHÍ§.ãl»Ü‘7fWﬂŸëû?7ˇÔMøæ»”‹nªm¢qÀ«è/Ô)‘„@˜—‚‚‚¸¸|©ùÁ°É™~¢YZZ ˙›Aõ+‘y[Ø[]V®\nIù‘eë\rŸêÅ˙f¿ÏÍ;ª\"rÊõû?7∆ZﬂœúÔπÁ√ÒWÍ˘ÛË¡8p‡¿ë#GJÌ<O?Ûù¡¸–Ω\"^}ı’Få>˝ÈO=z¥Ìo≠PV®!$uRóE6dCÍõ≥´ÔÏä»üoz˛‹¯€ùˇ∏∂ÆgŒçﬂ≥gOyOpXÙ¿ÃÃÃ<Ò‰S•vû◊ŒûΩÌ∂â™üËá÷◊◊?ÒâO4íÙ…O~Ú≥ü˝lkÉﬂ\\°˛¨™≠ÛﬁsO¡á⁄ÿ≠ÏÁ5<+‘p™0u]o<Î{⁄yá…î∫ÜJ≤±çõ›Ú*˘ãU\'Ÿh™ˆ]iÀ’†uáÇoIE“RvìÅ·ú]÷_m„∞∑Ì<òÂ}x^ø˘ÛMœüU∆YtzÊ‹∏ÒââœüáÂ@LI?•∫u{˜“ÂÒÒÒ™üËGö?£·Sü˙‘óø¸Â∆Lˇ∏±Òìæo]ﬂÚ:˜Ï˙yÁÖjØ˚o˚	V=’a7¯‘5áû≥÷Ê\'3kü¨G^jÿ‚•Æ⁄ltùi◊K:o!_N NHÌ≤QI∂GÁËªÆ_L≤Æ≤Â∫Q”T8ª¸€Ô|-wÓ∞ç◊Ô`9òŸë3ﬂÊO©.È,∫yßÂ=ª!:–Éëé≥Ôˇı™Î‰ö?›b»mlºUÍñuRÛè€∏ùÚû›X}Ê;ú N⁄ÿ«c”ıØä§+\'∆~v£ì∫íé^¡¿¥f¶Î£Í|Ñ€ªD6rîq|äﬂc˛>c-K X«Ú“u‹e<£°Õ¿ g∑Â∑º$Á<ª°ö›ñöuËµ≥gÀ8snﬁ~y`‰:œ‰‰‰˜œú@Á)¯xö{∂e+gœ^›rÀ-Õõù›îæ∫VVVíˇUÊÕR∑¨ªhΩ<ÎÛ∆ª ŸøÏg‘∏ó·ôÔp\ZpÍ:o≥qIÎ«÷?Êá™¯Ìº‘5Uª\"uùT[ZÚWïúıß◊¥ål6*\\Úßìˇ˛“∂[Áz≤ÂΩC\ZZ˜ﬂÚ∫≠9íŸmy_=ΩÎ5ªúQnπ€ñ;ÁÃ7=n\\^“YÙ¯¯x÷r‘/°N≥ä(ıóÛ4∂û˛?OÎ;PÁÖY;◊¸ŸÇiXøÚïØ§óññZopc„\\©[÷]åeÎz›ú€)rw}RC2ﬂ·TIÍ∫¶®ÒIÎ«Æii˝§-ÑYπ*H©k3¯lôi◊ú‰ƒ#g’ ⁄≠§T‘1’æ+Â‹~ÁBëˇ2Ô\\jÚ3Pjäk¨£Òv∫sü÷ø≠‰ı[‰Û÷âdÛ±n/‰ŒÅ	¿‡g◊6«|[N?ÎñÛÁ€¸bTg—ﬁæÿ∏ÒREOês¨‚ÊÁÁøıË£•vûœ>∑{˜ÓÇèßÎ˙íua¡d∑z„ç7“Í<33311±∞∞ê∫Ìﬁ76˛¥‘-Î.Z/Ô˙y€snßeù ºzﬂü‘êÃw8Uí∫ú5?∂Ê§M~ÿä\\Rvb§Æ™)Çm9Èö¢¨\\ï:ÙêŸ|∂|^]á€∂C€>mWÃ	√`‚Qp:c-ˇÿﬂ˙±süÆ™ŸÂ‰úKr>È¸<Î?ª¨e˝m~Êª^7æÈ˘s„∫ﬂ|‰ëæü9ˇÃô∆çOMMıt4zR◊ì™m;|¯p}lÁñv™˝˚ø¥Â#…â`Ø/Ä|ÀÀÀããã]‡˙\0÷‚¨ª(∏êπù÷O∫.aï¨PõÔp\Z|Í∫æiµ•\"ÁÛ¸∞uΩ§ß\0K]S%+R˛L[síìô¨øj›°-	≤—UUÔJm7ûs_[.ô¸{pZr3]#—∂sÖ≥À:∞[‰úKr>)~èUÕÆ∏±ÏÓö#gæáj‰$=≈Ì˚ôÛC6n|nnÆ/Oø´ayòtêi-µÛÏ€∑Ôÿ±cœXÓø∫uÓñÙı˝iÛ5¸F©[ëª»ŸßÛ}∫mÁÊü¥~,ıI?¬ÕO?ﬂ·T“tZo∂k*⁄.)™û.)x©ÀRÍ—€20˘õ±iÕOÎÖ9∑)≈UòÅ|]˜Ï:Ëú,È©ıîÅ§Â+<≠yh€!ÀÄgóı\n\ZÀX¥s{◊}äﬂKy„Îiv˘3Íúr÷∑aee•q/ªvÌz˜“Â˛û9Ô›ª∑q„i„⁄·„Ã1ÔsVÍè1Hsê¶°Òs˝ä(í¬Œ}vû›ÊÌl‹X-uÎz9ÎÈáÀP±€iΩ<Áì2ûTÒ#ºç}˙5ﬂ·T“\\\Z7€ñüÊÖmãÑ*œºvF0u•æfsf⁄5\'Õ,e≠-˘kNÒº…F€}ïwîZ◊áú;Íú]◊r&€ıç¨‘E£ß…∂û\"∑~Lv6˝2û`¡ÛÑÆÎ€a/8–^ﬂ/3ªmh=VIÓàãõòòh‹‡—£è˜ÒÃπ˘ÀyRmﬂP◊_’ø…\r^©ﬂﬁˆçáNoº¯ÉiçcõÊ…«3⁄y…∂mæÜ_/u+r;Ÿßq˘üØtØw~,ÈI’bæ√©§π¥›lÎõü∑¶•€;ËG˚Á‹Z©—\ZÒ‘ï˙ö›ràYó¥Âß5B]÷ı∫•∆&R6‚ÍC÷_e\r1+\0Y◊*;\0≈30÷q‚õ5Ò¨?xv˘Ø†∂ó‰ñ˚ºóÆ75Øﬂûdçx\'w∑∞∞–8>w‹qGø‘ÛÖ;Ôl‹ÏÏÏÏŒüxé∞ÁU9÷◊◊”™Z∆oÈIp€mÁœü/Ú0⁄ﬁiöÊÏüø√6læÜˇ§‘≠»]Ïdü÷À€>/ÔŸô¬0Ãw8ï4ó∂õÌ\Zå∂T	’ñ7>∞mRWÍk∂»Lª>Ä÷¸‰ﬂ¯ÿ«uΩD6Úï˙‚jùBÁj–öÅÆCoª0ˇ±nJ}j≈èpk≤›ıÒW8ªç‹Wqë√ﬁıØ:G”uÙ]Ô}≥ÀMÁò∆∫uû^Ô´Õ⁄⁄ZÛ∑Ë<t`_ŒúO<˚\\Ûëü>}z\'oK√ÚV7`KKKw›}wÂ_‰I∫Ö2geŸaXªﬁ‡∆ç≥˝›∂|5vΩ ˆnßıäù7R∆≥kﬁrÒ#‹˙…ÄÁ;úJùKg“:”R<ô≠; WR7òcõ?”∂œ∑}[ÿ⁄vLr\"e£å£îÛ\ZÔ˙∆—va◊lπ2tΩÂí–Sí_…Èöä-Ôb\0Øﬂ-◊ÌúKr^˛Ì‰ºA~vY…ôWŒ\'€p¯·∆ﬂ˘≠~ÌÏŸfâ:p‡¿∂UACÙn7`_∏ÛŒ˛~?‚˜œúi|±Øßá—˘îøgﬂÑ6_∆ØŸz›vrÜëøÁPùÄñDÍ§.ãl»ÜîöÅÆ]∑k⁄nÁ©˛P‘n+µÛ4›˘q€ˆÏŸ”∏Ÿ¥Æ§•e\'ﬂïû67o™‡7IÌƒ∞º’\rﬁ⁄⁄⁄Ó›ª˚ı√.º}1ù\\:˚^Fëw†∂qÈ˙0€≥πB˝ÿ÷Î÷ﬂ3åÚÊ;ú§NÍ≤»Ül»@yË:˝∂$¥~“…Ï™ö]Œ4ª∑má˛æ¢WWWwÌ⁄’Ï*/ºxjßÕÁﬁ|´Yx∆««è?æìáTPıorZYYIkœNJj≥|·Œ;üxÚ©^@gˇn[\\r¢Ÿó˜!+T©+TÂÛNR\'uYdC6d†‘$Ÿﬂ÷8ñqùsâŸ\rxvÕ„‹ı*mØÂùårKiEiﬁ]⁄Xz˝-•\'û}Æ˘-m©√áÔÒÛ§™∏”ßOßE3=˙;˘ñ∂¥8m£É4j7nºjÎuã⁄FCÍ§.ãl»Ü‘7fWﬂŸm√ÚÚrÛ´=cõ?…-=.ÚÂùªÓæªı_[Vxù\'Ÿ¸&∑Ω{˜ﬁˇﬁæÿÎw\"~„·áﬂ Wıìÿ¶ÕÍG∂^∑öÆPCBÍ§.ãl»Ü‘7fWﬂŸmœ   ‰‰dkÅIõOz:›˘ﬂF“™ÛÕGŸ∑o_ÎŒie\ZÃ∑¥5’ı@˜◊˙˙z:§€oüL;L¡ü8˛≠GM˜ø˜ﬁØ•M©Íáø}õ+‘ä≠◊≠æ+‘0ê:©À\"≤!ıÕÄŸ’wv€∂∂∂ˆ‡Ééèèèu≥{˜Ó¨øöõõ¿-hS„›wÈ—øÔæ˚“ﬁ˘Ö;ÔL+MÁˇ J/yÏ±ﬂªÎÓª”}ˆÔˇ“ÍÍj’yß6W®⁄z›jΩBUNÍ§.ãl»Ü‘7fWﬂŸÌPz˛<??ﬂµ€töûû>yÚd%è≥ˆ∫Ô÷◊◊óóó”·5†DÎ◊Ï“¬≥¥¥îˆ⁄™f§˝˚˝˜_∫q„∂û∂\0+TÖ§NÍ≤»Ül»@}3`vıù]_§ÁœÈrz˛<55’v˛<111;;{‰»ë¡mßUêÕˆ§)|˝ı?ºq„å≠ß-Ã\nU	©ì∫,≤!2Pﬂò]}g7\"Ëë633Û?éπë|ﬂV|˚· LNNV=∫\Zì:©À\"≤!ıÕÄŸ’wv#BÁiáZX¯rÂØ˘zmˇyÒ°˘˘˘™GWcR\'uYdC6d†æ0ª˙ŒnDË<#muuurÚWﬁ_·FÚä≠‡6;˚˜ñóó´]çIù‘eë\rŸêÅ˙f¿ÏÍ;ª°Ûå∫o:p#˘û≠»ˆﬂñ˛Ì‹‹L’C´=©ì∫,≤!2Pﬂò]}g7\ntûQ∑ææ>=˝k\'O˛◊$yŸñøù?ˇÃ‰‰Ø¨≠ù®zhµ\'uRóE6dCÍõ≥´ÔÏFÅŒ√Õﬂ§;5u˚  ríú∂emÈÚî.Â«èˇn’„\nBÍ§.ãl»Ü‘7fWﬂŸÖßÛp”…ì\'”EÍ–°ﬂHíólù€ÚÚ√ìì«èˇß™ä‘I]Ÿê\r®oÃÆæ≥ãMÁ·CÎÎÎÛ””ø∫∏¯ØŒüˇvíú≤≠≠˝Ô••37∑ovv:˝ºÍ$uRóE6dCÍõ≥´ÔÏ”y¯ò„«èœœˇ£[o˝+cåçÌ⁄ıÈππœßãT’c	NÍZI]+Ÿh5öŸêÅVı ÄŸµ™◊ÏB“y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 2ù\0\0àLÁ\0\0\"”y\0\0Ä»t\0\0 ≤ˇ[∏[’@Ò^\0\0\0\0IENDÆB`Ç',0),('13aa8026-54dc-11e9-8d3a-64006a862ca5',1,'D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\businessChange.bpmn','13aa5911-54dc-11e9-8d3a-64006a862ca5','<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.activiti.org/test\">\r\n  <process id=\"businessChange\" name=\"‰∏öÂä°ÂèòÊõ¥\" isExecutable=\"true\">\r\n    <startEvent id=\"startevent1\" name=\"Start\"></startEvent>\r\n    <userTask id=\"usertask1\" name=\"ÂàõÂª∫Â∑•Âçï\"></userTask>\r\n    <sequenceFlow id=\"flow1\" name=\"ÂêØÂä®\" sourceRef=\"startevent1\" targetRef=\"usertask1\"></sequenceFlow>\r\n    <userTask id=\"usertask2\" name=\" Ê¨†Ë¥πÁ°ÆËÆ§\"></userTask>\r\n    <sequenceFlow id=\"flow2\" name=\"Êèê‰∫§\" sourceRef=\"usertask1\" targetRef=\"usertask2\"></sequenceFlow>\r\n    <userTask id=\"usertask3\" name=\"‰∏äÈó®ÊúçÂä°\"></userTask>\r\n    <sequenceFlow id=\"flow3\" name=\"Á°ÆÂÆö\" sourceRef=\"usertask2\" targetRef=\"usertask3\"></sequenceFlow>\r\n    <userTask id=\"usertask4\" name=\" ÁîµËØùÂõûËÆø\"></userTask>\r\n    <sequenceFlow id=\"flow4\" name=\"Êèê‰∫§\" sourceRef=\"usertask3\" targetRef=\"usertask4\"></sequenceFlow>\r\n    <endEvent id=\"endevent1\" name=\"End\"></endEvent>\r\n    <sequenceFlow id=\"flow5\" name=\"ÁªìÊùü\" sourceRef=\"usertask4\" targetRef=\"endevent1\"></sequenceFlow>\r\n  </process>\r\n  <bpmndi:BPMNDiagram id=\"BPMNDiagram_businessChange\">\r\n    <bpmndi:BPMNPlane bpmnElement=\"businessChange\" id=\"BPMNPlane_businessChange\">\r\n      <bpmndi:BPMNShape bpmnElement=\"startevent1\" id=\"BPMNShape_startevent1\">\r\n        <omgdc:Bounds height=\"35.0\" width=\"35.0\" x=\"70.0\" y=\"250.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask1\" id=\"BPMNShape_usertask1\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"150.0\" y=\"240.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask2\" id=\"BPMNShape_usertask2\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"300.0\" y=\"240.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask3\" id=\"BPMNShape_usertask3\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"450.0\" y=\"240.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"usertask4\" id=\"BPMNShape_usertask4\">\r\n        <omgdc:Bounds height=\"55.0\" width=\"105.0\" x=\"600.0\" y=\"240.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNShape bpmnElement=\"endevent1\" id=\"BPMNShape_endevent1\">\r\n        <omgdc:Bounds height=\"35.0\" width=\"35.0\" x=\"750.0\" y=\"250.0\"></omgdc:Bounds>\r\n      </bpmndi:BPMNShape>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow1\" id=\"BPMNEdge_flow1\">\r\n        <omgdi:waypoint x=\"105.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"150.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"115.0\" y=\"267.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow2\" id=\"BPMNEdge_flow2\">\r\n        <omgdi:waypoint x=\"255.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"300.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"265.0\" y=\"267.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow3\" id=\"BPMNEdge_flow3\">\r\n        <omgdi:waypoint x=\"405.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"450.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"415.0\" y=\"267.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow4\" id=\"BPMNEdge_flow4\">\r\n        <omgdi:waypoint x=\"555.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"600.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"565.0\" y=\"267.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n      <bpmndi:BPMNEdge bpmnElement=\"flow5\" id=\"BPMNEdge_flow5\">\r\n        <omgdi:waypoint x=\"705.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <omgdi:waypoint x=\"750.0\" y=\"267.0\"></omgdi:waypoint>\r\n        <bpmndi:BPMNLabel>\r\n          <omgdc:Bounds height=\"14.0\" width=\"100.0\" x=\"715.0\" y=\"267.0\"></omgdc:Bounds>\r\n        </bpmndi:BPMNLabel>\r\n      </bpmndi:BPMNEdge>\r\n    </bpmndi:BPMNPlane>\r\n  </bpmndi:BPMNDiagram>\r\n</definitions>',0);

/*Table structure for table `act_ge_property` */

DROP TABLE IF EXISTS `act_ge_property`;

CREATE TABLE `act_ge_property` (
  `NAME_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `VALUE_` varchar(300) COLLATE utf8_bin DEFAULT NULL,
  `REV_` int(11) DEFAULT NULL,
  PRIMARY KEY (`NAME_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ge_property` */

insert  into `act_ge_property`(`NAME_`,`VALUE_`,`REV_`) values ('next.dbid','1',1),('schema.history','create(5.17.0.2)',1),('schema.version','5.17.0.2',1);

/*Table structure for table `act_hi_actinst` */

DROP TABLE IF EXISTS `act_hi_actinst`;

CREATE TABLE `act_hi_actinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `ACT_ID_` varchar(255) COLLATE utf8_bin NOT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `CALL_PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ACT_TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `ASSIGNEE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) DEFAULT NULL,
  `DURATION_` bigint(20) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_HI_ACT_INST_START` (`START_TIME_`),
  KEY `ACT_IDX_HI_ACT_INST_END` (`END_TIME_`),
  KEY `ACT_IDX_HI_ACT_INST_PROCINST` (`PROC_INST_ID_`,`ACT_ID_`),
  KEY `ACT_IDX_HI_ACT_INST_EXEC` (`EXECUTION_ID_`,`ACT_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_actinst` */

/*Table structure for table `act_hi_attachment` */

DROP TABLE IF EXISTS `act_hi_attachment`;

CREATE TABLE `act_hi_attachment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `URL_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `CONTENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TIME_` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_attachment` */

/*Table structure for table `act_hi_comment` */

DROP TABLE IF EXISTS `act_hi_comment`;

CREATE TABLE `act_hi_comment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACTION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `MESSAGE_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `FULL_MSG_` longblob,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_comment` */

/*Table structure for table `act_hi_detail` */

DROP TABLE IF EXISTS `act_hi_detail`;

CREATE TABLE `act_hi_detail` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TIME_` datetime(3) NOT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_HI_DETAIL_PROC_INST` (`PROC_INST_ID_`),
  KEY `ACT_IDX_HI_DETAIL_ACT_INST` (`ACT_INST_ID_`),
  KEY `ACT_IDX_HI_DETAIL_TIME` (`TIME_`),
  KEY `ACT_IDX_HI_DETAIL_NAME` (`NAME_`),
  KEY `ACT_IDX_HI_DETAIL_TASK_ID` (`TASK_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_detail` */

/*Table structure for table `act_hi_identitylink` */

DROP TABLE IF EXISTS `act_hi_identitylink`;

CREATE TABLE `act_hi_identitylink` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `GROUP_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_HI_IDENT_LNK_USER` (`USER_ID_`),
  KEY `ACT_IDX_HI_IDENT_LNK_TASK` (`TASK_ID_`),
  KEY `ACT_IDX_HI_IDENT_LNK_PROCINST` (`PROC_INST_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_identitylink` */

/*Table structure for table `act_hi_procinst` */

DROP TABLE IF EXISTS `act_hi_procinst`;

CREATE TABLE `act_hi_procinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `BUSINESS_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `END_TIME_` datetime(3) DEFAULT NULL,
  `DURATION_` bigint(20) DEFAULT NULL,
  `START_USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `END_ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SUPER_PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  UNIQUE KEY `PROC_INST_ID_` (`PROC_INST_ID_`),
  KEY `ACT_IDX_HI_PRO_INST_END` (`END_TIME_`),
  KEY `ACT_IDX_HI_PRO_I_BUSKEY` (`BUSINESS_KEY_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_procinst` */

/*Table structure for table `act_hi_taskinst` */

DROP TABLE IF EXISTS `act_hi_taskinst`;

CREATE TABLE `act_hi_taskinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ASSIGNEE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `START_TIME_` datetime(3) NOT NULL,
  `CLAIM_TIME_` datetime(3) DEFAULT NULL,
  `END_TIME_` datetime(3) DEFAULT NULL,
  `DURATION_` bigint(20) DEFAULT NULL,
  `DELETE_REASON_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `DUE_DATE_` datetime(3) DEFAULT NULL,
  `FORM_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_taskinst` */

/*Table structure for table `act_hi_varinst` */

DROP TABLE IF EXISTS `act_hi_varinst`;

CREATE TABLE `act_hi_varinst` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VAR_TYPE_` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `REV_` int(11) DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME_` datetime(3) DEFAULT NULL,
  `LAST_UPDATED_TIME_` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_HI_PROCVAR_PROC_INST` (`PROC_INST_ID_`),
  KEY `ACT_IDX_HI_PROCVAR_NAME_TYPE` (`NAME_`,`VAR_TYPE_`),
  KEY `ACT_IDX_HI_PROCVAR_TASK_ID` (`TASK_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_hi_varinst` */

/*Table structure for table `act_id_group` */

DROP TABLE IF EXISTS `act_id_group`;

CREATE TABLE `act_id_group` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_id_group` */

/*Table structure for table `act_id_info` */

DROP TABLE IF EXISTS `act_id_info`;

CREATE TABLE `act_id_info` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `USER_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `VALUE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PASSWORD_` longblob,
  `PARENT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_id_info` */

/*Table structure for table `act_id_membership` */

DROP TABLE IF EXISTS `act_id_membership`;

CREATE TABLE `act_id_membership` (
  `USER_ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `GROUP_ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  PRIMARY KEY (`USER_ID_`,`GROUP_ID_`),
  KEY `ACT_FK_MEMB_GROUP` (`GROUP_ID_`),
  CONSTRAINT `ACT_FK_MEMB_USER` FOREIGN KEY (`USER_ID_`) REFERENCES `act_id_user` (`ID_`),
  CONSTRAINT `ACT_FK_MEMB_GROUP` FOREIGN KEY (`GROUP_ID_`) REFERENCES `act_id_group` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_id_membership` */

/*Table structure for table `act_id_user` */

DROP TABLE IF EXISTS `act_id_user`;

CREATE TABLE `act_id_user` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `FIRST_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `LAST_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EMAIL_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PWD_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PICTURE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_id_user` */

/*Table structure for table `act_re_deployment` */

DROP TABLE IF EXISTS `act_re_deployment`;

CREATE TABLE `act_re_deployment` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `DEPLOY_TIME_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_re_deployment` */

insert  into `act_re_deployment`(`ID_`,`NAME_`,`CATEGORY_`,`TENANT_ID_`,`DEPLOY_TIME_`) values ('13aa5911-54dc-11e9-8d3a-64006a862ca5','SpringAutoDeployment',NULL,'','2019-04-02 08:13:01.056');

/*Table structure for table `act_re_model` */

DROP TABLE IF EXISTS `act_re_model`;

CREATE TABLE `act_re_model` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LAST_UPDATE_TIME_` timestamp(3) NULL DEFAULT NULL,
  `VERSION_` int(11) DEFAULT NULL,
  `META_INFO_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EDITOR_SOURCE_EXTRA_VALUE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_FK_MODEL_SOURCE` (`EDITOR_SOURCE_VALUE_ID_`),
  KEY `ACT_FK_MODEL_SOURCE_EXTRA` (`EDITOR_SOURCE_EXTRA_VALUE_ID_`),
  KEY `ACT_FK_MODEL_DEPLOYMENT` (`DEPLOYMENT_ID_`),
  CONSTRAINT `ACT_FK_MODEL_DEPLOYMENT` FOREIGN KEY (`DEPLOYMENT_ID_`) REFERENCES `act_re_deployment` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE` FOREIGN KEY (`EDITOR_SOURCE_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_MODEL_SOURCE_EXTRA` FOREIGN KEY (`EDITOR_SOURCE_EXTRA_VALUE_ID_`) REFERENCES `act_ge_bytearray` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_re_model` */

/*Table structure for table `act_re_procdef` */

DROP TABLE IF EXISTS `act_re_procdef`;

CREATE TABLE `act_re_procdef` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin NOT NULL,
  `VERSION_` int(11) NOT NULL,
  `DEPLOYMENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DGRM_RESOURCE_NAME_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `HAS_START_FORM_KEY_` tinyint(4) DEFAULT NULL,
  `HAS_GRAPHICAL_NOTATION_` tinyint(4) DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  UNIQUE KEY `ACT_UNIQ_PROCDEF` (`KEY_`,`VERSION_`,`TENANT_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_re_procdef` */

insert  into `act_re_procdef`(`ID_`,`REV_`,`CATEGORY_`,`NAME_`,`KEY_`,`VERSION_`,`DEPLOYMENT_ID_`,`RESOURCE_NAME_`,`DGRM_RESOURCE_NAME_`,`DESCRIPTION_`,`HAS_START_FORM_KEY_`,`HAS_GRAPHICAL_NOTATION_`,`SUSPENSION_STATE_`,`TENANT_ID_`) values ('businessChange:1:13f321ea-54dc-11e9-8d3a-64006a862ca5',1,'http://www.activiti.org/test','‰∏öÂä°ÂèòÊõ¥','businessChange',1,'13aa5911-54dc-11e9-8d3a-64006a862ca5','D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\businessChange.bpmn',NULL,NULL,0,1,1,''),('jszc:1:13f0d7f7-54dc-11e9-8d3a-64006a862ca5',1,'http://www.activiti.org/test','ÊäÄÊúØÊîØÊåÅ','jszc',1,'13aa5911-54dc-11e9-8d3a-64006a862ca5','D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\jszc.bpmn',NULL,NULL,0,1,1,''),('openService:1:13f2d3c9-54dc-11e9-8d3a-64006a862ca5',1,'http://www.activiti.org/test','‰∏öÂä°ÂºÄÈÄö','openService',1,'13aa5911-54dc-11e9-8d3a-64006a862ca5','D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\OpenService.bpmn','D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\OpenService.png','‰∏öÂä°ÂºÄÈÄö',0,1,1,''),('unsubscribe:1:13f285a8-54dc-11e9-8d3a-64006a862ca5',1,'http://www.activiti.org/test','‰∏öÂä°ÈÄÄËÆ¢','unsubscribe',1,'13aa5911-54dc-11e9-8d3a-64006a862ca5','D:\\soft\\apache-tomcat-8.0.27\\webapps\\spms\\WEB-INF\\classes\\bpm\\unsubscribe.bpmn',NULL,NULL,0,1,1,'');

/*Table structure for table `act_ru_event_subscr` */

DROP TABLE IF EXISTS `act_ru_event_subscr`;

CREATE TABLE `act_ru_event_subscr` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `EVENT_TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EVENT_NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACTIVITY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `CONFIGURATION_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CREATED_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_EVENT_SUBSCR_CONFIG_` (`CONFIGURATION_`),
  KEY `ACT_FK_EVENT_EXEC` (`EXECUTION_ID_`),
  CONSTRAINT `ACT_FK_EVENT_EXEC` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_event_subscr` */

/*Table structure for table `act_ru_execution` */

DROP TABLE IF EXISTS `act_ru_execution`;

CREATE TABLE `act_ru_execution` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BUSINESS_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `SUPER_EXEC_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `ACT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `IS_ACTIVE_` tinyint(4) DEFAULT NULL,
  `IS_CONCURRENT_` tinyint(4) DEFAULT NULL,
  `IS_SCOPE_` tinyint(4) DEFAULT NULL,
  `IS_EVENT_SCOPE_` tinyint(4) DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `CACHED_ENT_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `LOCK_TIME_` timestamp(3) NULL DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_EXEC_BUSKEY` (`BUSINESS_KEY_`),
  KEY `ACT_FK_EXE_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_EXE_PARENT` (`PARENT_ID_`),
  KEY `ACT_FK_EXE_SUPER` (`SUPER_EXEC_`),
  KEY `ACT_FK_EXE_PROCDEF` (`PROC_DEF_ID_`),
  CONSTRAINT `ACT_FK_EXE_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_EXE_PARENT` FOREIGN KEY (`PARENT_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_EXE_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ACT_FK_EXE_SUPER` FOREIGN KEY (`SUPER_EXEC_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_execution` */

/*Table structure for table `act_ru_identitylink` */

DROP TABLE IF EXISTS `act_ru_identitylink`;

CREATE TABLE `act_ru_identitylink` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `GROUP_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `USER_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_IDENT_LNK_USER` (`USER_ID_`),
  KEY `ACT_IDX_IDENT_LNK_GROUP` (`GROUP_ID_`),
  KEY `ACT_IDX_ATHRZ_PROCEDEF` (`PROC_DEF_ID_`),
  KEY `ACT_FK_TSKASS_TASK` (`TASK_ID_`),
  KEY `ACT_FK_IDL_PROCINST` (`PROC_INST_ID_`),
  CONSTRAINT `ACT_FK_IDL_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_ATHRZ_PROCEDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_TSKASS_TASK` FOREIGN KEY (`TASK_ID_`) REFERENCES `act_ru_task` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_identitylink` */

/*Table structure for table `act_ru_job` */

DROP TABLE IF EXISTS `act_ru_job`;

CREATE TABLE `act_ru_job` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `LOCK_EXP_TIME_` timestamp(3) NULL DEFAULT NULL,
  `LOCK_OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXCLUSIVE_` tinyint(1) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROCESS_INSTANCE_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `RETRIES_` int(11) DEFAULT NULL,
  `EXCEPTION_STACK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `EXCEPTION_MSG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `DUEDATE_` timestamp(3) NULL DEFAULT NULL,
  `REPEAT_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_TYPE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `HANDLER_CFG_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`ID_`),
  KEY `ACT_FK_JOB_EXCEPTION` (`EXCEPTION_STACK_ID_`),
  CONSTRAINT `ACT_FK_JOB_EXCEPTION` FOREIGN KEY (`EXCEPTION_STACK_ID_`) REFERENCES `act_ge_bytearray` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_job` */

/*Table structure for table `act_ru_task` */

DROP TABLE IF EXISTS `act_ru_task`;

CREATE TABLE `act_ru_task` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `REV_` int(11) DEFAULT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_DEF_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PARENT_TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DESCRIPTION_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TASK_DEF_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `OWNER_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ASSIGNEE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `DELEGATION_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PRIORITY_` int(11) DEFAULT NULL,
  `CREATE_TIME_` timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
  `DUE_DATE_` datetime(3) DEFAULT NULL,
  `CATEGORY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `SUSPENSION_STATE_` int(11) DEFAULT NULL,
  `TENANT_ID_` varchar(255) COLLATE utf8_bin DEFAULT '',
  `FORM_KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_TASK_CREATE` (`CREATE_TIME_`),
  KEY `ACT_FK_TASK_EXE` (`EXECUTION_ID_`),
  KEY `ACT_FK_TASK_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_TASK_PROCDEF` (`PROC_DEF_ID_`),
  CONSTRAINT `ACT_FK_TASK_PROCDEF` FOREIGN KEY (`PROC_DEF_ID_`) REFERENCES `act_re_procdef` (`ID_`),
  CONSTRAINT `ACT_FK_TASK_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_TASK_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_task` */

/*Table structure for table `act_ru_variable` */

DROP TABLE IF EXISTS `act_ru_variable`;

CREATE TABLE `act_ru_variable` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `TYPE_` varchar(255) COLLATE utf8_bin NOT NULL,
  `NAME_` varchar(255) COLLATE utf8_bin NOT NULL,
  `EXECUTION_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `PROC_INST_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TASK_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `BYTEARRAY_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `DOUBLE_` double DEFAULT NULL,
  `LONG_` bigint(20) DEFAULT NULL,
  `TEXT_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  `TEXT2_` varchar(4000) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`),
  KEY `ACT_IDX_VARIABLE_TASK_ID` (`TASK_ID_`),
  KEY `ACT_FK_VAR_EXE` (`EXECUTION_ID_`),
  KEY `ACT_FK_VAR_PROCINST` (`PROC_INST_ID_`),
  KEY `ACT_FK_VAR_BYTEARRAY` (`BYTEARRAY_ID_`),
  CONSTRAINT `ACT_FK_VAR_BYTEARRAY` FOREIGN KEY (`BYTEARRAY_ID_`) REFERENCES `act_ge_bytearray` (`ID_`),
  CONSTRAINT `ACT_FK_VAR_EXE` FOREIGN KEY (`EXECUTION_ID_`) REFERENCES `act_ru_execution` (`ID_`),
  CONSTRAINT `ACT_FK_VAR_PROCINST` FOREIGN KEY (`PROC_INST_ID_`) REFERENCES `act_ru_execution` (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `act_ru_variable` */

/*Table structure for table `dict_area_classification` */

DROP TABLE IF EXISTS `dict_area_classification`;

CREATE TABLE `dict_area_classification` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_area_classification` */

insert  into `dict_area_classification`(`code`,`value`,`iorder`) values ('1','ÊúçÂä°Âå∫Âüü',1),('2','Áî®ÁîµÂå∫Âüü',2),('1','ÊúçÂä°Âå∫Âüü',1),('2','Áî®ÁîµÂå∫Âüü',2);

/*Table structure for table `dict_area_policy` */

DROP TABLE IF EXISTS `dict_area_policy`;

CREATE TABLE `dict_area_policy` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_area_policy` */

insert  into `dict_area_policy`(`code`,`value`,`iorder`) values ('1','ÊéßÁîµÁ≠ñÁï•1',1),('2','ÊéßÁîµÁ≠ñÁï•2',2),('3','ÊéßÁîµÁ≠ñÁï•3',3),('4','ÊéßÁîµÁ≠ñÁï•4',4),('5','ÊéßÁîµÁ≠ñÁï•5',5);

/*Table structure for table `dict_area_type` */

DROP TABLE IF EXISTS `dict_area_type`;

CREATE TABLE `dict_area_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_area_type` */

insert  into `dict_area_type`(`code`,`value`,`iorder`) values ('1','ÂõΩÂÆ∂',1),('2','ÁúÅ‰ªΩ„ÄÅÁõ¥ËæñÂ∏Ç',2),('3','Âú∞Â∏Ç',3),('4','ÂéøÂå∫',4);

/*Table structure for table `dict_device_error` */

DROP TABLE IF EXISTS `dict_device_error`;

CREATE TABLE `dict_device_error` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `errorCode` varchar(255) DEFAULT NULL,
  `errorDetail` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_8gplq0tweiu82pev5nucrqhd5` (`createUser_id`),
  KEY `FK_9945hn0qauhp51oghtr4v7hj1` (`lastModifyUser_id`),
  CONSTRAINT `FK_9945hn0qauhp51oghtr4v7hj1` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_8gplq0tweiu82pev5nucrqhd5` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_device_error` */

/*Table structure for table `dict_device_operstatus` */

DROP TABLE IF EXISTS `dict_device_operstatus`;

CREATE TABLE `dict_device_operstatus` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_device_operstatus` */

insert  into `dict_device_operstatus`(`code`,`value`,`iorder`) values ('0','Á¶ªÁ∫ø',1),('1','Âú®Á∫ø',2),('2','ÂºÇÂ∏∏',3);

/*Table structure for table `dict_device_status` */

DROP TABLE IF EXISTS `dict_device_status`;

CREATE TABLE `dict_device_status` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_device_status` */

insert  into `dict_device_status`(`code`,`value`,`iorder`) values ('1','Â∫ìÂ≠ò',1),('2','Ê≠£Â∏∏ËøêËê•',2),('3','Áª¥‰øÆ',3),('4','Êä•Â∫ü',4);

/*Table structure for table `dict_device_storage` */

DROP TABLE IF EXISTS `dict_device_storage`;

CREATE TABLE `dict_device_storage` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_device_storage` */

insert  into `dict_device_storage`(`code`,`value`,`iorder`) values ('1','Êúõ‰∫¨1Âè∑‰ªìÂ∫ì',1),('2','Â≠¶Èô¢Ë∑Ø1Âè∑‰ªìÂ∫ì',2),('3','‰∫¶Â∫Ñ1Âè∑‰ªìÂ∫ì',3),('5','Â∑≤Âá∫Â∫ì',5);

/*Table structure for table `dict_device_type` */

DROP TABLE IF EXISTS `dict_device_type`;

CREATE TABLE `dict_device_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_device_type` */

insert  into `dict_device_type`(`code`,`value`,`iorder`) values ('1','Gateway',1),('2','Changhong Ac',2),('3','Plug Ac',3),('4','Central Ac',4),('5','Onoff Plug',5),('6','Onoff Pm Plug',6),('7','WinDoor',7),('8','Pir',8),('9','HtSensor',9),('10','Onoff Light',10),('11','Lamp',11);

/*Table structure for table `dict_spmsuser_type` */

DROP TABLE IF EXISTS `dict_spmsuser_type`;

CREATE TABLE `dict_spmsuser_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_spmsuser_type` */

insert  into `dict_spmsuser_type`(`code`,`value`,`iorder`) values ('1','ËØïÁî®',1),('2','ÂïÜÁî®',2),('3','‰∏™‰∫∫',3);

/*Table structure for table `dict_user_status` */

DROP TABLE IF EXISTS `dict_user_status`;

CREATE TABLE `dict_user_status` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_user_status` */

insert  into `dict_user_status`(`code`,`value`,`iorder`) values ('0','Ê≠£Â∏∏',0),('1','Âà†Èô§',1),('2','ÂÜªÁªì',2);

/*Table structure for table `dict_user_type` */

DROP TABLE IF EXISTS `dict_user_type`;

CREATE TABLE `dict_user_type` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_user_type` */

insert  into `dict_user_type`(`code`,`value`,`iorder`) values ('0','ÁÆ°ÁêÜÂëò',0),('1','ÊôÆÈÄöÁî®Êà∑',1);

/*Table structure for table `dict_warnset_level` */

DROP TABLE IF EXISTS `dict_warnset_level`;

CREATE TABLE `dict_warnset_level` (
  `code` varchar(4) DEFAULT NULL,
  `value` varchar(500) DEFAULT NULL,
  `iorder` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `dict_warnset_level` */

insert  into `dict_warnset_level`(`code`,`value`,`iorder`) values ('0','‰∏ÄÁ∫ß',0),('1','‰∫åÁ∫ß',1);

/*Table structure for table `ifttt_stragegy_test` */

DROP TABLE IF EXISTS `ifttt_stragegy_test`;

CREATE TABLE `ifttt_stragegy_test` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `action` varchar(255) DEFAULT NULL,
  `actionPara` varchar(255) DEFAULT NULL,
  `ifCondition` varchar(255) DEFAULT NULL,
  `ifConditionDTO` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `ifttt_stragegy_test` */

/*Table structure for table `operater_group` */

DROP TABLE IF EXISTS `operater_group`;

CREATE TABLE `operater_group` (
  `groupId` varchar(255) NOT NULL,
  `operaterId` varchar(255) NOT NULL,
  KEY `FK_eegrwe7euvtrqtlc38qi1f8b0` (`operaterId`),
  KEY `FK_kveqo2xkd68l1ks73g673ha2d` (`groupId`),
  CONSTRAINT `FK_kveqo2xkd68l1ks73g673ha2d` FOREIGN KEY (`groupId`) REFERENCES `tb_workflow_operatergroup` (`id`),
  CONSTRAINT `FK_eegrwe7euvtrqtlc38qi1f8b0` FOREIGN KEY (`operaterId`) REFERENCES `tb_workflow_operater` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `operater_group` */

/*Table structure for table `rairconcurve_clocksetting` */

DROP TABLE IF EXISTS `rairconcurve_clocksetting`;

CREATE TABLE `rairconcurve_clocksetting` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `alone` int(11) DEFAULT NULL,
  `clocking` varchar(255) DEFAULT NULL,
  `friday` int(11) DEFAULT NULL,
  `mode` int(11) DEFAULT NULL,
  `monday` int(11) DEFAULT NULL,
  `on_off` int(11) DEFAULT NULL,
  `saturday` int(11) DEFAULT NULL,
  `startend` int(11) DEFAULT NULL,
  `sunday` int(11) DEFAULT NULL,
  `temp` int(11) DEFAULT NULL,
  `thursday` int(11) DEFAULT NULL,
  `tuesday` int(11) DEFAULT NULL,
  `wednesday` int(11) DEFAULT NULL,
  `windspeed` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `raircon_Setting_id` varchar(255) DEFAULT NULL,
  `spmsDevice_id` varchar(255) DEFAULT NULL,
  `spms_userId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_pmey5st0dnnukmhpjoxobj129` (`createUser_id`),
  KEY `FK_5yqi68ket0951hjv4iptrerc2` (`lastModifyUser_id`),
  KEY `FK_ksrn74q8t6b9g2jyywjil08h9` (`raircon_Setting_id`),
  KEY `FK_au1yd2qe7lws005dxp8mkxrlh` (`spmsDevice_id`),
  KEY `FK_fa2ccbhmxyarsfikyfmef6y4w` (`spms_userId`),
  CONSTRAINT `FK_fa2ccbhmxyarsfikyfmef6y4w` FOREIGN KEY (`spms_userId`) REFERENCES `spms_user` (`id`),
  CONSTRAINT `FK_5yqi68ket0951hjv4iptrerc2` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_au1yd2qe7lws005dxp8mkxrlh` FOREIGN KEY (`spmsDevice_id`) REFERENCES `spms_ac` (`id`),
  CONSTRAINT `FK_ksrn74q8t6b9g2jyywjil08h9` FOREIGN KEY (`raircon_Setting_id`) REFERENCES `spms_raircon_setting` (`id`),
  CONSTRAINT `FK_pmey5st0dnnukmhpjoxobj129` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `rairconcurve_clocksetting` */

/*Table structure for table `role_permission` */

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
  `permissionentities_id` varchar(255) NOT NULL,
  `roleentities_id` varchar(255) NOT NULL,
  PRIMARY KEY (`permissionentities_id`,`roleentities_id`),
  KEY `FK_jd393pstremkhrsu2ecvalq9q` (`roleentities_id`),
  CONSTRAINT `FK_jd393pstremkhrsu2ecvalq9q` FOREIGN KEY (`roleentities_id`) REFERENCES `tb_user_role` (`id`),
  CONSTRAINT `FK_io1qrolaljsfg1fpe26m9p2w3` FOREIGN KEY (`permissionentities_id`) REFERENCES `tb_user_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_permission` */

/*Table structure for table `spms_ac` */

DROP TABLE IF EXISTS `spms_ac`;

CREATE TABLE `spms_ac` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `acTemp` int(11) DEFAULT NULL,
  `accumulatePower` int(11) DEFAULT NULL,
  `direction` int(11) DEFAULT NULL,
  `energy` int(11) DEFAULT NULL,
  `floorTemp` int(11) DEFAULT NULL,
  `humidity` int(11) DEFAULT NULL,
  `isPaired` int(11) DEFAULT NULL,
  `leftRightSwing` int(11) DEFAULT NULL,
  `power` float DEFAULT NULL,
  `rcuId` int(11) DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `temp` int(11) DEFAULT NULL,
  `upDownSwing` int(11) DEFAULT NULL,
  `upperTemp` int(11) DEFAULT NULL,
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_7t3a892ibtj084gtipg6vh4` (`gw_id`),
  CONSTRAINT `FK_7t3a892ibtj084gtipg6vh4` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_ac` */

/*Table structure for table `spms_ac_status` */

DROP TABLE IF EXISTS `spms_ac_status`;

CREATE TABLE `spms_ac_status` (
  `id` varchar(64) NOT NULL COMMENT '‰∏ªÈîÆ',
  `device_id` varchar(64) DEFAULT NULL COMMENT 'ËÆæÂ§áID',
  `ac_id` varchar(64) DEFAULT NULL COMMENT 'Á©∫Ë∞ÉID',
  `on_off` int(11) DEFAULT NULL COMMENT 'ÂºÄÂÖ≥',
  `temp` int(11) DEFAULT NULL COMMENT 'ÂÆ§ÂÜÖÊ∏©Â∫¶',
  `ac_temp` int(11) DEFAULT NULL COMMENT 'Á©∫Ë∞ÉÊ∏©Â∫¶',
  `power` bigint(11) DEFAULT NULL COMMENT 'ÂäüÁéá',
  `speed` int(11) DEFAULT NULL COMMENT 'È£éÈÄü',
  `direction` int(11) DEFAULT NULL COMMENT 'È£éÂêë',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ÂºÄÂßãÊó∂Èó¥',
  `run_duration` int(11) DEFAULT NULL COMMENT 'ÊåÅÁª≠Êó∂Èïø',
  `user_id` varchar(64) DEFAULT NULL COMMENT 'ÊâÄÂ±ûÁî®Êà∑',
  `model` int(11) DEFAULT NULL COMMENT 'Ê®°Âºè',
  `batery` int(11) DEFAULT NULL,
  `accumulatePower` bigint(11) DEFAULT NULL,
  `reactivePower` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÂäüÁéá',
  `reactiveEnergy` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÁîµËÉΩ',
  `apparentPower` int(11) DEFAULT NULL COMMENT 'ËßÜÂú®Â∑•Áéá',
  `voltage` int(11) DEFAULT NULL COMMENT 'ÁîµÂéã',
  `current` int(11) DEFAULT NULL COMMENT 'ÁîµÊµÅ',
  `frequency` int(11) DEFAULT NULL COMMENT 'È¢ëÁéá',
  `powerFactor` int(11) DEFAULT NULL COMMENT 'ÂäüÁéáÂõ†Êï∞',
  `demandTime` timestamp NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'ÈúÄÊ±ÇÂºÄÂßãÊó∂Èó¥',
  `period` int(11) DEFAULT NULL COMMENT 'ÊåÅÁª≠Êó∂Èó¥',
  `activeDemand` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÈúÄÊ±Ç',
  `reactiveDemand` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÈúÄÊ±Ç',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='Á©∫Ë∞ÉËøêË°åËÆ∞ÂΩï';

/*Data for the table `spms_ac_status` */

/*Table structure for table `spms_ac_status_month` */

DROP TABLE IF EXISTS `spms_ac_status_month`;

CREATE TABLE `spms_ac_status_month` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '‰∏ªÈîÆ',
  `device_id` varchar(64) DEFAULT NULL COMMENT 'ËÆæÂ§áID',
  `ac_id` varchar(64) DEFAULT NULL COMMENT 'Á©∫Ë∞ÉID',
  `on_off` int(11) DEFAULT NULL COMMENT 'ÂºÄÂÖ≥',
  `temp` int(11) DEFAULT NULL COMMENT 'ÂÆ§ÂÜÖÊ∏©Â∫¶',
  `ac_temp` int(11) DEFAULT NULL COMMENT 'Á©∫Ë∞ÉÊ∏©Â∫¶',
  `power` bigint(11) DEFAULT NULL COMMENT 'ÂäüÁéá',
  `speed` int(11) DEFAULT NULL COMMENT 'È£éÈÄü',
  `direction` int(11) DEFAULT NULL COMMENT 'È£éÂêë',
  `run_duration` int(11) DEFAULT NULL COMMENT 'ÊåÅÁª≠Êó∂Èïø',
  `user_id` varchar(64) DEFAULT NULL COMMENT 'ÊâÄÂ±ûÁî®Êà∑',
  `model` int(11) DEFAULT NULL COMMENT 'Ê®°Âºè',
  `batery` int(11) DEFAULT NULL,
  `accumulatePower` bigint(11) DEFAULT NULL,
  `reactivePower` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÂäüÁéá',
  `reactiveEnergy` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÁîµËÉΩ',
  `apparentPower` int(11) DEFAULT NULL COMMENT 'ËßÜÂú®Â∑•Áéá',
  `voltage` int(11) DEFAULT NULL COMMENT 'ÁîµÂéã',
  `current` int(11) DEFAULT NULL COMMENT 'ÁîµÊµÅ',
  `frequency` int(11) DEFAULT NULL COMMENT 'È¢ëÁéá',
  `powerFactor` int(11) DEFAULT NULL COMMENT 'ÂäüÁéáÂõ†Êï∞',
  `period` int(11) DEFAULT NULL COMMENT 'ÊåÅÁª≠Êó∂Èó¥',
  `activeDemand` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÈúÄÊ±Ç',
  `reactiveDemand` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÈúÄÊ±Ç',
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15593 DEFAULT CHARSET=utf8;

/*Data for the table `spms_ac_status_month` */

/*Table structure for table `spms_ac_status_year` */

DROP TABLE IF EXISTS `spms_ac_status_year`;

CREATE TABLE `spms_ac_status_year` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '‰∏ªÈîÆ',
  `device_id` varchar(64) DEFAULT NULL COMMENT 'ËÆæÂ§áID',
  `ac_id` varchar(64) DEFAULT NULL COMMENT 'Á©∫Ë∞ÉID',
  `on_off` int(11) DEFAULT NULL COMMENT 'ÂºÄÂÖ≥',
  `temp` int(11) DEFAULT NULL COMMENT 'ÂÆ§ÂÜÖÊ∏©Â∫¶',
  `ac_temp` int(11) DEFAULT NULL COMMENT 'Á©∫Ë∞ÉÊ∏©Â∫¶',
  `power` bigint(11) DEFAULT NULL COMMENT 'ÂäüÁéá',
  `speed` int(11) DEFAULT NULL COMMENT 'È£éÈÄü',
  `direction` int(11) DEFAULT NULL COMMENT 'È£éÂêë',
  `run_duration` int(11) DEFAULT NULL COMMENT 'ÊåÅÁª≠Êó∂Èïø',
  `user_id` varchar(64) DEFAULT NULL COMMENT 'ÊâÄÂ±ûÁî®Êà∑',
  `model` int(11) DEFAULT NULL COMMENT 'Ê®°Âºè',
  `batery` int(11) DEFAULT NULL,
  `accumulatePower` bigint(11) DEFAULT NULL,
  `reactivePower` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÂäüÁéá',
  `reactiveEnergy` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÁîµËÉΩ',
  `apparentPower` int(11) DEFAULT NULL COMMENT 'ËßÜÂú®Â∑•Áéá',
  `voltage` int(11) DEFAULT NULL COMMENT 'ÁîµÂéã',
  `current` int(11) DEFAULT NULL COMMENT 'ÁîµÊµÅ',
  `frequency` int(11) DEFAULT NULL COMMENT 'È¢ëÁéá',
  `powerFactor` int(11) DEFAULT NULL COMMENT 'ÂäüÁéáÂõ†Êï∞',
  `period` int(11) DEFAULT NULL COMMENT 'ÊåÅÁª≠Êó∂Èó¥',
  `activeDemand` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÈúÄÊ±Ç',
  `reactiveDemand` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÈúÄÊ±Ç',
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=7073 DEFAULT CHARSET=utf8;

/*Data for the table `spms_ac_status_year` */

/*Table structure for table `spms_account_bill` */

DROP TABLE IF EXISTS `spms_account_bill`;

CREATE TABLE `spms_account_bill` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `accountMonthRest` double DEFAULT NULL,
  `billCycle` varchar(32) DEFAULT NULL,
  `billDate` datetime NOT NULL,
  `countMonthRMB` double DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `monthCredit` double DEFAULT NULL,
  `printDate` datetime DEFAULT NULL,
  `sendMsgFlag` int(11) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `uesr_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_oisebiqkr1m1v50uyvpd0yby0` (`createUser_id`),
  KEY `FK_8ij00hurwc7ji7lw8b2rhdx85` (`lastModifyUser_id`),
  KEY `FK_g4x9thf6oux4k5c37fjpupaq6` (`uesr_id`),
  CONSTRAINT `FK_g4x9thf6oux4k5c37fjpupaq6` FOREIGN KEY (`uesr_id`) REFERENCES `spms_user` (`id`),
  CONSTRAINT `FK_8ij00hurwc7ji7lw8b2rhdx85` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_oisebiqkr1m1v50uyvpd0yby0` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_account_bill` */

/*Table structure for table `spms_area` */

DROP TABLE IF EXISTS `spms_area`;

CREATE TABLE `spms_area` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `classification` varchar(1) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `parentIds` varchar(255) DEFAULT NULL,
  `policy` int(11) DEFAULT NULL,
  `remarks` varchar(200) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `type` varchar(1) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_tc8r767h2swv3hk1dtov7p0ry` (`createUser_id`),
  KEY `FK_mlc0y2wvwq4acwihtgtkk37il` (`lastModifyUser_id`),
  KEY `FK_kycepwpc22bgkmsci63p8wmjj` (`parent_id`),
  CONSTRAINT `FK_kycepwpc22bgkmsci63p8wmjj` FOREIGN KEY (`parent_id`) REFERENCES `spms_area` (`id`),
  CONSTRAINT `FK_mlc0y2wvwq4acwihtgtkk37il` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_tc8r767h2swv3hk1dtov7p0ry` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_area` */

/*Table structure for table `spms_central_ac` */

DROP TABLE IF EXISTS `spms_central_ac`;

CREATE TABLE `spms_central_ac` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `acTemp` int(11) DEFAULT NULL,
  `accumulatePower` int(11) DEFAULT NULL,
  `direction` int(11) DEFAULT NULL,
  `floorTemp` int(11) DEFAULT NULL,
  `leftRightSwing` int(11) DEFAULT NULL,
  `power` float DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `temp` int(11) DEFAULT NULL,
  `upDownSwing` int(11) DEFAULT NULL,
  `upperTemp` int(11) DEFAULT NULL,
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5bsq0apd65rqwpejn54fp9tpe` (`gw_id`),
  CONSTRAINT `FK_5bsq0apd65rqwpejn54fp9tpe` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_central_ac` */

/*Table structure for table `spms_device_curve` */

DROP TABLE IF EXISTS `spms_device_curve`;

CREATE TABLE `spms_device_curve` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `friday` int(11) DEFAULT NULL,
  `monday` int(11) DEFAULT NULL,
  `saturday` int(11) DEFAULT NULL,
  `sunday` int(11) DEFAULT NULL,
  `thursday` int(11) DEFAULT NULL,
  `tuesday` int(11) DEFAULT NULL,
  `wednesday` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `curve_id` varchar(255) DEFAULT NULL,
  `spmsDevice_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_4e088s25evxkm8eq9we4ml95g` (`createUser_id`),
  KEY `FK_odp4wm9q0qm3pp0pka8mggsox` (`lastModifyUser_id`),
  KEY `FK_occpuehxw26d4h2cswgbqfn3u` (`curve_id`),
  KEY `FK_i118udrsic06q9etc8ol52djd` (`spmsDevice_id`),
  CONSTRAINT `FK_i118udrsic06q9etc8ol52djd` FOREIGN KEY (`spmsDevice_id`) REFERENCES `spms_ac` (`id`),
  CONSTRAINT `FK_4e088s25evxkm8eq9we4ml95g` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_occpuehxw26d4h2cswgbqfn3u` FOREIGN KEY (`curve_id`) REFERENCES `spms_raircon_setting` (`id`),
  CONSTRAINT `FK_odp4wm9q0qm3pp0pka8mggsox` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_device_curve` */

/*Table structure for table `spms_device_error` */

DROP TABLE IF EXISTS `spms_device_error`;

CREATE TABLE `spms_device_error` (
  `deviceId` varchar(100) NOT NULL,
  `errorCode` varchar(20) DEFAULT NULL,
  `id` varchar(255) NOT NULL,
  `version` bigint(20) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `tenantId` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_n1cfw8pd88xt869rlsolp4vhh` (`createUser_id`) USING BTREE,
  KEY `FK_8vlmdeel3yhpxommv7bgaug28` (`lastModifyUser_id`) USING BTREE,
  KEY `FK_6deoveg318ndy07gqie1rl6tf` (`deviceId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `spms_device_error` */

/*Table structure for table `spms_dsm` */

DROP TABLE IF EXISTS `spms_dsm`;

CREATE TABLE `spms_dsm` (
  `deviceId` varchar(255) NOT NULL,
  `lowTemp` int(3) DEFAULT NULL,
  `upperTemp` int(3) DEFAULT NULL,
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`deviceId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

/*Data for the table `spms_dsm` */

/*Table structure for table `spms_electro_result_data` */

DROP TABLE IF EXISTS `spms_electro_result_data`;

CREATE TABLE `spms_electro_result_data` (
  `id` varchar(64) NOT NULL,
  `area_id` varchar(64) NOT NULL COMMENT 'Âå∫Âüüid',
  `start_time` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'ÂºÄÂßãÊó∂Èó¥',
  `power` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÂäüÁéá/ÊúâÂäüÁîµËÉΩ',
  `reactive_power` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÂäüÁéá',
  `power_factor` int(11) DEFAULT NULL COMMENT 'ÂäüÁéáÂõ†Êï∞',
  `apparent_power` int(11) DEFAULT NULL COMMENT 'ËßÜÂú®ÂäüÁéá',
  `reactive_energy` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÁîµËÉΩ',
  `reactive_demand` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÈúÄÊ±Ç',
  `active_demand` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÈúÄÊ±Ç',
  `demand_time` timestamp NULL DEFAULT NULL,
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_electro_result_data` */

/*Table structure for table `spms_electro_result_data_hour` */

DROP TABLE IF EXISTS `spms_electro_result_data_hour`;

CREATE TABLE `spms_electro_result_data_hour` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT 'Âå∫Âüüid',
  `power` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÂäüÁéá/ÊúâÂäüÁîµËÉΩ',
  `reactive_power` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÂäüÁéá',
  `power_factor` int(11) DEFAULT NULL COMMENT 'ÂäüÁéáÂõ†Êï∞',
  `apparent_power` int(11) DEFAULT NULL COMMENT 'ËßÜÂú®ÂäüÁéá',
  `reactive_energy` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÁîµËÉΩ',
  `reactive_demand` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÈúÄÊ±Ç',
  `active_demand` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÈúÄÊ±Ç',
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  `date` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Data for the table `spms_electro_result_data_hour` */

/*Table structure for table `spms_electro_result_data_month` */

DROP TABLE IF EXISTS `spms_electro_result_data_month`;

CREATE TABLE `spms_electro_result_data_month` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(64) NOT NULL COMMENT 'Âå∫Âüüid',
  `power` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÂäüÁéá/ÊúâÂäüÁîµËÉΩ',
  `reactive_power` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÂäüÁéá',
  `power_factor` int(11) DEFAULT NULL COMMENT 'ÂäüÁéáÂõ†Êï∞',
  `apparent_power` int(11) DEFAULT NULL COMMENT 'ËßÜÂú®ÂäüÁéá',
  `reactive_energy` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÁîµËÉΩ',
  `reactive_demand` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÈúÄÊ±Ç',
  `active_demand` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÈúÄÊ±Ç',
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=237 DEFAULT CHARSET=utf8;

/*Data for the table `spms_electro_result_data_month` */

/*Table structure for table `spms_electro_result_data_year` */

DROP TABLE IF EXISTS `spms_electro_result_data_year`;

CREATE TABLE `spms_electro_result_data_year` (
  `id` int(64) NOT NULL AUTO_INCREMENT,
  `area_id` varchar(64) NOT NULL COMMENT 'Âå∫Âüüid',
  `power` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÂäüÁéá/ÊúâÂäüÁîµËÉΩ',
  `reactive_power` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÂäüÁéá',
  `power_factor` int(11) DEFAULT NULL COMMENT 'ÂäüÁéáÂõ†Êï∞',
  `apparent_power` int(11) DEFAULT NULL COMMENT 'ËßÜÂú®ÂäüÁéá',
  `reactive_energy` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÁîµËÉΩ',
  `reactive_demand` int(11) DEFAULT NULL COMMENT 'Êó†ÂäüÈúÄÊ±Ç',
  `active_demand` int(11) DEFAULT NULL COMMENT 'ÊúâÂäüÈúÄÊ±Ç',
  `device_num` int(11) DEFAULT NULL,
  `accumulatePower` bigint(60) DEFAULT NULL,
  `date` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `spms_electro_result_data_year` */

/*Table structure for table `spms_gateway` */

DROP TABLE IF EXISTS `spms_gateway`;

CREATE TABLE `spms_gateway` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `ip` varchar(100) DEFAULT NULL,
  `gw_id` varchar(255) DEFAULT NULL,
  `area_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_780hpfgl0rp59t6ubb4we38km` (`gw_id`),
  KEY `FK_3ynb2f7wdt26l1ghm1206s1jy` (`area_id`),
  CONSTRAINT `FK_3ynb2f7wdt26l1ghm1206s1jy` FOREIGN KEY (`area_id`) REFERENCES `spms_area` (`id`),
  CONSTRAINT `FK_780hpfgl0rp59t6ubb4we38km` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_gateway` */

/*Table structure for table `spms_gw_status` */

DROP TABLE IF EXISTS `spms_gw_status`;

CREATE TABLE `spms_gw_status` (
  `id` varchar(64) NOT NULL COMMENT '‰∏ªÈîÆ',
  `device_id` varchar(64) DEFAULT NULL COMMENT 'ËÆæÂ§áID',
  `gw_id` varchar(64) DEFAULT NULL COMMENT 'ÁΩëÂÖ≥ID',
  `status` int(11) DEFAULT NULL COMMENT 'ÁΩëÂÖ≥Áä∂ÊÄÅ',
  `user_id` varchar(64) DEFAULT NULL COMMENT 'ÊâÄÂ±ûÁî®Êà∑',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ÊúÄÂêéËÆ∞ÂΩïÊó∂Èó¥',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='ÁΩëÂÖ≥ÂΩìÂâçÁä∂ÊÄÅ';

/*Data for the table `spms_gw_status` */

/*Table structure for table `spms_ht_sensor` */

DROP TABLE IF EXISTS `spms_ht_sensor`;

CREATE TABLE `spms_ht_sensor` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `humidity` int(11) DEFAULT NULL,
  `remain` int(11) DEFAULT NULL,
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_iejyspk1iwwjkx18xmaxegegh` (`gw_id`),
  CONSTRAINT `FK_iejyspk1iwwjkx18xmaxegegh` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_ht_sensor` */

/*Table structure for table `spms_ht_sensor_status` */

DROP TABLE IF EXISTS `spms_ht_sensor_status`;

CREATE TABLE `spms_ht_sensor_status` (
  `id` varchar(100) NOT NULL,
  `mac` varchar(20) DEFAULT NULL,
  `temp` int(11) DEFAULT NULL,
  `humidity` int(11) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_ht_sensor_status` */

/*Table structure for table `spms_lamp` */

DROP TABLE IF EXISTS `spms_lamp`;

CREATE TABLE `spms_lamp` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `blue` int(11) DEFAULT NULL,
  `green` int(11) DEFAULT NULL,
  `illuminance` int(11) DEFAULT NULL,
  `red` int(11) DEFAULT NULL,
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_h4ib6itto97jy9ks53963c8nx` (`gw_id`),
  CONSTRAINT `FK_h4ib6itto97jy9ks53963c8nx` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_lamp` */

/*Table structure for table `spms_logs_device` */

DROP TABLE IF EXISTS `spms_logs_device`;

CREATE TABLE `spms_logs_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_after` varchar(255) DEFAULT NULL,
  `data_before` varchar(255) DEFAULT NULL,
  `deviceId` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `modify_user` varchar(255) DEFAULT NULL,
  `operateDate` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_logs_device` */

/*Table structure for table `spms_logs_user` */

DROP TABLE IF EXISTS `spms_logs_user`;

CREATE TABLE `spms_logs_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_after` varchar(255) DEFAULT NULL,
  `data_before` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `modify_user` varchar(255) DEFAULT NULL,
  `operateDate` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `userId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_logs_user` */

/*Table structure for table `spms_ndr_dsm` */

DROP TABLE IF EXISTS `spms_ndr_dsm`;

CREATE TABLE `spms_ndr_dsm` (
  `faId` varchar(255) NOT NULL,
  `id` varchar(255) NOT NULL,
  `infoType` int(11) NOT NULL,
  `abateDate` datetime DEFAULT NULL,
  `actualReduceP` int(11) DEFAULT NULL,
  `actualReduceQ` int(11) DEFAULT NULL,
  `confirmDate` datetime DEFAULT NULL,
  `contact` varchar(255) DEFAULT NULL,
  `contactPhone` varchar(255) DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `impDate` datetime DEFAULT NULL,
  `msgDes` varchar(255) DEFAULT NULL,
  `planReduceTargetP` int(11) DEFAULT NULL,
  `planReduceTargetQ` int(11) DEFAULT NULL,
  `promisedReduceTargetP` int(11) DEFAULT NULL,
  `promisedReduceTargetQ` int(11) DEFAULT NULL,
  `releaseDate` datetime DEFAULT NULL,
  `releaseUser` varchar(255) DEFAULT NULL,
  `resType` varchar(255) DEFAULT NULL,
  `schemeCode` varchar(255) DEFAULT NULL,
  `schemeDate` varchar(255) DEFAULT NULL,
  `schemeIntroduction` varchar(255) DEFAULT NULL,
  `schemeName` varchar(255) DEFAULT NULL,
  `schemePeople` varchar(255) DEFAULT NULL,
  `schemeType` varchar(255) DEFAULT NULL,
  `startConditions` varchar(255) DEFAULT NULL,
  `startDate` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`faId`,`id`,`infoType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_ndr_dsm` */

/*Table structure for table `spms_onoff_light` */

DROP TABLE IF EXISTS `spms_onoff_light`;

CREATE TABLE `spms_onoff_light` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_mdpmqsdhw6qhp7ipq9kekda6p` (`gw_id`),
  CONSTRAINT `FK_mdpmqsdhw6qhp7ipq9kekda6p` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_onoff_light` */

/*Table structure for table `spms_onoff_plug` */

DROP TABLE IF EXISTS `spms_onoff_plug`;

CREATE TABLE `spms_onoff_plug` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_28qptkre9s2uja1uxtnl2ghdy` (`gw_id`),
  CONSTRAINT `FK_28qptkre9s2uja1uxtnl2ghdy` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_onoff_plug` */

/*Table structure for table `spms_onoff_pm_plug` */

DROP TABLE IF EXISTS `spms_onoff_pm_plug`;

CREATE TABLE `spms_onoff_pm_plug` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fq2kgs43ln6gl3ga8je6r3ppi` (`gw_id`),
  CONSTRAINT `FK_fq2kgs43ln6gl3ga8je6r3ppi` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_onoff_pm_plug` */

/*Table structure for table `spms_onoff_pm_plug_status` */

DROP TABLE IF EXISTS `spms_onoff_pm_plug_status`;

CREATE TABLE `spms_onoff_pm_plug_status` (
  `id` varchar(50) NOT NULL,
  `mac` varchar(50) DEFAULT NULL,
  `onoff` int(11) DEFAULT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_onoff_pm_plug_status` */

/*Table structure for table `spms_pir` */

DROP TABLE IF EXISTS `spms_pir`;

CREATE TABLE `spms_pir` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `remain` int(11) DEFAULT NULL,
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_owamrj9xc8hhpyhap56eufavb` (`gw_id`),
  CONSTRAINT `FK_owamrj9xc8hhpyhap56eufavb` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_pir` */

/*Table structure for table `spms_pir_status` */

DROP TABLE IF EXISTS `spms_pir_status`;

CREATE TABLE `spms_pir_status` (
  `id` varchar(50) NOT NULL,
  `mac` varchar(20) DEFAULT NULL,
  `alarmed` int(11) DEFAULT NULL,
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_pir_status` */

/*Table structure for table `spms_plug_ac` */

DROP TABLE IF EXISTS `spms_plug_ac`;

CREATE TABLE `spms_plug_ac` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `acTemp` int(11) DEFAULT NULL,
  `accumulatePower` int(11) DEFAULT NULL,
  `direction` int(11) DEFAULT NULL,
  `floorTemp` int(11) DEFAULT NULL,
  `humidity` int(11) DEFAULT NULL,
  `isPaired` int(11) DEFAULT NULL,
  `leftRightSwing` int(11) DEFAULT NULL,
  `power` float DEFAULT NULL,
  `rcuId` int(11) DEFAULT NULL,
  `speed` int(11) DEFAULT NULL,
  `temp` int(11) DEFAULT NULL,
  `upDownSwing` int(11) DEFAULT NULL,
  `upperTemp` int(11) DEFAULT NULL,
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_k3uoi6xkqlw50ds4ejkmr3m81` (`gw_id`),
  CONSTRAINT `FK_k3uoi6xkqlw50ds4ejkmr3m81` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_plug_ac` */

/*Table structure for table `spms_portal_custom` */

DROP TABLE IF EXISTS `spms_portal_custom`;

CREATE TABLE `spms_portal_custom` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `isdisPlay` int(11) DEFAULT NULL,
  `rolecode` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `modules_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9i66b5wanjwww0klydrf6kijp` (`createUser_id`),
  KEY `FK_7h0pdj1lx77yy63hm7etftgbw` (`lastModifyUser_id`),
  KEY `FK_igg63a6yumhctoruc5ay4hrqw` (`modules_id`),
  KEY `FK_2h7x6bpmvdd0puace3lgclfs8` (`user_id`),
  CONSTRAINT `FK_2h7x6bpmvdd0puace3lgclfs8` FOREIGN KEY (`user_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_7h0pdj1lx77yy63hm7etftgbw` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_9i66b5wanjwww0klydrf6kijp` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_igg63a6yumhctoruc5ay4hrqw` FOREIGN KEY (`modules_id`) REFERENCES `spms_portal_modules` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_portal_custom` */

/*Table structure for table `spms_portal_default` */

DROP TABLE IF EXISTS `spms_portal_default`;

CREATE TABLE `spms_portal_default` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `role_code` varchar(10) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `modules_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_k3nk8dsiv8sipup5okrn8nn8b` (`createUser_id`),
  KEY `FK_b9n3v805n8lyu1w484wm35dal` (`lastModifyUser_id`),
  KEY `FK_a06nwm1u3ksb4hsl760n37sfr` (`modules_id`),
  CONSTRAINT `FK_a06nwm1u3ksb4hsl760n37sfr` FOREIGN KEY (`modules_id`) REFERENCES `spms_portal_modules` (`id`),
  CONSTRAINT `FK_b9n3v805n8lyu1w484wm35dal` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_k3nk8dsiv8sipup5okrn8nn8b` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_portal_default` */

/*Table structure for table `spms_portal_modules` */

DROP TABLE IF EXISTS `spms_portal_modules`;

CREATE TABLE `spms_portal_modules` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `divName` varchar(255) DEFAULT NULL,
  `divTitle` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_n0ply89q3nbiiusdt6us1pth4` (`createUser_id`),
  KEY `FK_5e290oggfnktfrohlnjxtbmvk` (`lastModifyUser_id`),
  CONSTRAINT `FK_5e290oggfnktfrohlnjxtbmvk` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_n0ply89q3nbiiusdt6us1pth4` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_portal_modules` */

/*Table structure for table `spms_product` */

DROP TABLE IF EXISTS `spms_product`;

CREATE TABLE `spms_product` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `activateDate` datetime DEFAULT NULL,
  `electricityCostRmb` double NOT NULL,
  `expireDate` datetime DEFAULT NULL,
  `initialCostRmb` double NOT NULL,
  `status` int(11) NOT NULL,
  `subscribeDate` datetime NOT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `type_id` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `spmsWorkOrder_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_nbtj42009m1qxnmpptr9rh3gy` (`createUser_id`),
  KEY `FK_6lafari40clvi0w6xk2fna0i` (`lastModifyUser_id`),
  KEY `FK_q4sdn1t5smtja5vk4oqig2psg` (`type_id`),
  KEY `FK_4gt93m8ta7sfewm75wcv4pq3e` (`user_id`),
  CONSTRAINT `FK_4gt93m8ta7sfewm75wcv4pq3e` FOREIGN KEY (`user_id`) REFERENCES `spms_user` (`id`),
  CONSTRAINT `FK_6lafari40clvi0w6xk2fna0i` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_nbtj42009m1qxnmpptr9rh3gy` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_q4sdn1t5smtja5vk4oqig2psg` FOREIGN KEY (`type_id`) REFERENCES `spms_product_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_product` */

/*Table structure for table `spms_product_m_fee` */

DROP TABLE IF EXISTS `spms_product_m_fee`;

CREATE TABLE `spms_product_m_fee` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `elecFee` double DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `productFee` double DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `product_id` varchar(255) DEFAULT NULL,
  `spms_user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_rnbaothvqv7rjk1brqe9cysmq` (`createUser_id`),
  KEY `FK_dqour72acbh6k0ijegibr3cia` (`lastModifyUser_id`),
  KEY `FK_8s7yra13hsx1alge53axf7ifc` (`product_id`),
  KEY `FK_sqcyurw3y4wurtqyoriux97ad` (`spms_user_id`),
  CONSTRAINT `FK_sqcyurw3y4wurtqyoriux97ad` FOREIGN KEY (`spms_user_id`) REFERENCES `spms_user` (`id`),
  CONSTRAINT `FK_8s7yra13hsx1alge53axf7ifc` FOREIGN KEY (`product_id`) REFERENCES `spms_product` (`id`),
  CONSTRAINT `FK_dqour72acbh6k0ijegibr3cia` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_rnbaothvqv7rjk1brqe9cysmq` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_product_m_fee` */

/*Table structure for table `spms_product_type` */

DROP TABLE IF EXISTS `spms_product_type`;

CREATE TABLE `spms_product_type` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `chuangGanCount` int(11) DEFAULT NULL,
  `configurationInformation` int(11) NOT NULL,
  `countRmb` double NOT NULL,
  `deleteStauts` int(11) DEFAULT NULL,
  `describes` varchar(255) DEFAULT NULL,
  `electrovalenceRmb` double NOT NULL,
  `indexRmb` double NOT NULL,
  `kongTiaoCount` int(11) DEFAULT NULL,
  `lianDong` int(11) DEFAULT NULL,
  `muluId` int(11) NOT NULL,
  `names` varchar(255) DEFAULT NULL,
  `refundable` varchar(64) DEFAULT NULL,
  `rmbType` int(11) DEFAULT NULL,
  `sensorEnabled` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `stops` int(11) DEFAULT NULL,
  `upgradeable` varchar(64) DEFAULT NULL,
  `validPeriod` varchar(32) NOT NULL,
  `zhiLengMax` int(11) DEFAULT NULL,
  `zhiLengMix` int(11) DEFAULT NULL,
  `zhiReMax` int(11) DEFAULT NULL,
  `zhiReMix` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `area_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ow3ptyxusw19e36psp7nhrcrm` (`createUser_id`),
  KEY `FK_ojlfpy65xmqruhw44423cn49y` (`lastModifyUser_id`),
  KEY `FK_3ug8cgh324497wsnmcos09u3w` (`area_id`),
  CONSTRAINT `FK_3ug8cgh324497wsnmcos09u3w` FOREIGN KEY (`area_id`) REFERENCES `spms_area` (`id`),
  CONSTRAINT `FK_ojlfpy65xmqruhw44423cn49y` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_ow3ptyxusw19e36psp7nhrcrm` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_product_type` */

/*Table structure for table `spms_raircon_setting` */

DROP TABLE IF EXISTS `spms_raircon_setting`;

CREATE TABLE `spms_raircon_setting` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `friday` int(11) DEFAULT NULL,
  `monday` int(11) DEFAULT NULL,
  `saturday` int(11) DEFAULT NULL,
  `startend` int(11) DEFAULT NULL,
  `sunday` int(11) DEFAULT NULL,
  `thursday` int(11) DEFAULT NULL,
  `tuesday` int(11) DEFAULT NULL,
  `wednesday` int(11) DEFAULT NULL,
  `week` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `spmsDevice_id` varchar(255) DEFAULT NULL,
  `spms_userId` varchar(255) DEFAULT NULL,
  `sys_user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_eav1xwr4fpe55lyogn7knqfp6` (`createUser_id`),
  KEY `FK_fhpi4srpswmney72qfmmoaky0` (`lastModifyUser_id`),
  KEY `FK_1ijng7xb2wphq3yv6f1cuqv2g` (`spmsDevice_id`),
  KEY `FK_7ehq0wf28fljdjjyvkampv8dc` (`spms_userId`),
  KEY `FK_odja0kjpt3hb9pcoph6ip7ihb` (`sys_user_id`),
  CONSTRAINT `FK_odja0kjpt3hb9pcoph6ip7ihb` FOREIGN KEY (`sys_user_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_1ijng7xb2wphq3yv6f1cuqv2g` FOREIGN KEY (`spmsDevice_id`) REFERENCES `spms_ac` (`id`),
  CONSTRAINT `FK_7ehq0wf28fljdjjyvkampv8dc` FOREIGN KEY (`spms_userId`) REFERENCES `spms_user` (`id`),
  CONSTRAINT `FK_eav1xwr4fpe55lyogn7knqfp6` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_fhpi4srpswmney72qfmmoaky0` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_raircon_setting` */

/*Table structure for table `spms_ud_group` */

DROP TABLE IF EXISTS `spms_ud_group`;

CREATE TABLE `spms_ud_group` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `groupType` int(11) DEFAULT NULL,
  `ud_id` varchar(255) DEFAULT NULL,
  `ud_group_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9p235l8smlqu46q0j9i9averg` (`ud_id`),
  KEY `FK_cnwfc80avx79vppob23xoi3ct` (`ud_group_id`),
  CONSTRAINT `FK_cnwfc80avx79vppob23xoi3ct` FOREIGN KEY (`ud_group_id`) REFERENCES `tb_ud_group` (`id`),
  CONSTRAINT `FK_9p235l8smlqu46q0j9i9averg` FOREIGN KEY (`ud_id`) REFERENCES `spms_user_device` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_ud_group` */

/*Table structure for table `spms_user` */

DROP TABLE IF EXISTS `spms_user`;

CREATE TABLE `spms_user` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `address` longtext,
  `email` varchar(255) DEFAULT NULL,
  `fullname` varchar(32) DEFAULT NULL,
  `idNumber` varchar(255) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `mobileMac` varchar(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `spmsWorkOrder_id` varchar(255) DEFAULT NULL,
  `sys_user_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1kanwjbsk6g3in9vvyatlxvmv` (`createUser_id`),
  KEY `FK_35sbdhhhmk63q86uyil1t3ucq` (`lastModifyUser_id`),
  KEY `FK_4obqh9qjbf4pbxf56ng9468hg` (`sys_user_id`),
  CONSTRAINT `FK_4obqh9qjbf4pbxf56ng9468hg` FOREIGN KEY (`sys_user_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_1kanwjbsk6g3in9vvyatlxvmv` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_35sbdhhhmk63q86uyil1t3ucq` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_user` */

/*Table structure for table `spms_user_async` */

DROP TABLE IF EXISTS `spms_user_async`;

CREATE TABLE `spms_user_async` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `image` longblob,
  `mobile` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_user_async` */

/*Table structure for table `spms_user_device` */

DROP TABLE IF EXISTS `spms_user_device`;

CREATE TABLE `spms_user_device` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `device_id` varchar(100) DEFAULT NULL,
  `device_type` int(11) DEFAULT NULL,
  `is_primary` int(11) DEFAULT NULL,
  `mac` varchar(100) DEFAULT NULL,
  `mobile` varchar(100) DEFAULT NULL,
  `user_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_user_device` */

/*Table structure for table `spms_user_product_binding` */

DROP TABLE IF EXISTS `spms_user_product_binding`;

CREATE TABLE `spms_user_product_binding` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_djl9h5m7ko8ki9cm2fuypp5fj` (`createUser_id`),
  KEY `FK_c23ebjoyelsr1engpfs51lpr0` (`lastModifyUser_id`),
  CONSTRAINT `FK_c23ebjoyelsr1engpfs51lpr0` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_djl9h5m7ko8ki9cm2fuypp5fj` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_user_product_binding` */

/*Table structure for table `spms_warning_setting` */

DROP TABLE IF EXISTS `spms_warning_setting`;

CREATE TABLE `spms_warning_setting` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `acAvgTemp` float DEFAULT NULL,
  `acJoinNum` int(11) DEFAULT NULL,
  `avgLoad` int(11) DEFAULT NULL,
  `gwJoinNum` int(11) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `maxLoad` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `area_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9rxrm3jg8nmhlf7y2l460vcst` (`createUser_id`),
  KEY `FK_js1sk7g0nvw09h1vo9i2hcth0` (`lastModifyUser_id`),
  KEY `FK_u8i089sy2mhn3lrggp575dox` (`area_id`),
  CONSTRAINT `FK_u8i089sy2mhn3lrggp575dox` FOREIGN KEY (`area_id`) REFERENCES `spms_area` (`id`),
  CONSTRAINT `FK_9rxrm3jg8nmhlf7y2l460vcst` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_js1sk7g0nvw09h1vo9i2hcth0` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_warning_setting` */

/*Table structure for table `spms_win_door` */

DROP TABLE IF EXISTS `spms_win_door`;

CREATE TABLE `spms_win_door` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `cursoft` varchar(128) DEFAULT 'V2.0',
  `disable` int(11) DEFAULT NULL,
  `hardware` varchar(128) NOT NULL DEFAULT '0',
  `mac` varchar(64) NOT NULL DEFAULT '0',
  `mode` int(11) DEFAULT NULL,
  `model` varchar(128) NOT NULL DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `onOff` int(11) DEFAULT NULL,
  `operStatus` int(11) DEFAULT NULL,
  `runDuration` int(11) DEFAULT NULL,
  `sessionId` varchar(64) DEFAULT NULL,
  `sn` varchar(32) NOT NULL DEFAULT '0',
  `software` varchar(128) NOT NULL DEFAULT '0',
  `startTime` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `storage` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(255) DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  `vender` varchar(256) NOT NULL DEFAULT 'innolinks',
  `remain` int(11) DEFAULT NULL,
  `gw_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_lr9ggln18s902o50fc678j5n7` (`gw_id`),
  CONSTRAINT `FK_lr9ggln18s902o50fc678j5n7` FOREIGN KEY (`gw_id`) REFERENCES `spms_gateway` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_win_door` */

/*Table structure for table `spms_win_door_status` */

DROP TABLE IF EXISTS `spms_win_door_status`;

CREATE TABLE `spms_win_door_status` (
  `id` varchar(64) NOT NULL COMMENT '‰∏ªÈîÆ',
  `device_id` varchar(64) DEFAULT NULL COMMENT 'ËÆæÂ§áID',
  `operate_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Êìç‰ΩúÊó∂Èó¥',
  `type_id` varchar(64) DEFAULT NULL COMMENT 'ËÆæÂ§áÁ±ªÂûã',
  `operate_type` int(11) DEFAULT NULL COMMENT 'Êìç‰ΩúÁ±ªÂûã',
  `user_id` varchar(64) DEFAULT NULL COMMENT 'Êìç‰Ωú‰∫∫',
  `remain` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Èó®Á™óÊìç‰ΩúÊó•Âøó';

/*Data for the table `spms_win_door_status` */

/*Table structure for table `spms_workorder` */

DROP TABLE IF EXISTS `spms_workorder`;

CREATE TABLE `spms_workorder` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `activitycount` int(11) DEFAULT NULL,
  `activityindex` int(11) DEFAULT NULL,
  `address` longtext,
  `allottype` int(11) DEFAULT NULL,
  `creationdate` datetime DEFAULT NULL,
  `deleteReason` longtext,
  `duration` datetime DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `idNumber` varchar(20) DEFAULT NULL,
  `meterNumber` varchar(50) DEFAULT NULL,
  `processDefineId` varchar(255) DEFAULT NULL,
  `processInstanceId` varchar(255) DEFAULT NULL,
  `spmsUserMobile` varchar(15) DEFAULT NULL,
  `spmsUserName` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `userId` varchar(255) DEFAULT NULL,
  `userType` int(11) DEFAULT NULL,
  `workOrderCode` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `service_id` varchar(255) DEFAULT NULL,
  `ele_id` varchar(255) DEFAULT NULL,
  `owner_id` varchar(255) DEFAULT NULL,
  `spmsProductType_id` varchar(255) DEFAULT NULL,
  `supervisor_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_lfrlvvob68tekln1vd4yo7bfm` (`createUser_id`),
  KEY `FK_63dtgut2odsaj9wpc66hv785o` (`lastModifyUser_id`),
  KEY `FK_ormkhx9et5effj2666y8ge2ow` (`service_id`),
  KEY `FK_pqjhvt4js38nk4qe284pr4op6` (`ele_id`),
  KEY `FK_pg8m40xci26a219gda2syf7ou` (`owner_id`),
  KEY `FK_71y9o7thlrbmx9qi0sa7ddo37` (`spmsProductType_id`),
  KEY `FK_a53ar74x6egdguujga4pwongt` (`supervisor_id`),
  CONSTRAINT `FK_a53ar74x6egdguujga4pwongt` FOREIGN KEY (`supervisor_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_63dtgut2odsaj9wpc66hv785o` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_71y9o7thlrbmx9qi0sa7ddo37` FOREIGN KEY (`spmsProductType_id`) REFERENCES `spms_product_type` (`id`),
  CONSTRAINT `FK_lfrlvvob68tekln1vd4yo7bfm` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_ormkhx9et5effj2666y8ge2ow` FOREIGN KEY (`service_id`) REFERENCES `spms_area` (`id`),
  CONSTRAINT `FK_pg8m40xci26a219gda2syf7ou` FOREIGN KEY (`owner_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_pqjhvt4js38nk4qe284pr4op6` FOREIGN KEY (`ele_id`) REFERENCES `spms_area` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `spms_workorder` */

/*Table structure for table `sys_log` */

DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `exception` varchar(255) DEFAULT NULL,
  `method` varchar(255) DEFAULT NULL,
  `params` varchar(255) DEFAULT NULL,
  `remoteAddr` varchar(255) DEFAULT NULL,
  `requestUri` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `userAgent` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `createBy_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_eowcxludyr8eeraymuol9wa9` (`createUser_id`),
  KEY `FK_ikox3oeqhwe5wnm07yg8uplx0` (`lastModifyUser_id`),
  KEY `FK_9lee62vngfq45nh5t85ia33ur` (`createBy_id`),
  CONSTRAINT `FK_9lee62vngfq45nh5t85ia33ur` FOREIGN KEY (`createBy_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_eowcxludyr8eeraymuol9wa9` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_ikox3oeqhwe5wnm07yg8uplx0` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_log` */

/*Table structure for table `tb_attachement_attachment` */

DROP TABLE IF EXISTS `tb_attachement_attachment`;

CREATE TABLE `tb_attachement_attachment` (
  `id` varchar(255) NOT NULL,
  `businessId` varchar(255) DEFAULT NULL,
  `businessType` varchar(255) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `fileName` varchar(255) DEFAULT NULL,
  `filePath` varchar(255) DEFAULT NULL,
  `filePostfix` varchar(255) DEFAULT NULL,
  `fileSize` bigint(20) DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `version` bigint(20) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_33enj6mphpqj4lc71j85t0286` (`createUser_id`),
  KEY `FK_jnqmgop82vkr3qvaspkh58bqb` (`lastModifyUser_id`),
  CONSTRAINT `FK_jnqmgop82vkr3qvaspkh58bqb` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_33enj6mphpqj4lc71j85t0286` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_attachement_attachment` */

/*Table structure for table `tb_code_generater` */

DROP TABLE IF EXISTS `tb_code_generater`;

CREATE TABLE `tb_code_generater` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `code` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_code_generater` */

/*Table structure for table `tb_log_log_lc` */

DROP TABLE IF EXISTS `tb_log_log_lc`;

CREATE TABLE `tb_log_log_lc` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `businessType` varchar(4) DEFAULT NULL,
  `exception` longtext,
  `mark` longtext,
  `message` longtext,
  `method` longtext,
  `params` longtext,
  `remoteAddr` varchar(100) DEFAULT NULL,
  `requestUri` varchar(200) DEFAULT NULL,
  `terminalType` varchar(255) DEFAULT NULL,
  `type` varchar(4) DEFAULT NULL,
  `userAgent` longtext,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_ivd0bo0158e9p0ij7jeek079y` (`createUser_id`),
  KEY `FK_pdj2340eqgwqmnsvsa3i4c05a` (`lastModifyUser_id`),
  CONSTRAINT `FK_pdj2340eqgwqmnsvsa3i4c05a` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_ivd0bo0158e9p0ij7jeek079y` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_log_log_lc` */

/*Table structure for table `tb_log_requestcontrol` */

DROP TABLE IF EXISTS `tb_log_requestcontrol`;

CREATE TABLE `tb_log_requestcontrol` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `businessName` varchar(255) DEFAULT NULL,
  `requestUrl` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_l6p0h3b6ou3meo8fdsk8n51mq` (`createUser_id`),
  KEY `FK_os0p3gbwuc4do61a09wb9g4h7` (`lastModifyUser_id`),
  CONSTRAINT `FK_os0p3gbwuc4do61a09wb9g4h7` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_l6p0h3b6ou3meo8fdsk8n51mq` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_log_requestcontrol` */

/*Table structure for table `tb_org_org` */

DROP TABLE IF EXISTS `tb_org_org`;

CREATE TABLE `tb_org_org` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `code` varchar(100) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `fax` varchar(200) DEFAULT NULL,
  `grade` int(11) DEFAULT NULL,
  `master` varchar(100) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `parentIds` varchar(255) DEFAULT NULL,
  `phone` varchar(200) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `zipCode` varchar(100) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `area_id` varchar(255) DEFAULT NULL,
  `parent_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2sc1mx7y0k5ckh9kvf7r2h9cq` (`createUser_id`),
  KEY `FK_30jk6o2wmcdsqgcu7sy5eb7b0` (`lastModifyUser_id`),
  KEY `FK_li6djx3xqq6ma3i4g2ssk7as2` (`area_id`),
  KEY `FK_9pb9v44pn53uq8gmelsttafhk` (`parent_id`),
  CONSTRAINT `FK_9pb9v44pn53uq8gmelsttafhk` FOREIGN KEY (`parent_id`) REFERENCES `tb_org_org` (`id`),
  CONSTRAINT `FK_2sc1mx7y0k5ckh9kvf7r2h9cq` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_30jk6o2wmcdsqgcu7sy5eb7b0` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_li6djx3xqq6ma3i4g2ssk7as2` FOREIGN KEY (`area_id`) REFERENCES `spms_area` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_org_org` */

/*Table structure for table `tb_query_querycustom` */

DROP TABLE IF EXISTS `tb_query_querycustom`;

CREATE TABLE `tb_query_querycustom` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `columnNames` longtext,
  `queryId` varchar(255) DEFAULT NULL,
  `userEntity_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_query_querycustom` */

/*Table structure for table `tb_system_config` */

DROP TABLE IF EXISTS `tb_system_config`;

CREATE TABLE `tb_system_config` (
  `config_name` varchar(255) NOT NULL,
  `config_value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`config_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_system_config` */

/*Table structure for table `tb_ud_group` */

DROP TABLE IF EXISTS `tb_ud_group`;

CREATE TABLE `tb_ud_group` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `name` varchar(255) DEFAULT NULL,
  `area_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_k5bcl4wh6pmfvgi0br7dufhiu` (`area_id`),
  CONSTRAINT `FK_k5bcl4wh6pmfvgi0br7dufhiu` FOREIGN KEY (`area_id`) REFERENCES `spms_area` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_ud_group` */

/*Table structure for table `tb_user_arithmetic` */

DROP TABLE IF EXISTS `tb_user_arithmetic`;

CREATE TABLE `tb_user_arithmetic` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `arithName` varchar(100) DEFAULT NULL,
  `className` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_user_arithmetic` */

/*Table structure for table `tb_user_permission` */

DROP TABLE IF EXISTS `tb_user_permission`;

CREATE TABLE `tb_user_permission` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `parentPermissionPath` longtext,
  `permissionCode` varchar(50) NOT NULL,
  `permissionName` varchar(100) DEFAULT NULL,
  `permissionType` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `url` varchar(150) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `parentPermissionEntity_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_noem3oo1qbv6mp47afchxv7mq` (`createUser_id`),
  KEY `FK_lc3aka9fvwpkx31kchqda6fqh` (`lastModifyUser_id`),
  KEY `FK_hfdxxsh43mutq1wldfvi4qu7d` (`parentPermissionEntity_id`),
  CONSTRAINT `FK_hfdxxsh43mutq1wldfvi4qu7d` FOREIGN KEY (`parentPermissionEntity_id`) REFERENCES `tb_user_permission` (`id`),
  CONSTRAINT `FK_lc3aka9fvwpkx31kchqda6fqh` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_noem3oo1qbv6mp47afchxv7mq` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_user_permission` */

/*Table structure for table `tb_user_role` */

DROP TABLE IF EXISTS `tb_user_role`;

CREATE TABLE `tb_user_role` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `roleCode` varchar(50) NOT NULL,
  `roleName` varchar(100) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9ivml14xgqtb28spm6wyt8nm5` (`createUser_id`),
  KEY `FK_g0jn9w0mondtu14escrb3icrl` (`lastModifyUser_id`),
  CONSTRAINT `FK_g0jn9w0mondtu14escrb3icrl` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_9ivml14xgqtb28spm6wyt8nm5` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_user_role` */

/*Table structure for table `tb_user_user` */

DROP TABLE IF EXISTS `tb_user_user`;

CREATE TABLE `tb_user_user` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `birthday` varchar(200) DEFAULT NULL,
  `email` varchar(200) DEFAULT NULL,
  `expire_time` bigint(20) DEFAULT NULL,
  `loginDate` datetime DEFAULT NULL,
  `loginIp` varchar(50) DEFAULT NULL,
  `mark_` longtext,
  `mobile` varchar(200) DEFAULT NULL,
  `mobile_mac` varchar(200) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `no` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `phone_Number` varchar(200) DEFAULT NULL,
  `sex` varchar(10) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `userCode` varchar(100) DEFAULT NULL,
  `userName` varchar(100) DEFAULT NULL,
  `userType` int(11) NOT NULL DEFAULT '1',
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `companyEntity_id` varchar(255) DEFAULT NULL,
  `pwd_type_id` varchar(255) DEFAULT NULL,
  `org_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_b7j59ytcgoaa0ja4u9pxox957` (`createUser_id`),
  KEY `FK_p5ou6f1opy8rivek3xsp3wo0f` (`lastModifyUser_id`),
  KEY `FK_ixe19wer6d100k2kg1jbk4cce` (`companyEntity_id`),
  KEY `FK_gje1s5psg0rwb0a55k4sv59ea` (`pwd_type_id`),
  KEY `FK_dt0r7g0tli2f7iqqdr2u7w5qx` (`org_id`),
  CONSTRAINT `FK_dt0r7g0tli2f7iqqdr2u7w5qx` FOREIGN KEY (`org_id`) REFERENCES `tb_org_org` (`id`),
  CONSTRAINT `FK_b7j59ytcgoaa0ja4u9pxox957` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_gje1s5psg0rwb0a55k4sv59ea` FOREIGN KEY (`pwd_type_id`) REFERENCES `tb_user_arithmetic` (`id`),
  CONSTRAINT `FK_ixe19wer6d100k2kg1jbk4cce` FOREIGN KEY (`companyEntity_id`) REFERENCES `tb_org_org` (`id`),
  CONSTRAINT `FK_p5ou6f1opy8rivek3xsp3wo0f` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_user_user` */

/*Table structure for table `tb_workflow_group` */

DROP TABLE IF EXISTS `tb_workflow_group`;

CREATE TABLE `tb_workflow_group` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `groupCode` varchar(255) DEFAULT NULL,
  `groupName` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_h3wxm8orlwcbtnhn7qy54fo05` (`createUser_id`),
  KEY `FK_or4c7h33j844u8ld5r03gj78g` (`lastModifyUser_id`),
  CONSTRAINT `FK_or4c7h33j844u8ld5r03gj78g` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_h3wxm8orlwcbtnhn7qy54fo05` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_workflow_group` */

/*Table structure for table `tb_workflow_operater` */

DROP TABLE IF EXISTS `tb_workflow_operater`;

CREATE TABLE `tb_workflow_operater` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `approvalUrl` varchar(100) DEFAULT NULL,
  `businessInfoMethod` varchar(100) DEFAULT NULL,
  `callBackDataFun` varchar(255) DEFAULT NULL,
  `completeFlag` varchar(255) DEFAULT NULL,
  `countersign` varchar(255) DEFAULT NULL,
  `createDataFun` varchar(255) DEFAULT NULL,
  `defaultAdvice` varchar(255) DEFAULT NULL,
  `handleTime` int(11) DEFAULT NULL,
  `haveOperaterStr` varchar(255) DEFAULT NULL,
  `isGiveUser` varchar(255) DEFAULT NULL,
  `isShowDate` varchar(255) DEFAULT NULL,
  `isShowDialog` varchar(255) DEFAULT NULL,
  `markType` varchar(255) DEFAULT NULL,
  `operatorId` varchar(255) DEFAULT NULL,
  `orderFlag` int(11) DEFAULT NULL,
  `phoneActionType` varchar(255) DEFAULT NULL,
  `phoneHaveOperaterStr` varchar(255) DEFAULT NULL,
  `phoneMarkType` varchar(255) DEFAULT NULL,
  `requestUrl` varchar(255) DEFAULT NULL,
  `roleCodes` longtext,
  `selectType` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `userType` varchar(255) DEFAULT NULL,
  `workFlowTypeId` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_t81ju23jmitstlitfduq569yc` (`createUser_id`),
  KEY `FK_khjufns6597gkpassfc6epq63` (`lastModifyUser_id`),
  CONSTRAINT `FK_khjufns6597gkpassfc6epq63` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_t81ju23jmitstlitfduq569yc` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_workflow_operater` */

/*Table structure for table `tb_workflow_operatergroup` */

DROP TABLE IF EXISTS `tb_workflow_operatergroup`;

CREATE TABLE `tb_workflow_operatergroup` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `actionText` varchar(255) DEFAULT NULL,
  `operater` varchar(255) DEFAULT NULL,
  `workFlowTypeId` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_5kq7p9yg8gipuyojxaesc5eyu` (`createUser_id`),
  KEY `FK_j3daj9cb5xwvg7vt0jv2ffq76` (`lastModifyUser_id`),
  CONSTRAINT `FK_j3daj9cb5xwvg7vt0jv2ffq76` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_5kq7p9yg8gipuyojxaesc5eyu` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_workflow_operatergroup` */

/*Table structure for table `tb_workflow_status` */

DROP TABLE IF EXISTS `tb_workflow_status`;

CREATE TABLE `tb_workflow_status` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `flag` varchar(4) DEFAULT NULL,
  `processInstanceId` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_oklbqj2bahrkgqx9o4b0h9lnh` (`createUser_id`),
  KEY `FK_7sip4r78ohh275i3vgwdisqo5` (`lastModifyUser_id`),
  CONSTRAINT `FK_7sip4r78ohh275i3vgwdisqo5` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_oklbqj2bahrkgqx9o4b0h9lnh` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_workflow_status` */

/*Table structure for table `tb_workflow_todoconfig` */

DROP TABLE IF EXISTS `tb_workflow_todoconfig`;

CREATE TABLE `tb_workflow_todoconfig` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `appId` varchar(255) DEFAULT NULL,
  `baseClassName` varchar(255) DEFAULT NULL,
  `pcUrl` varchar(255) DEFAULT NULL,
  `phoneUrl` varchar(255) DEFAULT NULL,
  `titleField` varchar(255) DEFAULT NULL,
  `workFlowTypeId` varchar(255) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_6j95jhril10stw9lq47v79w9` (`createUser_id`),
  KEY `FK_29m70jb26tmj0plboid9gjofi` (`lastModifyUser_id`),
  CONSTRAINT `FK_29m70jb26tmj0plboid9gjofi` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_6j95jhril10stw9lq47v79w9` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_workflow_todoconfig` */

/*Table structure for table `tb_workflow_variable` */

DROP TABLE IF EXISTS `tb_workflow_variable`;

CREATE TABLE `tb_workflow_variable` (
  `iKey` varchar(255) NOT NULL,
  `taskId` varchar(255) NOT NULL,
  `iValue` varchar(255) DEFAULT NULL,
  `processId` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`iKey`,`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_workflow_variable` */

/*Table structure for table `tb_workorder_visit` */

DROP TABLE IF EXISTS `tb_workorder_visit`;

CREATE TABLE `tb_workorder_visit` (
  `id` varchar(255) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `version` bigint(20) DEFAULT '0',
  `createDate` datetime DEFAULT NULL,
  `lastModifyDate` datetime DEFAULT NULL,
  `info` longtext,
  `infoType` int(11) DEFAULT NULL,
  `createUser_id` varchar(255) DEFAULT NULL,
  `lastModifyUser_id` varchar(255) DEFAULT NULL,
  `spmsWorkOrder_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_9uohu5639b7fsxm93qpcu35j6` (`createUser_id`),
  KEY `FK_rbatauqrmmx9pp87u0i2x41t8` (`lastModifyUser_id`),
  CONSTRAINT `FK_rbatauqrmmx9pp87u0i2x41t8` FOREIGN KEY (`lastModifyUser_id`) REFERENCES `tb_user_user` (`id`),
  CONSTRAINT `FK_9uohu5639b7fsxm93qpcu35j6` FOREIGN KEY (`createUser_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `tb_workorder_visit` */

/*Table structure for table `user_permission` */

DROP TABLE IF EXISTS `user_permission`;

CREATE TABLE `user_permission` (
  `permissionentities_id` varchar(255) NOT NULL,
  `userentities_id` varchar(255) NOT NULL,
  PRIMARY KEY (`permissionentities_id`,`userentities_id`),
  KEY `FK_sx0oteotpb2l9j1y4gu8gbal7` (`userentities_id`),
  CONSTRAINT `FK_dmr3j79w5nbnvtq1sv5xux4l8` FOREIGN KEY (`permissionentities_id`) REFERENCES `tb_user_permission` (`id`),
  CONSTRAINT `FK_sx0oteotpb2l9j1y4gu8gbal7` FOREIGN KEY (`userentities_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_permission` */

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `roleentities_id` varchar(255) NOT NULL,
  `userentities_id` varchar(255) NOT NULL,
  KEY `FK_ml0sgcdvvy49lovt77vje8fyf` (`userentities_id`),
  KEY `FK_9wtqdcjdfm4s72qauku6qj2yg` (`roleentities_id`),
  CONSTRAINT `FK_9wtqdcjdfm4s72qauku6qj2yg` FOREIGN KEY (`roleentities_id`) REFERENCES `tb_user_role` (`id`),
  CONSTRAINT `FK_ml0sgcdvvy49lovt77vje8fyf` FOREIGN KEY (`userentities_id`) REFERENCES `tb_user_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_role` */

/*Table structure for table `view_inbox` */

DROP TABLE IF EXISTS `view_inbox`;

CREATE TABLE `view_inbox` (
  `taskId` varchar(255) NOT NULL,
  `assignTime` datetime DEFAULT NULL,
  `businessKey` varchar(255) DEFAULT NULL,
  `completeDate` varchar(255) DEFAULT NULL,
  `flag` int(11) DEFAULT NULL,
  `orderId` bigint(20) DEFAULT NULL,
  `taskKey` varchar(255) DEFAULT NULL,
  `taskName` varchar(255) DEFAULT NULL,
  `userCode` varchar(255) DEFAULT NULL,
  `workFlowType` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `view_inbox` */

/*Table structure for table `view_tasklist` */

DROP TABLE IF EXISTS `view_tasklist`;

CREATE TABLE `view_tasklist` (
  `taskId` varchar(255) NOT NULL,
  `avgHandleTime` bigint(20) DEFAULT NULL,
  `businessKey` varchar(255) DEFAULT NULL,
  `businessType` varchar(255) DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `flag` int(11) DEFAULT NULL,
  `handleTime` bigint(20) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `orderId` bigint(20) DEFAULT NULL,
  `orgCode` varchar(255) DEFAULT NULL,
  `orgName` varchar(255) DEFAULT NULL,
  `sendTitle` varchar(255) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `taskName` varchar(255) DEFAULT NULL,
  `userCode` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `view_tasklist` */

/*Table structure for table `workflowgroup_role` */

DROP TABLE IF EXISTS `workflowgroup_role`;

CREATE TABLE `workflowgroup_role` (
  `tb_workflow_group_id` varchar(255) NOT NULL,
  `roleEntities_id` varchar(255) NOT NULL,
  KEY `FK_9yjtxk86s5buj3065nr4o0bsq` (`roleEntities_id`),
  KEY `FK_elynxksv7dgwjav56l7ddrarm` (`tb_workflow_group_id`),
  CONSTRAINT `FK_elynxksv7dgwjav56l7ddrarm` FOREIGN KEY (`tb_workflow_group_id`) REFERENCES `tb_workflow_group` (`id`),
  CONSTRAINT `FK_9yjtxk86s5buj3065nr4o0bsq` FOREIGN KEY (`roleEntities_id`) REFERENCES `tb_user_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `workflowgroup_role` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
