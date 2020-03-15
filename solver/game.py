from models import Dice, Paper

dice_set = []

for i in range(5):
    dice_set.append(Dice())

# print(dice_set)

dice_throws = 0

paper = Paper()
print(paper)

while dice_throws < 3:
    print(dice_set)
    option = input("Enter option number (1 - roll dice, 2 - keep dice, 3 - write down): ")
    dice_results = [0, 0, 0, 0, 0]
    if option == '1':
        dice_throws += 1
        for i, dice in enumerate(dice_set):
            dice.roll()
            dice_results[i] = dice.curr_num
    elif option == '2':
        keep_dice = '{:05b}'.format(int(input("Enter dice that you wish to keep: ")))
        for i in range(keep_dice.__len__()):
            dice_set[i].reserved = keep_dice[i] == '1'
    elif option == '3':
        print(paper.get_avail_boxes())
        row_num, box_num = int(input("Enter row and box number: "))
        paper.row_list[row_num].box_list[box_num].write(dice_results)
        print(paper)


# //
# //		int diceThrows = 0;
# //		String announcement = "";
# //		Map<Integer, String> availBoxMap;
# //		RowType rowType;
# //		BoxType boxType;
# //		while(diceThrows <= JambConstants.NUM_OF_ROLLS) {
# //			int input = 0;
# //			if (diceThrows == 0) {
# //				input = InputChecker.checkInput(1, 1, "option number (1 - throw dice)");
# //				//				input = 1;
# //			} else if (diceThrows == 1 && announcement.isEmpty()) {
# //				input = InputChecker.checkInput(1, 4, "option number (1 - throw dice, 2 - keep some dice, 3 - write down, 4 - announce)");
# //				//				input = 3;
# //			} else if (diceThrows == JambConstants.NUM_OF_ROLLS) {
# //				input = InputChecker.checkInput(3, 3, "option number (3 - write down)");
# //			} else {
# //				input = InputChecker.checkInput(1, 3, "option number (1 - throw dice, 2 - keep some dice, 3 - write down");
# //			}
# //			switch (input) {
# //			case 1:
# //				//				throwDice();
# //				diceThrows++;
# //				break;
# //			case 2:
# //				System.out.println(diceList);
# //				String diceForKeep = String.format("%5s", Integer.toBinaryString(InputChecker.checkInput(1, 31, "dice that you want to keep (select already kept dice to unkeep)"))
# //						.replace(' ', '0'));
# //				for (int i = 0; i < diceForKeep.length(); i++) {
# //					if (diceForKeep.charAt(i) == '1') {
# //						diceList.get(i).setReserved(!diceList.get(i).isReserved());
# //					}
# //				}
# //				System.out.println(diceList);
# //				break;
# //			case 3:
# //				System.out.println("\nAvailable boxes are:\n");
# //				availBoxMap = paper.getAvailBoxMap(diceThrows, announcement);
# //				for (Integer boxIndex : availBoxMap.keySet()) {
# //					System.out.println(boxIndex + ". " + availBoxMap.get(boxIndex).toString());
# //				}
# //				input = InputChecker.checkInput(1, availBoxMap.size(), "index of box");
# //				rowType = RowType.valueOf(availBoxMap.get(input).split(" ")[0]);
# //				boxType = BoxType.valueOf(availBoxMap.get(input).split(" ")[1]);
# //				paper.getRow(rowType).writeDown(diceList, boxType.ordinal());
# //				System.out.println(paper);
# //				return;
# //			case 4:
# //				System.out.println("\nAvailable announcements are:\n");
# //				announcement = "ANNOUNCE";
# //				availBoxMap = paper.getAvailBoxMap(diceThrows, announcement);
# //				for (Integer boxIndex : availBoxMap.keySet()) {
# //					System.out.println(boxIndex + ". " + availBoxMap.get(boxIndex).toString());
# //				}
# //				System.out.println(diceList);
# //				input = InputChecker.checkInput(1, availBoxMap.size(), "index of announcement");
# //				announcement += " " + BoxType.valueOf(availBoxMap.get(input).split(" ")[1]);
# //				break;
# //			}
# //		}
# //
# //		System.out.println("\n");
# //	}
