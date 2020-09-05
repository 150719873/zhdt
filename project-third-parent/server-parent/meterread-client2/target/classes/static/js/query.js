function getQueryStringArgs() {
	// 取得查询字符串并去掉开头的问好
	var qs = (location.search.length > 0 ? location.search.substring(1) : "");
	// 保存数据的对象
	var args = {};
	// 取得每一项
	var items = qs.length ? qs.split("&") : [];
	var item = null;
	var name = null;
	var value = null;
	// 在for循环中使用
	for (var i = 0; i < items.length; i++) {
		item = items[i].split("=");
		name = decodeURIComponent(item[0]);
		value = decodeURIComponent(item[1]);

		if (name.length) {
			args[name] = value;
		}
	}
	return args;
}