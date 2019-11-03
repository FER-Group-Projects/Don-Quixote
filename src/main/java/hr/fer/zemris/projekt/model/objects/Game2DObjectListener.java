package hr.fer.zemris.projekt.model.objects;

public interface Game2DObjectListener {

	void boundingBoxChanged(Game2DObject source);
	void objectDestroyed(Game2DObject source);
	
}
