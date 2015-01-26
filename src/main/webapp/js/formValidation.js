$(document).ready(function() {
	$("#form").validate({
		messages: {
			computerName: "Please enter a computer name",
			introducedDate: "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-19. You can also leave this field emtpy",
			discontinuedDate: "The date is not valid, valid format is yyyy-MM-dd and between 1970-01-01 and 2038-01-19. You can also leave this field emtpy"
		}
	});
});