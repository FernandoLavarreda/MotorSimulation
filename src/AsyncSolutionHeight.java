
public class AsyncSolutionHeight <T extends ISolve> implements Runnable {
		private T[] problems;
		private double[] inputs;
		private double[][] datay;
		
		public AsyncSolutionHeight(T[] problems, double[] input_angles, double[][] datay){
			this.problems = problems;
			this.inputs = input_angles;
			this.datay = datay;
		}
		
		
		public void run(){
			int counter = 0;
			double[] copied;
			for(;counter<problems.length;counter++){
				System.arraycopy(datay[counter], 1, datay[counter], 0, datay[counter].length-1);
				datay[counter][datay[counter].length-1] = problems[counter].height(inputs[counter]);
			}
		}

}