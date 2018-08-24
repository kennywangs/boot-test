Vue.component('comp1', {
	data : function() {
		return {
			count : 0
		}
	},
	methods : {
		changeVal : function(items) {
			this.items = items;
		},
		pushVal : function(item) {
			this.items.push(item);
		},
	},
	template : '<div><p>comp1</p></div>'
});
