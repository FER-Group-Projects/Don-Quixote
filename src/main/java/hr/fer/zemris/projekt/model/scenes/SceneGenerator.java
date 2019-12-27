package hr.fer.zemris.projekt.model.scenes;

import hr.fer.zemris.projekt.model.controller.GameController;

public interface SceneGenerator {
	
	GameController generateScene();
	GameController generateNewScene();

}
