<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!doctype html>
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="google" content="notranslate" />
	<meta charset="utf-8">
    <title>宁波（中国）供应链创新学院</title>
    <script type="text/javascript">
		var TzUniversityContextPath = "${contextPath}";
	</script>
    <style type="text/css">
      html,body
      {
        height:95%;
      }
      <!--以下设置全屏--> 
      body
      {
        margin-left: 0px;
        margin-top: 0px;
        margin-right: 0px;
        margin-bottom: 0px;
      }
      .lable_1{
      	margin-left:180px;
      	color:red;
      	font-size: 13px;
      	margin-top:-5px;
      }
      .thumb-wrap{
      	width:174px;
      	float:left;
      	padding:6px;
      	background:#fff;
      	border:1px solid #c3c3c3;
      	margin:8px;
      	cursor:pointer;
      }
      .transparent_class {
		filter:alpha(opacity=50);
		-moz-opacity:0.5;
		-khtml-opacity: 0.5;
		opacity: 0.5;
		background:#eee;
	 }
	 .picEider{
	 	width:18px;
	 	height:18px;
	 	float:right;
	 	cursor:pointer;
	 	display:none;
	 }
	 .addPic{
	 	margin:0px;
	 	padding:0px;
	 	height:147px; 
	 	width:175px;
	 	background: #fff;
	 	margin-left:-10px;
	 	margin-top:-7px;
	 }
	 .lanage_1 input{
	 	background-color:#eee;
	 }
	 #tranzvision-framework-app-header-logo
	 {
	 	<%= (String)(request.getAttribute("tz_gdcp_loginStyle_20150612184830")) %>
	 }
    </style>
    <link rel="stylesheet" type="text/css" href="${contextPath}/statics/css/kitchensink/tranzvision/resources/Sencha-Examples/style.css"/>
    <script language="">
    	var tz_gdcp_interaction_url_20150612184830 = '<%= (String)(request.getAttribute("tz_gdcp_interaction_url_20150612184830")) %>';
    	var tz_gdcp_frmwrk_init_msgset_20150612184830 = <%= (String)(request.getAttribute("tz_gdcp_frmwrk_init_msgset_20150612184830")) %>;
    	var tz_gdcp_theme_id_20150612184830 = '<%= (String)(request.getAttribute("tz_gdcp_theme_id_20150612184830")) %>';
    	var tz_gdcp_language_cd_20150612184830 = '<%= (String)(request.getAttribute("tz_gdcp_language_cd_20150612184830")) %>';
    </script>
  	<script type="text/javascript" src="${contextPath}/statics/js/lib/prettify/prettify.js"></script>
  	<script type="text/javascript" src="${contextPath}/statics/js/lib/ueditor/ueditor.config.js"></script>
	<script type="text/javascript" src="${contextPath}/statics/js/lib/ueditor/ueditor.all.min.js"></script>
    <script type="text/javascript" src="${contextPath}/statics/js/lib/extjs/bootstrap.js"></script>
    <script type="text/javascript" src="${contextPath}/statics/js/lib/extjs/ux/ueditor.js"></script>
    <script id="tranzvision_mkc_advanced_mainapp" type="text/javascript" src="${contextPath}/statics/js/tranzvision/extjs/main/mainapp.js"></script>
  </head>
  <body>
    <table  height="100%" width="100%" border="0" align="center" id="tranzvision_mkc_advanced_default_table">
      <tr>
        <td align="center" valign="middle"><center><div><p><h1 style='margin-top:-70px;' id="tranzvision_mkc_advanced_default_text"></h1></p></div></center></td>
      </tr>
    </table>
  </body>
</html>
