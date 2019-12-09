package hr.fer.zemris.projekt.feedforwardNeuralNet;

import java.util.ArrayList;

public class Layer {

    private ArrayList<Neuron> listOfNeurons;
    private int numOfNeurons;

    public Layer(int numOfNeurons){
        this.numOfNeurons = numOfNeurons;
        listOfNeurons = new ArrayList<>();
        initLayer();
    }

    public void initLayer(){
        for(int i=0; i<numOfNeurons; i++){
            listOfNeurons.add(new Neuron(i));
        }
    }

    public ArrayList<Neuron> getListOfNeurons() {
        return listOfNeurons;
    }

    public int getNumOfNeurons() {
        return numOfNeurons;
    }

}
