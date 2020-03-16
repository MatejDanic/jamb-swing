import pickle
from models import Form, roll_dice
import actions
import random


class Game:
    def __init__(self):
        self.dice_set = [0, 0, 0, 0, 0]
        self.form = Form()
        self.data_dir = '/data'

    def play_turn(self):
        self.form.announcement = None
        announcement_available = self.form.is_ann_avail()
        roll_dice(self.dice_set, 31)
        dice_rolls = 1
        print(self.dice_set)
        while True:
            try:
                announcement = self.form.announcement
                avail_boxes = self.form.get_avail_boxes(dice_rolls)
                if announcement is not None:
                    avail_boxes.clear()
                    avail_boxes.append(announcement)

                action = self.get_action(avail_boxes, dice_rolls)
                assert isinstance(action, actions.Action), "Invalid action"
                if isinstance(action, actions.Roll):
                    assert dice_rolls <= 3, "Dice roll limit is 3"
                    roll_dice(self.dice_set, action.dice_to_roll)
                    print(self.dice_set)
                elif isinstance(action, actions.Write):
                    assert self
                    self.form.write(self.dice_set, action.box_num)
                    print(self.form)
                    break
                elif isinstance(action, actions.Announce):
                    assert dice_rolls == 1 and announcement_available, "Cannot announce after second roll"
                    self.form.announce(action.box_num)
            except AssertionError as e:
                print(e)

    def get_action(self, avail_boxes, dice_rolls):
        pass
