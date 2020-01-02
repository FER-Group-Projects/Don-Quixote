package hr.fer.zemris.projekt.model.serialization;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;

import java.nio.file.Path;

public interface ArtificialPlayerSerializer {

    ArtificialPlayer deserialize(Path path) throws SerializationException;

    void serialize(Path path, ArtificialPlayer player) throws SerializationException;

}