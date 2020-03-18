window.onload = function () {
	var allButtons = document.querySelectorAll('button[class^=grid-item]');
	console.log("Found", allButtons.length, "div which class starts with “button”.");

	for (var i = 0; i < allButtons.length; i++) {
		allButtons[i].addEventListener('click', function() {
			console.log("You clicked:" + i, this.innerHTML);
		});
	}
}
