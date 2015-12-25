<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta charset="UTF-8">
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
    </style>
  </head>
  <body>
    <table height="100%" width="100%" border="0" align="center" id="tranzvision_mkc_advanced_default_table">
      <tr>
        <td align="center" valign="middle"><center><div><p><h1 id="tranzvision_mkc_advanced_default_text"><%= (String)(request.getAttribute("tmpInvalidSessionPrefix")) %><a target="_top" href="<%= (String)(request.getAttribute("tmpLoginURL")) %>"><%= (String)(request.getAttribute("tmpInvalidSessionMiddle")) %></a><%= (String)(request.getAttribute("tmpInvalidSessionPostfix")) %></h1></p></div></center></td>
      </tr>
    </table>
  </body>
</html>