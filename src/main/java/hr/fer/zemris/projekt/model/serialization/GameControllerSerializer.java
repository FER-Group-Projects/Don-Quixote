package hr.fer.zemris.projekt.model.serialization;

import hr.fer.zemris.projekt.model.controller.GameController;

import java.nio.file.Path;

public interface GameControllerSerializer {

    GameController deserialize(String path) throws SerializationException;

    void serialize(Path path, GameController controller) throws SerializationException;

}