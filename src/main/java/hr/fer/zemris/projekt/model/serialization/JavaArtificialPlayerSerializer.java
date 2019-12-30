package hr.fer.zemris.projekt.model.serialization;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;

import java.io.*;
import java.nio.file.Path;

public class JavaArtificialPlayerSerializer implements ArtificialPlayerSerializer {

    @Override
    public ArtificialPlayer deserialize(Path path) throws SerializationException {
        try (FileInputStream fileIn = new FileInputStream(path.toFile());
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            return (ArtificialPlayer) in.readObject();
        } catch (Exception exc) {
            throw new SerializationException(exc);
        }
    }

    @Override
    public void serialize(Path path, ArtificialPlayer player) throws SerializationException {
        try (FileOutputStream fileOut = new FileOutputStream(path.toFile());
             ObjectOutputStream out = new ObjectOutputStream(fileOut)){

            out.writeObject(player);
        } catch (Exception exc) {
            throw new SerializationException(exc);
        }
    }

}
