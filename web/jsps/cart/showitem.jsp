<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>showitem.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/showitem.css'/>">
	<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script src="<c:url value='/js/round.js'/>"></script>
	<style type="text/css">
	#addr{width: 500px; height: 32px;border: 1px solid #7f9db9; padding-left: 10px; line-height: 32px;}
	</style>

  </head>
  
  <body>
	  <form id="form1" action="<c:url value='/OrderServlet'/>" method="post">
		  <input type="hidden" name="cartItemIds" value="${cartItemIds}"/>
		  <input type="hidden" name="total" value="${total}"/>
		  <input type="hidden" name="method" value="generateOrder"/>
		  <table width="95%" align="center" cellpadding="0" cellspacing="0">
			  <tr bgcolor="#efeae5">
				  <td width="400px" colspan="5"><span style="font-weight: 900;">生成订单</span></td>
			  </tr>

			  <tr align="center">
				  <td width="10%">&nbsp;</td>
				  <td width="50%">图书名称</td>
				  <td>单价</td>
				  <td>数量</td>
				  <td>小计</td>
			  </tr>

			  <c:forEach items="${cartItems}" var="cartItem">
				  <tr align="center">
					  <td align="right" width="70px">
						  <a class="linkImage" href="<c:url value='/BookServlet?method=findByBid&bid=${cartItem.book.bid}'/>">
							  <img border="0" width="54" align="top" src="${cartItem.book.image_b}"/>
						  </a>
					  </td>
					  <td align="left" width="400px">
						  <a href="<c:url value='/BookServlet?method=findByBid&bid=${cartItem.book.bid}'/>">
							  <span>${cartItem.book.bname}</span>
						  </a>
					  </td>
					  <td><span>&yen;<span class="currPrice" id="CurrPrice">${cartItem.book.currPrice}</span></span></td>
					  <td>
						  <span class="quantity" id="${cartItem.cartItemId}Quantity">${cartItem.quantity}</span>
					  </td>
					  <td width="100px">
						  <span class="price_n">&yen;<span class="subTotal" id="${cartItem.cartItemId}SubTotal">${cartItem.subTotal}</span></span>
					  </td>
				  </tr>
			  </c:forEach>

			  <tr>
				  <td colspan="6" align="right">
					  <span>总计：</span><span class="price_t">&yen;<span id="total">${total}</span></span>
				  </td>
			  </tr>
			  <tr>
				  <td colspan="5" bgcolor="#efeae5"><span style="font-weight: 900">收货地址</span></td>
			  </tr>
			  <tr>
				  <td colspan="6">
					  <input id="addr" type="text" name="address" value="请输入地址  例如：XX省XX市XX区XX街道XXX"/>
				  </td>
			  </tr>
			  <tr>
				  <td style="border-top-width: 4px;" colspan="5" align="right">
					  <a id="linkSubmit" href="javascript:$('#form1').submit();">提交订单</a>
				  </td>
			  </tr>
		  </table>
	  </form>
  </body>
</html>
