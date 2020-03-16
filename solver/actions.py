import random


class Action:

    def __init__(self):
        pass


class Roll(Action):

    def __init__(self, dice_set, dice_to_roll):
        Action.__init__(self)
        dice_to_roll = '{:05b}'.format(int(dice_to_roll))
        for i in range(len(dice_set)):
            if dice_to_roll[i] == '1':
                dice_set[i] = random.randint(1, 6)


class Write(Action):

    def __init__(self, form, dice_set, box_num):
        Action.__init__(self)
        form.write(dice_set, box_num)


class Announce(Action):

    def __init__(self, form, box_num):
        Action.__init__(self)
        form.announce(box_num)
