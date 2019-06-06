$(function() {
	//校验cnt处的值
	$("#cnt").blur(function() {
		var quantity = $("#cnt").val();
		if(!/^[1-9]\d*$/.test(quantity)) {
			alert("数量必须是合法整数！");
			$("#cnt").val("1");
		}
	});
});