from models import Form
from actions import Roll, Write, Announce
import random

dice_set = [0, 0, 0, 0, 0]
form = Form()

while True:
    done = form.is_done()
    if done:
        break
    ann_avail = form.is_ann_avail()
    Roll(dice_set, 31)
    dice_rolls = 1
    announcement = None
    while dice_rolls <= 3:
        avail_boxes = form.get_avail_boxes(dice_rolls)
        print(form)
        print("Roll number: ", dice_rolls)
        print(dice_set)
        print(avail_boxes)
        if announcement is not None:
            avail_boxes.clear()
            avail_boxes.append(announcement)

        if dice_rolls == 1 and ann_avail:
            while True:
                # option = int(input("Enter option number (1 - roll dice, 2 - write down, 3 - announce):\n"))
                option = random.randint(1, 3)
                if 1 <= option <= 3:
                    break
        elif dice_rolls == 2 or not ann_avail:
            while True:
                # option = int(input("Enter option number (1 - roll dice, 2 - write down):\n"))
                option = random.randint(1, 2)
                if 1 <= option <= 2:
                    break
        elif dice_rolls == 3:
            option = 2

        if option == 1:
            dice_rolls += 1
            while True:
                # dice_to_roll = int(input("Enter dice to roll (0-31):\n"))
                dice_to_roll = random.randint(0, 31)
                if 0 <= dice_to_roll <= 31:
                    break
            Roll(dice_set, dice_to_roll)
            continue
        elif option == 2:
            while True:
                # box_number = int(input("Enter box number:\n"))
                box_number = random.choice(avail_boxes)
                if box_number in avail_boxes:
                    break
            Write(form, dice_set, box_number)
            break
        elif option == 3:
            avail_boxes = list(filter(lambda a: a >= 39, avail_boxes))
            while True:
                # announcement = int(input("Enter announcement box number"))
                announcement = random.choice(avail_boxes)
                if announcement in avail_boxes:
                    break
            Announce(form, announcement)
            continue

print(form)
print(form.calculate_score())

