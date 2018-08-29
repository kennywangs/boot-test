(function(owner) {
//	owner.serverBase='http://localhost:8080';
	owner.serverBase='http://192.168.1.107:8080';
	owner.getServerUrl = function(url){
		return owner.serverBase+url;
	};
	var re = /([^&=]+)=?([^&]*)/g,
    decodeRE = /\+/g,
    decode = function (str) { return decodeURIComponent( str.replace(decodeRE, " ") ); };

    owner.parseParams = function(url) {
    	url = url.split('?')[1] || '';
	    let params = {}, e;
	
	    while ( e = re.exec(url) ) params[ decode(e[1]) ] = decode( e[2] );
	    return params;
	};
	owner.containInList = function(list,key,value){
		var ret = false;
		$.each(list,function(i,item){
			if (item[key]==value){
				ret = true;
			}
		});
		return ret;
	};
	owner.toInt = function(number) {
	    return Infinity === number ? 0 : (number*1 || 0).toFixed(0)*1;
	};
}(window.app = {}));