import itertools

from score_calc import get_score
import random


class Form:
    def __init__(self):
        self.form = [[], [], [], []]
        self.announcement = None
        for i in range(4):
            for j in range(13):
                self.form[i].append(None)

    def __repr__(self):
        string = 'Form\n'
        for i in range(13):
            string += '| '
            for j in range(4):
                if self.form[j][i] is not None:
                    string += str(self.form[j][i]) + ' | '
                else:
                    string += '- | '
            string += '\n'
        return string

    def get_avail_boxes(self, dice_rolls):
        avail_boxes = []
        for i in range(4):
            if i == 0:
                for j in range(13):
                    if self.form[i][j] is None:
                        avail_boxes.append(j)
                        break
            elif i == 1:
                for j in range(13):
                    if self.form[i][12 - j] is None:
                        avail_boxes.append(13 + (12 - j))
                        break
            elif i == 2:
                for j in range(13):
                    if self.form[i][j] is None:
                        avail_boxes.append(2 * 13 + j)
            elif i == 3 and dice_rolls == 1:
                for j in range(13):
                    if self.form[i][j] is None:
                        avail_boxes.append(3 * 13 + j)
        return avail_boxes

    def write(self, dice_set, box_num):
        for i in range(4):
            for j in range(13):
                if i * 13 + j == box_num:
                    self.form[i][j] = get_score(dice_set, j)
                    break
        self.announcement = None

    def announce(self, box_num):
        self.announcement = box_num

    def is_ann_avail(self):
        for j in range(13):
            if self.form[3][j] is None:
                return True
        return False

    def calculate_score(self):
        score = 0
        for i in range(4):
            upper_score = 0
            for j in range(6):
                upper_score += self.form[i][j] if self.form[i][j] is not None else 0
            if upper_score >= 60:
                upper_score += 30
            if self.form[i][6] is not None and self.form[i][7] is not None and self.form[i][0] is not None:
                middle_score = (self.form[i][6] - self.form[i][7]) * self.form[i][0]
            else:
                middle_score = 0
            lower_score = 0
            for j in range(5):
                lower_score += self.form[i][j + 8] if self.form[i][j + 8] is not None else 0
            score += upper_score + middle_score + lower_score
        return score

    def is_done(self):
        for i in range(4):
            for j in range(13):
                if self.form[i][j] is None:
                    return False
        return True


def roll_dice(dice_set, dice_to_roll):
    dice_to_roll = '{:05b}'.format(int(dice_to_roll))
    for i in range(len(dice_set)):
        if dice_to_roll[i] == '1':
            dice_set[i] = random.randint(1, 6)


def get_all_rolls():
    dice = [1, 2, 3, 4, 5, 6]
    return list(itertools.combinations_with_replacement(dice, 5))
