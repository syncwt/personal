function init() {
	$("#click").click(poll);
}

function poll() {
	$.ajax({
		async : true,
		url : "s",
		data : "data",
		success : function(data) {
			$("#content").html("Success:" + data);
		},
		complete : poll,
		error : function(data) {
			$("#content").html("Error:" + data);
		}
	});
}

Person = Ember.Object.extend({
	  // these will be supplied by `create`
	  firstName: null,
	  lastName: null,
	  fullName: function() {
	    var firstName = this.get('firstName');
	    var lastName = this.get('lastName');
	   return firstName + ' ' + lastName;
	  }.property('firstName', 'lastName')
	});
var tom = Person.create({
  firstName: "Tom",
  lastName: "Dale"
});
tom.get('fullName') // "Tom Dale"
