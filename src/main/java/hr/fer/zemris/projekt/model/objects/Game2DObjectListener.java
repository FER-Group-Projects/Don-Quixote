package hr.fer.zemris.projekt.model.objects;

public interface Game2DObjectListener {

	void positionChanged(Game2DObject source);
	void objectDestroyed(Game2DObject source);
	
}
