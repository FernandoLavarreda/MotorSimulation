
import java.util.ArrayList;

public class AsyncSolution <T extends ISolve> implements Runnable {
		private T[] problems;
		private double[] inputs;
		private ArrayList<Link> solutions;
		
		public AsyncSolution(T[] problems, double[] input_angles){
			this.problems = problems;
			this.inputs = input_angles;
			solutions = new ArrayList<Link>(10);
		}
		
		public void run(){
			int counter = 0;
			for(;counter<problems.length;counter++){
				Link[] solved = problems[counter].solve(inputs[counter]);
				for(Link t: solved){
					solutions.add(t);
				}
			}
		}
		
		public Link[] getSolutions(){
			Link[] arr = new Link[solutions.size()];
			return solutions.toArray(arr);
		}

}