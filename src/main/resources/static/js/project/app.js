(function(owner) {
	owner.serverBase='http://localhost:8080';
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
}(window.app = {}));