var diceRolls;
var diceButtons;
var rollDiceButton;
var gridItems;
var scores;
var announcement;
var counter;

window.onload = function () {
	diceRolls = 0;
	diceButtons = document.querySelectorAll('button[class^=dice-button]');
	rollDiceButton = document.getElementById("roll-dice");
//	gridItems = document.querySelectorAll('button[class^=grid-item]');
	gridItems = [];
	scores = [];
	announcement = -1;
	counter = 0;

	for (var i = 0; i < diceButtons.length; i++) {
		diceButtons[i].style.backgroundColor = "rgb(169, 169, 169)";
		diceButtons[i].innerHTML = '<img src="../images/dice/1.bmp">';
		diceButtons[i].value = 1;
		diceButtons[i].disabled = true;
	}
	for (var i = 0; i < 4; i++) {
		for (var j = 0; j < 13; j++) {
			gridItems.push(document.getElementById(i*13+j));
			gridItems[i*13+j].disabled = true;
			gridItems[i*13+j].written = false;
			gridItems[i*13+j].available = false;
			gridItems[i*13+j].value = 0;
		}
	}

	for (var i = 0; i < 3; i++) {
		var name;
		if (i == 0) {
			name = 'upper';
		} else if (i == 1) {
			name = 'middle';
		} else if (i == 2) {
			name = 'lower';
		}
		for (var j = 0; j < 5; j ++) {
//			console.log(name+j);
			scores.push(document.getElementById(name+j))
			scores[i*5+j].value = 0;
//			console.log(i*5+j, scores[i*3+j].value)
		}
	}
	scores.push(document.getElementById("final"));
	scores[15].value = 0;
	initializeGrid();
}

function replyClick(id)
{
	var elem = document.getElementById(id);
	if (elem.style.backgroundColor == "rgb(169, 169, 169)") {
		elem.style.backgroundColor = "rgb(134, 134, 134)";
	} else {
		elem.style.backgroundColor = "rgb(169, 169, 169)";
	}
}

function writeDown(id) {
	for (var i = 0; i < 4; i++) {
		for (var j = 0; j < 13; j++) {
			if (id == i*13+j) {
//				console.log(j);
				if (i==3 && announcement == -1) {
					announce(id);
				} else {
					document.getElementById(id).style.border = "1px solid black";
					announcement = -1;
					document.getElementById(id).value = getScore(j);
					break;
				}
			}
		}
	}
	if (announcement == -1) {
		gridItems[id].available = false;
		gridItems[id].written = true;
		if (id < 12) {
//			console.log("?");
			gridItems[parseInt(id, 10)+1].available = true;
		} else if (id > 13 && id <= 25) {

//			console.log("?2");
			gridItems[parseInt(id, 10)-1].available = true;
		}
		diceRolls = 0;
//		console.log("?3");
		toggleButtons();
//		console.log("?4");
		calculateSums();

		document.getElementById(id).innerHTML = document.getElementById(id).value;
		counter++;
		if (counter == 52){
			setTimeout(function() {
				endGame();
			}, 1000);
		}
	}
}

function announce(id) {
	announcement = id;
	document.getElementById(id).style.border = "2px solid red";
	for (var i = 0; i < 4; i++) {
		for (var j = 0; j < 13; j++) {
			if (i*13+j == id) {
				gridItems[i*13+j].disabled = false;
			} else {
				gridItems[i*13+j].disabled = true;
			}
		}
	}
}

function toggleButtons() {

	if (diceRolls == 0) {
		rollDiceButton.disabled = false;
		for (var i = 0; i < gridItems.length; i++) {
//			console.log(i);
			gridItems[i].disabled = true;
			for (var j = 0; j < diceButtons.length; j++) {
//				console.log(j)
				diceButtons[j].disabled = true;
				diceButtons[j].style.backgroundColor = "rgb(169, 169, 169)";

			}
		}
//		console.log("?5");
	} else if (diceRolls == 1) {
		for (var i = 0; i < gridItems.length; i++) {
			if (gridItems[i].available == true) {
				gridItems[i].disabled = false;
			}
		}
		for (var i = 0; i < diceButtons.length; i++) {
			diceButtons[i].disabled = false;
		}
	} else if (diceRolls == 2) {
		for(var i = 39; i < 52; i++) {
			if (i != announcement) {
				gridItems[i].disabled = true;
			}
		}
	} else if (diceRolls == 3) {
		for (var i = 0; i < diceButtons.length; i++) {
			diceButtons[i].disabled = true;
			rollDiceButton.disabled = true;
		}
	} 
}

function initializeGrid() {
	gridItems[0].available = true;

	gridItems[25].available = true;

	for (var j = 0; j < 26; j++) {
		gridItems[26+j].available = true;
	}
	calculateSums();
}

