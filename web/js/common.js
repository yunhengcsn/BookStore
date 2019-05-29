function vcChange() {
	$("#verifyCodeImg").attr("src","${pageContext.request.contextPath}/vcServlet?a="+new Date());
}