public class TruthTable {
    public boolean[][] inputs;
    public boolean[] outputs;

    int numInputs;

    public TruthTable(int numInputs) {
        this.numInputs = numInputs;

        inputs = new boolean[(int) Math.pow(2, numInputs)][numInputs];
        outputs = new boolean[(int) Math.pow(2, numInputs)];

        int count = (int) Math.pow(2, numInputs);
        for (int i = 0; i < count; i++){

            for (int j = 0; j < numInputs; j++){
                int lengthOfCycle = count / (int) Math.pow(2, j);
                int indexInCycle = i % lengthOfCycle;
                inputs[i][j] = indexInCycle < lengthOfCycle/2; 
            }
        }
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

    public String toString(){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < inputs.length; i++){
            for (int j = 0; j < numInputs; j++){
                sb.append(inputs[i][j] ? "T " : "F ");
            }
            sb.append("| ");
            sb.append(outputs[i] ? "T" : "F");
            sb.append("\n");
        }

        return sb.toString();
    }
}
