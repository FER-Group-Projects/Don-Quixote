package hr.fer.zemris.projekt.model.controller;

import hr.fer.zemris.projekt.model.objects.Game2DObject;

public interface GameController {
	
	void setPlayerAction(PlayerAction action);
	void unsetPlayerAction(PlayerAction action);

	void addListener(GameControllerListener listener);
	void removeListener(GameControllerListener listener);

	void tick();

	void addGameObject(Game2DObject object);
	void removeGameObject(Game2DObject object);

}
