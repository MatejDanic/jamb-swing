import models
from game import Game

if __name__ == '__main__':
    print(models.get_all_rolls())
    game = Game()
    while True:
        done = game.form.is_done()
        if done:
            break
        game.play_turn()

    print(game.form)
    print(game.form.calculate_score())
