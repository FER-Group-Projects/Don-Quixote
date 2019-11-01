package hr.fer.zemris.projekt.model.controller;

import hr.fer.zemris.projekt.model.controller.GameController.PlayerAction;
import hr.fer.zemris.projekt.model.objects.Game2DObject;
import hr.fer.zemris.projekt.model.objects.impl.Player;

public interface GameControllerListener {
	
	void gameObjectAdded(Game2DObject object);
	void gameObjectRemoved(Game2DObject object);
	void gameObjectChanged(Game2DObject object);

	void tickPerformed();

	void playerActionStateChanged(PlayerAction action, boolean isSet);

	void playerDestroyed(Player player);

}
