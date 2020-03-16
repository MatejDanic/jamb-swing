class Action:

    def __init__(self):
        pass


class Roll(Action):

    def __init__(self, dice_to_roll):
        Action.__init__(self)
        self.dice_to_roll = dice_to_roll


class Write(Action):

    def __init__(self, box_num):
        Action.__init__(self)
        self.box_num = box_num


class Announce(Action):

    def __init__(self, box_num):
        Action.__init__(self)
        self.box_num = box_num
