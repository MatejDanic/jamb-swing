window.onload = function () {

	var diceButtons = document.querySelectorAll('button[class^=dice-button]');
	for (var i = 0; i < diceButtons.length; i++) {
		diceButtons[i].style.backgroundColor = "rgb(169, 169, 169)";
		diceButtons[i].innerHTML = '<img src="../static/images/dice/1.bmp">';
		diceButtons[i].value = 1;
//		diceButtons[i].disabled = true;
	}


	var allButtons = document.querySelectorAll('button[class^=grid-item]');

	for (var i = 0; i < allButtons.length; i++) {
		allButtons[i].addEventListener('click', function() {
		});
	}
}

function reply_click(id)
{
	var elem = document.getElementById(id);
	if (elem.style.backgroundColor == "rgb(169, 169, 169)") {
		elem.style.backgroundColor = "rgb(134, 134, 134)";
	} else {
		elem.style.backgroundColor = "rgb(169, 169, 169)";
	}
}

function roll_dice() {
	var diceButtons = document.querySelectorAll('button[class^=dice-button]');
	var number;

	for (var i = 0; i < diceButtons.length; i++) {
		if (diceButtons[i].style.backgroundColor == "rgb(169, 169, 169)") {
			number = Math.floor(Math.random() * (6 - 1) ) + 1;
			$(diceButtons[i]).animateRotate(360, {
				duration: 700,
				easing: 'linear',
				complete: function () {},
				step: function () {}
			});
			$(diceButtons[i]).html('<img src="../static/images/dice/' + number + '.bmp">');
			diceButtons[i].value = number;
		}
	}
}

$.fn.animateRotate = function(angle, duration, easing, complete) {
	var args = $.speed(duration, easing, complete);
	var step = args.step;
	return this.each(function(i, e) {
		args.complete = $.proxy(args.complete, e);
		args.step = function(now) {
			$.style(e, 'transform', 'rotate(' + now + 'deg)');
			if (step) return step.apply(e, arguments);
		};
		$({deg: 0}).animate({deg: angle}, args);
	});
};