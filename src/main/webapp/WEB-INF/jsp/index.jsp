<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="google" content="notranslate" />
    
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
  	<script type="text/javascript" charset="UTF-8" src="${contextPath}/statics/js/lib/prettify/prettify.js"></script>
    <script type="text/javascript" charset="UTF-8" src="${contextPath}/statics/js/lib/extjs/bootstrap.js"></script>
    <script id="tranzvision_mkc_advanced_mainapp" type="text/javascript" src="${contextPath}/statics/js/tranzvision/extjs/main/mainapp.js"></script>
  </head>
  <body>
    <table height="100%" width="100%" border="0" align="center" id="tranzvision_mkc_advanced_default_table">
      <tr>
        <td align="center" valign="middle"><center><div><p><h1 id="tranzvision_mkc_advanced_default_text"></h1></p></div></center></td>
      </tr>
    </table>
  </body>
</html>
