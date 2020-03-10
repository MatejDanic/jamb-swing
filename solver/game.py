import random


class Dice(object):
    def __init__(self):
        self.dice = []
        for i in range(5):
            self.dice.append(0)

    def __repr__(self):
        string = "["
        for i in range(5):
            string += self.dice[i].__str__() + ", "
        string = string[:-2] + "]"
        return string

    def throw_dice(self, dice_to_throw):
        dice_to_throw = ("{0:{fill}5b}".format(dice_to_throw, fill='0'))
        for index in range(5):
            if dice_to_throw[index] == '1':
                self.dice[index] = random.randint(1, 6)


dice = Dice()
dice.throw_dice(31)
print(dice)
