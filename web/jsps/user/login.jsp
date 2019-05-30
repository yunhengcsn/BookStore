<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>网上书城用户登录</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/user/login.css'/>">
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script src="<c:url value='/js/common.js'/>"></script>

  </head>
  
  <body>
	<div class="main">
	  <div>
	    <div class="imageDiv"><img class="img" src="<c:url value='/images/zj.png'/>"/></div>
        <div class="login1">
	      <div class="login2">
            <div class="loginTopDiv">
              <span class="loginTop">网上书城用户登录</span>
              <span>
                <a href="<c:url value='/jsps/user/regist.jsp'/>" class="registBtn"></a>
              </span>
            </div>
            <div>
              <form target="_top" action="<c:url value='/UserServlet'/>" method="post" id="loginForm">
                <input type="hidden" name="method" value="login" />
                  <table>
                    <tr>
                      <td class="tdLabel">用户名：</td>
                      <td class="tdInput">
                        <input type="text" name="username" id="username" class="input" value="${form.username}"/>
                      </td>
                      <td class="tdError">
                        <label class="labelError" id="usernameError">${errors.usernameError}</label>
                      </td>
                    </tr>
                    <tr>
                      <td class="tdLabel">密&nbsp;&nbsp;&nbsp;&nbsp;码：</td>
                      <td class="tdInput">
                        <input type="password" name="password" id="password" class="input" value="${form.password}"/>
                      </td>
                      <td class="tdError">
                        <label class="labelError" id="passwordError">${errors.passwordError}</label>
                      </td>
                    </tr>
                    <tr>
                      <td class="tdLabel">验证码：</td>
                      <td class="tdInput">
                        <input type="text" name="verifyCode" id="verifyCode" class="input" value="${form.verifyCode}"/>
                      </td>
                      <td class="tdError">
                        <label class="labelError" id="verifyCodeError">${errors.verifyCodeError}</label>
                      </td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td>
                        <span class="verifyCodeImg"><img id="vCode" width="100" src="<c:url value="/vcServlet"/>" /></span>
                      </td>
                      <td><a onclick="vcChange();" href="">换一张</a></td>
                    </tr>
                    <tr>
                      <td>&nbsp;</td>
                      <td align="left">
                        <input type="image" id="submit" src="<c:url value='/images/login1.jpg'/>" class="loginBtn"/>
                      </td>
                      <td class="tdError">
                        <label class="labelError" id="submitError">${submitError}</label>
                      </td>
                    </tr>																				
                 </table>
              </form>
            </div>
          </div>
        </div>
      </div>
	</div>
  </body>
</html>
	