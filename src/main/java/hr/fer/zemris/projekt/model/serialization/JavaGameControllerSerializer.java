package hr.fer.zemris.projekt.model.serialization;

import hr.fer.zemris.projekt.model.controller.GameController;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;

public class JavaGameControllerSerializer implements GameControllerSerializer {

    @Override
    public GameController deserialize(String path) throws SerializationException {
        try (InputStream in = getClass().getResourceAsStream(path)) {
            ObjectInputStream objIn = new ObjectInputStream(in);

            return (GameController) objIn.readObject();
        } catch (Exception exc) {
            throw new SerializationException(exc);
        }
    }

    @Override
    public void serialize(Path path, GameController controller) throws SerializationException {
        try (FileOutputStream fileOut = new FileOutputStream(path.toFile());
             ObjectOutputStream out = new ObjectOutputStream(fileOut)){

            out.writeObject(controller);
        } catch (Exception exc) {
            throw new SerializationException(exc);
        }
    }

}