function rollDice() {
	var number;
	diceRolls++;

	for (var i = 0; i < diceButtons.length; i++) {
		if (diceButtons[i].style.backgroundColor == "rgb(169, 169, 169)") {
			number = Math.floor(Math.random() * (7 - 1) ) + 1;
			$(diceButtons[i]).animateRotate(360, {
				duration: 700,
				easing: 'linear',
				complete: function () {},
				step: function () {}
			});
			$(diceButtons[i]).html('<img src="../images/dice/' + number + '.bmp">');
			diceButtons[i].value = number;
		}
	}

	toggleButtons();
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


function getScore(boxNum) {
	var score = 0;

	if (boxNum <= 5){
		for (var i = 0; i < diceButtons.length; i++) {
			if (boxNum+1 == diceButtons[i].value) {
				score += parseInt(diceButtons[i].value, 10);
			}
		}
	} else if (boxNum == 6 || boxNum == 7) {
		for (var i = 0; i < diceButtons.length; i++) {
			score += parseInt(diceButtons[i].value, 10);
		}
	} else if (boxNum == 8) {
		for (var i = 0; i < diceButtons.length; i++) {
			var num = 1; 
			var result = parseInt(diceButtons[i].value, 10);
			for (var j = 0; j < diceButtons.length; j++) {
				if (diceButtons[i] != diceButtons[j] && diceButtons[i].value == diceButtons[j].value) { 
					num++; 
					if (num <= 3) result += parseInt(diceButtons[j].value, 10);
				} 
			} 
			if (num >= 3) { 
				score = result + 10; 
				break; 
			} 
		} 
	} else if (boxNum == 9) {
		var straight = [2, 3, 4, 5];
		var hasStraight = true;
		var diceResults = [];
		for (var i = 0; i < diceButtons.length; i++) {
			diceResults.push(parseInt(diceButtons[i].value, 10));
		}

		for (var i = 0; i < straight.length; i++) {
			if (!diceResults.includes(straight[i])) {
				hasStraight = false;
			}
		}
		if (hasStraight) {
			if (diceResults.includes(1)) {
				score = 35;
			} else if (diceResults.includes(6)) {
				score = 45;
			} else {
				score = 0;
			}
		}
	} else if (boxNum == 10) {
		var hasPair = false;
		var hasTrips = false; 
		for (var i = 0; i < diceButtons.length; i++) {
			var num = 1; 
			var result = parseInt(diceButtons[i].value, 10);
			for (var j = 0; j < diceButtons.length; j++) {
				if (diceButtons[i] != diceButtons[j] && diceButtons[i].value == diceButtons[j].value) { 
					num++; 
					if (num <= 3) result += parseInt(diceButtons[j].value, 10);
				} 
			} 
			if (num == 2 && !hasPair) { 
				hasPair = true; 
				score += result
			} else if (num == 3 && !hasTrips) { 
				hasTrips = true;
				score += result
			}
		} 

		if (hasPair && hasTrips) { 
			score += 30;
		}  else {
			score = 0;
		}

	} else if (boxNum == 11) {
		for (var i = 0; i < diceButtons.length; i++) {
			var num = 1; 
			var result = parseInt(diceButtons[i].value, 10);
			for (var j = 0; j < diceButtons.length; j++) {
				if (diceButtons[i] != diceButtons[j] && diceButtons[i].value == diceButtons[j].value) { 
					num++; 
					if (num <= 4) result += parseInt(diceButtons[j].value, 10);
				} 
			} 
			if (num >= 4) { 
				score = result + 40; 
				break; 
			} 
		} 
	} else if (boxNum == 12) {
		for (var i = 0; i < diceButtons.length; i++) {
			var num = 1; 
			var result = parseInt(diceButtons[i].value, 10);
			for (var j = 0; j < diceButtons.length; j++) {
				if (diceButtons[i] != diceButtons[j] && diceButtons[i].value == diceButtons[j].value) { 
					num++; 
					result += parseInt(diceButtons[j].value, 10);
				} 
			} 
			if (num == 5) { 
				score = result + 50; 
				break; 
			} 
		} 
	}
	return score;
}

function calculateSums() {
	var array = [];
	for (var i = 0; i < scores.length; i++) {
		array.push(parseInt(scores[i].value, 10));
	}
	for (var i = 0; i < 4; i++) {
		for (var j = 0; j < 6; j++) {
//			console.log(i*13+j, parseInt(gridItems[i*13+j].value, 10));
			array[i] += parseInt(gridItems[i*13+j].value, 10);
		}
		if (array[i] >= 60 ) {
			array[i] += 30;
		}
		if (gridItems[i*13+0].written && gridItems[i*13+6].written && gridItems[i*13+7].written) {
			array[i+5] = gridItems[i*13+0].value * (gridItems[i*13+6].value - gridItems[i*13+7].value);
		}
		for (var j = 8; j < 13; j++) {
//			console.log(i*13+j, parseInt(gridItems[i*13+j].value, 10));
			array[i+10] += parseInt(gridItems[i*13+j].value, 10);
		}
	}
	for (var i = 0; i < 3; i++) {
		for (var j = 0; j < 4; j++) {
			array[i*5+4] += array[i*5+j]
		}
		array[15] += array[i*5+4];
	}
	for (var i = 0; i < scores.length; i++) {
		scores[i].innerHTML = array[i];
		scores[i].value = array[i];
	}
}

function endGame() {
	person = prompt("Molimo unesite vase ime:", "Matej");
	if (person != null && person != "") {
		var http = new XMLHttpRequest();
		var url = 'http://localhost:8080/scores';
		var params = JSON.stringify({"name": person, "value": parseInt(scores[15].value, 10), "date": Date.now()});
		http.open('POST', url, true);
		http.setRequestHeader('Content-type', 'application/json');

		http.onreadystatechange = function() {
			if(http.readyState == 4 && http.status == 200) {
				alert(http.responseText);
			}
		}
		console.log(params);
		http.send(params);
	}
	location.reload();
}