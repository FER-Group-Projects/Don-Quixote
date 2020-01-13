package hr.fer.zemris.projekt.model.serialization;

import hr.fer.zemris.projekt.algorithm.player.ArtificialPlayer;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public class JavaArtificialPlayerSerializer implements ArtificialPlayerSerializer {

    @Override
    public ArtificialPlayer deserialize(String path) throws SerializationException {
        try (InputStream in = getClass().getResourceAsStream(path)) {
            ObjectInputStream objIn = new ObjectInputStream(in);

            return (ArtificialPlayer) objIn.readObject();
        } catch (Exception exc) {
            throw new SerializationException(exc);
        }
    }

    @Override
    public void serialize(Path path, ArtificialPlayer player) throws SerializationException {
        try (FileOutputStream fileOut = new FileOutputStream(path.toFile());
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(player);
        } catch (Exception exc) {
            throw new SerializationException(exc);
        }
    }

}
