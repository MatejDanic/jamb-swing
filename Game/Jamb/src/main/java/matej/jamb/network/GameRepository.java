package matej.jamb.network;

import org.springframework.data.jpa.repository.JpaRepository;

import matej.jamb.models.Game;

public interface GameRepository extends JpaRepository<Game, Integer> {
}
