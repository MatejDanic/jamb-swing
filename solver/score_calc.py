# Method for calculating score depending on index of box in row
def get_score(dice_set, box_num):
    score = 0

    if box_num <= 5:
        score = dice_set.count(box_num + 1) * (box_num + 1)

    elif box_num == 6 or box_num == 7:
        score = sum(dice_set)

    elif box_num == 8:
        for dice in dice_set:
            if dice_set.count(dice) >= 3:
                score = 3 * dice + 10
                break

    elif box_num == 9:
        has_straight = True
        straight = [2, 3, 4, 5]
        for n in straight:
            if n not in dice_set:
                has_straight = False
        if has_straight:
            if 1 in dice_set:
                score = 35
            elif 6 in dice_set:
                score = 45

    elif box_num == 10:
        found_pair = False
        found_trips = False
        for dice in dice_set:
            if dice_set.count(dice) == 3 and not found_trips:
                score += 3 * dice
                found_trips = True
                continue
            elif dice_set.count(dice) == 2 and not found_pair:
                score += 2 * dice
                found_pair = True
                continue
        if not found_pair or not found_trips:
            score = 0
        else:
            score += 30

    elif box_num == 11:
        for dice in dice_set:
            if dice_set.count(dice) >= 4:
                score = 4 * dice + 40
                break

    elif box_num == 12:
        for dice in dice_set:
            if dice_set.count(dice) == 5:
                score = 5 * dice + 50
                break
    return score
