public class TruthTable {
    public boolean[][] inputs;
    public boolean[] outputs;

    int numInputs;

    public TruthTable(int numInputs) {
        this.numInputs = numInputs;

        inputs = new boolean[numInputs * numInputs][numInputs];
        outputs = new boolean[numInputs * numInputs];
    }

    public TruthTable(boolean[][] inputs, boolean[] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.numInputs = inputs[0].length;
    }

    public boolean calculate(boolean[] in) {
        for (int i = 0; i < inputs.length; i++){
            for (int j = 0; j < numInputs; j++){
                
                if (inputs[i][j] != in[j]) break;

                if (j == numInputs - 1) return outputs[i];
            }
        }

        return false;
    }
}
