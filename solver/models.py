import random


class Paper:
    def __init__(self):
        self.row_list = []
        for i in range(4):
            self.row_list.append(Row(i))

    def __repr__(self):
        string = ''
        for i in range(13):
            for j in range(4):
                string += str(self.row_list[j].box_list[i].value) + ' '
            string += '\n'
        return string


class Row:
    def __init__(self, row_num):
        self.row_num = row_num
        self.box_list = []
        for i in range(13):
            self.box_list.append(Box(i))


class Box:
    def __init__(self, box_num):
        self.value = 0
        self.box_num = box_num
        self.available = False

    def write(self, dice_list):
        if self.box_num < 6:
            self.value = dice_list.count(self.box_num + 1)


class Dice:
    def __init__(self):
        self.reserved = False
        self.curr_num = 1

    def roll(self):
        if not self.reserved:
            self.curr_num = random.randint(1, 6)

    def __repr__(self):
        string = str(self.curr_num)
        if self.reserved:
            string = '|' + string + '|'
        return string

# dice_list = []
#
# for i in range(5):
#     dice_list.append(Dice())
#
# for i in range(5):
#     for dice in dice_list:
#         dice.roll()
#     dice_list[i].reserved = True
#     print(dice_list)
