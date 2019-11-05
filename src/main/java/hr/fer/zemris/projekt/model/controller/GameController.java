package hr.fer.zemris.projekt.model.controller;

import java.util.List;

import hr.fer.zemris.projekt.model.objects.Game2DObject;

public interface GameController {
	
	void setPlayerAction(PlayerAction action);
	void unsetPlayerAction(PlayerAction action);

	void addListener(GameControllerListener listener);
	void removeListener(GameControllerListener listener);
	
	int getTickRatePerSec();
	
	void start();
	void stop();

	void addGameObject(Game2DObject object);
	void removeGameObject(Game2DObject object);
	List<Game2DObject> getGameObjects();

}
