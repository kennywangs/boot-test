(function(owner) {
	owner.serverBase='http://localhost:8080';
	owner.getServerUrl = function(url){
		return owner.serverBase+url;
	};
}(window.app = {}));