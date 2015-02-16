$(document).ready(function() {
	$("#form").validate({
		messages: {
			name: "Please enter a computer name",
			introduced: "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-19. You can also leave this field emtpy",
			discontinued: "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-19. You can also leave this field emtpy"
		}
	});
});